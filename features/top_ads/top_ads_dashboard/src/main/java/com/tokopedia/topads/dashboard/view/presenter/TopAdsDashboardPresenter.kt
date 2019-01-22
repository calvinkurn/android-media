package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositUseCase
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DashboardPopulateResponse
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.topads.dashboard.data.model.ticker.Data
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsTotalAdUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetPopulateDataAdUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsPopulateTotalAdsUseCase
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import javax.inject.Inject

import rx.Subscriber

/**
 * Created by hadi.putra on 23/04/18.
 */

class TopAdsDashboardPresenter @Inject
constructor(private val topAdsGetShopDepositUseCase: TopAdsGetShopDepositUseCase,
            private val getShopInfoUseCase: GetShopInfoUseCase,
            private val topAdsDatePickerInteractor: TopAdsDatePickerInteractor,
            private val topAdsPopulateTotalAdsUseCase: TopAdsPopulateTotalAdsUseCase,
            private val topAdsGetStatisticsUseCase: TopAdsGetStatisticsUseCase,
            private val topAdsAddSourceTaggingUseCase: TopAdsAddSourceTaggingUseCase,
            private val topAdsGetPopulateDataAdUseCase: TopAdsGetPopulateDataAdUseCase,
            private val deleteTopAdsStatisticsUseCase: DeleteTopAdsStatisticsUseCase,
            private val deleteTopAdsTotalAdUseCase: DeleteTopAdsTotalAdUseCase,
            private val userSession: UserSession) : BaseDaggerPresenter<TopAdsDashboardView>() {

    val lastSelectionDatePickerIndex: Int
        get() = topAdsDatePickerInteractor.lastSelectionDatePickerIndex

    val lastSelectionDatePickerType: Int
        get() = topAdsDatePickerInteractor.lastSelectionDatePickerType

    val endDate: Date
        get() {
            val endCalendar = Calendar.getInstance()
            return topAdsDatePickerInteractor.getEndDate(endCalendar.time)
        }

    val startDate: Date
        get() {
            val startCalendar = Calendar.getInstance()
            startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK)
            return topAdsDatePickerInteractor.getStartDate(startCalendar.time)
        }

    fun getPopulateDashboardData(rawQuery: String) {
        topAdsGetPopulateDataAdUseCase.execute(TopAdsGetPopulateDataAdUseCase
                .createRequestParams(rawQuery, Integer.parseInt(userSession.shopId)),
                object : Subscriber<DashboardPopulateResponse>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.onErrorPopulateData(e)
                    }

                    override fun onNext(dashboardPopulateResponse: DashboardPopulateResponse) {
                        view?.onSuccessPopulateData(dashboardPopulateResponse)
                    }
                })
    }

    fun getShopDeposit() {
        topAdsGetShopDepositUseCase.execute(TopAdsGetShopDepositUseCase.createParams(userSession.shopId),
                object : Subscriber<DataDeposit>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.onLoadTopAdsShopDepositError(e)
                    }

                    override fun onNext(dataDeposit: DataDeposit) {
                        view?.onLoadTopAdsShopDepositSuccess(dataDeposit)
                    }
                })
    }

    fun getShopInfo() {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(userSession.shopId), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view?.onErrorGetShopInfo(e)
            }

            override fun onNext(shopInfo: ShopInfo) {
                view?.onSuccessGetShopInfo(shopInfo)
            }
        })
    }

    fun saveSourceTagging(@TopAdsSourceOption source: String) {
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source),
                object : Subscriber<Void>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {}

                    override fun onNext(aVoid: Void) {}
                })
    }

    override fun detachView() {
        super.detachView()
        topAdsGetShopDepositUseCase.unsubscribe()
        getShopInfoUseCase.unsubscribe()
        topAdsPopulateTotalAdsUseCase.unsubscribe()
        topAdsGetStatisticsUseCase.unsubscribe()
        topAdsAddSourceTaggingUseCase.unsubscribe()
        deleteTopAdsStatisticsUseCase.unsubscribe()
        deleteTopAdsTotalAdUseCase.unsubscribe()
        topAdsGetPopulateDataAdUseCase.unsubscribe()
    }

    fun isDateUpdated(startDate: Date?, endDate: Date?): Boolean {
        if (startDate == null || endDate == null) {
            return true
        }
        var dateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate)
        var dateTextCache = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate)
        if (!dateText.equals(dateTextCache, ignoreCase = true)) {
            return true
        }
        dateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate)
        dateTextCache = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate)
        return (!dateText.equals(dateTextCache, ignoreCase = true))
    }

    fun saveDate(startDate: Date, endDate: Date) {
        topAdsDatePickerInteractor.saveDate(startDate, endDate)
    }

    fun saveSelectionDatePicker(selectionType: Int, lastSelection: Int) {
        topAdsDatePickerInteractor.saveSelectionDatePicker(selectionType, lastSelection)
    }

    fun populateTotalAds() {
        topAdsPopulateTotalAdsUseCase.execute(TopAdsPopulateTotalAdsUseCase.createRequestParams(userSession.shopId),
                object : Subscriber<TotalAd>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.onErrorPopulateTotalAds(e)
                    }

                    override fun onNext(totalAd: TotalAd) {
                        view?.onSuccessPopulateTotalAds(totalAd)
                    }
                })
    }

    fun getTopAdsStatistic(startDate: Date, endDate: Date, @TopAdsStatisticsType selectedStatisticType: Int) {
        topAdsGetStatisticsUseCase.execute(TopAdsGetStatisticsUseCase.createRequestParams(startDate, endDate,
                selectedStatisticType, userSession.shopId), object : Subscriber<DataStatistic>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onErrorGetStatisticsInfo(e)
            }

            override fun onNext(dataStatistic: DataStatistic) {
                view?.onSuccesGetStatisticsInfo(dataStatistic)
            }
        })
    }

    fun getTickerTopAds(resources: Resources) {
        val graphqlUseCase = GraphqlUseCase()
        val shopId: Int = userSession.shopId.toIntOrNull() ?: 0
        val variables = mapOf<String, Any>(TopAdsDashboardConstant.SHOP_ID to shopId)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.query_ticker), Data::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onErrorGetTicker(e)
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val topAdsTicker = graphqlResponse.getData<Data>(Data::class.java)
                view?.onSuccessGetTicker(topAdsTicker.topAdsTicker.data.message ?: listOf())
            }
        })
    }

    fun clearStatisticsCache() {
        deleteTopAdsStatisticsUseCase.executeSync()
    }

    fun clearTotalAdCache() {
        deleteTopAdsTotalAdUseCase.executeSync()
    }

    fun resetDate() {
        topAdsDatePickerInteractor.resetDate()
    }
}
