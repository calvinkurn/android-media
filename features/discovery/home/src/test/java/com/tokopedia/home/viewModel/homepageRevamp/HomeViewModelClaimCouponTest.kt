@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeClaimCouponUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.model.ClaimCouponUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponTrackerModel
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.network.exception.MessageErrorException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomeViewModelClaimCouponTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val claimCouponUseCase = mockk<HomeClaimCouponUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    private val model = CouponWidgetDataModel(
        channelModel = ChannelModel("", ""),
        backgroundImageUrl = "",
        backgroundGradientColor = arrayListOf(),
        coupons = listOf(
            CouponWidgetDataItemModel(
                trackerModel = CouponTrackerModel(),
                button = CouponCtaState.Claim(CouponCtaState.Data("", "", "")),
                coupon = AutomateCouponModel.Grid(
                    type = DynamicColorText("", ""),
                    benefit = DynamicColorText("", ""),
                    tnc = DynamicColorText("", ""),
                    backgroundUrl = "",
                    iconUrl = null,
                    shopName = null,
                    badgeText = null
                )
            )
        )
    )

    @Test
    fun `It should be show the coupon widget in Homepage from dynamic channel`() = runTest {
        val model = CouponWidgetDataModel(
            channelModel = ChannelModel("", ""),
            backgroundImageUrl = "",
            backgroundGradientColor = arrayListOf(),
            coupons = listOf()
        )

        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(model)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeDataModel.findWidget<HomeRecommendationFeedDataModel>(
            actionOnFound = { widget, _ ->
                Assert.assertEquals(widget, model)
            },
            actionOnNotFound = {}
        )
    }

    @Test
    fun `It should be able to claim coupon`() = runTest {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(model)
            )
        )

        coEvery { claimCouponUseCase.invoke(any()) } returns ClaimCouponUiModel(
            isRedeemSucceed = true,
            errorException = null
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeClaimCouponUseCase = claimCouponUseCase
        )

        homeViewModel.onCouponClaim(model, "", 0)

        homeViewModel.homeDataModel.list.filterIsInstance<CouponWidgetDataModel>()
            .let {
                Assert.assertTrue(it.isNotEmpty())
            }
    }

    @Test
    fun `It should be unable to claim coupon`() = runTest {
        coEvery { claimCouponUseCase.invoke(any()) } throws MessageErrorException("Yah, kuota kupon habis.")

        homeViewModel = createHomeViewModel(
            homeClaimCouponUseCase = claimCouponUseCase
        )

        homeViewModel.onCouponClaim(model, "", 0)

        homeViewModel.errorEventLiveData.observeOnce {
            Assert.assertTrue(it.getContentIfNotHandled() is MessageErrorException)
        }
    }
}
