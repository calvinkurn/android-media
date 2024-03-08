package com.tokopedia.seller.menu.presentation.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.imagepicker_insta.common.ImagePickerInstaQueryBuilder
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.component.SellerMenuScreen
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuActionClick
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuComposeViewModel
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.shopadmin.common.util.AdminFeature
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.WebViewHelper
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject
import com.tokopedia.seller.menu.common.R as sellermenucommonR

class SellerMenuComposeActivity : BaseSellerMenuActivity() {

    @Inject
    lateinit var viewModel: SellerMenuComposeViewModel

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
                            is SellerMenuUIEvent.OnSuccessGetShopInfo -> {
                                shopAge = state.shopAge
                                isNewSeller = state.isNewSeller
                                viewModel.onEvent(SellerMenuUIEvent.GetShopInfo)
                            }
                            else -> {
                            }
                        }
                    }
                })

                SellerMenuScreen(
                    viewModel = viewModel,
                    onSuccessLoadInitialState = ::onSuccessLoadInitialState,
                    onActionClick = ::onActionClick,
                    onRefresh = ::onRefresh,
                    onReload = ::onReloading,
                    onTickerClick = ::onTickerClick,
                    onShowToaster = ::onShowToaster,
                    onShopInfoImpressed = ::onShopInfoImpressed,
                    onShopScoreImpressed = ::onShopScoreImpressed
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

    private fun onRefresh() {
        viewModel.onEvent(SellerMenuUIEvent.OnRefresh)
    }

    private fun onActionClick(actionClick: SellerMenuActionClick) {
        when (actionClick) {
            SellerMenuActionClick.BACK_BUTTON -> {
                finish()
            }
            SellerMenuActionClick.INBOX -> {
                goToInbox()
            }
            SellerMenuActionClick.NOTIF_CENTER -> {
                goToNotifCenter()
            }
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
                sellerMenuTracker.sendEventClickShopType()
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
            SellerMenuActionClick.MIGRATION_FEED -> {
                goToFeedMigration()
            }
            SellerMenuActionClick.MIGRATION_PROMO -> {
                goToPromoMigration()
            }
            SellerMenuActionClick.MIGRATION_STATISTIC -> {
                goToStatisticMigration()
            }
            SellerMenuActionClick.MIGRATION_FINANCE -> {
                goToFinanceMigration()
            }
            else -> {
                // No-op
            }
        }
    }

    private fun onReloading(isLoading: Boolean) {
        if (isLoading) {
            viewModel.onEvent(SellerMenuUIEvent.OnRefresh)
        }
    }

    private fun onTickerClick(link: String) {
        goToSlaWebView(link)
    }

    private fun goToSlaWebView(linkUrl: CharSequence) {
        val link = linkUrl.toString()
        if (WebViewHelper.isUrlValid(linkUrl.toString())) {
            RouteManager.route(
                this,
                String.format(
                    Locale.getDefault(),
                    ALLOW_OVERRIDE_URL_FORMAT,
                    ApplinkConst.WEBVIEW,
                    false,
                    link
                )
            )
        } else {
            RouteManager.route(this, link)
        }
    }

    private fun onShowToaster(errorMessage: String) {
        Toaster.build(
            findViewById(android.R.id.content),
            errorMessage,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(sellermenucommonR.string.setting_toaster_error_retry)
        ) {
            viewModel.onEvent(SellerMenuUIEvent.GetInitialMenu)
        }.show()
    }

    private fun onShopInfoImpressed(shopType: ShopType?) {
        sellerMenuTracker.sendEventViewShopAccount(shopType)
    }

    private fun onShopScoreImpressed() {
        sellerMenuTracker.impressShopScoreEntryPoint(isNewSeller)
    }

    private fun goToInbox() {
        RouteManager.route(this, ApplinkConst.INBOX)
        sellerMenuTracker.sendEventClickInbox()
    }

    private fun goToNotifCenter() {
        RouteManager.route(this, ApplinkConst.NOTIFICATION)
        sellerMenuTracker.sendEventClickNotification()
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

    private fun goToStatisticMigration() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.STATISTIC)
        sellerMenuTracker.sendEventClickShopStatistic()
    }

    private fun goToPromoMigration() {
        checkAccessPermissionIfNotShopOwner(AdminFeature.ADS_AND_PROMOTION)
        sellerMenuTracker.sendEventClickCentralizePromo()
    }

    private fun goToFeedMigration() {
        val appLinks = ArrayList<String>().apply {
            add(ApplinkConstInternalSellerapp.SELLER_HOME)
            add(UriUtil.buildUri(ApplinkConst.SHOP, userSession.shopId))
            add(generateFeedCreatePostAppLink())
        }
        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_PLAY_FEED, appLinks)
        sellerMenuTracker.sendEventClickFeedAndPlay()
    }

    private fun goToFinanceMigration() {
        val appLinks = ArrayList<String>().apply {
            add(ApplinkConstInternalSellerapp.SELLER_HOME)
            add("${ApplinkConst.LAYANAN_FINANSIAL}/")
        }
        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_FINANCIAL_SERVICES, appLinks)
        sellerMenuTracker.sendEventClickFintech()
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

    private fun goToSellerMigrationPage(@SellerMigrationFeatureName featureName: String, appLinks: ArrayList<String>) {
        val intent = SellerMigrationActivity.createIntent(
            this,
            featureName,
            SCREEN_NAME,
            appLinks
        )
        startActivity(intent)
    }

    private fun generateFeedCreatePostAppLink(): String {
        val queries = listOf<Pair<String, Any>>(
            Pair(BundleData.TITLE, BundleData.VALUE_POST_SEBAGAI),
            Pair(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2),
            Pair(BundleData.APPLINK_FOR_GALLERY_PROCEED, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2),
            Pair(BundleData.MAX_MULTI_SELECT_ALLOWED, BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED),
            Pair(BundleData.KEY_IS_OPEN_FROM, BundleData.VALUE_IS_OPEN_FROM_SHOP_PAGE)
        )
        return "${ApplinkConst.IMAGE_PICKER_V2}?${ImagePickerInstaQueryBuilder.generateQuery(queries)}"
    }

    companion object {
        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"

        private const val APPLINK_FORMAT = "%s?url=%s%s"

        private const val SCREEN_NAME = "MA - Akun Toko"

        private const val ALLOW_OVERRIDE_URL_FORMAT = "%s?allow_override=%b&url=%s"
    }
}
