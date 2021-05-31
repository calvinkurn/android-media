package com.tokopedia.product.estimasiongkir.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class RatesEstimationDetailViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named(RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION)
        private val rawQuery: String,
        val dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.main){

    val rateEstResp =  MutableLiveData<Result<RatesEstimationModel>>()

    fun getCostEstimation(productWeight: Float, shopDomain: String = "", origin: String?,
                          shopId: String, productId: String){
        val params = mapOf(
                PARAM_PRODUCT_WEIGHT to productWeight,
                PARAM_SHOP_DOMAIN to shopDomain,
                "origin" to origin,
                "shop_id" to shopId,
                "product_id" to productId)
        val request = GraphqlRequest(rawQuery, RatesEstimationModel.Response::class.java, params)

        launchCatchError(block = {
            val result = withContext(dispatcher.io){
                val resp = graphqlRepository.getReseponse(listOf(request))
                    .getSuccessData<RatesEstimationModel.Response>().data?.data ?: throw NullPointerException()

                val filteredService = resp.rates.services.asSequence()
                        .filter { it.status == 200 || it.error.id == "501" }
                        .map { service ->  service.copy(
                                products = service.products.asSequence()
                                        .filter { it.status == 200 || it.error.id == "501" }.toList())}
                        .filter { it.products.isNotEmpty() }.toList()
                resp.copy(rates = resp.rates.copy(services = filteredService))
            }

            rateEstResp.value = Success(result)
        }){
            rateEstResp.value = Fail(it)
        }
    }

    companion object {
        private const val PARAM_PRODUCT_WEIGHT = "weight"
        private const val PARAM_SHOP_DOMAIN = "domain"
    }
}