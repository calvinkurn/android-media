package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMBasicInfoUseCase
import com.tokopedia.power_merchant.subscribe.domain.usecase.GetShopModerationStatusUseCase
import com.tokopedia.power_merchant.subscribe.view.model.ModerationShopStatusUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyLong

/**
 * Created By @ilhamsuaib on 27/05/21
 */

@ExperimentalCoroutinesApi
class PowerMerchantSharedViewModelTest {

    @get:Rule
    val ruleForLivaData = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getPmBasicInfo: GetPMBasicInfoUseCase

    @RelaxedMockK
    lateinit var getShopModerationStatusUseCase: GetShopModerationStatusUseCase

    private lateinit var viewModel: PowerMerchantSharedViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PowerMerchantSharedViewModel(
            { getPmBasicInfo },
            { getShopModerationStatusUseCase },
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when get pm basic info should return success`() = runTest {
        val result = PowerMerchantBasicInfoUiModel(
            PMStatusUiModel(),
            PMShopInfoUiModel(),
            emptyList()
        )
        val isFirstLoad = anyBoolean()

        coEvery {
            getPmBasicInfo.executeOnBackground(isFirstLoad)
        } returns result

        viewModel.getPowerMerchantBasicInfo(isFirstLoad)

        coVerify {
            getPmBasicInfo.executeOnBackground(isFirstLoad)
        }

        val expected = Success(result)

        viewModel.powerMerchantBasicInfo.verifySuccessEquals(expected)
    }

    @Test
    fun `when get pm basic info should return failed`() = runTest {
        val throwable = Throwable()
        val isFirstLoad = anyBoolean()

        coEvery {
            getPmBasicInfo.executeOnBackground(isFirstLoad)
        } throws throwable

        viewModel.getPowerMerchantBasicInfo(isFirstLoad)

        coVerify {
            getPmBasicInfo.executeOnBackground(isFirstLoad)
        }

        val expected = Fail(throwable)

        viewModel.powerMerchantBasicInfo.verifyErrorEquals(expected)
    }

    @Test
    fun `when get shop moderation status then return success`() = runTest {
        val shopId = anyLong()

        val result = ModerationShopStatusUiModel(3)

        getShopModerationStatusUseCase.params = GetShopModerationStatusUseCase.createParam(shopId)

        coEvery {
            getShopModerationStatusUseCase.executeOnBackground()
        } returns result

        viewModel.getShopModerationStatus(shopId)

        coVerify {
            getShopModerationStatusUseCase.executeOnBackground()
        }

        val expected = Success(result)

        viewModel.shopModerationStatus.verifySuccessEquals(expected)
    }

    @Test
    fun `when get shop moderation status then return failed`() = runTest {
        val shopId = anyLong()

        val throwable = Throwable()

        getShopModerationStatusUseCase.params = GetShopModerationStatusUseCase.createParam(shopId)

        coEvery {
            getShopModerationStatusUseCase.executeOnBackground()
        } throws throwable

        viewModel.getShopModerationStatus(shopId)

        coVerify {
            getShopModerationStatusUseCase.executeOnBackground()
        }

        val expected = Fail(throwable)

        viewModel.shopModerationStatus.verifyErrorEquals(expected)
    }
}
