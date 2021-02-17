package com.tokopedia.entertainment.pdp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.calendar.Legend
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.*
import com.tokopedia.entertainment.pdp.data.pdp.EventVerifyResponseV2
import com.tokopedia.entertainment.pdp.data.pdp.VerifyRequest
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class EventPDPTicketViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventPDPTicketViewModel: EventPDPTicketViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var eventProductDetailUseCase: EventProductDetailUseCase

    @MockK
    lateinit var usecaseHoliday: TravelCalendarHolidayUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        eventPDPTicketViewModel = EventPDPTicketViewModel(Dispatchers.Unconfined, graphqlRepository, eventProductDetailUseCase, usecaseHoliday)
    }

    @Test
    fun `DataTicket_NotNullData_Shownotnulldata`() {
        Assert.assertNotNull(eventProductDetailUseCase)
        Assert.assertNotNull(usecaseHoliday)
        Assert.assertNotNull(graphqlRepository)
        Assert.assertNotNull(eventPDPTicketViewModel)
    }

    @Test
    fun `ProductdetailTicketData_SuccessShowProductSuccessShowHoliday_ShowActualResult`() {
        //given
        val contentMock = Gson().fromJson(getJson("content_mock.json"), EventContentByIdEntity::class.java)
        val pdpMock = Gson().fromJson(getJson("pdp_mock.json"), EventProductDetailEntity::class.java)

        val travelHoliday = TravelCalendarHoliday(id = "123123", attribute = TravelCalendarHoliday.HolidayAttribute("2020-01-01", label = "LabelTest"))
        val travelHolidayData = TravelCalendarHoliday.HolidayData(listOf(travelHoliday))

        coEvery { eventProductDetailUseCase.executeUseCase("", "", false, "") } returns Success(EventPDPContentCombined(contentMock, pdpMock))
        coEvery {
            usecaseHoliday.execute()
        } returns Success(travelHolidayData)


        //When
        eventPDPTicketViewModel.getData("", "1604250000", false, "", "")

        //then
        val selectedDatePackages = arrayListOf(pdpMock.eventProductDetail.productDetailData.packages[0])
        val recommendationPackages = arrayListOf<PackageV3>()
        for ((index, item) in pdpMock.eventProductDetail.productDetailData.packages.withIndex()) {
            if (index > 0) {
                recommendationPackages.add(item)
            }
        }

        assertNotNull(eventPDPTicketViewModel.ticketModel)
        assertEquals(eventPDPTicketViewModel.lists, selectedDatePackages)
        assertEquals(eventPDPTicketViewModel.ticketModel.value, eventPDPTicketViewModel.lists)
        assertEquals(eventPDPTicketViewModel.recommendationList, recommendationPackages)
        assertEquals(eventPDPTicketViewModel.recommendationTicketModel.value, eventPDPTicketViewModel.recommendationList)
        assertNotNull(eventPDPTicketViewModel.eventHoliday.value)
    }

    @Test
    fun `ProductdetailTicketData_FailShowProductSuccessShowHoliday_ShowActualResult`() {
        //given
        val error = Throwable("Error Ticket Data")
        val travelHoliday = TravelCalendarHoliday(id = "123123", attribute = TravelCalendarHoliday.HolidayAttribute("2020-01-01", label = "LabelTest"))
        val travelHolidayData = TravelCalendarHoliday.HolidayData(listOf(travelHoliday))

        coEvery { eventProductDetailUseCase.executeUseCase("", "", false, "") } returns
                Fail(error)
        coEvery {
            usecaseHoliday.execute()
        } returns Success(travelHolidayData)


        //When
        eventPDPTicketViewModel.getData("", "1557853200", false, "", "")

        //then
        assertNull(eventPDPTicketViewModel.ticketModel.value)
        assertEquals(eventPDPTicketViewModel.error.value, error.message)
        assertNotNull(eventPDPTicketViewModel.eventHoliday.value)
    }

    @Test
    fun `ProductdetailTicketData_FailShowProductFailShowHoliday_FailActualResult`() {
        //given
        val error = Throwable("Error Ticket Data")
        coEvery {
            eventProductDetailUseCase.executeUseCase("",
                    "", false, "")
        } returns Fail(error)
        coEvery {
            usecaseHoliday.execute()
        } returns Fail(Throwable())

        //When
        eventPDPTicketViewModel.getData("", "1604250000", false, "", "")

        //then
        assertNull(eventPDPTicketViewModel.ticketModel.value)
        assertNotNull(eventPDPTicketViewModel.error.value)
        assertEquals(eventPDPTicketViewModel.error.value, error.message)
        assert(eventPDPTicketViewModel.eventHoliday.value == arrayListOf<Legend>())
    }

    @Test
    fun `VerifyData_SuccessVerify_ShouldSuccessVerify`() {

        val verifyMock = Gson().fromJson(getJson("verify_mock.json"), EventVerifyResponseV2::class.java)

        val result = HashMap<Type, Any>()
        result[EventVerifyResponseV2::class.java] = verifyMock
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        eventPDPTicketViewModel.verify("", VerifyRequest())

        val actual = eventPDPTicketViewModel.verifyResponse.value
        assertEquals(actual, verifyMock)
    }


    @Test
    fun `VerifyData_FailVerify_ShouldFailVerify`() {
        //given
        val error = Throwable("Error Verify")
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw error }

        //when
        eventPDPTicketViewModel.verify("", VerifyRequest())

        //then
        assertEquals(eventPDPTicketViewModel.error.value, error.message)

    }

    @Test
    fun categoryData_setCategoryData_successSetCategoryData() {
        //given
        val categoryData = Category()

        //when
        eventPDPTicketViewModel.categoryData = categoryData

        //then
        assertEquals(eventPDPTicketViewModel.categoryData, categoryData)
    }

    @Test
    fun listPdpTicket_setPdpData_successSetPDPData() {
        //given
        val listTicket = mutableListOf<EventPDPTicketModel>()

        //then
        eventPDPTicketViewModel.lists = listTicket

        //when
        assertEquals(eventPDPTicketViewModel.lists, listTicket)
    }
}