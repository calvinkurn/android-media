package com.tokopedia.tokofood.purchase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokofood.common.domain.param.KeroAddressParamData
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyString

@FlowPreview
@ExperimentalCoroutinesApi
abstract class TokoFoodPurchaseViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    protected lateinit var keroEditAddressUseCase: Lazy<KeroEditAddressUseCase>

    @RelaxedMockK
    protected lateinit var keroGetAddressUseCase: Lazy<KeroGetAddressUseCase>

    @RelaxedMockK
    protected lateinit var checkoutTokoFoodUseCase: Lazy<CheckoutTokoFoodUseCase>

    @RelaxedMockK
    protected lateinit var checkoutGeneralTokoFoodUseCase: Lazy<CheckoutGeneralTokoFoodUseCase>

    protected lateinit var viewModel: TokoFoodPurchaseViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoFoodPurchaseViewModel(
            keroEditAddressUseCase,
            keroGetAddressUseCase,
            checkoutTokoFoodUseCase,
            checkoutGeneralTokoFoodUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    protected fun onGetCheckoutTokoFood_thenReturn(response: CartGeneralCartListData) {
        coEvery {
            checkoutTokoFoodUseCase.get().execute(CHECKOUT_PAGE_SOURCE)
        } returns response
    }

    protected fun onGetCheckoutTokoFood_thenThrow(throwable: Throwable) {
        coEvery {
            checkoutTokoFoodUseCase.get().execute(CHECKOUT_PAGE_SOURCE)
        } throws throwable
    }

    protected fun onCheckoutGeneral_thenReturn(cartListData: CartGeneralCartListData,
                                               response: CheckoutGeneralTokoFoodResponse) {
        coEvery {
            checkoutGeneralTokoFoodUseCase.get().execute(cartListData)
        } returns response
    }

    protected fun onCheckoutGeneral_thenThrow(cartListData: CartGeneralCartListData,
                                              throwable: Throwable) {
        coEvery {
            checkoutGeneralTokoFoodUseCase.get().execute(cartListData)
        } throws throwable
    }

    protected fun onGetAddressUseCase_thenReturn(addressId: String,
                                                 keroAddressParamData: KeroAddressParamData?) {
        coEvery {
            keroGetAddressUseCase.get().execute(addressId)
        } returns keroAddressParamData
    }

    protected fun onGetAddressUseCase_thenThrow(throwable: Throwable) {
        coEvery {
            keroGetAddressUseCase.get().execute(anyString())
        } throws throwable
    }

    companion object {
        const val PURCHASE_SUCCESS_JSON = "json/purchase/purchase_success.json"
        const val PURCHASE_SUCCESS_SINGLE_PRODUCT_JSON = "json/purchase/purchase_success_single_product.json"
        const val PURCHASE_SUCCESS_UNAVAILABLE_PRODUCTS_JSON = "json/purchase/purchase_success_unavailable_products.json"
        const val PURCHASE_SUCCESS_EMPTY_PRODUCT_JSON = "json/purchase/purchase_success_empty_product.json"
        const val PURCHASE_SUCCESS_SHOW_CONSENT_JSON = "json/purchase/purchase_success_show_consent.json"
        const val PURCHASE_SUCCESS_DISABLED_JSON = "json/purchase/purchase_success_disabled.json"
        const val PURCHASE_SUCCESS_CHECKOUT_GENERAL_JSON = "json/purchase/purchase_success_checkout_general.json"
        const val PURCHASE_FAILED_CHECKOUT_GENERAL_JSON = "json/purchase/purchase_failed_checkout_general.json"

        private const val CHECKOUT_PAGE_SOURCE = "checkout_page"
    }


}
