package com.tokopedia.moneyin.viewmodel

import android.content.Intent
import android.os.Build
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.common_tradein.model.*
import com.tokopedia.common_tradein.usecase.ProcessMessageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.moneyin.MoneyinConstants
import com.tokopedia.moneyin.usecase.CheckMoneyInUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class MoneyInHomeViewModel @Inject constructor(
        private val processMessageUseCase: ProcessMessageUseCase,
        private val checkMoneyInUseCase: CheckMoneyInUseCase,
        private val userSession: UserSessionInterface
) : BaseTradeInViewModel(),
        LifecycleObserver, Laku6TradeIn.TradeInListener {
    val homeResultData: MutableLiveData<HomeResult> = MutableLiveData()
    val askUserLogin = MutableLiveData<Int>()
    var tradeInParams = TradeInParams()
    var imeiStateLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var imeiResponseLiveData: MutableLiveData<String?> = MutableLiveData()
    var imei: String? = null
    var finalPrice: String = "-"

    override fun doOnCreate() {
        super.doOnCreate()
        checkLogin()
    }

    fun checkLogin() {
        if (!userSession.isLoggedIn)
            askUserLogin.value = MoneyinConstants.LOGIN_REQUIRED
        else {
            askUserLogin.value = MoneyinConstants.LOGEED_IN
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

    fun checkMoneyIn(modelId: Int, jsonObject: JSONObject) {
        progBarVisibility.value = true
        launchCatchError(block = {
            checkIfElligible(checkMoneyInUseCase.checkMoneyIn(getResource(), modelId, tradeInParams, userSession.userId), jsonObject)
        }, onError = {
            progBarVisibility.value = false
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    private fun checkIfElligible(validateTradePDP: ValidateTradePDP?, jsonObject: JSONObject) {
        validateTradePDP?.let {
            it.response?.let { validateResponse ->
                if (validateResponse.isEligible) {
                    tradeInParams.isEligible = if (validateResponse.isEligible) 1 else 0
                    tradeInParams.usedPrice = validateResponse.usedPrice
                    tradeInParams.isUseKyc = if (validateResponse.isUseKyc) 1 else 0
                    setHomeResultData(jsonObject)
                } else {
                    val result = HomeResult()
                    result.apply {
                        isSuccess = true
                        priceStatus = HomeResult.PriceState.MONEYIN_ERROR
                        displayMessage = validateResponse.message
                    }
                    homeResultData.value = result
                }
            }
        }
    }

    override fun onFinished(jsonObject: JSONObject) {
        progBarVisibility.value = false
        var modelId = 0
        try {
            modelId = jsonObject.getInt("model_id")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && (tradeInParams.deviceId == null || tradeInParams.deviceId == checkMoneyInUseCase.fcmDeviceId)) {
            imeiStateLiveData.value = true
            setHomeResultData(jsonObject)
        } else {
            imeiStateLiveData.value = false
            checkMoneyIn(modelId, jsonObject)
        }

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
            result.priceStatus = HomeResult.PriceState.DIAGNOSED_VALID
            result.displayMessage = CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnosedPrice, true)
        } else {
            result.displayMessage = String.format("%1\$s",
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, true))
            result.priceStatus = HomeResult.PriceState.NOT_DIAGNOSED
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

    fun getMaxPrice(laku6TradeIn: Laku6TradeIn) {
        progBarVisibility.value = true
        laku6TradeIn.getMinMaxPrice(this)
    }

    fun setDeviceId(deviceId: String?) {
        tradeInParams.deviceId = deviceId
    }

}
