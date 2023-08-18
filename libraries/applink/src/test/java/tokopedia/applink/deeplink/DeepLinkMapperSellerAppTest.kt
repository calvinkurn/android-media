package tokopedia.applink.deeplink

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.config.GlobalConfig
import io.mockk.every
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkMapperSellerAppTest : DeepLinkMapperTestFixture() {

    override fun setup() {
        super.setup()
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
    }

    @Test
    fun `check home appLink login then return seller home in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome"
        every {
            DeeplinkMapperHome.isLoginAndHasShop(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.HOME, expectedDeepLink)
    }

    @Test
    fun `check home appLink not login then return seller home in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/welcome"
        every {
            DeeplinkMapperHome.isLoginAndHasShop(context)
        } returns false
        assertEqualsDeepLinkMapper(ApplinkConst.HOME, expectedDeepLink)
    }

    @Test
    fun `check product add then should return tokopedia internal open product preview in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/open-product-preview"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.PRODUCT_ADD, expectedDeepLink)
    }

    @Test
    fun `check top ads credit appLink then should return tokopedia internal top ads credit in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/buy"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREDIT, expectedDeepLink)
    }

    @Test
    fun `check top ads auto topup appLink then should return tokopedia internal top ads topup in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/auto-topup"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP, expectedDeepLink)
    }

    @Test
    fun `check top ads product create appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE, "")
    }

    @Test
    fun `check top ads create ads then appLink should return tokopedia internal top ads create ads in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/create-ads"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_ADS, expectedDeepLink)
    }

    @Test
    fun `check top ads creation onboard appLink then should return tokopedia internal top ads creation onboard in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/creation-onboard"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_ONBOARDING, expectedDeepLink)
    }

    @Test
    fun `check top ads create auto ads appLink then should return tokopedia internal top ads create auto ads in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/create-autoads"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_AUTO_ADS, expectedDeepLink)
    }

    @Test
    fun `check top ads edit auto ads appLink then should return tokopedia internal top ads edit auto ads in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/edit-autoads"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_EDIT_AUTO_ADS, expectedDeepLink)
    }

    @Test
    fun `check top ads history credit appLink then should return tokopedia internal top ads history credit in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/history-credit"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREDIT_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check top ads picker appLink then should return tokopedia internal top ads picker in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/ad-picker"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_CHOOSER, expectedDeepLink)
    }

    @Test
    fun `check gold merchant appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.GOLD_MERCHANT, "")
    }

    @Test
    fun `check seller app onboarding appLink then should return tokopedia internal onboarding in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/welcome"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SELLER_ONBOARDING, expectedDeepLink)
    }

    @Test
    fun `check seller app home appLink then should return tokopedia internal home in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SELLER_APP_HOME, expectedDeepLink)
    }

    @Test
    fun `check topads dashboard appLink then should return tokopedia internal topads dashboard in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_DASHBOARD, expectedDeepLink)
    }

    @Test
    fun `check power merchant subscribe appLink then should return tokopedia internal power merchant subscribe in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/power-merchant-subscribe"
        every {
            PowerMerchantDeepLinkMapper.isEnablePMSwitchToWebView(context)
        } returns false
        every {
            PowerMerchantDeepLinkMapper.isLoginAndHasShop(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE, expectedDeepLink)
    }

    @Test
    fun `check pm benefit package appLink then should return tokopedia internal pm benefit package in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/pm-benefit-package"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.PM_BENEFIT_PACKAGE, expectedDeepLink)
    }

    @Test
    fun `check voucher list appLink then should return tokopedia internal voucher list in sellerapp`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_REDIRECTION_PAGE
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_LIST, expectedDeepLink)
    }

    @Test
    fun `check voucher active appLink then should return tokopedia internal voucher active in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/seller-mvc/list/active/"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_ACTIVE, expectedDeepLink)
    }

    @Test
    fun `check history voucher appLink then should return tokopedia internal voucher list history in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/seller-mvc/list/history/"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check voucher detail appLink then should return tokopedia internal voucher detail in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/seller-mvc/detail//"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check create voucher appLink then should return tokopedia internal create voucher in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/seller-mvc/create/shop/"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.CREATE_VOUCHER, expectedDeepLink)
    }
    @Test
    fun `check seller search appLink then should return tokopedia internal seller search in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/seller-search"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SELLER_SEARCH, expectedDeepLink)
    }

    @Test
    fun `check play broadcaster appLink then should return tokopedia internal play broadcaster in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://play-broadcaster"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.PLAY_BROADCASTER, expectedDeepLink)
    }

    @Test
    fun `check centralized promo appLink then should return tokopedia internal centralized promo in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/centralized-promo"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.CENTRALIZED_PROMO, expectedDeepLink)
    }

    @Test
    fun `check centralized promo appLink and remote config true then should return tokopedia internal centralized promo in sellerapp`() {
        setRemoteConfig(true)
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.CENTRALIZED_PROMO,
            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_COMPOSE
        )
    }

    @Test
    fun `check centralized promo internal appLink and remote config true then should return tokopedia internal centralized promo compose in sellerapp`() {
        setRemoteConfig(true)
        assertEqualsDeepLinkMapper(
            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO,
            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_COMPOSE
        )
    }

    @Test
    fun `check centralized promo internal appLink and remote config false then should return tokopedia internal centralized promo compose in sellerapp`() {
        setRemoteConfig(false)
        assertEqualsDeepLinkMapper(
            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO,
            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
        )
    }

    @Test
    fun `check shop feed appLink then should return tokopedia internal feed in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/feed"
        val appLink = UriUtil.buildUri(ApplinkConst.SellerApp.SHOP_FEED, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check seller info detail appLink then should return tokopedia internal seller info detail in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://sellerinfo"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_INFO_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check seller info detail with url appLink then should return webview`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/webview?url=https://www.tokopedia.com/help"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_INFO_DETAIL + "?url=https://www.tokopedia.com/help", expectedDeepLink)
    }

    @Test
    fun `check shipping editor appLink then should return tokopedia internal seller shipping editor in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SELLER_SHIPPING_EDITOR, expectedDeepLink)
    }

    @Test
    fun `check statistic appLink then should return tokopedia internal statistic in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/statistic_dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.STATISTIC_DASHBOARD, expectedDeepLink)
    }

    @Test
    fun `check seller history appLink then should return seller home seller history in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-som-allorder"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check seller history appLink then should return seller home seller history in sellerapp with search param`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-som-allorder?search=product"
        assertEqualsDeepLinkMapper("${ApplinkConst.SELLER_HISTORY}?search=product", expectedDeepLink)
    }

    @Test
    fun `check seller history internal appLink then should return seller home seller history in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-som-allorder"
        assertEqualsDeepLinkMapper(ApplinkConstInternalOrder.HISTORY, expectedDeepLink)
    }

    @Test
    fun `check seller history internal appLink then should return seller home seller history in sellerapp with search param`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-som-allorder?search=product"
        assertEqualsDeepLinkMapper("${ApplinkConstInternalOrder.HISTORY}?search=product", expectedDeepLink)
    }

    @Test
    fun `check review reminder then should return tokopedia internal review reminder`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/review-reminder"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.REVIEW_REMINDER, expectedDeepLink)
    }

    @Test
    fun `check review reminder previous then should return tokopedia internal review reminder`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/review-reminder"
        assertEqualsDeepLinkMapper(ApplinkConst.REVIEW_REMINDER_PREVIOUS, expectedDeepLink)
    }

    @Test
    fun `check reputation applink with tab param then should return internal review in sellerapp`() {
        val tabInboxReview = "inbox-review"
        val tabParam = "tab"

        val appLink = Uri.parse(ApplinkConst.REPUTATION)
            .buildUpon()
            .appendQueryParameter(tabParam, tabInboxReview)
            .build()
            .toString()

        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/review?$tabParam=$tabInboxReview"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, tabParam to tabInboxReview)
    }

    @Test
    fun `check talk applink with filter param then should return internal talk in sellerapp`() {
        val filterUnread = "unread"
        val filterParam = "filter"

        val appLink = Uri.parse(ApplinkConst.TALK)
            .buildUpon()
            .appendQueryParameter(filterParam, filterUnread)
            .build()
            .toString()

        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/inbox-talk?$filterParam=$filterUnread"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, filterParam to filterUnread)
    }

    @Test
    fun `check shop setting appLink then should return tokopedia internal shop setting in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/menu-setting"
        val appLink = UriUtil.buildUri(ApplinkConst.SellerApp.SHOP_SETTINGS_SELLER_APP, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop score detail appLink then should return tokopedia internal shop score detail in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop/performance"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SHOP_SCORE_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check shop score detail appLink with coachmark param then should return tokopedia internal shop score performance with coachmark param in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop/performance?coachmark=disabled"
        val coachMarkParam = mapOf("coachmark" to "disabled")
        val actualDeeplink = UriUtil.buildUriAppendParam(ApplinkConst.SellerApp.SHOP_SCORE_DETAIL, coachMarkParam)
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check shop admin invitation appLink then should return tokopedia internal shop admin invitation in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/invitation-page"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.ADMIN_INVITATION, expectedDeepLink)
    }

    @Test
    fun `check shop admin redirection appLink then should return tokopedia internal shop admin redirection in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/redirection-page"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.ADMIN_REDIRECTION, expectedDeepLink)
    }

    @Test
    fun `check shop admin accepted appLink then should return tokopedia internal shop admin accepted in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/accepted-page"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.ADMIN_ACCEPTED, expectedDeepLink)
    }

    @Test
    fun `check shop admin accepted appLink with shop name param then should return tokopedia internal shop admin accepted with shop name param in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/accepted-page?shop_name=toko"
        val shopNameParam = mapOf("shop_name" to "toko")
        val actualDeeplink = UriUtil.buildUriAppendParam(ApplinkConst.SellerApp.ADMIN_ACCEPTED, shopNameParam)
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check sellerapp webview deeplink`() {
        val queryParam = "?titlebar=false&allow_override=false&need_login=false&title=abc&pull_to_refresh=false&url=https://www.tokopedia.com/help"
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/webview" + queryParam
        val appLink = ApplinkConst.SellerApp.WEBVIEW + queryParam
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check sellerapp browser deeplink`() {
        val queryParam = "?ext=true&titlebar=false&allow_override=false&need_login=false&title=abc&pull_to_refresh=false&url=https://www.tokopedia.com/help"
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/browser" + queryParam
        val appLink = ApplinkConst.SellerApp.BROWSER + queryParam
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check power merchant pro interrupt sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome?state="
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.POWER_MERCHANT_PRO_INTERRUPT, expectedDeepLink)
    }

    @Test
    fun `check NOTIFICATION sellerapp`() {
        val expectedDeepLink = "tokopedia://sellerinfo"
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.NOTIFICATION, expectedDeepLink)
    }

    @Test
    fun `check topchat settings bubble activation sellerapp applink`() {
        val deepLink = ApplinkConst.SellerApp.TOPCHAT_BUBBLE_ACTIVATION
        val expectedDeepLink = ApplinkConstInternalMarketplace.TOPCHAT_BUBBLE_ACTIVATION
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, deepLink, expectedDeepLink)
    }

    @Test
    fun `check seller shop nib app link should mapping to sellerapp internal app link`() {
        val appLink = ApplinkConst.SellerApp.SELLER_SHOP_NIB
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_SHOP_NIB
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check seller mvc redirection page, should return internal shop mvc redirection page`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_REDIRECTION_PAGE
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.SellerApp.SELLER_MVC_REDIRECTION_PAGE, expectedDeepLink)
    }

    @Test
    fun `check seller mvc intro, should return internal shop mvc intro`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_INTRO
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.SellerApp.SELLER_MVC_INTRO, expectedDeepLink)
    }

    @Test
    fun `check seller mvc list, should return internal shop mvc list`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_LIST
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.SellerApp.SELLER_MVC_LIST, expectedDeepLink)
    }

    @Test
    fun `check seller mvc list active, should return internal shop mvc list active`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.SellerApp.SELLER_MVC_LIST_ACTIVE, expectedDeepLink)
    }

    @Test
    fun `check seller mvc list history, should return internal shop mvc list history`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_LIST_HISTORY
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.SellerApp.SELLER_MVC_LIST_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check seller mvc list upcoming, should return internal shop mvc list upcoming`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_LIST_UPCOMING
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.SellerApp.SELLER_MVC_LIST_UPCOMING, expectedDeepLink)
    }

    @Test
    fun `check seller mvc list ongoing, should return internal shop mvc list ongoing`() {
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ONGOING
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.SellerApp.SELLER_MVC_LIST_ONGOING, expectedDeepLink)
    }

    @Test
    fun `check seller mvc create with voucher type param, should return internal shop mvc create with voucher type param`() {
        val mockVoucherType = "shop"
        val appLink = UriUtil.buildUri(
            ApplinkConst.SellerApp.SELLER_MVC_CREATE,
            mockVoucherType
        )
        val expectedDeepLink = UriUtil.buildUri(
            ApplinkConstInternalSellerapp.SELLER_MVC_CREATE,
            mockVoucherType
        )
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check seller mvc detail with voucher id param, should return internal seller mvc detail with voucher id param`() {
        val mockVoucherId = "123"
        val appLink = UriUtil.buildUri(
            ApplinkConst.SellerApp.SELLER_MVC_DETAIL,
            mockVoucherId
        )
        val expectedDeepLink = UriUtil.buildUri(
            ApplinkConstInternalSellerapp.SELLER_MVC_DETAIL,
            mockVoucherId
        )
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check persona external app link should mapping to internal app link`() {
        val appLink = ApplinkConst.SellerApp.SELLER_PERSONA
        val expectedDeepLink = ApplinkConstInternalSellerapp.SELLER_PERSONA
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check topads dashboard applink should return topads internal applink`() {
        val expectedDeepLink = ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
        val actualDeeplink = ApplinkConst.SellerApp.TOPADS_DASH_BOARD
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }
}
