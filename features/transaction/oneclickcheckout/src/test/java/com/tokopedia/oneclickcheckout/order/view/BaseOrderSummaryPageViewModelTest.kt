package com.tokopedia.oneclickcheckout.order.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartOccExternalUseCase
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.oneclickcheckout.common.dispatchers.TestDispatchers
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.domain.CheckoutOccUseCase
import com.tokopedia.oneclickcheckout.order.domain.GetOccCartUseCase
import com.tokopedia.oneclickcheckout.order.domain.UpdateCartOccUseCase
import com.tokopedia.oneclickcheckout.order.view.model.OrderData
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import rx.Observable

open class BaseOrderSummaryPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var addToCartOccExternalUseCase: AddToCartOccExternalUseCase

    @MockK
    lateinit var getOccCartUseCase: GetOccCartUseCase

    @MockK
    lateinit var ratesUseCase: GetRatesUseCase

    @MockK
    lateinit var getPreferenceListUseCase: GetPreferenceListUseCase

    @MockK
    lateinit var updateCartOccUseCase: UpdateCartOccUseCase

    @MockK
    lateinit var ratesResponseStateConverter: RatesResponseStateConverter

    @MockK
    lateinit var editAddressUseCase: EditAddressUseCase

    @MockK
    lateinit var checkoutOccUseCase: CheckoutOccUseCase

    @MockK
    lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    @MockK
    lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    val testDispatchers: TestDispatchers = TestDispatchers

    private val testSchedulers: ExecutorSchedulers = TestSchedulers

    lateinit var helper: OrderSummaryPageViewModelTestHelper

    lateinit var orderSummaryPageViewModel: OrderSummaryPageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        helper = OrderSummaryPageViewModelTestHelper()
        orderSummaryPageViewModel = OrderSummaryPageViewModel(testDispatchers, testSchedulers, addToCartOccExternalUseCase,
                getOccCartUseCase, ratesUseCase, getPreferenceListUseCase, updateCartOccUseCase, ratesResponseStateConverter,
                editAddressUseCase, checkoutOccUseCase, clearCacheAutoApplyStackUseCase,
                validateUsePromoRevampUseCase, userSessionInterface, orderSummaryAnalytics)
    }

    fun setUpCartAndRates() {
        setUpCartAndRatesMocks()

        orderSummaryPageViewModel.getOccCart(true, "")
    }

    fun setUpCartAndRatesMocks() {
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(helper.orderData) }
        every { ratesResponseStateConverter.fillState(any(), any(), any(), any()) } returnsArgument (0)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
    }
}