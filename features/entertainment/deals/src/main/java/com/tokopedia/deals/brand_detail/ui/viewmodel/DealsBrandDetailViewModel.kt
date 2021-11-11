package com.tokopedia.deals.brand_detail.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.brand_detail.data.DealsBrandDetail
import com.tokopedia.deals.common.data.DealsNearestLocationParam
import com.tokopedia.deals.common.domain.DealsGqlQueries
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DealsBrandDetailViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
        ): BaseViewModel(dispatcher.io) {

        private val mutableBrandDetail= MutableLiveData<Result<DealsBrandDetail>>()
        val brandDetail: LiveData<Result<DealsBrandDetail>>
                get() = mutableBrandDetail

        fun getBrandDetail(mapParam: Map<String, Any>) {
                launchCatchError(block = {
                        val data = withContext(dispatcher.io) {
                                val graphqlRequest = GraphqlRequest(DealsGqlQueries.getBrandDetail(),
                                        DealsBrandDetail::class.java, mapParam)
                                graphqlRepository.response(listOf(graphqlRequest),
                                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
                        }.getSuccessData<DealsBrandDetail>()

                        mutableBrandDetail.postValue(Success(data))
                }) {
                        mutableBrandDetail.postValue(Fail(it))
                }
        }

        fun createParams(coordinates: String, seoUrl: String): Map<String, Any> =
                mapOf(PARAM_BRAND_DETAIL to arrayListOf(
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_SEO_URL, seoUrl),
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_COORDINATES, coordinates),
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_SIZE, DealsNearestLocationParam.VALUE_SIZE_PRODUCT),
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_PAGE, DealsNearestLocationParam.VALUE_PAGE_BRAND)
                ))

        companion object{
                const val PARAM_BRAND_DETAIL = "params"
        }
}