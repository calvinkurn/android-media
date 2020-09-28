package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_IDS
import com.tokopedia.topads.common.data.internal.ParamObject.SEARCH_TERM
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_id
import com.tokopedia.topads.common.data.response.KeywordSearch
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.KeywordData
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
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
class KeywordAdsViewModel @Inject constructor(
        private val context: Context,
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val searchKeywordUseCase: GraphqlUseCase<KeywordSearch>,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {


    fun getSuggestionKeyword(productIds: String, groupId: Int, onSuccess: ((List<KeywordData>) -> Unit), onEmpty: (() -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ad_keyword_suggestion),
                                ResponseKeywordSuggestion.Result::class.java, mapOf(PRODUCT_IDS to productIds, GROUP_ID to groupId, SHOP_id to Integer.parseInt(userSession.shopId)))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseKeywordSuggestion.Result>().let {
                        if (it.topAdsGetKeywordSuggestionV3.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topAdsGetKeywordSuggestionV3.data)
                        }
                    }
                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    fun searchKeyword(keyword: String, product_ids: String, onSucceed: (List<SearchData>) -> Unit) {
        GraphqlHelper.loadRawString(context.resources, R.raw.topads_gql_search_keywords)?.let { query ->
            val params = mapOf(PRODUCT_IDS to product_ids,
                    SEARCH_TERM to keyword, SHOP_id to userSession.shopId.toInt())

            searchKeywordUseCase.setTypeClass(KeywordSearch::class.java)
            searchKeywordUseCase.setRequestParams(params)
            searchKeywordUseCase.setGraphqlQuery(query)

            searchKeywordUseCase.execute(
                    onSuccessSearch(onSucceed),
                    onError()
            )

        }
    }


    private fun onSuccessSearch(onSucceed: (List<SearchData>) -> Unit): (KeywordSearch) -> Unit {
        return {
            onSucceed(it.topAdsKeywordSearchTerm.data)
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
        }

    }

}