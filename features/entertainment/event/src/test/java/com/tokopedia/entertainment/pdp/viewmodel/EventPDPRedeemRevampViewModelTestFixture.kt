package com.tokopedia.entertainment.pdp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemedUseCase
import com.tokopedia.entertainment.pdp.network_api.RedeemTicketEventUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Type

open class EventPDPRedeemRevampViewModelTestFixture {

    @RelaxedMockK
    lateinit var getEventRedeemUseCase: GetEventRedeemUseCase
    @RelaxedMockK
    lateinit var getEventRedeemedUseCase: GetEventRedeemedUseCase
    @RelaxedMockK
    lateinit var getEventOldRedeemUseCase: RedeemTicketEventUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: EventRedeemRevampViewModel
    protected var errorMessage = "Error Message"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = EventRedeemRevampViewModel(
            CoroutineTestDispatchersProvider,
            getEventRedeemUseCase,
            getEventRedeemedUseCase,
            getEventOldRedeemUseCase
        )
    }

    protected fun onGetRedeemData_thenReturn(mapResponse: Map<Type, RestResponse?>) {
        coEvery {
            getEventRedeemUseCase.executeOnBackground()
        } returns mapResponse
    }

    protected fun onGetRedeemData_thenReturn(throwable: Throwable) {
        coEvery {
            getEventRedeemUseCase.executeOnBackground()
        } throws throwable
    }

    protected fun onGetNewRedeemProcess_thenReturn(mapResponse: Map<Type, RestResponse?>) {
        coEvery {
            getEventRedeemedUseCase.executeOnBackground()
        } returns mapResponse
    }

    protected fun onGetNewRedeemProcess_thenReturn(throwable: Throwable) {
        coEvery {
            getEventRedeemedUseCase.executeOnBackground()
        } throws throwable
    }

    protected fun onGetOldRedeemProcess_thenReturn(mapResponse: Map<Type, RestResponse?>) {
        coEvery {
            getEventOldRedeemUseCase.executeOnBackground()
        } returns mapResponse
    }

    protected fun onGetOldRedeemProcess_thenReturn(throwable: Throwable) {
        coEvery {
            getEventOldRedeemUseCase.executeOnBackground()
        } throws throwable
    }
}
