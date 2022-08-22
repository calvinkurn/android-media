package com.tokopedia.product.estimasiongkir.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.estimasiongkir.data.RatesMapper
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.estimasiongkir.usecase.GetRatesEstimateUseCase
import com.tokopedia.product.estimasiongkir.util.ProductDetailShippingLogger
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yehezkiel on 16/02/21
 */
class RatesEstimationBoeViewModel @Inject constructor(private val ratesUseCase: GetRatesEstimateUseCase,
                                                      private val userSession: UserSessionInterface,
                                                      val dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {

    private val _ratesRequest = MutableLiveData<RatesEstimateRequest>()

    fun setRatesRequest(request: RatesEstimateRequest) {
        _ratesRequest.value = request
    }

    val ratesVisitableResult: LiveData<Result<List<ProductShippingVisitable>>> = Transformations.switchMap(_ratesRequest) {
        val result = MutableLiveData<Result<List<ProductShippingVisitable>>>()

        launchCatchError(dispatcher.io, block = {
            val ratesData = getRatesEstimate(it)
            result.postValue(RatesMapper.mapToVisitable(ratesData, it).asSuccess())
        }) {
            ProductDetailShippingLogger.logRateEstimate(
                    throwable = it,
                    rateRequest = _ratesRequest.value,
                    deviceId = userSession.deviceId
            )
            result.postValue(it.asFail())
        }
        result
    }

    private suspend fun getRatesEstimate(request: RatesEstimateRequest): RatesEstimationModel {
        return ratesUseCase.executeOnBackground(
                GetRatesEstimateUseCase.createParams(
                        request.getWeightRequest(),
                        request.shopDomain,
                        request.origin,
                        request.productId,
                        request.shopId,
                        request.isFulfillment,
                        request.destination,
                        request.boType,
                        request.poTime,
                        request.shopTier,
                        generateUniqueId(request),
                        request.orderValue,
                        request.boCampaignIDs),
                request.forceRefresh)
    }

    private fun generateUniqueId(request: RatesEstimateRequest): String {
        return "${request.addressId}-${request.shopId}-${request.poTime}-${request.warehouseId}"
    }
}