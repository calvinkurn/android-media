package com.tokopedia.tokopedianow.common.base.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
open class BaseTokoNowViewModelTestFixture {

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: BaseTokoNowViewModel

    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    private lateinit var affiliateHelper: AffiliateCookieHelper
    private lateinit var affiliateService: NowAffiliateService
    private lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase
    private lateinit var addressData: TokoNowLocalAddress
    private lateinit var userSession: UserSessionInterface

    @Before
    fun setUp() {
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        getMiniCartUseCase = mockk(relaxed = true)
        affiliateHelper = mockk(relaxed = true)
        getTargetedTickerUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        affiliateService = NowAffiliateService(affiliateHelper)

        viewModel = BaseTokoNowViewModel(
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartUseCase,
            affiliateService,
            getTargetedTickerUseCase,
            addressData,
            userSession,
            coroutineTestRule.dispatchers
        ).apply {
            miniCartSource = MiniCartSource.TokonowHome
        }

        onGetIsOutOfCoverage_thenReturn(outOfCoverage = false)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
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

    protected fun onInitAffiliateCookie_thenReturn(error: Throwable) {
        coEvery { affiliateHelper.initCookie(any(), any(), any()) } throws error
    }

    protected fun onGetTickerDataAsync_thenReturn(error: Throwable) {
        coEvery { getTargetedTickerUseCase.execute(anyString(), anyString()) } throws error
    }

    protected fun verifyGetMiniCartUseCaseCalled() {
        coVerify { getMiniCartUseCase.executeOnBackground() }
    }

    protected fun verifyGetMiniCartUseCaseNotCalled() {
        coVerify(exactly = 0) { getMiniCartUseCase.execute(any(), any()) }
    }

    protected fun verifyAddToCartUseCaseCalled(times: Int = 1) {
        verify(exactly = times) { addToCartUseCase.execute(any(), any()) }
    }

    protected fun verifyAddToCartUseCaseNotCalled() {
        verify(exactly = 0) { addToCartUseCase.execute(any(), any()) }
    }

    protected fun verifyDeleteCartUseCaseCalled() {
        verify { deleteCartUseCase.execute(any(), any()) }
    }

    protected fun verifyDeleteCartUseCaseNotCalled() {
        verify(exactly = 0) { deleteCartUseCase.execute(any(), any()) }
    }

    protected fun verifyUpdateCartUseCaseCalled() {
        verify { updateCartUseCase.execute(any(), any()) }
    }

    protected fun verifyUpdateCartUseCaseNotCalled() {
        verify(exactly = 0) { updateCartUseCase.execute(any(), any()) }
    }

    protected fun verifyInitAffiliateCookieCalled(
        affiliateUUID: String,
        affiliateChannel: String
    ) {
        coVerify(exactly = 2) { affiliateHelper.initCookie(affiliateUUID, affiliateChannel, any()) }
    }

    protected fun verifyInitAffiliateCookieNotCalled(
        affiliateUUID: String,
        affiliateChannel: String
    ) {
        coVerify(exactly = 0) { affiliateHelper.initCookie(affiliateUUID, affiliateChannel, any()) }
    }

    protected fun verifyCreateAffiliateLinkCalled(url: String) {
        verify { affiliateHelper.createAffiliateLink(url, anyString()) }
    }
}
