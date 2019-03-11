package com.tokopedia.product.detail.estimasiongkir.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class RatesEstimationDetailViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named(RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION)
        private val rawQuery: String,
        @Named("Main")
        val dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    val rateEstResp =  MutableLiveData<Result<RatesEstimationModel>>()

    fun getCostEstimation(productWeight: Float, shopDomain: String = ""){
        val params = mapOf(PARAM_PRODUCT_WEIGHT to productWeight,
                PARAM_SHOP_DOMAIN to shopDomain)
        val request = GraphqlRequest(rawQuery, RatesEstimationModel.Response::class.java, params)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
        launchCatchError(block = {
            val result = withContext(Dispatchers.IO){
                val resp = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
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