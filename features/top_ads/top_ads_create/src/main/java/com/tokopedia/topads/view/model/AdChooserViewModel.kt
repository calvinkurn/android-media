package com.tokopedia.topads.view.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.param.AutoAdsParam
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.data.response.AutoAdsResponse
import com.tokopedia.topads.data.response.TopAdsAutoAds
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject
import javax.inject.Named

class AdChooserViewModel @Inject constructor(private val context: Context,
                                             private val userSession: UserSessionInterface,
                                             @Named("Main")
                                             private val dispatcher: CoroutineDispatcher,
                                             private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {

    val CHANNEL = "topchat"
    val SOURCE = "one_click_promo"
    val autoAdsData = MutableLiveData<TopAdsAutoAds.Response.TopAdsAutoAdsData>()

    fun getAdsState(onSuccess: ((AdCreationOption) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_ads_creation_shop_info),
                                AdCreationOption::class.java, mapOf(SHOP_Id to userSession.shopId.toIntOrZero()))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<AdCreationOption>().let {
                        onSuccess(it)
                    }
                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    fun postAutoAds(toggle_status: String, budget: Int) {
        launchCatchError(block = {
            val param = AutoAdsParam(AutoAdsParam.Input(
                    toggle_status,
                    CHANNEL,
                    budget,
                    userSession.shopId.toInt(),
                    SOURCE
            ))
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_post_autoads)
                        , TopAdsAutoAds.Response::class.java, getParams(param).parameters)
                val cacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<TopAdsAutoAds.Response>().autoAds.data.let {
                autoAdsData.postValue(it)
            }
        }) {
            it.printStackTrace()
        }
    }

    fun getParams(dataParams: AutoAdsParam): RequestParams {
        val params = RequestParams.create()
        try {
            params.putAll(Utils.jsonToMap(Gson().toJson(dataParams)))
        } catch (e: JSONException) {
            e.printStackTrace()
        } finally {
            return params
        }
    }

    fun getAutoAdsStatus(onSuccess: ((AutoAdsResponse) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_auto_ads_status),
                                AutoAdsResponse::class.java, mapOf(SHOP_Id to userSession.shopId.toIntOrZero()))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<AutoAdsResponse>().let {
                        onSuccess(it)
                    }
                },
                onError = {
                    it.printStackTrace()
                }
        )
    }
}