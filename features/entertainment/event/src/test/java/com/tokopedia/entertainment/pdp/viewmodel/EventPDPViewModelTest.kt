package com.tokopedia.entertainment.pdp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.calendar.Legend
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.*
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPAboutEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPHighlightEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPInformationEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPLocationDetailEntity
import com.tokopedia.entertainment.pdp.data.redeem.validate.EventValidateResponse
import com.tokopedia.entertainment.pdp.network_api.GetWhiteListValidationUseCase
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class EventPDPViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventPDPViewModel: EventPDPViewModel

    @MockK
    lateinit var eventProductDetailUseCase: EventProductDetailUseCase

    @RelaxedMockK
    lateinit var getEventWhiteListValidationUseCase: GetWhiteListValidationUseCase

    @MockK
    lateinit var usecaseHoliday: TravelCalendarHolidayUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventPDPViewModel = EventPDPViewModel(Dispatchers.Unconfined, eventProductDetailUseCase,
                usecaseHoliday, getEventWhiteListValidationUseCase)
        eventPDPViewModel.getIntialList()
    }

    @Test
    fun `DataPDP_NotNullData_ShowNotNullData`(){
        assertNotNull(eventProductDetailUseCase)
        assertNotNull(usecaseHoliday)
        assertNotNull(eventPDPViewModel)
    }

    @Test
    fun `PDPandHolidayData_ShouldReturnPDPandHoliday_ShowActualResult`(){
        //given
        val pdpMock = Gson().fromJson(getJson("pdp_mock.json"), EventProductDetailEntity::class.java)
        val contentMock = Gson().fromJson(getJson("content_mock.json"), EventContentByIdEntity::class.java)

        val eventCombined = EventPDPContentCombined(contentMock, pdpMock)

        val travelHoliday = TravelCalendarHoliday(id = "123123", attribute = TravelCalendarHoliday.HolidayAttribute("2020-01-01", label = "LabelTest"))
        val travelHolidayData = TravelCalendarHoliday.HolidayData(listOf(travelHoliday))

        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", true, "")
        } returns Success(eventCombined)

        coEvery {
            usecaseHoliday.execute()
        } returns Success(travelHolidayData)

        //when
        eventPDPViewModel.getDataProductDetail("", "", "")

        //then
        assertNotNull(eventPDPViewModel.eventProductDetail.value)
        assertNotNull(eventPDPViewModel.eventHoliday.value)
        assertNull(eventPDPViewModel.isError.value)

        assertEquals(eventPDPViewModel.eventProductDetail.value, pdpMock)
    }

    @Test
    fun `PDPandHolidayData_ShouldFailPDPandSuccessHoliday_FailActualResult`(){
        //given
        val travelHoliday = TravelCalendarHoliday(id = "123123", attribute = TravelCalendarHoliday.HolidayAttribute("2020-01-01", label = "LabelTest"))
        val travelHolidayData = TravelCalendarHoliday.HolidayData(listOf(travelHoliday))

        val error = Throwable("Error PDP")

        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", true, "")
        } returns Fail(error)

        coEvery {
            usecaseHoliday.execute()
        } returns Success(travelHolidayData)

        //when
        eventPDPViewModel.getDataProductDetail("", "", "")

        //then
        assertNull(eventPDPViewModel.eventProductDetail.value)
        assertNotNull(eventPDPViewModel.isError.value)

        assertNotNull(eventPDPViewModel.eventHoliday.value)
        assertEquals(eventPDPViewModel.isError.value?.message, error.message)
    }

    @Test
    fun `PDPandHolidayData_ShouldSuccessPDPandFailHoliday_FailActualResult`(){
        //given
        val pdpMock = Gson().fromJson(getJson("pdp_mock.json"), EventProductDetailEntity::class.java)
        val contentMock = Gson().fromJson(getJson("content_mock.json"), EventContentByIdEntity::class.java)

        val eventCombined = EventPDPContentCombined(contentMock, pdpMock)

        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", true, "")
        } returns Success(eventCombined)

        coEvery {
            usecaseHoliday.execute()
        } returns Fail(Throwable())

        //when
        eventPDPViewModel.getDataProductDetail("", "", "")

        //then
        assertNotNull(eventPDPViewModel.eventProductDetail.value)
        assertNull(eventPDPViewModel.isError.value)
        assert(eventPDPViewModel.eventHoliday.value == arrayListOf<Legend>())

    }

    @Test
    fun `PDPandHolidayData_ShouldFailPDPandFailHoliday_FailActualResult`(){
        //given
        val error = Throwable("Error PDP")
        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", true, "")
        } returns Fail(error)


        coEvery {
            usecaseHoliday.execute()
        } returns Fail(Throwable())

        //when
        eventPDPViewModel.getDataProductDetail("", "", "")

        //then
        assertNull(eventPDPViewModel.eventProductDetail.value)
        assertNotNull(eventPDPViewModel.isError.value)
        assert(eventPDPViewModel.eventHoliday.value == arrayListOf<Legend>())
        assertEquals(eventPDPViewModel.isError.value?.message, error.message)
    }


    @Test
    fun `PDPHighlightMapper_ShouldReturnHighlightMapper_ShowActualResult`(){
        //given
        val eventHighlight = EventPDPHighlightEntity("aaa", "bbb", listOf())
        //when
        eventPDPViewModel.getDataHighlight(eventHighlight)
        //then
        assertNotNull(eventPDPViewModel.eventProductDetailList)
        assertEquals(eventHighlight, eventPDPViewModel.eventProductDetailList.value?.get(EventPDPViewModel.EVENT_PDP_DESC_ORDER))
    }

    @Test
    fun `PDPAboutMapper_ShouldReturnAboutMapper_ShowActualResult`(){
        //given
        val eventPDPAbout = EventPDPAboutEntity("tetirully", SectionData(listOf(), 11, "rulll"))
        //when
        eventPDPViewModel.getDataAbout(eventPDPAbout)
        //then
        assertNotNull(eventPDPViewModel.eventProductDetailList)
        assertEquals(eventPDPAbout, eventPDPViewModel.eventProductDetailList.value?.get(EventPDPViewModel.EVENT_PDP_ABOUT_ORDER))
    }

    @Test
    fun `PDPLocationMapper_ShouldReturnLocationMapper_ShowActualResult`(){
        //given
        val eventPDPLocationDetailEntity = EventPDPLocationDetailEntity(Outlet("q2331", "asdasda"), SectionData(listOf(), 55, "123") )
        //when
        eventPDPViewModel.getDataLocationDetail(eventPDPLocationDetailEntity)
        //then
        assertNotNull(eventPDPViewModel.eventProductDetailList)
        assertEquals(eventPDPLocationDetailEntity, eventPDPViewModel.eventProductDetailList.value?.get(EventPDPViewModel.EVENT_PDP_LOCATION_DETAIL_ORDER))
    }

    @Test
    fun `PDPInformationMapper_ShouldReturnInformationMapper_ShowActualResult`(){
        //given
        val eventPDPInformationEntity = EventPDPInformationEntity(SectionData(listOf(), 1231, "saidsaiRull"))
        //when
        eventPDPViewModel.getDataInformation(eventPDPInformationEntity)
        //then
        assertNotNull(eventPDPViewModel.eventProductDetailList)
        assertEquals(eventPDPInformationEntity, eventPDPViewModel.eventProductDetailList.value?.get(EventPDPViewModel.EVENT_PDP_INFORMATION_ORDER))
    }

    @Test
    fun `PDPWhiteList_ShouldReturnWhiteListTrue_ShowActualResult`(){
        //given
        val whiteListMock = Gson().fromJson(getJson("whitelist_mock.json"), EventValidateResponse::class.java)
        val restResponse = RestResponse(whiteListMock, 200, false)
        val whiteListMapped = mapOf<Type, RestResponse>(
                EventValidateResponse::class.java to restResponse
        )

        coEvery {
            getEventWhiteListValidationUseCase.executeOnBackground()
        } returns whiteListMapped

        //when

        eventPDPViewModel.getWhiteListUser(0,"", ProductDetailData(id="0"))

        //then

        assertNotNull(eventPDPViewModel.validateScanner.value)
        assertEquals(eventPDPViewModel.validateScanner.value, true)
    }

    @Test
    fun `PDPWhiteList_ShouldReturnWhiteListFalse_ShowFalseResult`(){
        //given
        val restResponse = RestResponse(EventValidateResponse(), 400, false)
        val whiteListMapped = mapOf<Type, RestResponse>(
                EventValidateResponse::class.java to restResponse
        )

        coEvery {
            getEventWhiteListValidationUseCase.executeOnBackground()
        } returns whiteListMapped

        //when
        eventPDPViewModel.getWhiteListUser(0,"", ProductDetailData(id="0"))

        //then
        assertNotNull(eventPDPViewModel.validateScanner.value)
        assertEquals(eventPDPViewModel.validateScanner.value, false)
    }
}