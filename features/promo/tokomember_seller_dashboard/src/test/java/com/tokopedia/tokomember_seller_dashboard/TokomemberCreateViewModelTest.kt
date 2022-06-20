package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.tokomember_seller_dashboard.domain.*
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.model.CardDataTemplate
import com.tokopedia.tokomember_seller_dashboard.model.MembershipCreateEditCard
import com.tokopedia.tokomember_seller_dashboard.model.ProgramDetailData
import com.tokopedia.tokomember_seller_dashboard.model.ProgramUpdateResponse
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TokomemberCreateViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockThrowable = Throwable(message = "exception")
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TmDashCreateViewModel
    private val tokomemberDashEditCardUsecase = mockk<TokomemberDashEditCardUsecase>(relaxed = true)
    private val tokomemberDashCardUsecase = mockk<TokomemberDashCardUsecase>(relaxed = true)
    private val tokomemberDashGetProgramFormUsecase =
        mockk<TokomemberDashGetProgramFormUsecase>(relaxed = true)
    private val tokomemberDashUpdateProgramUsecase =
        mockk<TokomemberDashUpdateProgramUsecase>(relaxed = true)
    private val tokomemberCardColorMapperUsecase =
        mockk<TokomemberCardColorMapperUsecase>(relaxed = true)
    private val tokomemeberCardBgUsecase = mockk<TokomemeberCardBgUsecase>(relaxed = true)

    private val tmKuponCreateValidateUsecase =  mockk<TmKuponCreateValidateUsecase>(relaxed = true)
    private val tmKuponCreateUsecase = mockk<TmKuponCreateUsecase>(relaxed = true)
    private val tmKuponUpdateUsecase = mockk<TmKuponUpdateUsecase>(relaxed = true)
    private val tmKuponProgramValidateUsecase = mockk<TmKuponProgramValidateUsecase>(relaxed = true)
    private val tmKuponInitialUsecase = mockk<TmKuponInitialUsecase>(relaxed = true)
    private val uploaderUseCase = mockk<UploaderUseCase>(relaxed = true)
    private val tmCouponDetailUsecase = mockk<TmCouponDetailUsecase>(relaxed = true)

    private val editCardObserver: Observer<TokoLiveDataResult<MembershipCreateEditCard>> = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = TmDashCreateViewModel(
            tokomemberDashCardUsecase,
            tokomemberCardColorMapperUsecase,
            tokomemeberCardBgUsecase,
            tokomemberDashEditCardUsecase,
            tokomemberDashGetProgramFormUsecase,
            tokomemberDashUpdateProgramUsecase,
            tmKuponCreateValidateUsecase,
            tmKuponCreateUsecase,
            tmKuponUpdateUsecase,
            tmKuponProgramValidateUsecase,
            tmKuponInitialUsecase,
            uploaderUseCase,
            tmCouponDetailUsecase,
            dispatcher
        )
    }

    @Test
    fun successCardInfo() {
        val data = mockk<CardData>(relaxed = true)
        coEvery {
            tokomemberDashCardUsecase.getMembershipCardInfo(any(), any(), 0)
        } coAnswers {
            firstArg<(CardData) -> Unit>().invoke(data)
        }
        viewModel.getCardInfo(0)

        val cardData = mockk<CardDataTemplate>(relaxed = true)
        every {
            cardData.card
        } returns data.membershipGetCardForm?.card
        every {
            cardData.cardTemplate
        } returns data.membershipGetCardForm?.cardTemplate
        every {
            cardData.cardTemplateImageList
        } returns data.membershipGetCardForm?.cardTemplateImageList

        Assert.assertEquals(
            (viewModel.tmCardResultLiveData.value as TokoLiveDataResult).data,
            cardData
        )
    }

    @Test
    fun failCardInfo() {
        coEvery {
            tokomemberDashCardUsecase.getMembershipCardInfo(any(), any(), 0)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getCardInfo(0)
        Assert.assertEquals(
            (viewModel.tmCardResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun successProgramForm(){
        val data = mockk<ProgramDetailData>(relaxed = true)
        coEvery {
            tokomemberDashGetProgramFormUsecase.getProgramInfo(any(), any(), 0,0,"","")
        } coAnswers {
            firstArg<(ProgramDetailData) -> Unit>().invoke(data)
        }
        viewModel.getProgramInfo(0,0,"","")

        Assert.assertEquals(
            (viewModel.tmProgramResultLiveData.value as TokoLiveDataResult).data,
            data
        )
    }

    @Test
    fun failProgramForm() {
        coEvery {
            tokomemberDashGetProgramFormUsecase.getProgramInfo(any(), any(), 0,0,"","")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getProgramInfo(0,0,"","")
        Assert.assertEquals(
            (viewModel.tmProgramResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun successUpdateProgram(){
        val data = mockk<ProgramUpdateResponse>(relaxed = true)
        coEvery {
            tokomemberDashUpdateProgramUsecase.updateProgram(any(), any(), any())
        } coAnswers {
            firstArg<(ProgramUpdateResponse) -> Unit>().invoke(data)
        }
        viewModel.updateProgram(ProgramUpdateDataInput())

        Assert.assertEquals(
            (viewModel.tokomemberProgramUpdateResultLiveData.value as TokoLiveDataResult).data,
            data
        )
    }

    @Test
    fun failUpdateProgram() {
        coEvery {
            tokomemberDashUpdateProgramUsecase.updateProgram(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.updateProgram(ProgramUpdateDataInput())
        Assert.assertEquals(
            (viewModel.tokomemberProgramUpdateResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }


    @Test
    fun successUpdateCard(){
        val observer = mockk<Observer<TokoLiveDataResult<MembershipCreateEditCard>>> {
            every { onChanged(any()) } just Runs
        }
        val data = mockk<MembershipCreateEditCard>(relaxed = true)
        coEvery {
            tokomemberDashEditCardUsecase.modifyShopCard(any(), any(), any())
        } coAnswers {
            firstArg<(MembershipCreateEditCard) -> Unit>().invoke(data)
        }
        viewModel.modifyShopCard(TmCardModifyInput())

        Assert.assertEquals(
            viewModel.tokomemberCardModifyLiveData.value?.data ,
            data
        )
    }

    @Test
    fun failUpdateCard() {
        coEvery {
            tokomemberDashEditCardUsecase.modifyShopCard(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.modifyShopCard(TmCardModifyInput())
        Assert.assertEquals(
            (viewModel.tokomemberCardModifyLiveData.value?.error),
            mockThrowable
        )
    }

}