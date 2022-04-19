package com.tokopedia.entertainment.pdp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeemedData
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.entertainment.pdp.network_api.RedeemTicketEventUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class EventRedeemViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventRedeemViewModel: EventRedeemViewModel

    @RelaxedMockK
    lateinit var getEventRedeemUseCase: GetEventRedeemUseCase

    @RelaxedMockK
    lateinit var redeemTicketEventUseCase: RedeemTicketEventUseCase

    val errorMessage = "Something went wrong"

    val restResponse = RestResponse(EventRedeem(), 500, false).apply {
        errorBody = "{\n" +
                "  \"message_error\": [\"$errorMessage\"]\n" +
                "}"
    }

    val codeResponse = RestResponse(EventRedeem(), 200, false).apply {
        isError = true
        errorBody = "{\n" +
                "  \"message_error\": [\"$errorMessage\"]\n" +
                "}"
    }

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventRedeemViewModel = EventRedeemViewModel(Dispatchers.Unconfined, getEventRedeemUseCase,
                redeemTicketEventUseCase)
    }

    @Test
    fun `RedeemData_ShouldReturnRedeem_showActualResult`(){
        //given
        val redeemable = Gson().fromJson(getJson("redeem_event.json"), EventRedeem::class.java)
        val restResponse = RestResponse(redeemable, 200, false)
        val redeemableMap = mapOf<Type, RestResponse>(
                EventRedeem::class.java to restResponse
        )
        coEvery {
           getEventRedeemUseCase.executeOnBackground()
        } returns redeemableMap

        //when
        eventRedeemViewModel.getDataRedeem("")

        assertNotNull(eventRedeemViewModel.eventRedeem.value)
        assertEquals(eventRedeemViewModel.eventRedeem.value, redeemable)
    }

    @Test
    fun `RedeemData_ShouldReturnRedeem_showFailFromThrowable`(){
        //given
        val restResponse = RestResponse(Throwable(), 500, false)
        val redeemableMap = mapOf<Type, RestResponse>(
                Throwable::class.java to restResponse
        )
        coEvery {
            getEventRedeemUseCase.executeOnBackground()
        } returns redeemableMap

        //when
        eventRedeemViewModel.getDataRedeem("")

        assertNotNull(eventRedeemViewModel.isError.value)
    }

    @Test
    fun `RedeemedData_ShouldReturnRedeem_showActualResult`(){
        //given
        val redeemed = Gson().fromJson(getJson("redeemed_event.json"), EventRedeemedData::class.java)
        val restResponse = RestResponse(redeemed, 200, false)
        val redeemableMap = mapOf<Type, RestResponse>(
                EventRedeemedData::class.java to restResponse
        )
        coEvery {
            redeemTicketEventUseCase.executeOnBackground()
        } returns redeemableMap

        //when
        eventRedeemViewModel.redeemData("")

        assertNotNull(eventRedeemViewModel.eventRedeemed.value)
        assertEquals(eventRedeemViewModel.eventRedeemed.value, redeemed)
    }

    @Test
    fun `RedeemedData_ShouldReturnRedeem_showFailFromThrowable`(){
        //given
        val restResponse = RestResponse(Throwable(), 500, false)
        val redeemableMap = mapOf<Type, RestResponse>(
                Throwable::class.java to restResponse
        )
        coEvery {
            redeemTicketEventUseCase.executeOnBackground()
        } returns redeemableMap

        //when
        eventRedeemViewModel.redeemData("")

        assertNotNull(eventRedeemViewModel.isError.value)
    }


    @Test
    fun `RedeemData_ShouldReturnRedeem_showFailFromResult`(){
        //given
        val redeemableMap = mapOf<Type, RestResponse>(
            EventRedeem::class.java to restResponse
        )
        coEvery {
            getEventRedeemUseCase.executeOnBackground()
        } returns redeemableMap

        //when
        eventRedeemViewModel.getDataRedeem("")

        //then
        assertNotNull(eventRedeemViewModel.isError.value)
        assertEquals((eventRedeemViewModel.isError.value)?.message, errorMessage)
    }


    @Test
    fun `RedeemedData_ShouldReturnRedeem_showFailFromResult`(){
        //given
        val redeemedableMap = mapOf<Type, RestResponse>(
            EventRedeemedData::class.java to restResponse
        )
        coEvery {
            redeemTicketEventUseCase.executeOnBackground()
        } returns redeemedableMap

        //when
        eventRedeemViewModel.redeemData("")

        assertNotNull(eventRedeemViewModel.isError.value)
        assertEquals((eventRedeemViewModel.isError.value)?.message, errorMessage)
    }

    @Test
    fun `RedeemData_ShouldReturnRedeem_showFailFromCode`(){
        //given
        val codeRedeem = mapOf<Type, RestResponse>(
            EventRedeem::class.java to codeResponse
        )
        coEvery {
            getEventRedeemUseCase.executeOnBackground()
        } returns codeRedeem

        //when
        eventRedeemViewModel.getDataRedeem("")

        //then
        assertNotNull(eventRedeemViewModel.isError.value)
        assertEquals((eventRedeemViewModel.isError.value)?.message, errorMessage)
    }


    @Test
    fun `RedeemedData_ShouldReturnRedeem_showFailFromCode`(){
        //given
        val codeRedeem = mapOf<Type, RestResponse>(
            EventRedeemedData::class.java to codeResponse
        )
        coEvery {
            redeemTicketEventUseCase.executeOnBackground()
        } returns codeRedeem

        //when
        eventRedeemViewModel.redeemData("")

        assertNotNull(eventRedeemViewModel.isError.value)
        assertEquals((eventRedeemViewModel.isError.value)?.message, errorMessage)
    }
}