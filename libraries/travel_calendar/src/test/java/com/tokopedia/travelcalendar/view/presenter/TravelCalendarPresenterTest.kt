package com.tokopedia.travelcalendar.view.presenter

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.travelcalendar.domain.GetHolidayUseCase
import com.tokopedia.travelcalendar.domain.TravelCalendarTestScheduler
import com.tokopedia.travelcalendar.view.TravelCalendarContract
import com.tokopedia.travelcalendar.view.model.HolidayDetail
import com.tokopedia.travelcalendar.view.model.HolidayResult
import com.tokopedia.usecase.RequestParams
import org.junit.Before
import org.junit.Test
import rx.Observable
import java.util.*

/**
 * Created by nabillasabbaha on 18/01/19.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TravelCalendarPresenterTest {

    @RelaxedMockK
    lateinit var view: TravelCalendarContract.View

    private val getHolidayUseCase: GetHolidayUseCase = mockk()
    private val presenter: TravelCalendarPresenter = TravelCalendarPresenter(getHolidayUseCase, TravelCalendarTestScheduler())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun showErrorGetHolidayDatesWhenFailed() {
        //given
        val message = "Terjadi kesalahan. Ulangi beberapa saat lagi"
        val requestParam = RequestParams.EMPTY
        val throwable = Throwable(MessageErrorException(message))
        every { getHolidayUseCase.createObservable(requestParam) } returns Observable.error(MessageErrorException(message))
        //when
        presenter.getDataHolidayCalendar(true)
        //then
        verify { view.renderErrorMessage(any()) }
        confirmVerified(view)
    }

    @Test
    fun showHolidayDatesWhenSuccess() {
        //given
        val holidayResultList = ArrayList<HolidayResult>()
        val holidayDetail = HolidayDetail("25-12-10", "Natal", Date())
        val holidayResult = HolidayResult("25-12-10", holidayDetail)
        holidayResultList.add(holidayResult)
        val requestParam = RequestParams.EMPTY
        every { getHolidayUseCase.createObservable(requestParam) } returns Observable.just(holidayResultList)
        //when
        presenter.getDataHolidayCalendar(true)
        //then
        verify { view.renderAllHolidayEvent(holidayResultList) }
        confirmVerified(view)
    }
}