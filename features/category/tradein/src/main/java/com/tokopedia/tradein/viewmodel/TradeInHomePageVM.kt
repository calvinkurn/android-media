package com.tokopedia.tradein.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.Laku6TradeIn
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
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomePageVM @Inject constructor(
    private val userSession: UserSessionInterface,
    private val insertLogisticPreferenceUseCase: InsertLogisticPreferenceUseCase,
    private val addToCartOcsUseCase: AddToCartOcsUseCase
) : BaseTradeInViewModel(), CoroutineScope {

    var data: TradeInPDPData? = null

    val askUserLogin = MutableLiveData<Int>()
    val laku6DeviceModel = MutableLiveData<Laku6DeviceModel>()
    var is3PLSelected = MutableLiveData<Boolean>()
    var tradeInHomeStateLiveData: MutableLiveData<TradeInHomeState> = MutableLiveData()

    private val _addToCartLiveData = MutableLiveData<AddToCartDataModel>()
    val addToCartLiveData: LiveData<AddToCartDataModel>
        get() = _addToCartLiveData

    val userId: String
        get() = userSession.userId

    private var laku6TradeIn: Laku6TradeIn? = null
    var imei: String = ""
    var tradeInPrice: String = ""
    var tradeInPriceInt: Int = 0
    var finalPriceInt: Int = 0
    var tradeInUniqueCode: String = ""

    fun getPDPData(tradeinPDPData: TradeInPDPData?): TradeInPDPData? {
        data = tradeinPDPData ?: data
        return data
    }

    fun setLaku6(context: Context) {
        var campaignId = TradeinConstants.CAMPAIGN_ID_PROD
        if (TokopediaUrl.getInstance().TYPE == Env.STAGING) campaignId =
            TradeinConstants.CAMPAIGN_ID_STAGING
        laku6TradeIn = Laku6TradeIn.getInstance(
            context, campaignId,
            TokopediaUrl.getInstance().TYPE == Env.STAGING, TradeinConstants.TRADEIN_EXCHANGE
        )
        laku6TradeIn?.setTokopediaTestType(TradeinConstants.TRADEIN_EXCHANGE)
    }

    fun isPermissionGranted(): Boolean {
        return laku6TradeIn?.permissionGranted() ?: false
    }

    fun getDeviceModel() {
        laku6DeviceModel.value =
            Gson().fromJson(laku6TradeIn?.deviceModel.toString(), Laku6DeviceModel::class.java)
    }

    fun getDiagnosticData(intent: Intent): DeviceDiagnostics {
        val result = intent.getStringExtra("test-result")
        return Gson().fromJson(result, DeviceDiagnostics::class.java)
    }

    private fun setTestData(deviceAttribute: TradeInDetailModel.GetTradeInDetail.DeviceAttribute?) {
        laku6DeviceModel.value?.apply {
            deviceAttribute?.let { deviceAttribute ->
                val json = Gson().toJson(
                    Laku6TestDataModel(
                        rootBlocked = rootDetected,
                        deviceInfo = Laku6TestDataModel.DeviceInfo(
                            brand = deviceAttribute.brand,
                            modelDisplayName = "",
                            modelId = deviceAttribute.modelId.toString(),
                            modelName = deviceAttribute.model,
                            ram = deviceAttribute.ram,
                            storage = deviceAttribute.storage
                        )
                    )
                )
                try {
                    laku6TradeIn?.setTestData(json, TradeinConstants.CAMPAIGN_TAG_SELECTION)
                } catch (exception: Exception) {
                    errorMessage.value = exception
                }
            }
        }
    }

    fun startLaku6Testing(deviceAttribute: TradeInDetailModel.GetTradeInDetail.DeviceAttribute?) {
        setTestData(deviceAttribute)
        try {
            laku6TradeIn?.startGUITest()
        } catch (exception: Exception) {
            errorMessage.value = exception
        }
    }

    fun checkLogin() {
        if (!userSession.isLoggedIn)
            askUserLogin.value = TradeinConstants.LOGIN_REQUIRED
        else {
            askUserLogin.value = TradeinConstants.LOGGED_IN
        }
    }

    fun updateLogistics(is3Pl: Boolean) {
        is3PLSelected.value = is3Pl
    }

    fun goToCheckout() {
        tradeInHomeStateLiveData.value = GoToCheckout(imei, laku6DeviceModel.value?.model ?: "", tradeInPrice)
    }

    fun insertLogisticOptions(intent: Intent) {
        launchCatchError(block = {
            val diagnosticsData = getDiagnosticData(intent)
            tradeInUniqueCode = diagnosticsData.tradeInUniqueCode ?: ""
            val data = insertLogisticPreferenceUseCase.insertLogistic(
                is3PLSelected.value ?: false,
                finalPriceInt,
                tradeInPriceInt,
                imei
            )
            data.insertTradeInLogisticPreference.apply {
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
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartOcsRequestParams)
            val result = addToCartOcsUseCase.createObservable(requestParams).toBlocking().single()
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