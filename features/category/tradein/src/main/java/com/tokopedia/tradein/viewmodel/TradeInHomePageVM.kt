package com.tokopedia.tradein.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.common_tradein.model.DeviceDiagnostics
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.request.Laku6TestDataModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.usecase.InsertLogisticPreferenceUseCase
import com.tokopedia.tradein.viewmodel.liveState.GoToCheckout
import com.tokopedia.tradein.viewmodel.liveState.TradeInHomeState
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomePageVM @Inject constructor(
    private val userSession: UserSessionInterface,
    private val insertLogisticPreferenceUseCase: InsertLogisticPreferenceUseCase) : BaseTradeInViewModel(), CoroutineScope {

    val askUserLogin = MutableLiveData<Int>()
    val laku6DeviceModel = MutableLiveData<Laku6DeviceModel>()
    var is3PLSelected = MutableLiveData<Boolean>()
    var tradeInHomeStateLiveData: MutableLiveData<TradeInHomeState> = MutableLiveData()

    private var laku6TradeIn: Laku6TradeIn? = null
    var imei: String = ""
    var price: String = ""

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
        laku6DeviceModel.value = Gson().fromJson(laku6TradeIn?.deviceModel.toString(), Laku6DeviceModel::class.java)
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
                } catch (exception : Exception){
                    errorMessage.value = exception
                }
            }
        }
    }

    fun startLaku6Testing(deviceAttribute: TradeInDetailModel.GetTradeInDetail.DeviceAttribute?) {
        setTestData(deviceAttribute)
        try {
            laku6TradeIn?.startGUITest()
        } catch (exception : Exception){
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
        tradeInHomeStateLiveData.value = GoToCheckout(imei, laku6DeviceModel.value?.model ?: "", price)
    }

    fun insertLogisticOptions(intent: Intent) {
        launchCatchError(block = {
            val diagnosticsData = getDiagnosticData(intent)
            val data = insertLogisticPreferenceUseCase.insertLogistic()
            data?.insertTradeInLogisticPreference?.apply {
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
}