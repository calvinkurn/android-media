package com.tokopedia.topads.headline.view.model

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.model.StatsData
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetAdKeywordUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetProductKeyCountUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsKeywordsActionUseCase
import com.tokopedia.topads.dashboard.view.presenter.StatsList
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.headline.data.ShopAdInfo
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Pika on 19/10/20.
 */

class HeadlineDetailViewModel @Inject constructor(@Named("Main")
                                                  private val dispatcher: CoroutineDispatcher,
                                                  private val topAdsGetAdKeywordUseCase: TopAdsGetAdKeywordUseCase,
                                                  private val topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase,
                                                  private val topAdsKeywordsActionUseCase: TopAdsKeywordsActionUseCase) : BaseViewModel(dispatcher) {


    fun getGroupKeywordData(resources: Resources, isPositive: Int, group: Int, search: String, sort: String?, status: Int?, page: Int, onSuccess: ((KeywordsResponse.GetTopadsDashboardKeywords) -> Unit), onEmpty: (() -> Unit)) {
        topAdsGetAdKeywordUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_get_keywords_group))
        topAdsGetAdKeywordUseCase.setParams(isPositive, group, search, sort, status, page)
        topAdsGetAdKeywordUseCase.executeQuerySafeMode(
                {
                    if (it.getTopadsDashboardKeywords.data.isEmpty()) {
                        onEmpty()
                    } else
                        onSuccess(it.getTopadsDashboardKeywords)
                },
                {
                    it.printStackTrace()
                })
    }

    fun setKeywordAction(action: String, keywordIds: List<String>, resources: Resources, onSuccess: (() -> Unit)) {
        topAdsKeywordsActionUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_keyword_action))
        topAdsKeywordsActionUseCase.setParams(action, keywordIds)
        topAdsKeywordsActionUseCase.executeQuerySafeMode(
                {
                    onSuccess()
                },
                {
                    it.printStackTrace()
                })
    }


    fun getCountProductKeyword(resources: Resources, groupIds: List<String>, onSuccess: ((List<CountDataItem>) -> Unit)) {
        topAdsGetProductKeyCountUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_total_products_keywords))
        topAdsGetProductKeyCountUseCase.setParams(groupIds)
        topAdsGetProductKeyCountUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsGetTotalAdsAndKeywords.data)
                },
                {
                    it.printStackTrace()
                })
    }

}