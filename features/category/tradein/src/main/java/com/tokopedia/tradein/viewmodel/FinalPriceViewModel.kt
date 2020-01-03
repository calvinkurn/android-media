package com.tokopedia.tradein.viewmodel

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import android.content.Intent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import rx.Subscriber
import java.util.*
import kotlin.coroutines.CoroutineContext

class FinalPriceViewModel(@ApplicationContext val context: Context, val intent: Intent) : BaseTradeInViewModel(), LifecycleObserver, CoroutineScope {
    val deviceDiagData: MutableLiveData<DeviceDataResponse> = MutableLiveData()
    val addressLiveData = MutableLiveData<AddressResult>()
    val STATUS_NO_ADDRESS: Int = 12324
    var tradeInParams: TradeInParams? = null
        private set

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun getDiagnosticData() {
        tradeInParams = intent.getParcelableExtra(TradeInParams::class.java.simpleName)
        val productid = tradeInParams!!.productId
        val deviceid = tradeInParams!!.deviceId
        val newprice = tradeInParams!!.newPrice
        val params = DeviceDiagParams()
        params.productId = productid
        params.deviceId = deviceid
        params.newPrice = newprice
        params.tradeInType = intent.getIntExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, 1)
        val variables1 = HashMap<String, Any>()
        variables1["params"] = params
        val gqlDeviceDiagInput = GraphqlUseCase()
        gqlDeviceDiagInput.clearRequest()
        gqlDeviceDiagInput.addRequest(GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_get_device_diag), DeviceDiagGQL::class.java, variables1, false))
        if (tradeInParams!!.isUseKyc == 1) {
            val variables2 = HashMap<String, Any>()
            variables2["projectID"] = 4
            gqlDeviceDiagInput.addRequest(GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                    R.raw.gql_get_kyc_status), KYCDetailGQL::class.java, variables2, false))
        }

        gqlDeviceDiagInput.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                warningMessage.value = context.getString(com.tokopedia.abstraction.R.string.default_request_error_timeout)
            }

            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (graphqlResponse != null) {
                    val deviceDiagGQL = graphqlResponse.getData<DeviceDiagGQL>(DeviceDiagGQL::class.java)
                    if (deviceDiagGQL != null) {
                        val deviceDataResponse = deviceDiagGQL.diagResponse
                        val kycDetailGQL = graphqlResponse.getData<KYCDetailGQL>(KYCDetailGQL::class.java)
                        if (deviceDataResponse != null) {
                            if (kycDetailGQL != null) {
                                val kycDetails = kycDetailGQL.kycDetails
                                if (kycDetails != null)
                                    deviceDataResponse.kycDetails = kycDetails
                            }
                            deviceDiagData.setValue(deviceDataResponse)
                        } else
                            throw RuntimeException("")
                    } else
                        throw RuntimeException("")
                }
            }
        })
    }

    fun getAddress() {
        progBarVisibility.value = true
        launchCatchError(block = {
            val request = mapOf("is_default" to 1,
                    "limit" to 1,
                    "page" to 1,
                    "show_corner" to false,
                    "show_address" to true)
            val queryString = GraphqlHelper.loadRawString(context.resources, R.raw.tradein_address_corner)
            val response = getMYRepository().getGQLData(queryString, MoneyInKeroGetAddressResponse.ResponseData::class.java, request) as MoneyInKeroGetAddressResponse.ResponseData?
            progBarVisibility.value = false
            response?.let {
                it.keroGetAddress.data?.let { listAddress ->
                    var addressData: MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data? = null
                    if (listAddress.isNotEmpty()) {
                        listAddress.forEach { moneyInKero ->
                            if (moneyInKero.isPrimary) {
                                addressData = moneyInKero
                                return@forEach
                            }
                        }
                    }
                    addressLiveData.value = AddressResult(addressData, it.keroGetAddress.token)
                }
            }
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it.localizedMessage
        })
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

}
