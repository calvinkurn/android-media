package com.tokopedia.topads.edit.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.*
import com.tokopedia.topads.internal.ParamObject
import com.tokopedia.topads.internal.ParamObject.KEYWORD
import com.tokopedia.topads.internal.ParamObject.REQUEST_TYPE
import com.tokopedia.topads.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.internal.ParamObject.SOURCE
import com.tokopedia.topads.internal.ParamObject.SOURCE_VALUE
import com.tokopedia.topads.internal.ParamObject.SUGGESTION
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Pika on 6/4/20.
 */

class EditFormDefaultViewModel @Inject constructor(private val context: Context,
                                                   @Named("Main")
                                                   private val dispatcher: CoroutineDispatcher,
                                                   private val userSession: UserSessionInterface,
                                                   private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateName.Data) -> Unit),
                      onError: ((error: String) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_validate_group_name),
                                ResponseGroupValidateName::class.java, mapOf(ParamObject.SHOP_ID to userSession.shopId.toIntOrZero(), ParamObject.GROUP_NAME to groupName))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseGroupValidateName>().let {
                        if (it.topAdsGroupValidateName.errors.isEmpty()) {
                            onSuccess(it.topAdsGroupValidateName.data)
                        } else {
                            onError(it.topAdsGroupValidateName.errors[0].detail)
                        }
                    }
                },
                onError = {
                    onError(it.localizedMessage)
                }
        )
    }


    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit, onError: ((Throwable) -> Unit), onEmpty: (() -> Unit)) {

        launchCatchError(
                block = {
                    val queryMap = HashMap<String, Any?>()
                    queryMap[ParamObject.SHOP_Id] = Integer.parseInt(userSession.shopId)
                    queryMap[ParamObject.SOURCE] = ParamObject.SOURCE_VALUE
                    queryMap[ParamObject.SUGGESTION] = suggestions
                    queryMap[ParamObject.REQUEST_TYPE] = ParamObject.PRODUCT
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_bid_info), ResponseBidInfo.Result::class.java, queryMap)
                        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseBidInfo.Result>().let {
                        if (it.topadsBidInfo.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topadsBidInfo.data)
                        }
                    }

                },
                onError = {
                    onError(it)
                })
    }

    fun getAds(groupId: Int?, onSuccess: (List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>) -> Unit, onError: ((Throwable) -> Unit), onEmpty: (() -> Unit)) {

        launchCatchError(
                block = {
                    val queryMap = HashMap<String, Any?>()
                    queryMap[ParamObject.SHOP_ID] = Integer.parseInt(userSession.shopId)
                    queryMap[ParamObject.TYPE] = ParamObject.PRODUCT
                    queryMap[ParamObject.PAGE] = 1
                    queryMap[ParamObject.PER_PAGE] = 20
                    queryMap[ParamObject.GROUPID] = groupId
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_get_ads_product), GetAdProductResponse::class.java, queryMap)
                        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<GetAdProductResponse>().let {
                        if (it.topadsGetListProductsOfGroup.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topadsGetListProductsOfGroup.data)
                        }
                    }

                },
                onError = {
                    onError(it)
                })
    }

    fun getAdKeyword(onSuccess: ((List<GetKeywordResponse.KeywordsItem>) -> Unit), onError: ((String) -> Unit), onEmpty: (() -> Unit)) {
        launchCatchError(
                block = {
                    val filter = mutableMapOf<String, String>()
                    filter[ParamObject.SHOP_id] = userSession.shopId
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_get_ad_keyword),
                                GetKeywordResponse::class.java, mapOf(ParamObject.SOURCE to "editkeyword", "filter" to filter))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<GetKeywordResponse>().let {
                        when {
                            it.topAdsListKeyword.data.keywords.isEmpty() -> {
                                onEmpty()
                            }
                            it.topAdsListKeyword.error.detail != "" -> {
                                onError(it.topAdsListKeyword.error.detail)
                            }
                            else -> {
                                onSuccess(it.topAdsListKeyword.data.keywords)
                            }
                        }
                    }
                },
                onError = {
                    onError(it.localizedMessage)
                })
    }


    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit, onError: ((Throwable) -> Unit), onEmpty: (() -> Unit)) {

        launchCatchError(
                block = {
                    val queryMap = HashMap<String, Any?>()
                    queryMap[SHOP_Id] = Integer.parseInt(userSession.shopId)
                    queryMap[SOURCE] = SOURCE_VALUE
                    queryMap[SUGGESTION] = suggestions
                    queryMap[REQUEST_TYPE] = KEYWORD
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_bid_info), ResponseBidInfo.Result::class.java, queryMap)
                        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseBidInfo.Result>().let {
                        if (it.topadsBidInfo.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topadsBidInfo.data)
                        }
                    }

                },
                onError = {
                    onError(it)
                }
        )
    }

    fun topAdsCreated(param: HashMap<String, Any>, onSuccessGetDeposit: ((FinalAdResponse) -> Unit),
                      onErrorGetAds: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_edit_activated_ads),
                                FinalAdResponse::class.java,
                                param)
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<FinalAdResponse>().let {
                        onSuccessGetDeposit(it)
                    }
                },
                onError = {
                    onErrorGetAds(it)
                }
        )
    }
}

