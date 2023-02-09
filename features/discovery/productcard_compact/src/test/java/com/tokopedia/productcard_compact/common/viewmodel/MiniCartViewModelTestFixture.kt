package com.tokopedia.productcard_compact.common.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.productcard_compact.common.util.LocalAddress
import com.tokopedia.productcard_compact.similarproduct.presentation.viewmodel.MiniCartViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class MiniCartViewModelTestFixture {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: MiniCartViewModel

    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    private lateinit var addressData: LocalAddress
    private lateinit var userSession: UserSessionInterface

    @Before
    fun setUp() {
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        getMiniCartUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        viewModel = MiniCartViewModel(
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartUseCase,
            addressData,
            userSession,
            coroutineTestRule.dispatchers
        )

        onGetIsOutOfCoverage_thenReturn(outOfCoverage = false)
    }

    protected fun onAddToCart_thenReturn(response: AddToCartDataModel) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(response)
        }
    }

    protected fun onAddToCart_thenReturn(error: Throwable) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onRemoveItemCart_thenReturn(response: RemoveFromCartData) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(response)
        }
    }

    protected fun onRemoveItemCart_thenReturn(error: Throwable) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onUpdateItemCart_thenReturn(response: UpdateCartV2Data) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(response)
        }
    }

    protected fun onUpdateItemCart_thenReturn(error: Throwable) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onGetMiniCart_thenReturn(response: MiniCartSimplifiedData) {
        coEvery {
            getMiniCartUseCase.executeOnBackground()
        } returns response
    }

    protected fun onGetMiniCart_throwException(error: Throwable) {
        coEvery {
            getMiniCartUseCase.executeOnBackground()
        } throws error
    }

    protected fun onGetUserLoggedIn_thenReturn(isLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isLoggedIn
    }

    protected fun onGetShopId_thenReturn(shopId: Long) {
        every { addressData.getShopId() } returns shopId
    }

    protected fun onGetWarehouseId_thenReturn(warehouseId: Long) {
        every { addressData.getWarehouseId() } returns warehouseId
    }

    protected fun onGetIsOutOfCoverage_thenReturn(outOfCoverage: Boolean) {
        every { addressData.isOutOfCoverage() } returns outOfCoverage
    }

    protected fun verifyGetMiniCartUseCaseCalled() {
        coVerify { getMiniCartUseCase.executeOnBackground() }

    }

    protected fun verifyGetMiniCartUseCaseNotCalled(){
        coVerify(exactly = 0) { getMiniCartUseCase.execute(any(), any()) }
    }

    protected fun verifyAddToCartUseCaseCalled(times: Int = 1) {
        verify(exactly = times) { addToCartUseCase.execute(any(), any()) }
    }

    protected fun verifyDeleteCartUseCaseCalled() {
        verify { deleteCartUseCase.execute(any(), any()) }
    }

    protected fun verifyUpdateCartUseCaseCalled() {
        verify { updateCartUseCase.execute(any(), any()) }
    }
}
