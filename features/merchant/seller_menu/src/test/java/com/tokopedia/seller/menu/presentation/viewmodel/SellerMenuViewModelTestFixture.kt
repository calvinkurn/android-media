package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.gm.common.domain.interactor.GetShopScoreUseCase
import com.tokopedia.product.manage.common.list.data.model.filter.ProductListMetaData
import com.tokopedia.product.manage.common.list.data.model.filter.ProductListMetaResponse
import com.tokopedia.product.manage.common.list.data.model.filter.ProductListMetaWrapper
import com.tokopedia.product.manage.common.list.data.model.filter.Tab
import com.tokopedia.product.manage.common.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.common.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingResponse
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType.*
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoUiModel
import com.tokopedia.seller.menu.coroutine.TestCoroutineDispatchers
import com.tokopedia.seller.menu.data.model.SellerMenuNotificationResponse
import com.tokopedia.seller.menu.data.model.SellerMenuNotificationResponse.*
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class SellerMenuViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getAllShopInfoUseCase: GetAllShopInfoUseCase
    private lateinit var getProductListMetaUseCase: GetProductListMetaUseCase
    private lateinit var getSellerMenuNotifications: GetSellerNotificationUseCase
    private lateinit var getShopScoreUseCase: GetShopScoreUseCase
    private lateinit var userSession: UserSessionInterface

    protected lateinit var testDispatcher: TestCoroutineDispatcher
    protected lateinit var viewModel: SellerMenuViewModel

    @Before
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers

        getAllShopInfoUseCase = mockk(relaxed = true)
        getProductListMetaUseCase = mockk(relaxed = true)
        getSellerMenuNotifications = mockk(relaxed = true)
        getShopScoreUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        testDispatcher = dispatchers.testDispatcher

        viewModel = SellerMenuViewModel(
            getAllShopInfoUseCase,
            getProductListMetaUseCase,
            getSellerMenuNotifications,
            getShopScoreUseCase,
            userSession,
            dispatchers
        )

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    protected fun onGetAllShopInfoUseCase_thenReturn(response: Pair<PartialSettingResponse, PartialSettingResponse>) {
        coEvery { getAllShopInfoUseCase.executeOnBackground() } returns response
    }

    protected fun onGetAllShopInfoUseCase_thenReturn(error: Throwable) {
        coEvery { getAllShopInfoUseCase.executeOnBackground() } throws error
    }

    protected fun onGetShopScore_thenReturn(response: ShopScoreResult) {
        coEvery { getShopScoreUseCase.getData(any()) } returns response
    }

    protected fun onGetShopScore_thenReturn(error: Throwable) {
        coEvery { getShopScoreUseCase.getData(any()) } throws error
    }

    protected fun onGetProductListMeta_thenReturn(tabs: List<Tab>) {
        val productListMetaData = ProductListMetaData(tabs = tabs)
        val productListMetaWrapper = ProductListMetaWrapper(productListMetaData = productListMetaData)
        val productListMetaResponse = ProductListMetaResponse(productListMetaWrapper)

        coEvery { getProductListMetaUseCase.executeOnBackground() } returns productListMetaResponse
    }

    protected fun onGetProductListMeta_thenReturn(error: Throwable) {
        coEvery { getProductListMetaUseCase.executeOnBackground() } throws error
    }

    protected fun onGetNotifications_thenReturn(response: SellerMenuNotificationResponse) {
        coEvery { getSellerMenuNotifications.executeOnBackground() } returns response
    }

    protected fun onGetNotifications_thenReturn(error: Throwable) {
        coEvery { getSellerMenuNotifications.executeOnBackground() } throws  error
    }

    protected fun createShopSettingsResponse(
        totalFollowers: Int = 35000,
        topAdsBalance: Float = 2000f,
        isAutoTopUp: Boolean = true,
        shopBadgeUrl: String = "https://www.tokopedia/shop_bage.png",
        shopType: PowerMerchantStatus = PowerMerchantStatus.Active
    ): Pair<PartialSettingResponse, PartialSettingResponse> {
        val shopInfoResponse = PartialShopSettingSuccessInfo(
            shopType,
            totalFollowers,
            shopBadgeUrl
        )

        val topAdsInfoResponse = PartialTopAdsSettingSuccessInfo(
            OthersBalance(),
            topAdsBalance,
            isAutoTopUp
        )

        return Pair(shopInfoResponse, topAdsInfoResponse)
    }

    protected fun createShopInfoUiModel(
        totalFollowers: Int = 35000,
        topAdsBalance: Float = 2000f,
        isAutoTopUp: Boolean = true,
        shopBadgeUrl: String = "https://www.tokopedia/shop_bage.png",
        shopType: PowerMerchantStatus = PowerMerchantStatus.Active
    ): ShopInfoUiModel {

        val shopInfoResponse = PartialShopSettingSuccessInfo(
            shopType,
            totalFollowers,
            shopBadgeUrl
        )

        val topAdsInfoResponse = PartialTopAdsSettingSuccessInfo(
            OthersBalance(),
            topAdsBalance,
            isAutoTopUp
        )

        return ShopInfoUiModel(SettingShopInfoUiModel(
            shopInfoResponse,
            topAdsInfoResponse,
            userSession
        ))
    }

    protected fun createNotificationResponse(
        newOrder: Int,
        readyToShip: Int,
        totalUnread: Int,
        talk: Int
    ): SellerMenuNotificationResponse {
        val orderStatus = SellerOrderStatus(newOrder, readyToShip)
        val notifications = Notifications(orderStatus)
        val notifCenterTotalUnread = NotifCenterTotalUnread(totalUnread)
        val inbox = Inbox(talk)

        return SellerMenuNotificationResponse(notifications, notifCenterTotalUnread, inbox)
    }
}