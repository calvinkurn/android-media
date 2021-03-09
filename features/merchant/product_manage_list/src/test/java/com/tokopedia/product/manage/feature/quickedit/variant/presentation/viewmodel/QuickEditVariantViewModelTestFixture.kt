package com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel.QuickEditVariantViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel.QuickEditVariantViewModelTestFixture.LocationType.MAIN_LOCATION
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel.QuickEditVariantViewModelTestFixture.LocationType.OTHER_LOCATION
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ShopLocationResponse
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

open class QuickEditVariantViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: QuickEditVariantViewModel
    private lateinit var getProductVariantUseCase: GetProductVariantUseCase
    private lateinit var getProductManageAccessUseCase: GetProductManageAccessUseCase
    private lateinit var getAdminInfoShopLocationUseCase: GetAdminInfoShopLocationUseCase
    private lateinit var userSession: UserSessionInterface

    @Before
    fun setUp() {
        getProductVariantUseCase = mockk(relaxed = true)
        getProductManageAccessUseCase = mockk(relaxed = true)
        getAdminInfoShopLocationUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        viewModel = QuickEditVariantViewModel(
                getProductVariantUseCase,
                getProductManageAccessUseCase,
                getAdminInfoShopLocationUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )

        val locationList = listOf(
            ShopLocationResponse("1", MAIN_LOCATION),
            ShopLocationResponse("2", OTHER_LOCATION)
        )

        onGetIsShopOwner_thenReturn(true)
        onGetWarehouseId_thenReturn(locationList)
    }

    private fun onGetIsShopOwner_thenReturn(isShopOwner: Boolean) {
        every { userSession.isShopOwner } returns isShopOwner
    }

    private fun onGetWarehouseId_thenReturn(locationList: List<ShopLocationResponse>) {
        coEvery {
            getAdminInfoShopLocationUseCase.execute(any())
        } returns locationList
    }

    protected fun onGetProductVariant_thenReturn(response: GetProductVariantResponse) {
        coEvery { getProductVariantUseCase.execute(any()) } returns response
    }

    protected fun onGetProductVariant_thenError(error: Throwable) {
        coEvery { getProductVariantUseCase.execute(any()) } throws error
    }

    protected fun verifyHideProgressBar() {
        viewModel.showProgressBar
            .verifyValueEquals(false)
    }

    protected fun verifyShowErrorView() {
        viewModel.showErrorView
            .verifyValueEquals(true)
    }

    private object LocationType {
        const val MAIN_LOCATION = 1
        const val OTHER_LOCATION = 99
    }
}