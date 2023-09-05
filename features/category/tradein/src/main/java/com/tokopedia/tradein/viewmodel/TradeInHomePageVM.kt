package com.tokopedia.tradein.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.TradeInApiService
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.common_tradein.model.DeviceDiagnostics
import com.tokopedia.common_tradein.model.TradeInPDPData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.model.request.Laku6TestDataModel
import com.tokopedia.tradein.usecase.InsertLogisticPreferenceUseCase
import com.tokopedia.tradein.viewmodel.liveState.GoToCheckout
import com.tokopedia.tradein.viewmodel.liveState.TradeInHomeState
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class TradeInHomePageVM @Inject constructor(
    private val userSession: UserSessionInterface,
    private val insertLogisticPreferenceUseCase: InsertLogisticPreferenceUseCase,
    private val addToCartOcsUseCase: AddToCartOcsUseCase
) : BaseTradeInViewModel(), CoroutineScope {

    var data: TradeInPDPData? = null
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    val askUserLogin = MutableLiveData<Int>()
    val laku6DeviceModel = MutableLiveData<Laku6DeviceModel>()
    var is3PLSelected = MutableLiveData<Boolean>()
    var tradeInHomeStateLiveData: MutableLiveData<TradeInHomeState> = MutableLiveData()

    private val _addToCartLiveData = MutableLiveData<AddToCartDataModel>()
    val addToCartLiveData: LiveData<AddToCartDataModel>
        get() = _addToCartLiveData

    val userId: String
        get() = userSession.userId

    val deviceId: String
        get() = userSession.deviceId

    private var tradeInApiService: TradeInApiService? = null
    var imei: String = ""
    var isDiagnosed: Boolean = false
    var tradeInPrice: String = ""
    var tradeInPriceDouble: Double = 0.0
    var finalPriceDouble: Double = 0.0
    var tradeInUniqueCode: String = ""
    var campaginTagId: String = ""

    fun getPDPData(tradeinPDPData: TradeInPDPData?): TradeInPDPData? {
        data = tradeinPDPData ?: data
        return data
    }

    fun setLaku6(context: Context) {
        var campaignId = TradeinConstants.CAMPAIGN_ID_PROD
        if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            campaignId =
                TradeinConstants.CAMPAIGN_ID_STAGING
        }
        tradeInApiService = TradeInApiService.getInstance(
            context,
            campaignId,
            TokopediaUrl.getInstance().TYPE == Env.STAGING,
            TradeinConstants.TRADEIN_EXCHANGE
        )
        tradeInApiService?.setTokopediaTestType(TradeinConstants.TRADEIN_EXCHANGE)
    }

    fun isPermissionGranted(): Boolean {
        return tradeInApiService?.ispermissionGranted() ?: false
    }

    fun getDeviceModel() {
        try {
            val deviceModel =
                Gson().fromJson(
                    tradeInApiService?.getDeviceModel().toString(),
                    Laku6DeviceModel::class.java
                )
            deviceModel.modelInfoBase64 = Base64.getEncoder()
                .encodeToString(tradeInApiService?.getDeviceModel().toString().toByteArray())
            tradeInUniqueCode = deviceModel?.uniqueCode ?: ""
            laku6DeviceModel.value = deviceModel
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDiagnosticData(intent: Intent): DeviceDiagnostics {
        val result = intent.getStringExtra("test-result")
        return Gson().fromJson(result, DeviceDiagnostics::class.java)
    }

    private fun setTestData(deviceAttribute: TradeInDetailModel.GetTradeInDetail.DeviceAttribute?) {
        laku6DeviceModel.value?.apply {
            deviceAttribute?.let { deviceAttribute ->
                if (deviceAttribute.imei.isNotEmpty()) {
                    this@TradeInHomePageVM.imei = deviceAttribute.imei.firstOrNull() ?: ""
                }
                val json = Gson().toJson(
                    Laku6TestDataModel(
                        rootBlocked = rootDetected,
                        deviceInfo = Laku6TestDataModel.DeviceInfo(
                            brand = deviceAttribute.brand,
                            modelId = deviceAttribute.modelId.toString(),
                            model = deviceAttribute.model,
                            ram = deviceAttribute.ram,
                            storage = deviceAttribute.storage
                        ),
                        finalPageInfo = Laku6TestDataModel.FinalPageInfo(
                            imei = this@TradeInHomePageVM.imei,
                            productImage = data?.productImage ?: "",
                            productName = data?.productName ?: "",
                            productOriginalValue = data?.productPrice ?: 0.0,
                            productValue = finalPriceDouble,
                            location = data?.shopLocation ?: "",
                            storeIcon = data?.shopBadge ?: "",
                            storeName = data?.shopName ?: "",
                            storeType = ""
                        )
                    )
                )
                try {
                    tradeInApiService?.setTestData(
                        json,
                        if (is3PLSelected.value == true) TradeinConstants.CAMPAIGN_TAG_SELECTION3PL else TradeinConstants.CAMPAIGN_TAG_SELECTION1PL
                    )
                } catch (exception: Exception) {
                    errorMessage.value = exception
                }
            }
        }
    }

    fun startLaku6Testing(deviceAttribute: TradeInDetailModel.GetTradeInDetail.DeviceAttribute?) {
        setTestData(deviceAttribute)
        try {
            tradeInApiService?.startGUITest()
        } catch (exception: Exception) {
            errorMessage.value = exception
        }
    }

    fun checkLogin() {
        if (!userSession.isLoggedIn) {
            askUserLogin.value = TradeinConstants.LOGIN_REQUIRED
        } else {
            askUserLogin.value = TradeinConstants.LOGGED_IN
        }
    }

    fun updateLogistics(is3Pl: Boolean) {
        is3PLSelected.value = is3Pl
    }

    fun goToCheckout() {
        tradeInHomeStateLiveData.value =
            GoToCheckout(imei, laku6DeviceModel.value?.model ?: "", tradeInPrice)
    }

    fun insertLogisticOptions(intent: Intent) {
        progBarVisibility.value = true
        launchCatchError(block = {
            val diagnosticsData = getDiagnosticData(intent)
            val insertData = insertLogisticPreferenceUseCase.insertLogistic(
                is3PL = is3PLSelected.value ?: false,
                finalPrice = data?.productPrice?.minus(
                    diagnosticsData.tradeInPrice?.toDouble() ?: tradeInPriceDouble
                ) ?: finalPriceDouble,
                tradeInPrice = diagnosticsData.tradeInPrice?.toDouble() ?: tradeInPriceDouble,
                imei = imei,
                diagnosticsData = diagnosticsData,
                campaignTagId = campaginTagId
            )
            insertData.insertTradeInLogisticPreference.apply {
                if (isSuccess) {
                    goToCheckout()
                } else {
                    warningMessage.value = "$errCode : $errMessage"
                }
            }
            progBarVisibility.value = false
        }, onError = {
                it.printStackTrace()
                progBarVisibility.value = false
                warningMessage.value = it.localizedMessage
            })
    }

    fun getAddToCartOcsUseCase(addToCartOcsRequestParams: AddToCartOcsRequestParams) {
        progBarVisibility.value = true
        launchCatchError(block = {
            val requestParams = RequestParams.create()
            requestParams.putObject(
                AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST,
                addToCartOcsRequestParams
            )
            val result = withContext(dispatcher) {
                addToCartOcsUseCase.createObservable(requestParams).toBlocking().single()
            }
            if (result.isDataError()) {
                val errorMessage = result.errorMessage.firstOrNull() ?: ""
                if (errorMessage.isNotBlank()) {
                    warningMessage.value = errorMessage
                }
            } else {
                _addToCartLiveData.value = result
            }
            progBarVisibility.value = false
        }, onError = {
                it.printStackTrace()
                progBarVisibility.value = false
                warningMessage.value = it.localizedMessage
            })
    }
}
