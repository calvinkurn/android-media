package com.tokopedia.topads.view.model

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseEtalase
import com.tokopedia.topads.data.response.ResponseGroupValidate
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
        private val repository: GraphqlRepository): BaseViewModel(dispatcher) {

    companion object {
        val PRODUCT_IDS = "productIds"
        val GROUP_ID = "groupId"
    }


    fun getSugestionKeyword(productIds: String, groupId: Int, onSuccess: ((List<ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data>) -> Unit),
                      onError: ((Throwable) -> Unit), onEmpty:(()->Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_etalase_list),
                                ResponseKeywordSuggestion::class.java, mapOf(PRODUCT_IDS to productIds, GROUP_ID to groupId))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseKeywordSuggestion>().let {
                        if(it.topAdsGetKeywordSuggestion.data.isEmpty()){
                            onEmpty()
                        } else {
                            onSuccess(it.topAdsGetKeywordSuggestion.data)
                        }
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }
}