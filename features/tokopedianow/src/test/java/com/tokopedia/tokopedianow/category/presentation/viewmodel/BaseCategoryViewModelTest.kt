package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseCategoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private lateinit var getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase
    private lateinit var addressData: TokoNowLocalAddress
    private lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase
    private lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var affiliateService: NowAffiliateService
    private lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: BaseCategoryViewModel

    @Before
    fun setUp() {
        getShopAndWarehouseUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)
        getTargetedTickerUseCase = mockk(relaxed = true)
        getMiniCartUseCase = mockk(relaxed = true)
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        affiliateService = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        viewModel = BaseCategoryViewModel(
            getShopAndWarehouseUseCase = getShopAndWarehouseUseCase,
            addressData = addressData,
            getTargetedTickerUseCase = getTargetedTickerUseCase,
            getMiniCartUseCase = getMiniCartUseCase,
            addToCartUseCase = addToCartUseCase,
            updateCartUseCase = updateCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            affiliateService = affiliateService,
            userSession = userSession,
            dispatchers = coroutineTestRule.dispatchers
        ).apply {
            miniCartSource = MiniCartSource.TokonowCategoryPage
        }
    }

    @Test
    fun `given shopId is not valid when onViewCreated should call getShopAndWarehouseUseCase`() {
        val invalidShopId = 0L
        val getShopAndWarehouseResponse = GetStateChosenAddressResponse()

        onGetShopId_thenReturn(invalidShopId)
        onGetShopAndWarehouse_thenReturn(getShopAndWarehouseResponse)

        viewModel.onViewCreated()

        verifyGetShopAndWarehouseUseCaseCalled()
        verifyUpdateAddressData(getShopAndWarehouseResponse)

        viewModel.isPageLoading
            .verifyValueEquals(true)

        viewModel.onPageError
            .verifyValueEquals(null)
    }

    @Test
    fun `given shopId is not valid when getShopAndWarehouseUseCase throws error should do nothing`() {
        val invalidShopId = 0L

        onGetShopId_thenReturn(invalidShopId)
        onGetShopAndWarehouse_throwsError()

        viewModel.onViewCreated()

        verifyGetShopAndWarehouseUseCaseCalled()
    }

    @Test
    fun `given isChoosenAddressUpdated true when onViewResume should refresh layout and update toolbar notification`() {
        onGetIsChoosenAddressUpdated_thenReturn(isUpdated = true)

        viewModel.onViewResume()

        viewModel.refreshState
            .verifyValueEquals(Unit)

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `given isChoosenAddressUpdated false when onViewResume should call getMiniCart and update toolbar notification`() {
        onGetIsChoosenAddressUpdated_thenReturn(isUpdated = false)
        onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetWarehouseId_thenReturn(warehouseId = 1523)
        onGetShopId_thenReturn(shopId = 9124)

        viewModel.onViewResume()

        verifyGetMiniCartUseCaseCalled()

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `when get currentCategoryId should return currentCategoryId`() {
        viewModel.currentCategoryId = "1252"

        val expectedCurrentCategoryId = "1252"
        val actualCurrentCategoryId = viewModel.currentCategoryId

        assertEquals(expectedCurrentCategoryId, actualCurrentCategoryId)
    }

    @Test
    fun `when get queryParamMap should return queryParamMap`() {
        viewModel.queryParamMap = hashMapOf("sc" to "1247")

        val expectedQueryParamMap = hashMapOf("sc" to "1247")
        val actualQueryParamMap = viewModel.queryParamMap

        assertEquals(expectedQueryParamMap, actualQueryParamMap)
    }

    private fun onGetShopAndWarehouse_thenReturn(response: GetStateChosenAddressResponse) {
        coEvery {
            getShopAndWarehouseUseCase(any())
        } returns response
    }

    private fun onGetShopAndWarehouse_throwsError() {
        coEvery {
            getShopAndWarehouseUseCase(any())
        } throws NullPointerException()
    }

    private fun onGetIsChoosenAddressUpdated_thenReturn(isUpdated: Boolean) {
        every { addressData.isChoosenAddressUpdated() } returns isUpdated
    }

    private fun onGetIsUserLoggedIn_thenReturn(isLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isLoggedIn
    }

    private fun onGetShopId_thenReturn(shopId: Long) {
        every { addressData.getShopId() } returns shopId
    }

    private fun onGetWarehouseId_thenReturn(warehouseId: Long) {
        every { addressData.getWarehouseId() } returns warehouseId
    }

    private fun verifyGetMiniCartUseCaseCalled() {
        coVerify { getMiniCartUseCase.executeOnBackground() }
    }

    private fun verifyGetShopAndWarehouseUseCaseCalled() {
        coVerify { getShopAndWarehouseUseCase(any()) }
    }

    private fun verifyUpdateAddressData(response: GetStateChosenAddressResponse) {
        verify { addressData.updateAddressData(response) }
    }
}
