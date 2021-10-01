package tokopedia.applink.deeplinkmatcher

import com.tokopedia.applink.DeepLinkChecker.BLOG
import com.tokopedia.applink.DeepLinkChecker.BROWSE
import com.tokopedia.applink.DeepLinkChecker.CATALOG
import com.tokopedia.applink.DeepLinkChecker.CATEGORY
import com.tokopedia.applink.DeepLinkChecker.CONTENT
import com.tokopedia.applink.DeepLinkChecker.DEALS
import com.tokopedia.applink.DeepLinkChecker.DISCOVERY_PAGE
import com.tokopedia.applink.DeepLinkChecker.ETALASE
import com.tokopedia.applink.DeepLinkChecker.FIND
import com.tokopedia.applink.DeepLinkChecker.FLIGHT
import com.tokopedia.applink.DeepLinkChecker.HOT
import com.tokopedia.applink.DeepLinkChecker.HOTEL
import com.tokopedia.applink.DeepLinkChecker.HOT_LIST
import com.tokopedia.applink.DeepLinkChecker.INVOICE
import com.tokopedia.applink.DeepLinkChecker.LOGIN_BY_QR
import com.tokopedia.applink.DeepLinkChecker.ORDER_LIST
import com.tokopedia.applink.DeepLinkChecker.OTHER
import com.tokopedia.applink.DeepLinkChecker.PLAY
import com.tokopedia.applink.DeepLinkChecker.PRODUCT
import com.tokopedia.applink.DeepLinkChecker.PRODUCT_REVIEW
import com.tokopedia.applink.DeepLinkChecker.PROFILE
import com.tokopedia.applink.DeepLinkChecker.PROMO_DETAIL
import com.tokopedia.applink.DeepLinkChecker.PROMO_LIST
import com.tokopedia.applink.DeepLinkChecker.RECHARGE
import com.tokopedia.applink.DeepLinkChecker.RECOMMENDATION
import com.tokopedia.applink.DeepLinkChecker.REFERRAL
import com.tokopedia.applink.DeepLinkChecker.SALE
import com.tokopedia.applink.DeepLinkChecker.SHOP
import com.tokopedia.applink.DeepLinkChecker.SMCREFERRAL
import com.tokopedia.applink.DeepLinkChecker.TOKOPOINT
import com.tokopedia.applink.DeepLinkChecker.TRAVEL_HOMEPAGE
import com.tokopedia.applink.DeepLinkChecker.WALLET_OVO
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tokopedia.applink.util.DeepLinkUrlConstant.ABOUT_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.ACTIVATION_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.AKTIVASI_POWER_MERCHANT_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.BLOG_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.CAMPAIGN_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.CATALOG_URL_INK
import tokopedia.applink.util.DeepLinkUrlConstant.CONTACT_US_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.CONTENT_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.CREDIT_MOTOR_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.DEALS_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.DISCOVERY_B_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.DISCOVERY_PAGE_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.ETALASE_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.EVENTS_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.FIND_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.FLIGHT_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.GOLD_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.HELP_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.HOTEL_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.HOT_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.HOT_LIST_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.INVOICE_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.KUPON_THR_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.LOGIN_BY_QR_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.MERCHANT_KYC_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.MODAL_TOKO_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.MYSHOP_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.MY_SHOP_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.NEW_CATEGORY_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.OLD_CATEGORY_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.ORDER_LIST_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.OVO_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.PEOPLE_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.PLAY_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.PRODUCT_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.PRODUCT_REVIEW_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.PROMO_DETAIL_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.PROMO_LIST_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.PULSA_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.RECOMMENDATION_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.REFERALL_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.REKSA_DANA_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.RESET_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.SALE_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.SEARCH_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.SERU_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.SHOP_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.TOKO_POINTS_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.TRAVEL_ENTERTAINMENT_LINK_URL
import tokopedia.applink.util.DeepLinkUrlConstant.TRAVEL_ENTERTAINTMENT_DETAIL_LINK_URL

@RunWith(RobolectricTestRunner::class)
class DeepLinkMatcherTest: DeepLinkMatcherTestFixture() {

    @Test
    fun `check link url of play then should return PLAY`() {
        assertEqualsDeepLinkMatcher(PLAY, PLAY_LINK_URL)
    }

    @Test
    fun `check link url of flight then should return FLIGHT`() {
        assertEqualsDeepLinkMatcher(FLIGHT, FLIGHT_LINK_URL)
    }

    @Test
    fun `check link url of promo detail then should return PROMO_DETAIL`() {
        assertEqualsDeepLinkMatcher(PROMO_DETAIL, PROMO_DETAIL_LINK_URL)
    }

    @Test
    fun `check link url of promo list then should return PROMO_LIST`() {
        assertEqualsDeepLinkMatcher(PROMO_LIST, PROMO_LIST_LINK_URL)
    }

    @Test
    fun `check link url of sale then should return SALE`() {
        assertEqualsDeepLinkMatcher(SALE, SALE_LINK_URL)
    }

    @Test
    fun `check link url of invoice then should return INVOICE`() {
        assertEqualsDeepLinkMatcher(INVOICE, INVOICE_LINK_URL)
    }

    @Test
    fun `check link url of blog then should return BLOG`() {
        assertEqualsDeepLinkMatcher(BLOG, BLOG_LINK_URL)
    }

    @Test
    fun `check link url of new category then should return CATEGORY`() {
        assertEqualsDeepLinkMatcher(CATEGORY, NEW_CATEGORY_LINK_URL)
    }

    @Test
    fun `check link url of old category then should return CATEGORY`() {
        assertEqualsDeepLinkMatcher(CATEGORY, OLD_CATEGORY_LINK_URL)
    }

    @Test
    fun `check link url of search then should return BROWSE`() {
        assertEqualsDeepLinkMatcher(BROWSE, SEARCH_LINK_URL)
    }

    @Test
    fun `check link url of hot list then should return HOT_LIST`() {
        assertEqualsDeepLinkMatcher(HOT_LIST, HOT_LIST_LINK_URL)
    }

    @Test
    fun `check link url of hot then should return BROWSE`() {
        assertEqualsDeepLinkMatcher(HOT, HOT_LINK_URL)
    }

    @Test
    fun `check link url of find then should return FIND`() {
        assertEqualsDeepLinkMatcher(FIND, FIND_LINK_URL)
    }

    @Test
    fun `check link url of catalog then should return CATALOG`() {
        assertEqualsDeepLinkMatcher(CATALOG, CATALOG_URL_INK)
    }

    @Test
    fun `check link url of b page then should return DISCOVERY_PAGE`() {
        assertEqualsDeepLinkMatcher(DISCOVERY_PAGE, DISCOVERY_B_LINK_URL)
    }

    @Test
    fun `check link url of discovery page then should return DISCOVERY_PAGE`() {
        assertEqualsDeepLinkMatcher(DISCOVERY_PAGE, DISCOVERY_PAGE_LINK_URL)
    }

    @Test
    fun `check link url of pulsa then should return RECHARGE`() {
        assertEqualsDeepLinkMatcher(RECHARGE, PULSA_LINK_URL)
    }

    @Test
    fun `check link url of etalase then should return CATALOG`() {
        assertEqualsDeepLinkMatcher(ETALASE, ETALASE_LINK_URL)
    }

    @Test
    fun `check link url of referall then should return REFERRAL`() {
        assertEqualsDeepLinkMatcher(REFERRAL, REFERALL_LINK_URL)
    }

    @Test
    fun `check link url of tokopoints then should return TOKOPOINT`() {
        assertEqualsDeepLinkMatcher(TOKOPOINT, TOKO_POINTS_LINK_URL)
    }

    @Test
    fun `check link url of ovo then should return WALLET_OVO`() {
        assertEqualsDeepLinkMatcher(WALLET_OVO, OVO_LINK_URL)
    }

    @Test
    fun `check link url of people then should return PROFILE`() {
        assertEqualsDeepLinkMatcher(PROFILE, PEOPLE_LINK_URL)
    }

    @Test
    fun `check link url of content then should return CONTENT`() {
        assertEqualsDeepLinkMatcher(CONTENT, CONTENT_LINK_URL)
    }

    @Test
    fun `check link url of kupon thr then should return SMCREFERRAL`() {
        assertEqualsDeepLinkMatcher(SMCREFERRAL, KUPON_THR_LINK_URL)
    }

    @Test
    fun `check link url of seru then should return SMCREFERRAL`() {
        assertEqualsDeepLinkMatcher(SMCREFERRAL, SERU_LINK_URL)
    }

    @Test
    fun `check link url of gold then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, GOLD_LINK_URL)
    }

    @Test
    fun `check link url of reksa dana then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, REKSA_DANA_LINK_URL)
    }

    @Test
    fun `check link url of help then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, HELP_LINK_URL)
    }

    @Test
    fun `check link url of events then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, EVENTS_LINK_URL)
    }

    @Test
    fun `check link url of merchant kyc then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MERCHANT_KYC_LINK_URL)
    }

    @Test
    fun `check link url of contact us then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, CONTACT_US_LINK_URL)
    }

    @Test
    fun `check link url of about then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, ABOUT_LINK_URL)
    }

    @Test
    fun `check link url of reset then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, RESET_LINK)
    }

    @Test
    fun `check link url of order list then should return ORDER_LIST`() {
        assertEqualsDeepLinkMatcher(ORDER_LIST, ORDER_LIST_LINK_URL)
    }

    @Test
    fun `check link url of activation then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, ACTIVATION_LINK_URL)
    }

    @Test
    fun `check link url of credit motor then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, CREDIT_MOTOR_LINK_URL)
    }

    @Test
    fun `check link url of modal toko then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MODAL_TOKO_LINK_URL)
    }

    @Test
    fun `check link url of hotel then should return HOTEL`() {
        assertEqualsDeepLinkMatcher(HOTEL, HOTEL_LINK_URL)
    }

    @Test
    fun `check link url of travel entertainment then should return TRAVEL_HOMEPAGE`() {
        assertEqualsDeepLinkMatcher(TRAVEL_HOMEPAGE, TRAVEL_ENTERTAINMENT_LINK_URL)
    }

    @Test
    fun `check link url of travel entertainment detail then should return TRAVEL_HOMEPAGE`() {
        assertEqualsDeepLinkMatcher(TRAVEL_HOMEPAGE, TRAVEL_ENTERTAINTMENT_DETAIL_LINK_URL)
    }

    @Test
    fun `check link url of recommendation then should return RECOMMENDATION`() {
        assertEqualsDeepLinkMatcher(RECOMMENDATION, RECOMMENDATION_LINK_URL)
    }

    @Test
    fun `check link url of product review then should return PRODUCT_REVIEW`() {
        assertEqualsDeepLinkMatcher(PRODUCT_REVIEW,  PRODUCT_REVIEW_LINK_URL)
    }

    @Test
    fun `check link url of MYSHOP then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MYSHOP_LINK_URL)
    }

    @Test
    fun `check link url of MY-SHOP then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MY_SHOP_LINK_URL)
    }

    @Test
    fun `check link url of deals then should return DEALS`() {
        assertEqualsDeepLinkMatcher(DEALS, DEALS_LINK_URL)
    }

    @Test
    fun `check link url of activation power merchant then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, AKTIVASI_POWER_MERCHANT_LINK_URL)
    }

    @Test
    fun `check link url of shop then should return SHOP`() {
        assertEqualsDeepLinkMatcher(SHOP, SHOP_LINK_URL)
    }

    @Test
    fun `check link url of product then should return PRODUCT`() {
        assertEqualsDeepLinkMatcher(PRODUCT, PRODUCT_LINK_URL)
    }

    @Test
    fun `check link url of campaign then should return ETALASE`() {
        assertEqualsDeepLinkMatcher(ETALASE, CAMPAIGN_LINK_URL)
    }

    @Test
    fun `check link url of login by QR then should return LOGIN_BY_QR`() {
        assertEqualsDeepLinkMatcher(LOGIN_BY_QR, LOGIN_BY_QR_LINK_URL)
    }
}