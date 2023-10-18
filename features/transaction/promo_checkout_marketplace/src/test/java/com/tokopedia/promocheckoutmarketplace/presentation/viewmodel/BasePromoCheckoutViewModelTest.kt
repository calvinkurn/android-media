package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.promocheckoutmarketplace.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.promocheckoutmarketplace.domain.usecase.GetPromoSuggestionUseCase
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.*
import org.junit.Before
import org.junit.Rule

abstract class BasePromoCheckoutViewModelTest {

    lateinit var viewModel: PromoCheckoutViewModel
    lateinit var testDispatchers: CoroutineTestDispatchers

    var getCouponListRecommendationUseCase: GetCouponListRecommendationUseCase = mockk()
    var validateUseUseCase: ValidateUsePromoRevampUseCase = mockk()
    var clearCacheAutoApplyUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    var getPromoSuggestionUseCase: GetPromoSuggestionUseCase = mockk()

    var uiModelMapper: PromoCheckoutUiModelMapper = spyk()
    var analytics: PromoCheckoutAnalytics = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        testDispatchers = CoroutineTestDispatchers
        viewModel = PromoCheckoutViewModel(
            testDispatchers.main,
            getCouponListRecommendationUseCase,
            validateUseUseCase,
            clearCacheAutoApplyUseCase,
            getPromoSuggestionUseCase,
            uiModelMapper,
            analytics
        )

        every { analytics.eventViewAvailablePromoListEligiblePromo(any(), any()) } just Runs
        every { analytics.eventViewAvailablePromoListIneligibleProduct(any(), any()) } just Runs

        viewModel.initFragmentUiModel(PAGE_CART, "Error Message")
    }
}
