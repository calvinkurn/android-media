package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.model.installment.InstallmentResponse
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRICE
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductInstallmentViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>,
        val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main){

    private val installmentResp = MutableLiveData<Result<List<InstallmentBank>>>()
    val transformedInstallment = Transformations.map(installmentResp){
        when(it){
            is Success -> {
                Success(it.data.flatMap { it.installmentList.map { installment -> it.copy(
                        installmentList = listOf(installment))}}.groupBy { it.installmentList.first().term })
            }
            is Fail -> it

        }

    }

    fun loadInstallment(price: Float){
        launchCatchError(block = {
            val installmentParams = mapOf(PARAM_PRICE to price, "qty" to 1)
            val installmentRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_INSTALLMENT],
                    InstallmentResponse::class.java, installmentParams)
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
            val result = withContext(Dispatchers.IO){
                val gqlResponse = graphqlRepository.getReseponse(listOf(installmentRequest), cacheStrategy)
                gqlResponse.getSuccessData<InstallmentResponse>().result
            }
            if (result.response == STATUS_OK){
                installmentResp.value = Success(result.bank)
            }

        }){
            installmentResp.value = Fail(it)
        }
    }

    companion object {
        private const val STATUS_OK = "200"
    }
}