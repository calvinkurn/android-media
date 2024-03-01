package com.tokopedia.shopdiscount.subsidy.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopdiscount.subsidy.domain.DoOptOutSubsidyUseCase
import com.tokopedia.shopdiscount.subsidy.domain.SubsidyEngineGetSellerOutReasonListUseCase
import com.tokopedia.shopdiscount.subsidy.model.response.DoOptOutResponse
import com.tokopedia.shopdiscount.subsidy.model.response.SubsidyEngineGetSellerOutReasonListResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopDiscountOptOutReasonBottomSheetViewModelTest {

    @RelaxedMockK
    lateinit var subsidyEngineGetSellerOutReasonListUseCase: SubsidyEngineGetSellerOutReasonListUseCase

    @RelaxedMockK
    lateinit var doOptOutSubsidyUseCase: DoOptOutSubsidyUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        ShopDiscountOptOutReasonBottomSheetViewModel(
            CoroutineTestDispatchersProvider,
            subsidyEngineGetSellerOutReasonListUseCase,
            doOptOutSubsidyUseCase,
            userSession
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `When get getListOptOutReason success, then result should success`(){
        //Given
        coEvery {
            subsidyEngineGetSellerOutReasonListUseCase.executeOnBackground()
        } returns SubsidyEngineGetSellerOutReasonListResponse()

        //When
        viewModel.getListOptOutReason()

        //Then
        assert(viewModel.listOptOutReasonLiveData.value is Success)
    }

    @Test
    fun `When get getListOptOutReason error, then result should fail`(){
        //Given
        val error = MessageErrorException("error")
        coEvery {
            subsidyEngineGetSellerOutReasonListUseCase.executeOnBackground()
        } throws error

        //When
        viewModel.getListOptOutReason()

        //Then
        assert(viewModel.listOptOutReasonLiveData.value is Fail)
    }

    @Test
    fun `When do opt out is_success is true, then result should success`(){
        //Given
        coEvery {
            doOptOutSubsidyUseCase.executeOnBackground()
        } returns DoOptOutResponse(
            doSellerOutProgram = DoOptOutResponse.DoSellerOutProgram(
                responseHeader = DoOptOutResponse.DoSellerOutProgram.ResponseHeader(
                    isSuccess = true
                )
            )
        )

        //When
        viewModel.doOptOutProductSubsidy(mutableListOf(), listOf(), "")

        //Then
        assert(viewModel.doOptOutSubsidyResultLiveData.value is Success)
    }

    @Test
    fun `When do opt out is_success is false, then result should fail`(){
        //Given
        coEvery {
            doOptOutSubsidyUseCase.executeOnBackground()
        } returns DoOptOutResponse(
            doSellerOutProgram = DoOptOutResponse.DoSellerOutProgram(
                responseHeader = DoOptOutResponse.DoSellerOutProgram.ResponseHeader(
                    isSuccess = false
                )
            )
        )

        //When
        viewModel.doOptOutProductSubsidy(mutableListOf(), listOf(), "")

        //Then
        assert(viewModel.doOptOutSubsidyResultLiveData.value is Fail)
    }

    @Test
    fun `When do opt out error from network, then result should fail`(){
        //Given
        val error = MessageErrorException("error")
        coEvery {
            subsidyEngineGetSellerOutReasonListUseCase.executeOnBackground()
        } throws error

        //When
        viewModel.doOptOutProductSubsidy(mutableListOf(), listOf(), "")

        //Then
        assert(viewModel.doOptOutSubsidyResultLiveData.value is Fail)
    }
}
