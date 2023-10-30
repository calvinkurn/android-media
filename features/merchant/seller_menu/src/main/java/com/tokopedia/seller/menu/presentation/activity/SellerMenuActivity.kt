package com.tokopedia.seller.menu.presentation.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.component.SellerMenuContent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuActionClick
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuViewModel
import com.tokopedia.shopadmin.common.util.AdminFeature
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SellerMenuActivity : BaseSellerMenuActivity() {

    @Inject
    lateinit var viewModel: SellerMenuViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var adminPermissionMapper: AdminPermissionMapper

    private var shopAge: Long = 0L
    private var isNewSeller: Boolean = false

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)

        setContent {
            NestTheme {
                LaunchedEffect(key1 = false, block = {
                    viewModel.uiEvent.collectLatest { state ->
                        when (state) {
                            is SellerMenuUIEvent.OnSuccessGetShopInfoUse -> {
                                shopAge = state.shopAge
                                isNewSeller = state.isNewSeller
                                viewModel.getAllSettingShopInfo(false)
                            }
                            else -> {
                            }
                        }
                    }
                })

                val state = viewModel.uiState.collectAsState()

                SellerMenuContent(
                    uiState = state.value,
                    onSuccessLoadInitialState = ::onSuccessLoadInitialState,
                    onActionClick = ::onActionClick
                )
            }
        }
    }

    private fun initInjector() {
        DaggerSellerMenuComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun onSuccessLoadInitialState() {
        viewModel.getShopAccountInfo()
    }

    private fun onActionClick(actionClick: SellerMenuActionClick) {
        when (actionClick) {
            SellerMenuActionClick.SHOP -> {
                goToShop()
            }
            SellerMenuActionClick.SHOP_FAVORITES -> {
                goToShopFavouriteList()
            }
            SellerMenuActionClick.SHOP_SCORE -> {
                goToShopScore()
            }
            SellerMenuActionClick.BALANCE -> {
                goToBalance()
            }
            SellerMenuActionClick.POWER_MERCHANT -> {
                goToPowerMerchantSubscribe(false)
                sellerMenuTracker.sendEventClickShopSettingNew()
            }
            SellerMenuActionClick.POWER_MERCHANT_UPGRADE -> {
                goToPowerMerchantSubscribe(true)
                sellerMenuTracker.sendEventClickShopSettingNew()
            }
            SellerMenuActionClick.POWER_MERCHANT_INACTIVE -> {
                goToPowerMerchantSubscribe(false)
                sellerMenuTracker.sendEventClickShopSettingNew()
            }
            SellerMenuActionClick.OFFICIAL_STORE -> {
                sellerMenuTracker.sendEventClickShopSettingNew()
            }
            SellerMenuActionClick.NEW_ORDER -> {
                goToNewOrder()
            }
            SellerMenuActionClick.READY_TO_SHIP_ORDER -> {
                goToReadyToShipOrder()
            }
            SellerMenuActionClick.ALL_ORDER -> {
                goToAllOrders()
            }
            SellerMenuActionClick.PRODUCT_LIST -> {
                goToProductList()
            }
            SellerMenuActionClick.ADD_PRODUCT -> {
                goToAddProduct()
            }
            SellerMenuActionClick.REVIEW -> {
                goToReview()
            }
            SellerMenuActionClick.DISCUSSION -> {
                goToDiscussion()
            }
            SellerMenuActionClick.COMPLAINTS -> {
                goToComplaints()
            }
            SellerMenuActionClick.SELLER_EDU -> {
                goToSellerEdu()
            }
            SellerMenuActionClick.TOKOPEDIA_CARE -> {
                goToTokopediaCare()
            }
            SellerMenuActionClick.SETTINGS -> {
                goToSettings()
            }
            else -> {
                // No-op
            }
        }
    }

    private fun goToShop() {
        RouteManager.route(
            this,
            ApplinkConstInternalMarketplace.SHOP_PAGE,
            userSession.shopId
        )
    }

    private fun goToShopFavouriteList() {
        val intent =
            RouteManager.getIntent(
                this,
                ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST
            )
        intent.putExtra(EXTRA_SHOP_ID, userSession.shopId)
        startActivity(intent)
    }

    private fun goToShopScore() {
        RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_PERFORMANCE, userSession.shopId)
        sellerMenuTracker.sendShopScoreEntryPoint(isNewSeller)
    }

    private fun goToBalance() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false)) {
            RouteManager.route(this, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        } else {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
            startActivity(intent)
        }
    }

    private fun goToPowerMerchantSubscribe(isUpdate: Boolean) {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val appLinkPMTabBuilder = Uri.parse(appLink).buildUpon()
        if (isUpdate) {
            appLinkPMTabBuilder.appendQueryParameter(
                ApplinkConstInternalMarketplace.ARGS_IS_UPGRADE,
                true.toString()
            )
        }
        RouteManager.route(this, appLink)
    }

    private fun goToNewOrder() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.NEW_ORDER)
        sellerMenuTracker.sendEventClickOrderNew()
    }

    private fun goToReadyToShipOrder() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.READY_TO_SHIP_ORDER)
        sellerMenuTracker.sendEventClickOrderReadyToShip()
    }

    private fun goToAllOrders() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.ORDER_HISTORY)
        sellerMenuTracker.sendEventClickOrderHistory()
    }

    private fun goToProductList() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.MANAGE_PRODUCT)
        sellerMenuTracker.sendEventClickProductList()
    }

    private fun goToAddProduct() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.ADD_PRODUCT)
        sellerMenuTracker.sendEventAddProductClick()
    }

    private fun goToReview() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.REVIEW)
        sellerMenuTracker.sendEventClickReview()
    }

    private fun goToDiscussion() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.DISCUSSION)
        sellerMenuTracker.sendEventClickDiscussion()
    }

    private fun goToComplaints() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.COMPLAINT)
        sellerMenuTracker.sendEventClickComplain()
    }

    private fun goToSellerEdu() {
        val sellerEduApplink = String.format(
            APPLINK_FORMAT,
            ApplinkConst.WEBVIEW,
            SellerBaseUrl.SELLER_HOSTNAME,
            SellerBaseUrl.SELLER_EDU
        )
        RouteManager.route(this, sellerEduApplink)
        sellerMenuTracker.sendEventClickSellerEdu()
    }

    private fun goToTokopediaCare() {
        RouteManager.route(this, ApplinkConst.TOKOPEDIA_CARE_HELP)
        sellerMenuTracker.sendEventClickTokopediaCare()
    }

    private fun goToSettings() {
        RouteManager.route(this, ApplinkConstInternalSellerapp.SELLER_SETTINGS)
        sellerMenuTracker.sendEventClickShopSettings()
    }

    private fun checkAccessPermissionIfNotShopOwner(@AdminFeature feature: String) {
        val intent =
            if (userSession.isShopOwner) {
                adminPermissionMapper.mapFeatureToDestination(this, feature)
            } else {
                RouteManager.getIntent(this, ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, feature)
            }
        startActivity(intent)
    }

    companion object {
        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"

        private const val APPLINK_FORMAT = "%s?url=%s%s"
    }
}
