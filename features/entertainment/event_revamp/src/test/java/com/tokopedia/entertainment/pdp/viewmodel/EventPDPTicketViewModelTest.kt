package com.tokopedia.entertainment.pdp.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.EventContentByIdEntity
import com.tokopedia.entertainment.pdp.data.EventPDPContentCombined
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventPDPTicketViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventPDPTicketViewModel: EventPDPTicketViewModel

    @MockK
    lateinit var graphqlRepository: MultiRequestGraphqlUseCase

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var resources: Resources

    @MockK
    lateinit var eventProductDetailUseCase: EventProductDetailUseCase

    @MockK
    lateinit var usecaseHoliday: TravelCalendarHolidayUseCase

    val context = mockk<Context>(relaxed = true)

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventPDPTicketViewModel = EventPDPTicketViewModel(Dispatchers.Unconfined, eventProductDetailUseCase)
    }

    @Test
    fun `DataTicket_NotNullData_Shownotnulldata`(){
        Assert.assertNotNull(eventProductDetailUseCase)
        Assert.assertNotNull(usecaseHoliday)
        Assert.assertNotNull(graphqlRepository)
        Assert.assertNotNull(eventPDPTicketViewModel)
        Assert.assertNotNull(userSessionInterface)
    }

    @Test
    fun `ProductdetailTicketData_SuccessShowProduct_ShowActualResult`(){
        //given
        val contentMock = Gson().fromJson(getJson("content_mock.json"), EventContentByIdEntity::class.java)
        val pdpMock = Gson().fromJson(getJson("pdp_mock.json"), EventProductDetailEntity::class.java)

        coEvery { eventProductDetailUseCase.executeUseCase("", "", false, "") } returns Success(EventPDPContentCombined(contentMock, pdpMock))

        //When
        eventPDPTicketViewModel.getData("", "1557853200", false,"","")

        //then
        assertNotNull(eventPDPTicketViewModel.ticketModel)
        assertEquals(eventPDPTicketViewModel.lists, pdpMock.eventProductDetail.productDetailData.schedules[0].groups[0].packages)
        assertEquals(eventPDPTicketViewModel.ticketModel.value, eventPDPTicketViewModel.lists)
    }

    @Test
    fun `ProductdetailTicketData_FailShowProduct_FailActualResult`(){
        //given
        coEvery { eventProductDetailUseCase.executeUseCase("",
                "", false, "") } returns Fail(Throwable("Error Ticket Data"))

        //When
        eventPDPTicketViewModel.getData( "", "", false,"","")

        //then
        assertNull(eventPDPTicketViewModel.ticketModel.value)
        assertNotNull(eventPDPTicketViewModel.error.value)
        assertEquals(eventPDPTicketViewModel.error.value,"java.lang.Throwable: Error Ticket Data")
    }

}