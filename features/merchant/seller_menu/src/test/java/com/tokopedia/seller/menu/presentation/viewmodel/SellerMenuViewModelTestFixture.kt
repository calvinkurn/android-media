package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaData
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaResponse
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaWrapper
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingFail
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingResponse
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.seller.menu.data.model.SellerMenuNotificationResponse
import com.tokopedia.seller.menu.data.model.SellerMenuNotificationResponse.*
import com.tokopedia.seller.menu.domain.query.ShopScoreLevelResponse
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class SellerMenuViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var getAllShopInfoUseCase: GetAllShopInfoUseCase
    private lateinit var getProductListMetaUseCase: GetProductListMetaUseCase
    private lateinit var getSellerMenuNotifications: GetSellerNotificationUseCase
    private lateinit var getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase
    private lateinit var getShopScoreLevelUseCase: GetShopScoreLevelUseCase
    private lateinit var userSession: UserSessionInterface

    protected lateinit var viewModel: SellerMenuViewModel

    @Before
    fun setUp() {
        getAllShopInfoUseCase = mockk(relaxed = true)
        getProductListMetaUseCase = mockk(relaxed = true)
        getSellerMenuNotifications = mockk(relaxed = true)
        getShopCreatedInfoUseCase = mockk(relaxed = true)
        getShopScoreLevelUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        viewModel = SellerMenuViewModel(
                getAllShopInfoUseCase,
                getShopCreatedInfoUseCase,
                getProductListMetaUseCase,
                getSellerMenuNotifications,
                getShopScoreLevelUseCase,
                userSession,
                coroutineTestRule.dispatchers
        )
    }

    protected fun onGetShopInfoPeriodUseCase_thenReturn(response: ShopInfoPeriodUiModel) {
        coEvery { getShopCreatedInfoUseCase.executeOnBackground() } returns response
    }

    protected fun onGetGetShopInfoPeriodUseCase_thenReturnError(error: Throwable) {
        coEvery { getShopCreatedInfoUseCase.executeOnBackground() } throws error
    }

    protected fun onGetAllShopInfoUseCase_thenReturn(response: Pair<PartialSettingResponse, PartialSettingResponse>) {
        coEvery { getAllShopInfoUseCase.executeOnBackground() } returns response
    }

    protected fun onGetAllShopInfoUseCase_thenReturn(error: Throwable) {
        coEvery { getAllShopInfoUseCase.executeOnBackground() } throws error
    }

    protected fun onGetShopScoreLevel_thenReturn(shopId: String, shopScoreLevel: ShopScoreLevelResponse.ShopScoreLevel.Result) {
        coEvery { getShopScoreLevelUseCase.execute(shopId) } returns shopScoreLevel
    }

    protected fun onGetShopScoreLevel_thenReturn(shopId: String, error: Throwable) {
        coEvery { getShopScoreLevelUseCase.execute(shopId) } throws  error
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
        coEvery { getSellerMenuNotifications.executeOnBackground() } throws error
    }

    protected fun createShopSettingsResponse(
            totalFollowers: Long = 35000,
            shopBadgeUrl: String = "https://www.tokopedia/shop_bage.png",
            shopType: PowerMerchantStatus = PowerMerchantStatus.Active,
            successPair: Pair<Boolean, Boolean> = true to true
    ): Pair<PartialSettingResponse, PartialSettingResponse> {
        val (isShopInfoSuccess, topAdsInfoSuccess) = successPair
        val userShopInfoWrapper = UserShopInfoWrapper(shopType = shopType)
        val shopInfoResponse =
            if (isShopInfoSuccess) {
                PartialShopSettingSuccessInfo(
                    userShopInfoWrapper,
                    totalFollowers,
                    shopBadgeUrl
                )
            } else {
                PartialSettingFail
            }

        val topAdsInfoResponse =
            if (topAdsInfoSuccess) {
                PartialTopAdsSettingSuccessInfo(
                    OthersBalance()
                )
            } else {
                PartialSettingFail
            }

        return Pair(shopInfoResponse, topAdsInfoResponse)
    }

    protected fun createShopInfoUiModel(
            totalFollowers: Long = 35000,
            shopBadgeUrl: String = "https://www.tokopedia/shop_bage.png",
            shopType: PowerMerchantStatus = PowerMerchantStatus.Active,
            shopScore: Long = 70,
            shopAge: Long = 65
    ): ShopInfoUiModel {
        val userShopInfoWrapper = UserShopInfoWrapper(shopType = shopType)
        val shopInfoResponse = PartialShopSettingSuccessInfo(
                userShopInfoWrapper,
                totalFollowers,
                shopBadgeUrl
        )

        val topAdsInfoResponse = PartialTopAdsSettingSuccessInfo(
                OthersBalance()
        )

        return ShopInfoUiModel(SettingShopInfoUiModel(
                shopInfoResponse,
                topAdsInfoResponse,
                userSession
        ), shopScore = shopScore, shopAge = shopAge)
    }

    protected fun createNotificationResponse(
            newOrder: Int,
            readyToShip: Int,
            totalUnread: Int,
            talk: Int,
            inResolution: Int
    ): SellerMenuNotificationResponse {
        val orderStatus = SellerOrderStatus(newOrder, readyToShip, inResolution)
        val notifCenterTotalUnread = NotifCenterTotalUnread(totalUnread)
        val inbox = Inbox(talk)
        val notifications = Notifications(orderStatus, notifCenterTotalUnread, inbox)

        return SellerMenuNotificationResponse(notifications)
    }
}