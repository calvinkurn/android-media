package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEditKeywordUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsInsightUseCase
import com.tokopedia.topads.dashboard.view.listener.TopAdsInsightView
import javax.inject.Inject

/**
 * Created by Pika on 22/7/20.
 */

class TopAdsInsightPresenter @Inject constructor(private val topAdsInsightUseCase: TopAdsInsightUseCase,
                                                 private val topAdsEditKeywordUseCase: TopAdsEditKeywordUseCase
) : BaseDaggerPresenter<TopAdsInsightView>() {

    fun getInsight(resources: Resources) {
        topAdsInsightUseCase.run {
            setGraphqlQuery(GraphqlHelper.loadRawString(resources, R.raw.gql_query_insights_keyword))
            setParams()
            executeQuerySafeMode(
                    {
                        view?.onSuccessKeywordInsight(it)
                    }, {
                it.printStackTrace()
            })
        }
    }

    fun topAdsCreated(groupId: String, query: String, data: List<MutationData>) {
        topAdsEditKeywordUseCase.setParam(groupId, query, data)
        topAdsEditKeywordUseCase.executeQuerySafeMode(
                {
                    if(it.topadsManageGroupAds.keywordResponse.errors?.isEmpty()!!)
                        view?.onSuccessEditKeywords(it)
                    else
                        view?.onErrorEditKeyword(it.topadsManageGroupAds.keywordResponse.errors)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }


    override fun detachView() {
        super.detachView()
        topAdsInsightUseCase.cancelJobs()
    }

}