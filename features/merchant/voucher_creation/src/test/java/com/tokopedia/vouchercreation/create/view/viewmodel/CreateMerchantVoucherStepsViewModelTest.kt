package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.domain.usecase.BasicShopInfoUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.create.domain.model.ShopInfo
import com.tokopedia.vouchercreation.create.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.InitiateVoucherUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateMerchantVoucherStepsViewModelTest {

    companion object {
        private const val DUMMY_STEP_VALUE = VoucherCreationStep.BENEFIT
        private const val DUMMY_MAX_POSITION = 4
    }

    @RelaxedMockK
    lateinit var initiateVoucherUseCase: InitiateVoucherUseCase

    @RelaxedMockK
    lateinit var basicShopInfoUseCase: BasicShopInfoUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var mViewModel: CreateMerchantVoucherStepsViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = CreateMerchantVoucherStepsViewModel(CoroutineTestDispatchersProvider, initiateVoucherUseCase, basicShopInfoUseCase, userSession)
    }

    @Test
    fun `setting step position without setting max position will not change live data value`() {
        val initialStepPosition = mViewModel.stepPositionLiveData.value

        mViewModel.setStepPosition(DUMMY_STEP_VALUE)

        assert(mViewModel.stepPositionLiveData.value == initialStepPosition)
    }

    @Test
    fun `setting step position with also setting max position will change live data value`() {
        mViewModel.setMaxPosition(DUMMY_MAX_POSITION)
        mViewModel.setStepPosition(DUMMY_STEP_VALUE)

        assert(mViewModel.stepPositionLiveData.value == DUMMY_STEP_VALUE)
    }

    @Test
    fun `setting step position higher than max position will not change live data value`() {
        val initialStepPosition = mViewModel.stepPositionLiveData.value

        mViewModel.setMaxPosition(0)
        mViewModel.setStepPosition(DUMMY_STEP_VALUE)

        assert(mViewModel.stepPositionLiveData.value == initialStepPosition)
    }

    @Test
    fun `setting next step will increment step position live data value if current step is lower than max position`() {
        mViewModel.setMaxPosition(DUMMY_MAX_POSITION)
        mViewModel.setStepPosition(DUMMY_STEP_VALUE)
        val initialStepPosition = mViewModel.stepPositionLiveData.value

        mViewModel.setNextStep()

        assert(mViewModel.stepPositionLiveData.value == initialStepPosition?.plus(1))
    }

    @Test
    fun `setting next step will not increment step position live data value if current step is same or higher than max position`() {
        mViewModel.setMaxPosition(DUMMY_MAX_POSITION)
        mViewModel.setStepPosition(DUMMY_MAX_POSITION + 1)
        val initialStepPosition = mViewModel.stepPositionLiveData.value

        mViewModel.setNextStep()

        assert(mViewModel.stepPositionLiveData.value == initialStepPosition?.plus(1))
    }

    @Test
    fun `setting back step will decrement step position live data value if current step is more than 0`() {
        mViewModel.setMaxPosition(DUMMY_MAX_POSITION)
        mViewModel.setStepPosition(DUMMY_STEP_VALUE)
        val initialStepPosition = mViewModel.stepPositionLiveData.value

        mViewModel.setBackStep()

        assert(mViewModel.stepPositionLiveData.value == initialStepPosition?.minus(1))
    }

    @Test
    fun `setting back step will not decrement step position live data value if current step is same or lower than 0`() {
        mViewModel.setMaxPosition(DUMMY_MAX_POSITION)
        mViewModel.setStepPosition(0)
        val initialStepPosition = mViewModel.stepPositionLiveData.value

        mViewModel.setBackStep()

        assert(mViewModel.stepPositionLiveData.value != initialStepPosition?.minus(1))
    }

    @Test
    fun `success get initiate voucher page`() = runBlocking {
        val dummySuccessInitiateVoucher = InitiateVoucherUiModel()

        coEvery {
            initiateVoucherUseCase.executeOnBackground()
        } returns dummySuccessInitiateVoucher

        mViewModel.initiateVoucherPage()

        coVerify {
            initiateVoucherUseCase.executeOnBackground()
        }

        assert(mViewModel.initiateVoucherLiveData.value == Success(dummySuccessInitiateVoucher))
    }

    @Test
    fun `failed get initiate voucher page`() = runBlocking {
        val throwable = MessageErrorException()

        coEvery {
            initiateVoucherUseCase.executeOnBackground()
        } throws throwable

        mViewModel.initiateVoucherPage()

        mViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            initiateVoucherUseCase.executeOnBackground()
        }

        assert(mViewModel.initiateVoucherLiveData.value is Fail)
    }

    @Test
    fun `success initiate edit duplicate voucher`() = runBlocking {
        val dummySuccessBasicInfo = ShopInfo()
        val dummySuccessInitiateVoucher = InitiateVoucherUiModel()

        coEvery {
            userSession.userId
        } returns "1"
        coEvery {
            basicShopInfoUseCase.executeOnBackground()
        } returns dummySuccessBasicInfo
        coEvery {
            initiateVoucherUseCase.executeOnBackground()
        } returns dummySuccessInitiateVoucher

        mViewModel.initiateEditDuplicateVoucher()

        coVerify {
            basicShopInfoUseCase.executeOnBackground()
            initiateVoucherUseCase.executeOnBackground()
        }

        assert(mViewModel.initiateVoucherLiveData.value == Success(dummySuccessInitiateVoucher))
        assert(mViewModel.basicShopInfoLiveData.value == Success(dummySuccessBasicInfo))
    }

    @Test
    fun `failed initiate edit duplicate voucher`() = runBlocking {
        val throwable = MessageErrorException("")

        coEvery {
            userSession.userId
        } returns "1"
        coEvery {
            basicShopInfoUseCase.executeOnBackground()
        } throws throwable
        coEvery {
            initiateVoucherUseCase.executeOnBackground()
        } throws throwable

        mViewModel.initiateEditDuplicateVoucher()

        coVerify {
            basicShopInfoUseCase.executeOnBackground()
        }

        assert(mViewModel.initiateVoucherLiveData.value is Fail)
    }

}