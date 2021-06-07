package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.*
import org.junit.Test

class DeepLinkDFMapperCustomerAppTest: DeepLinkDFMapperTestFixture() {

    @Test
    fun `check onboarding appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/onboarding"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check age restriction appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalCategory.INTERNAL_CATEGORY}/age_restriction"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check tradein appLink then should return DF_CATEGORY_TRADE_IN in customerapp`() {
        val appLink = "${ApplinkConstInternalCategory.INTERNAL_CATEGORY}/tradein"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CATEGORY_TRADE_IN)
    }

    @Test
    fun `check internal belanja category appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://category_belanja"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal hotlist revamp applink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://hotlist"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check final price appLink then should return DF_CATEGORY_TRADE_IN in customerapp`() {
        val appLink = "${ApplinkConstInternalCategory.INTERNAL_CATEGORY}/host_final_price"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CATEGORY_TRADE_IN)
    }

    @Test
    fun `check money in internal appLink then should return DF_CATEGORY_TRADE_IN in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://money_in/device_validation"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CATEGORY_TRADE_IN)
    }

    @Test
    fun `check internal explore category appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://category-explore"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal catalog appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://catalog"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal find appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://find"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal category appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://category"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check profile appLink then should return DF_CONTENT_PROFILE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://people/12345"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_PROFILE)
    }

    @Test
    fun `check internal affiliate explorer appLink then should return DF_CONTENT_AFFILIATE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_AFFILIATE}/explore"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check internal affiliate dashboard appLink then should return DF_CONTENT_AFFILIATE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_AFFILIATE}/dashboard"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check internal affiliate education appLink then should return DF_CONTENT_AFFILIATE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_AFFILIATE}/education"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check internal affiliate by me tracking appLink then should return DF_CONTENT_AFFILIATE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_AFFILIATE}/tracking/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check play detail appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://play/12345"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check comment appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/comment/6789"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal content post detail appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/post-detail/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check kol youtube appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://kolyoutube/https://www.youtube.com/watch?v=WMh0KzwTviY"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check content report appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/content-report/12345"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check video detail appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/video-detail/{id}"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check media preview appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/media-preview/12345"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check interest pick appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://interestpick"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal affiliate create post appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_AFFILIATE}/create_post/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check internal affiliate draft post appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_AFFILIATE}/draft/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check affiliate edit appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/affiliate/12345/edit"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check internal content create post appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/create_post/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check internal content draft post appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/draft/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check shop post edit appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/content-shop/12345/edit"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_CONTENT_AFFILIATE)
    }

    @Test
    fun `check digital sub homepage home appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://recharge/home"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check telco postpaid digital appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_DIGITAL}/telcopost"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check telco prepaid digital appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_DIGITAL}/telcopre"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check digital product form appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://digital/form"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check general template appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_DIGITAL}/general"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check camera ocr appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_RECHARGE}/ocr"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check voucher game appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_DIGITAL}/vouchergame"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check cart digital appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_DIGITAL}/cart"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check digital cart external appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/cart"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal smartcard emoney appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_DIGITAL}/smartcard/emoney?calling_page_check_saldo=calling_from_nfc"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal smartcard brizzy appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalDigital.INTERNAL_DIGITAL}/smartcard/brizzi?calling_page_check_saldo=calling_from_nfc"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check global internal digital deal slug base appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/deals-slug/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check global internal digital deal brand detail base appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/deals-brand/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check similiar search result base appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY}/similar-search-result"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check search result appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY}/search-result"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check autocomplete appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY}/autocomplete"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check home wishlist appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalHome.INTERNAL_HOME}/wishlist"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check default home recommendation appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConsInternalHome.INTERNAL_HOME}/rekomendasi"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check home recent view appLink then should return DF_MERCHANT_LOGIN in customerapp`() {
        val appLink = "${ApplinkConsInternalHome.INTERNAL_HOME}/recentlyviewed"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check ovo pay with qr entry appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/ovo-pay-with-qr"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check oqr pin url entry appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/ovoqrthanks/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check ovo wallet appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://ovo"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check saldo deposit appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/saldo"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check saldo intro appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/saldo-intro"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check ovo p2 transfer form short appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://ovop2ptransfer"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check referral appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://referral"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check drop off picker appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalLogistic.INTERNAL_LOGISTIC}/dropoff/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shipping confirmation appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalLogistic.INTERNAL_LOGISTIC}/shipping-confirmation/{mode}"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check order tracking appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shipping/tracking/12345"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check manage address appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalLogistic.INTERNAL_LOGISTIC}/manageaddress/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add address v1 appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalLogistic.INTERNAL_LOGISTIC}/addaddress/v1/12345/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add address v2 appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalLogistic.INTERNAL_LOGISTIC}/addaddress/v2/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check district recommendation shop settings then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/district-recommendation-shop-settings"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check geolocation appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/geolocation"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check open shop appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/shop-open"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check favorite appLink then should return DF_MERCHANT_LOGIN in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://home/favorite"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check report product appLink then should return DF_MERCHANT_LOGIN in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/product-report/12345/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check internal seller appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check product manage appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://seller/product/manage"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check product manage list appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/product-manage-list"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check seller home product manage list appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP}/sellerhome-product-list"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check power merchant subscribe appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://power_merchant/subscribe"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check power merchant subscribe internal appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/power-merchant-subscribe"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check shop setting base appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/shop-settings"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check topads dashboard customer appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://topads/dashboard"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check topads dashboard internal appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${ApplinkConstInternalTopAds.INTERNAL_TOPADS}/dashboard"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check seller transaction appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://seller"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check merchant shop showcase list appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/shop-showcase-list"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist internal appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/official-store/brand/123456/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist search appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/official-store/brand-search"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist external appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://official-store/brand"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist with slash appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://official-store/brand/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check merchant open product review appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/open-product-preview"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check product add appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://product/add"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check shop page base appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/shop-page"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop etalase appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/etalase/56789"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop etalase with keyword and sort appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/etalase/6789/?search=baju&sort=1"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop review appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/review"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop note appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/note"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop info appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/info"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop home appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/home"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop settings note appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://setting/shop/note"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check contact us native appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://contactus"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check contact us appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://contact-us"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check ticket detail appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://customercare/12345"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check internal inbox list appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://customercare/inbox-list"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check chat bot appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/chatbot"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check payment setting appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalPayment.INTERNAL_PAYMENT}/setting"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check install debit bca entry pattern appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/instantdebitbca"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check edit bca one click entry pattern appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/editbcaoneklik"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal toko points appLink then should return DF_PROMO_TOKOPOINTS in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_PROMO_TOKOPOINTS)
    }

    @Test
    fun `check internal gamification crack appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = "${ApplinkConstInternalPromo.INTERNAL_GLOBAL}/gamification"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification tap tap mantap appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = "${ApplinkConstInternalPromo.INTERNAL_GLOBAL}/gamification2"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification smc referal appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = "${ApplinkConstInternalPromo.INTERNAL_GLOBAL}/smc-referral"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification daily gift appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = "${ApplinkConstInternalPromo.INTERNAL_GLOBAL}/gamification_gift_daily"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification tap tap gift appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = "${ApplinkConstInternalPromo.INTERNAL_GLOBAL}/gamification_gift_60s"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check event home appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = "${ApplinkConstInternalEntertainment.INTERNAL_EVENT}/home"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check event pdp appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = "${ApplinkConstInternalEntertainment.INTERNAL_EVENT}/detail"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check deals homepage appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = "${ApplinkConstInternalEntertainment.INTERNAL_EVENT}/home-new"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check deals brand page appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = "${ApplinkConstInternalDeals.INTERNAL_DEALS}/brand-new/page"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check deals category page appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://deals/category-new/page"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check salam umrah home page appLink then should return DF_SALAM_UMRAH in customerapp`() {
        val appLink = "${ApplinkConstInternalSalam.INTERNAL_SALAM}/umroh"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_SALAM_UMRAH)
    }

    @Test
    fun `check salam order detail appLink then should return DF_SALAM_UMRAH in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order-details/umroh"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_SALAM_UMRAH)
    }

    @Test
    fun `check travel sub homepage appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://travelentertainment"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check flight external appLink then should return DF_TRAVEL in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://pesawat"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_TRAVEL)
    }

    @Test
    fun `check flight internal appLink then should return DF_TRAVEL in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_INTERNAL}://pesawat"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_TRAVEL)
    }

    @Test
    fun `check hotel external appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://hotel"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check promo campaign shake landing prefix appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalPromo.INTERNAL_PROMO}/campaign-shake-landing"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check setting profile appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/setting-profile"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add phone appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-phone"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add email appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-email"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add bod appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-bod"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change name appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/change-name?oldName=Bobo&chances=Baba"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change gender appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/change-gender"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add name register appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-name-register"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change pin appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/change-pin"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add pin onboarding appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-pin-onboarding"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add pin appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-pin"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add pin complete appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-pin-complete"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check profile completion appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/profile-completion"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change phone number appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/change-phone-number"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check change password appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/change-password"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check setting bank appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/setting-bank"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check user notification setting appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/user-notification-setting"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check user identification form appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/user-identification-form?projectId=123456"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check attach invoice appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/user-attach-invoice"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check attach voucher appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/user-attach-voucher"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check order history appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://product-order-history"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check topchat idless appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://topchat"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check topchat appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/topchat"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check inbox talk appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/inbox-talk"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop talk appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/talk"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check product talk appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://product/12345/talk"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check detail talk base appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/detail-talk/"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add talk appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-talk"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add fingerprint onboarding appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://add-fingerprint-onboarding"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check liveness detection appLink then should return DF_USER_LIVENESS in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/liveness-detection"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_USER_LIVENESS)
    }

    @Test
    fun `check notification appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalNotification.INTERNAL_MARKETPLACE}/notification"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check notification buyer appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalNotification.INTERNAL_MARKETPLACE}/notif-center"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check push notification troubleshooter appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/push-notification-troubleshooter"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check otp appLink then should return DF_BASE in customerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://otp"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check checkout appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/checkout"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check checkout address selection appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/checkout-address-selection"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check one click checkout appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/one-click-checkout"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check promo checkout marketplace appLink then should return DF_BASE in customerapp`() {
        val appLink = "${ApplinkConstInternalPromo.INTERNAL_PROMO}/checkout-marketplace"
        assertEqualDeepLinkCustomerApp(appLink, DeeplinkDFMapper.DF_BASE)
    }
}