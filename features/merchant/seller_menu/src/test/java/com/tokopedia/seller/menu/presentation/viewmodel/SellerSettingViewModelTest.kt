package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticCommon.data.response.shoplocation.DataEligibility
import com.tokopedia.logisticCommon.data.response.shoplocation.KeroGetRolloutEligibility
import com.tokopedia.logisticCommon.data.response.shoplocation.KeroGetRolloutEligibilityResponse
import com.tokopedia.logisticCommon.domain.usecase.ShopMultilocWhitelistUseCase
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by @ilhamsuaib on 14/10/22.
 */

@ExperimentalCoroutinesApi
class SellerSettingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var shopLocWhitelistUseCase: ShopMultilocWhitelistUseCase

    private lateinit var viewModel: SellerSettingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SellerSettingViewModel(
            dispatchers = coroutineTestRule.dispatchers,
            shopLocWhitelist = shopLocWhitelistUseCase
        )
    }

    @Test
    fun `given shop id when get shop location eligibility should be eligible multi location`() {
        coroutineTestRule.runTest {
            val shopId = 123L
            val eligibleMultiLoc = 1
            val mockResponse = KeroGetRolloutEligibilityResponse(
                KeroGetRolloutEligibility(
                    data = DataEligibility(eligibilityState = eligibleMultiLoc)
                )
            )

            coEvery {
                shopLocWhitelistUseCase.invoke(shopId)
            } returns mockResponse

            viewModel.getShopLocEligible(shopId)

            coVerify {
                shopLocWhitelistUseCase.invoke(shopId)
            }

            viewModel.shopLocEligible.verifySuccessEquals(Success(true))
        }
    }

    @Test
    fun `given shop id when get shop location eligibility should not be eligible multi location`() {
        coroutineTestRule.runTest {
            val shopId = 123L
            val notEligibleMultiLoc = -1
            val mockResponse = KeroGetRolloutEligibilityResponse(
                KeroGetRolloutEligibility(
                    data = DataEligibility(
                        eligibilityState = notEligibleMultiLoc
                    )
                )
            )

            coEvery {
                shopLocWhitelistUseCase.invoke(shopId)
            } returns mockResponse

            viewModel.getShopLocEligible(shopId)

            coVerify {
                shopLocWhitelistUseCase.invoke(shopId)
            }

            viewModel.shopLocEligible.verifySuccessEquals(Success(false))
        }
    }

    @Test
    fun `given shop id when get shop location eligibility should be failed`() {
        coroutineTestRule.runTest {
            val shopId = 123L
            val throwable = Throwable("error")

            coEvery {
                shopLocWhitelistUseCase.invoke(shopId)
            } throws throwable

            viewModel.getShopLocEligible(shopId)

            coVerify {
                shopLocWhitelistUseCase.invoke(shopId)
            }

            viewModel.shopLocEligible.verifyErrorEquals(Fail(throwable))
        }
    }
}
