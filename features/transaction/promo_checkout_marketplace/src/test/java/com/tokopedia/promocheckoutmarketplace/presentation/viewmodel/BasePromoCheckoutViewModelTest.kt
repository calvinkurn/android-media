package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.promocheckoutmarketplace.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.promocheckoutmarketplace.domain.usecase.GetPromoSuggestionUseCase
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule

abstract class BasePromoCheckoutViewModelTest {

    lateinit var viewModel: PromoCheckoutViewModel
    private lateinit var dispatcher: CoroutineDispatcher

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
        dispatcher = Dispatchers.Unconfined
        viewModel = PromoCheckoutViewModel(
            dispatcher,
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
