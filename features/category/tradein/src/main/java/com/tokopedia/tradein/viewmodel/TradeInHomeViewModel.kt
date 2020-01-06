package com.tokopedia.tradein.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.Constants
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.DeviceAttr
import com.tokopedia.tradein.model.DeviceDiagInput
import com.tokopedia.tradein.model.DeviceDiagInputResponse
import com.tokopedia.tradein.model.DeviceDiagnostics
import com.tokopedia.tradein.view.viewcontrollers.BaseTradeInActivity.TRADEIN_MONEYIN
import com.tokopedia.tradein.view.viewcontrollers.BaseTradeInActivity.TRADEIN_OFFLINE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.json.JSONException
import org.json.JSONObject
import rx.Subscriber
import java.util.*
import kotlin.coroutines.CoroutineContext

class TradeInHomeViewModel(val intent: Intent) : BaseTradeInViewModel(),
        CoroutineScope, LifecycleObserver, Laku6TradeIn.TradeInListener {
    val homeResultData: MutableLiveData<HomeResult> = MutableLiveData()
    val askUserLogin = MutableLiveData<Int>()
    var tradeInParams: TradeInParams
    lateinit var applicationContext:Application

    init {
        tradeInParams = if (intent.hasExtra(TradeInParams::class.java.simpleName)) {
            val parcelable = intent.getParcelableExtra(TradeInParams::class.java.simpleName) as TradeInParams?
            parcelable ?: TradeInParams()
        } else
            TradeInParams()
    }

    var tradeInType: Int = TRADEIN_OFFLINE

    override fun doOnCreate() {
        super.doOnCreate()
        checkLogin()
    }

    fun checkLogin() {
        getMYRepository().let {
            if (!it.getUserLoginState()?.isLoggedIn)
                askUserLogin.value = Constants.LOGIN_REQUIRED
            else {
                askUserLogin.value = Constants.LOGEED_IN
            }
        }
    }

    fun processMessage(intent: Intent) {
        val result = intent.getStringExtra("test-result")
        val diagnostics = Gson().fromJson(result, DeviceDiagnostics::class.java)
        val variables = HashMap<String, Any>()
        try {
            val deviceDiagInput = DeviceDiagInput()
            deviceDiagInput.uniqueCode = diagnostics.tradeInUniqueCode
            val deviceAttr = DeviceAttr()
            deviceAttr.brand = diagnostics.brand
            deviceAttr.grade = diagnostics.grade
            val imei = ArrayList<String>()
            imei.add(diagnostics.imei)
            deviceAttr.imei = imei
            deviceAttr.model = diagnostics.model
            deviceAttr.modelId = diagnostics.modelId
            deviceAttr.ram = diagnostics.ram
            deviceAttr.storage = diagnostics.storage
            deviceDiagInput.deviceAttr = deviceAttr
            deviceDiagInput.deviceId = diagnostics.imei
            tradeInParams.deviceId = diagnostics.imei
            deviceDiagInput.deviceReview = diagnostics.reviewDetails
            deviceDiagInput.newPrice = tradeInParams.newPrice
            deviceDiagInput.oldPrice = diagnostics.tradeInPrice
            deviceDiagInput.tradeInType = tradeInType
            variables["params"] = deviceDiagInput
            val gqlDeviceDiagInput = GraphqlUseCase()
            gqlDeviceDiagInput.clearRequest()
            gqlDeviceDiagInput.addRequest(GraphqlRequest(GraphqlHelper.loadRawString(applicationContext.resources,
                    R.raw.gql_insert_device_diag), DeviceDiagInputResponse::class.java, variables, false))
            gqlDeviceDiagInput.execute(object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(graphqlResponse: GraphqlResponse?) {
                    if (graphqlResponse != null) {
                        val deviceDiagInputResponse = graphqlResponse.getData<DeviceDiagInputResponse>(DeviceDiagInputResponse::class.java)
                        if (deviceDiagInputResponse != null && deviceDiagInputResponse.deviceDiagInputRepsponse != null) {
                            if (deviceDiagInputResponse.deviceDiagInputRepsponse.isEligible) {
                                //                                finalPriceData = new MutableLiveData<>();
                                //                                finalPriceData.setValue(inData);
                                val result = HomeResult()
                                result.isSuccess = true
                                result.priceStatus = HomeResult.PriceState.DIAGNOSED_VALID
                                homeResultData.setValue(result)
                            } else {
                                val result = HomeResult()
                                result.isSuccess = true
                                result.priceStatus = HomeResult.PriceState.DIAGNOSED_INVALID
                                result.displayMessage = CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnostics.tradeInPrice!!, true)
                                homeResultData.value = result
                                errorMessage.setValue(deviceDiagInputResponse.deviceDiagInputRepsponse.message)
                            }
                        }
                    }

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun checkMoneyIn(modelId: Int, jsonObject: JSONObject) {
        progBarVisibility.value = true
        tradeInParams.deviceId = TradeInUtils.getDeviceId(applicationContext)
        tradeInParams.userId = getMYRepository().getUserLoginState().userId?.toInt() ?: 0
        tradeInParams.tradeInType = 2
        tradeInParams.modelID = modelId
        val variables = HashMap<String, Any>()
        variables["params"] = tradeInParams
        launchCatchError(block = {
            val query = GraphqlHelper.loadRawString(applicationContext.resources, com.tokopedia.common_tradein.R.raw.gql_validate_tradein)
            val response = getMYRepository().getGQLData(query, ValidateTradePDP::class.java, variables) as ValidateTradePDP?
            checkIfElligible(response, jsonObject)
        }, onError = {
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
                    result.run {
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
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (tradeInType == 2) {
            checkMoneyIn(modelId, jsonObject)
        } else {
            setHomeResultData(jsonObject)
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
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val result = HomeResult()
        result.isSuccess = true
        if (diagnosedPrice > 0) {
            if (tradeInType != TRADEIN_MONEYIN) {
                if (diagnosedPrice > tradeInParams.newPrice) {
                    result.priceStatus = HomeResult.PriceState.DIAGNOSED_INVALID
                } else {
                    result.priceStatus = HomeResult.PriceState.DIAGNOSED_VALID
                }
            } else {
                result.priceStatus = HomeResult.PriceState.DIAGNOSED_VALID
            }
            result.displayMessage = CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnosedPrice, true)
        } else {
            result.displayMessage = String.format("%1\$s - %2\$s",
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, true),
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, true))
            result.priceStatus = HomeResult.PriceState.NOT_DIAGNOSED

        }
        result.deviceDisplayName = devicedisplayname
        progBarVisibility.value = false
        homeResultData.value = result
    }

    override fun onError(jsonObject: JSONObject) {
        progBarVisibility.value = false
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

    fun getMaxPrice(laku6TradeIn: Laku6TradeIn, tradeinType: Int) {
        progBarVisibility.value = true
        this.tradeInType = tradeinType
        laku6TradeIn.getMinMaxPrice(this)
    }

    fun initilizeAppContext(application: Application) {
        applicationContext = application
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()
}
