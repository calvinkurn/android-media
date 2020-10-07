package tokopedia.applink.deeplink

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
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

    @Test
    fun `check home appLink then should return tokopedia internal seller home in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME, actualDeepLink)
    }

    @Test
    fun `check product add then should return tokopedia internal open product preview in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/open-product-preview"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.PRODUCT_ADD, actualDeepLink)
    }

    @Test
    fun `check top ads credit appLink then should return tokopedia internal top ads credit in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/buy"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREDIT, actualDeepLink)
    }

    @Test
    fun `check top ads auto topup appLink then should return tokopedia internal top ads topup in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/auto-topup"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP, actualDeepLink)
    }

    @Test
    fun `check top ads product create appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE, "")
    }

    @Test
    fun `check top ads create ads then appLink should return tokopedia internal top ads create ads in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/create-ads"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_ADS, actualDeepLink)
    }

    @Test
    fun `check top ads creation onboard appLink then should return tokopedia internal top ads creation onboard in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/creation-onboard"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_ONBOARDING, actualDeepLink)
    }

    @Test
    fun `check top ads create auto ads appLink then should return tokopedia internal top ads create auto ads in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/create-autoads"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_AUTO_ADS, actualDeepLink)
    }

    @Test
    fun `check top ads edit auto ads appLink then should return tokopedia internal top ads edit auto ads in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/edit-autoads"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_EDIT_AUTO_ADS, actualDeepLink)
    }

    @Test
    fun `check top ads history credit appLink then should return tokopedia internal top ads history credit in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/history-credit"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREDIT_HISTORY, actualDeepLink)
    }

    @Test
    fun `check top ads picker appLink then should return tokopedia internal top ads picker in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/ad-picker"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_CREATE_CHOOSER, actualDeepLink)
    }

    @Test
    fun `check gold merchant appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.GOLD_MERCHANT, "")
    }

    @Test
    fun `check seller app home appLink then should return tokopedia internal home in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SELLER_APP_HOME, actualDeepLink)
    }

    @Test
    fun `check topads dashboard appLink then should return tokopedia internal topads dashboard in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_DASHBOARD, actualDeepLink)
    }

    @Test
    fun `check power merchant subscribe appLink then should return tokopedia internal power merchant subscribe in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/power-merchant-subscribe"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE, actualDeepLink)
    }

    @Test
    fun `check browser appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.BROWSER, "")
    }

    @Test
    fun `check topads auto ads appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.TOPADS_AUTOADS, "")
    }

    @Test
    fun `check webview appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.WEBVIEW, "")
    }

    @Test
    fun `check voucher list appLink then should return tokopedia internal voucher list in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-list"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_LIST, actualDeepLink)
    }

    @Test
    fun `check voucher active appLink then should return tokopedia internal voucher active in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-list/active"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_ACTIVE, actualDeepLink)
    }

    @Test
    fun `check history voucher appLink then should return tokopedia internal voucher list history in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-list/history"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_HISTORY, actualDeepLink)
    }

    @Test
    fun `check voucher detail appLink then should return tokopedia internal voucher detail in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-detail"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.VOUCHER_DETAIL, actualDeepLink)
    }

    @Test
    fun `check create voucher appLink then should return tokopedia internal create voucher in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/create-voucher"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.CREATE_VOUCHER, actualDeepLink)
    }

    @Test
    fun `check seller search appLink then should return empty in sellerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.SELLER_SEARCH, "")
    }

    @Test
    fun `check play broadcaster appLink then should return tokopedia internal play broadcaster in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://play-broadcaster"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.PLAY_BROADCASTER, actualDeepLink)
    }

    @Test
    fun `check centralized promo appLink then should return tokopedia internal centralized promo in sellerapp`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/centralized-promo"
        assertEqualsDeepLinkMapper(ApplinkConst.SellerApp.CENTRALIZED_PROMO, actualDeepLink)
    }

}