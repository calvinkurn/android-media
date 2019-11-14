package com.tokopedia.topads.view.model

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseEtalase
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class ProductAdsListViewModel @Inject constructor(
        private val context: Context,
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val restRepository: RestRepository,
        private val gqlRepository: GraphqlRepository,
        private val urlMap: Map<String, String>): BaseViewModel(dispatcher) {

    fun etalaseList(onSuccess: ((List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>)->Unit), onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_etalase_list),
                                ResponseEtalase.Data::class.java, mapOf("shopId" to userSession.shopId))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        gqlRepository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseEtalase.Data>().shopShowcasesByShopID.let {
                        onSuccess(it.result)
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }

    fun productList(keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int, onSuccess: ((List<ResponseProductList.Data>) -> Unit),
                    onEmpty: (()->Unit), onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val queryMap = mutableMapOf<String, Any>(
                                "keyword" to keyword,
                                "etalase" to etalaseId,
                                "sort_by" to sortBy,
                                "is_promoted" to isPromoted,
                                "rows" to rows,
                                "start" to start,
                                "shop_id" to userSession.shopId
                        )
                        val restRequest = RestRequest.Builder(
                                urlMap[UrlConstant.PATH_PRODUCT_LIST] ?: "",
                                object : TypeToken<ResponseProductList>() {}.type)
                                .setQueryParams(queryMap)
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    (result.getData() as ResponseProductList).data?.let{
                        if(it.isEmpty()){
                            onEmpty()
                        } else {
                            onSuccess(it)
                        }
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }
}