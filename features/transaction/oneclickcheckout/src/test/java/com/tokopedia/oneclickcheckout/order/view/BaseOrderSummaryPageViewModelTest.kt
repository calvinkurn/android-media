package com.tokopedia.oneclickcheckout.order.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartOccExternalUseCase
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.domain.usecase.EditAddressUseCase
import com.tokopedia.oneclickcheckout.common.dispatchers.TestDispatchers
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.domain.CheckoutOccUseCase
import com.tokopedia.oneclickcheckout.order.domain.GetOccCartUseCase
import com.tokopedia.oneclickcheckout.order.domain.UpdateCartOccUseCase
import com.tokopedia.oneclickcheckout.order.view.processor.*
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule

open class BaseOrderSummaryPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var addToCartOccExternalUseCase: Lazy<AddToCartOccExternalUseCase>

    @MockK
    lateinit var getOccCartUseCase: GetOccCartUseCase

    @MockK(relaxed = true)
    lateinit var ratesUseCase: GetRatesUseCase

    @MockK
    lateinit var getPreferenceListUseCase: Lazy<GetPreferenceListUseCase>

    @MockK(relaxed = true)
    lateinit var updateCartOccUseCase: UpdateCartOccUseCase

    private val ratesResponseStateConverter: RatesResponseStateConverter = RatesResponseStateConverter()

    @MockK
    lateinit var editAddressUseCase: Lazy<EditAddressUseCase>

    @MockK(relaxed = true)
    lateinit var checkoutOccUseCase: CheckoutOccUseCase

    @MockK
    lateinit var clearCacheAutoApplyStackUseCase: Lazy<ClearCacheAutoApplyStackUseCase>

    @MockK(relaxed = true)
    lateinit var validateUsePromoRevampUseCase: Lazy<ValidateUsePromoRevampUseCase>

    @MockK(relaxed = true)
    lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    val testDispatchers: TestDispatchers = TestDispatchers

    lateinit var helper: OrderSummaryPageViewModelTestHelper

    lateinit var orderSummaryPageViewModel: OrderSummaryPageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        helper = OrderSummaryPageViewModelTestHelper()
        orderSummaryPageViewModel = OrderSummaryPageViewModel(testDispatchers, getPreferenceListUseCase,
                OrderSummaryPageCartProcessor(addToCartOccExternalUseCase, getOccCartUseCase, updateCartOccUseCase, testDispatchers),
                OrderSummaryPageLogisticProcessor(ratesUseCase, ratesResponseStateConverter, editAddressUseCase, orderSummaryAnalytics, testDispatchers),
                OrderSummaryPageCheckoutProcessor(checkoutOccUseCase, orderSummaryAnalytics, testDispatchers),
                OrderSummaryPagePromoProcessor(validateUsePromoRevampUseCase, clearCacheAutoApplyStackUseCase, orderSummaryAnalytics, testDispatchers),
                OrderSummaryPageCalculator(orderSummaryAnalytics, testDispatchers),
                userSessionInterface, orderSummaryAnalytics)
    }
}