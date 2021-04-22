package tokopedia.applink.deeplink

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.config.GlobalConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkMapperSellerAppTest: DeepLinkMapperTestFixture() {

    override fun setup() {
        super.setup()
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
    }

    override fun finish() {
        super.finish()
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    @Test
    fun `check home appLink then should return tokopedia internal seller home in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome"
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
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE, expectedDeepLink)
    }

    @Test
    fun `check browser appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.BROWSER, "")
    }

    @Test
    fun `check webview appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.WEBVIEW, "")
    }

    @Test
    fun `check voucher list appLink then should return tokopedia internal voucher list in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-list"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_LIST, expectedDeepLink)
    }

    @Test
    fun `check voucher active appLink then should return tokopedia internal voucher active in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-list/active"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_ACTIVE, expectedDeepLink)
    }

    @Test
    fun `check history voucher appLink then should return tokopedia internal voucher list history in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-list/history"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check voucher detail appLink then should return tokopedia internal voucher detail in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-detail"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check create voucher appLink then should return tokopedia internal create voucher in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/create-voucher"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.CREATE_VOUCHER, expectedDeepLink)
    }

    @Test
    fun `check seller search appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SELLER_SEARCH, "")
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
    fun `check shop feed appLink then should return tokopedia internal feed in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/feed"
        val appLink = UriUtil.buildUri(ApplinkConst.SellerApp.SHOP_FEED,  "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check content create post appLink then should return tokopedia internal content create post in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://content/create_post/"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.CONTENT_CREATE_POST, expectedDeepLink)
    }

    @Test
    fun `check seller info detail appLink then should return tokopedia internal seller info detail in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://sellerinfo"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_INFO_DETAIL, expectedDeepLink)
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
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-som-allorder?search=product&search=product"
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
    fun `check reputation applink with tab param then should return internal review in sellerapp`() {
        val tabInboxReview = "inbox-review"
        val tabParam = "tab"

        val appLink = Uri.parse(ApplinkConst.REPUTATION)
                .buildUpon()
                .appendQueryParameter(tabParam, tabInboxReview)
                .build()
                .toString()

        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/review?${tabParam}=${tabInboxReview}&${tabParam}=${tabInboxReview}"

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

        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/inbox-talk?${filterParam}=${filterUnread}&${filterParam}=${filterUnread}"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, filterParam to filterUnread)
    }

    @Test
    fun `check shop setting appLink then should return tokopedia internal shop setting in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/menu-setting"
        val appLink = UriUtil.buildUri(ApplinkConst.SellerApp.SHOP_SETTINGS_SELLER_APP, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }
}