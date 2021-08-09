package com.tokopedia.tradein.viewmodel

import android.content.Intent
import android.os.Build
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.common_tradein.model.DeviceDiagInputResponse
import com.tokopedia.common_tradein.model.DeviceDiagnostics
import com.tokopedia.common_tradein.model.HomeResult
import com.tokopedia.common_tradein.usecase.ProcessMessageUseCase
import com.tokopedia.tradein.viewmodel.liveState.*
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class TradeInHomeViewModel @Inject constructor(
        private val processMessageUseCase: ProcessMessageUseCase,
        private val userSession: UserSessionInterface
) : BaseTradeInViewModel(),
        LifecycleObserver, Laku6TradeIn.TradeInListener {
    val homeResultData: MutableLiveData<HomeResult> = MutableLiveData()
    val askUserLogin = MutableLiveData<Int>()
    var tradeInParams = TradeInParams()
    var imeiStateLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var imeiResponseLiveData: MutableLiveData<String?> = MutableLiveData()
    var tradeInHomeStateLiveData: MutableLiveData<TradeInHomeState> = MutableLiveData()
    var imei: String? = null
    var finalPrice: String = "-"
    var xSessionId: String = "-"

    var tradeInType: Int = 0

    override fun doOnCreate() {
        super.doOnCreate()
        checkLogin()
    }

    fun checkLogin() {
        if (!userSession.isLoggedIn)
            askUserLogin.value = TradeinConstants.LOGIN_REQUIRED
        else {
            askUserLogin.value = TradeinConstants.LOGEED_IN
        }
    }

    fun processMessage(intent: Intent) {
        val diagnostics = getDiagnosticData(intent)
        if (diagnostics.imei.isEmpty()) {
            diagnostics.imei = imei
        }
        tradeInParams.deviceId = diagnostics.imei
        launchCatchError(block = {
            setDiagnoseResult(processMessageUseCase.processMessage(tradeInParams, diagnostics), diagnostics)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    fun setDiagnoseResult(response: DeviceDiagInputResponse?, diagnostics: DeviceDiagnostics) {
        if (response != null && response.deviceDiagInputRepsponse != null) {
            val result = HomeResult()
            result.isSuccess = true
            if (tradeInParams.newPrice - diagnostics.tradeInPrice >= 0)
                finalPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeInParams.newPrice - diagnostics.tradeInPrice, true)
            if (response.deviceDiagInputRepsponse.isEligible) {
                if (homeResultData.value?.deviceDisplayName != null) {
                    result.deviceDisplayName = homeResultData.value?.deviceDisplayName ?: ""
                }
                result.displayMessage = CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnostics.tradeInPrice!!, true)
                result.priceStatus = HomeResult.PriceState.DIAGNOSED_VALID
            } else {
                result.priceStatus = HomeResult.PriceState.DIAGNOSED_INVALID
                result.displayMessage = CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnostics.tradeInPrice!!, true)
                errorMessage.setValue(response.deviceDiagInputRepsponse.message)
            }
            homeResultData.value = result
        }
    }

    fun getDiagnosticData(intent: Intent): DeviceDiagnostics {
        val result = intent.getStringExtra("test-result")
        return Gson().fromJson(result, DeviceDiagnostics::class.java)
    }

    override fun onFinished(jsonObject: JSONObject) {
        progBarVisibility.value = false
        imeiStateLiveData.value = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && tradeInParams.deviceId == null
        setHomeResultData(jsonObject)

    }

    private fun setHomeResultData(jsonObject: JSONObject) {
        val diagnosedPrice = tradeInParams.usedPrice
        var maxPrice = 0
        var minPrice = 0
        var devicedisplayname = ""
        try {
            maxPrice = jsonObject.getInt("max_price")
            minPrice = jsonObject.getInt("min_price")
            devicedisplayname = jsonObject.getString("model_display_name")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val result = HomeResult()
        result.isSuccess = true
        result.maxPrice = maxPrice
        result.minPrice = minPrice
        if (tradeInParams.newPrice - maxPrice >= 0)
            finalPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeInParams.newPrice - maxPrice, true)
        if (diagnosedPrice > 0) {
            if (diagnosedPrice > tradeInParams.newPrice) {
                result.priceStatus = HomeResult.PriceState.DIAGNOSED_INVALID
            } else {
                result.priceStatus = HomeResult.PriceState.DIAGNOSED_VALID
            }
            result.displayMessage = CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnosedPrice, true)
        } else {
            if (maxPrice > tradeInParams.newPrice) {
                result.priceStatus = HomeResult.PriceState.DIAGNOSED_INVALID
            } else {
                result.displayMessage = String.format("%1\$s",
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, true))
                result.priceStatus = HomeResult.PriceState.NOT_DIAGNOSED
            }
        }
        if (homeResultData.value?.deviceDisplayName != null) {
            result.deviceDisplayName = homeResultData.value?.deviceDisplayName ?: ""
        } else {
            result.deviceDisplayName = devicedisplayname
        }
        progBarVisibility.value = false
        homeResultData.value = result
    }

    override fun onError(jsonObject: JSONObject) {
        progBarVisibility.value = false
        if (imei != null) {
            var errorMessage: String? = null
            try {
                errorMessage = jsonObject.getString("message")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            imeiResponseLiveData.value = errorMessage
        } else {
            val homeResult = HomeResult()
            try {
                homeResult.displayMessage = jsonObject.getString("message")
            } catch (e: JSONException) {
                homeResult.displayMessage = ""
                e.printStackTrace()
            }

            homeResult.isSuccess = false
            homeResultData.value = homeResult
        }
    }

    fun getMaxPrice(laku6TradeIn: Laku6TradeIn, tradeinType: Int) {
        progBarVisibility.value = true
        this.tradeInType = tradeinType
        laku6TradeIn.getMinMaxPrice(this)
    }

    fun initSessionId(laku6TradeIn: Laku6TradeIn) {
        xSessionId = laku6TradeIn.xSessionId
    }

    fun getIMEI(laku6TradeIn: Laku6TradeIn, imei: String?) {
        this.imei = imei
        laku6TradeIn.checkImeiValidation(this, imei)
    }

    fun onHargaFinalClick(deviceId: String?, price: String) {
        tradeInHomeStateLiveData.value = GoToCheckout(deviceId, price)
    }

    fun onInitialPriceClick(imei: String?) {
        tradeInHomeStateLiveData.value = GoToHargaFinal(imei)
    }

}
