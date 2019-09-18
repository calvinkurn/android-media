package com.tokopedia.affiliate.feature.dashboard.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.common.domain.usecase.CheckAffiliateUseCase
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetAffiliateDashboardUseCase
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.CheckAffiliateSubscriber
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetAffiliateDashboardSubscriber
import com.tokopedia.calendar.Legend
import com.tokopedia.common.travel.data.entity.TravelCalendarHoliday
import com.tokopedia.common.travel.domain.TravelCalendarHolidayUseCase
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 2019-09-02.
 */
class AffiliateDashboardPresenter
@Inject constructor(
        private val getAffiliateDashboardUseCase: GetAffiliateDashboardUseCase,
        private val checkAffiliateUseCase: CheckAffiliateUseCase,
        private val travelCalendarHolidayUseCase: TravelCalendarHolidayUseCase
) : BaseDaggerPresenter<AffiliateDashboardContract.View>(), AffiliateDashboardContract.Presenter, CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun checkAffiliate() {
        checkAffiliateUseCase.execute(CheckAffiliateSubscriber(view))
    }

    override fun loadDashboardDetail(startDate: Date?, endDate: Date?) {
        view.showLoading()
        getAffiliateDashboardUseCase.run {
            clearRequest()
            addRequest(getRequest(startDate, endDate))
            execute(GetAffiliateDashboardSubscriber(view))
        }
    }

    override fun loadHolidayList() {
        launch {
            val result = travelCalendarHolidayUseCase.execute()
            withContext(Dispatchers.Main) {
                when (result) {
                    is Success -> view.onGetHolidayList(mappingHolidayData(result.data))
                    is Fail -> view.onErrorGetHoliday(result.throwable)
                }
            }
        }
    }

    override fun detachView() {
        super.detachView()
        getAffiliateDashboardUseCase.unsubscribe()
        checkAffiliateUseCase.unsubscribe()
        job.cancel()
    }

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, holiday.attribute.date),
                    holiday.attribute.label))
        }
        return legendList
    }
}