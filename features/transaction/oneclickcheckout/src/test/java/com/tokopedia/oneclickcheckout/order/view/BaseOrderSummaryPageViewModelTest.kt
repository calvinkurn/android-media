package com.tokopedia.oneclickcheckout.order.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiExternalUseCase
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.domain.CheckoutOccUseCase
import com.tokopedia.oneclickcheckout.order.domain.CreditCardTenorListUseCase
import com.tokopedia.oneclickcheckout.order.domain.GetOccCartUseCase
import com.tokopedia.oneclickcheckout.order.domain.UpdateCartOccUseCase
import com.tokopedia.oneclickcheckout.order.view.processor.*
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseOrderSummaryPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var addToCartOccMultiExternalUseCase: Lazy<AddToCartOccMultiExternalUseCase>

    @MockK
    lateinit var getOccCartUseCase: GetOccCartUseCase

    @MockK(relaxed = true)
    lateinit var ratesUseCase: GetRatesUseCase

    @MockK(relaxed = true)
    lateinit var updateCartOccUseCase: UpdateCartOccUseCase

    @MockK(relaxed = true)
    lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    private val ratesResponseStateConverter: RatesResponseStateConverter = RatesResponseStateConverter()

    @MockK
    lateinit var chooseAddressRepository: Lazy<ChooseAddressRepository>

    @MockK
    lateinit var chooseAddressMapper: Lazy<ChooseAddressMapper>

    @MockK
    lateinit var editAddressUseCase: Lazy<EditAddressUseCase>

    @MockK(relaxed = true)
    lateinit var checkoutOccUseCase: CheckoutOccUseCase

    @MockK(relaxed = true)
    lateinit var creditCardTenorListUseCase: CreditCardTenorListUseCase

    @MockK(relaxed = true)
    lateinit var clearCacheAutoApplyStackUseCase: Lazy<ClearCacheAutoApplyStackUseCase>

    @MockK(relaxed = true)
    lateinit var validateUsePromoRevampUseCase: Lazy<ValidateUsePromoRevampUseCase>

    @MockK(relaxed = true)
    lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    @MockK(relaxed = true)
    lateinit var gson: Gson

    val testDispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers

    lateinit var helper: OrderSummaryPageViewModelTestHelper

    lateinit var orderSummaryPageViewModel: OrderSummaryPageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        helper = OrderSummaryPageViewModelTestHelper()
        orderSummaryPageViewModel = OrderSummaryPageViewModel(testDispatchers,
                OrderSummaryPageCartProcessor(addToCartOccMultiExternalUseCase, getOccCartUseCase, updateCartOccUseCase, testDispatchers),
                OrderSummaryPageLogisticProcessor(ratesUseCase, ratesResponseStateConverter, chooseAddressRepository, chooseAddressMapper, editAddressUseCase, orderSummaryAnalytics, testDispatchers),
                OrderSummaryPageCheckoutProcessor(checkoutOccUseCase, orderSummaryAnalytics, testDispatchers, gson),
                OrderSummaryPagePromoProcessor(validateUsePromoRevampUseCase, clearCacheAutoApplyStackUseCase, orderSummaryAnalytics, testDispatchers),
                { OrderSummaryPagePaymentProcessor(creditCardTenorListUseCase, testDispatchers) },
                OrderSummaryPageCalculator(orderSummaryAnalytics, testDispatchers),
                userSessionInterface, orderSummaryAnalytics, eligibleForAddressUseCase)
    }
}