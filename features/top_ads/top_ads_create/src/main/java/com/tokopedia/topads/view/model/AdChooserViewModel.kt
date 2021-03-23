package com.tokopedia.topads.view.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.data.response.TopAdsAutoAdsCreate
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject

class AdChooserViewModel @Inject constructor(private val context: Context,
                                             private val userSession: UserSessionInterface,
                                             private val dispatcher: CoroutineDispatchers,
                                             private val repository: GraphqlRepository) : BaseViewModel(dispatcher.main) {

    private val CHANNEL = "topchat"
    private val SOURCE = "one_click_promo"
    val autoAdsData = MutableLiveData<TopAdsAutoAdsCreate.Response.TopAdsAutoAdsData>()

    fun getAdsState(onSuccess: ((AdCreationOption) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(dispatcher.io) {
                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_autoads_shop_info),
                                AdCreationOption::class.java, hashMapOf(SHOP_Id to userSession.shopId.toIntOrZero()))
                        val cacheStrategy = RequestHelper.getCacheStrategy()
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
            val data = withContext(dispatcher.io) {
                val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_post_autoads),
                        TopAdsAutoAdsCreate.Response::class.java, getParams(param).parameters)
                val cacheStrategy = RequestHelper.getCacheStrategy()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<TopAdsAutoAdsCreate.Response>().autoAds.data.let {
                autoAdsData.postValue(it)
            }
        }) {
            it.printStackTrace()
        }
    }

    private fun getParams(dataParams: AutoAdsParam): RequestParams {
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
                    val data = withContext(dispatcher.io) {
                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_auto_ads_status),
                                AutoAdsResponse::class.java, hashMapOf(SHOP_Id to userSession.shopId.toIntOrZero()))
                        val cacheStrategy = RequestHelper.getCacheStrategy()
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