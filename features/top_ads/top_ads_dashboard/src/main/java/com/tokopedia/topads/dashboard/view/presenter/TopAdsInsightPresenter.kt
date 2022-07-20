package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEditKeywordUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsInsightUseCase
import com.tokopedia.topads.dashboard.view.listener.TopAdsInsightView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import rx.Subscriber
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Pika on 22/7/20.
 */

class TopAdsInsightPresenter @Inject constructor(
    private val topAdsInsightUseCase: TopAdsInsightUseCase,
    private val topAdsEditKeywordUseCase: TopAdsEditKeywordUseCase
) : BaseDaggerPresenter<TopAdsInsightView>() , CoroutineScope{

    private val job = SupervisorJob()

    fun getInsight(resources: Resources) {
        launchCatchError(block = {
            val query = GraphqlHelper.loadRawString(resources, R.raw.gql_query_insights_keyword)
            val requestParams = topAdsInsightUseCase.setParams()

            val data = topAdsInsightUseCase.execute(query, requestParams)

            view?.onSuccessKeywordInsight(data)
        }, onError = {
            it.printStackTrace()
        })
    }

    fun topAdsCreated(groupId: String, query: String, data: List<MutationData>) {
        launchCatchError(block = {
            val requestParams = topAdsEditKeywordUseCase.setParam(groupId, data)

            val response = topAdsEditKeywordUseCase.execute(query, requestParams)

            if (response.topadsManageGroupAds.keywordResponse.errors?.isEmpty()!!)
                view?.onSuccessEditKeywords(response)
            else
                view?.onErrorEditKeyword(response.topadsManageGroupAds.keywordResponse.errors)
        }, onError = {
            it.printStackTrace()
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun detachView() {
        super.detachView()
        job.cancel()
    }
}