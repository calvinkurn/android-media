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
import com.tokopedia.applink.DeepLinkChecker.GROUPCHAT
import com.tokopedia.applink.DeepLinkChecker.HOT
import com.tokopedia.applink.DeepLinkChecker.HOTEL
import com.tokopedia.applink.DeepLinkChecker.HOT_LIST
import com.tokopedia.applink.DeepLinkChecker.INVOICE
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
import tokopedia.applink.util.DeepLinkUrlConstant.ABOUT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.ACTIVATION_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.AKTIVASI_POWER_MERCHANT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.BLOG_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.CAMPAIGN_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.CATALOG_URL_INK
import tokopedia.applink.util.DeepLinkUrlConstant.CONTACT_US_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.CONTENT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.CREDIT_MOTOR_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.DEALS_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.DISCOVERY_B_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.DISCOVERY_PAGE_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.ETALASE_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.EVENTS_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.FIND_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.FLIGHT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.GOLD_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.GROUP_CHAT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.HELP_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.HOTEL_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.HOT_LIST_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.HOT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.INVOICE_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.KUPON_THR_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.MERCHANT_KYC_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.MODAL_TOKO_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.MYSHOP_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.MY_SHOP_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.NEW_CATEGORY_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.OLD_CATEGORY_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.ORDER_LIST_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.OVO_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.PEOPLE_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.PLAY_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.PRODUCT_REVIEW_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.PRODUCT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.PROMO_DETAIL_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.PROMO_LIST_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.PULSA_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.RECOMMENDATION_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.REFERALL_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.REKSA_DANA_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.RESET_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.SALE_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.SEARCH_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.SERU_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.SHOP_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.TOKO_POINTS_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.TRAVEL_ENTERTAINMENT_URL_LINK
import tokopedia.applink.util.DeepLinkUrlConstant.TRAVEL_ENTERTAINTMENT_DETAIL_URL_LINK

@RunWith(RobolectricTestRunner::class)
class DeepLinkMatcherTest: DeepLinkMatcherTestFixture() {

    @Test
    fun `check url link of play then should return PLAY`() {
        assertEqualsDeepLinkMatcher(PLAY, PLAY_URL_LINK)
    }

    @Test
    fun `check url link of group chat then should return GROUPCHAT`() {
        assertEqualsDeepLinkMatcher(GROUPCHAT, GROUP_CHAT_URL_LINK)
    }

    @Test
    fun `check url link of flight then should return FLIGHT`() {
        assertEqualsDeepLinkMatcher(FLIGHT, FLIGHT_URL_LINK)
    }

    @Test
    fun `check url link of promo detail then should return PROMO_DETAIL`() {
        assertEqualsDeepLinkMatcher(PROMO_DETAIL, PROMO_DETAIL_URL_LINK)
    }

    @Test
    fun `check url link of promo list then should return PROMO_LIST`() {
        assertEqualsDeepLinkMatcher(PROMO_LIST, PROMO_LIST_URL_LINK)
    }

    @Test
    fun `check url link of sale then should return SALE`() {
        assertEqualsDeepLinkMatcher(SALE, SALE_URL_LINK)
    }

    @Test
    fun `check url link of invoice then should return INVOICE`() {
        assertEqualsDeepLinkMatcher(INVOICE, INVOICE_URL_LINK)
    }

    @Test
    fun `check url link of blog then should return BLOG`() {
        assertEqualsDeepLinkMatcher(BLOG, BLOG_URL_LINK)
    }

    @Test
    fun `check url link of new category then should return CATEGORY`() {
        assertEqualsDeepLinkMatcher(CATEGORY, NEW_CATEGORY_URL_LINK)
    }

    @Test
    fun `check url link of old category then should return CATEGORY`() {
        assertEqualsDeepLinkMatcher(CATEGORY, OLD_CATEGORY_URL_LINK)
    }

    @Test
    fun `check url link of search then should return BROWSE`() {
        assertEqualsDeepLinkMatcher(BROWSE, SEARCH_URL_LINK)
    }

    @Test
    fun `check url link of hot list then should return HOT_LIST`() {
        assertEqualsDeepLinkMatcher(HOT_LIST, HOT_LIST_URL_LINK)
    }

    @Test
    fun `check url link of hot then should return BROWSE`() {
        assertEqualsDeepLinkMatcher(HOT, HOT_URL_LINK)
    }

    @Test
    fun `check url link of find then should return FIND`() {
        assertEqualsDeepLinkMatcher(FIND, FIND_URL_LINK)
    }

    @Test
    fun `check url link of catalog then should return CATALOG`() {
        assertEqualsDeepLinkMatcher(CATALOG, CATALOG_URL_INK)
    }

    @Test
    fun `check url link of b page then should return DISCOVERY_PAGE`() {
        assertEqualsDeepLinkMatcher(DISCOVERY_PAGE, DISCOVERY_B_URL_LINK)
    }

    @Test
    fun `check url link of discovery page then should return DISCOVERY_PAGE`() {
        assertEqualsDeepLinkMatcher(DISCOVERY_PAGE, DISCOVERY_PAGE_URL_LINK)
    }

    @Test
    fun `check url link of pulsa then should return RECHARGE`() {
        assertEqualsDeepLinkMatcher(RECHARGE, PULSA_URL_LINK)
    }

    @Test
    fun `check url link of etalase then should return CATALOG`() {
        assertEqualsDeepLinkMatcher(ETALASE, ETALASE_URL_LINK)
    }

    @Test
    fun `check url link of referall then should return REFERRAL`() {
        assertEqualsDeepLinkMatcher(REFERRAL, REFERALL_URL_LINK)
    }

    @Test
    fun `check url link of tokopoints then should return TOKOPOINT`() {
        assertEqualsDeepLinkMatcher(TOKOPOINT, TOKO_POINTS_URL_LINK)
    }

    @Test
    fun `check url link of ovo then should return WALLET_OVO`() {
        assertEqualsDeepLinkMatcher(WALLET_OVO, OVO_URL_LINK)
    }

    @Test
    fun `check url link of people then should return PROFILE`() {
        assertEqualsDeepLinkMatcher(PROFILE, PEOPLE_URL_LINK)
    }

    @Test
    fun `check url link of content then should return CONTENT`() {
        assertEqualsDeepLinkMatcher(CONTENT, CONTENT_URL_LINK)
    }

    @Test
    fun `check url link of kupon thr then should return SMCREFERRAL`() {
        assertEqualsDeepLinkMatcher(SMCREFERRAL, KUPON_THR_URL_LINK)
    }

    @Test
    fun `check url link of seru then should return SMCREFERRAL`() {
        assertEqualsDeepLinkMatcher(SMCREFERRAL, SERU_URL_LINK)
    }

    @Test
    fun `check url link of gold then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, GOLD_URL_LINK)
    }

    @Test
    fun `check url link of reksa dana then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, REKSA_DANA_URL_LINK)
    }

    @Test
    fun `check url link of help then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, HELP_URL_LINK)
    }

    @Test
    fun `check url link of events then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, EVENTS_URL_LINK)
    }

    @Test
    fun `check url link of merchant kyc then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MERCHANT_KYC_URL_LINK)
    }

    @Test
    fun `check url link of contact us then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, CONTACT_US_URL_LINK)
    }

    @Test
    fun `check url link of about then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, ABOUT_URL_LINK)
    }

    @Test
    fun `check url link of reset then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, RESET_LINK)
    }

    @Test
    fun `check url link of order list then should return ORDER_LIST`() {
        assertEqualsDeepLinkMatcher(ORDER_LIST, ORDER_LIST_URL_LINK)
    }

    @Test
    fun `check url link of activation then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, ACTIVATION_URL_LINK)
    }

    @Test
    fun `check url link of credit motor then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, CREDIT_MOTOR_URL_LINK)
    }

    @Test
    fun `check url link of modal toko then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MODAL_TOKO_URL_LINK)
    }

    @Test
    fun `check url link of hotel then should return HOTEL`() {
        assertEqualsDeepLinkMatcher(HOTEL, HOTEL_URL_LINK)
    }

    @Test
    fun `check url link of travel entertainment then should return TRAVEL_HOMEPAGE`() {
        assertEqualsDeepLinkMatcher(TRAVEL_HOMEPAGE, TRAVEL_ENTERTAINMENT_URL_LINK)
    }

    @Test
    fun `check url link of travel entertainment detail then should return TRAVEL_HOMEPAGE`() {
        assertEqualsDeepLinkMatcher(TRAVEL_HOMEPAGE, TRAVEL_ENTERTAINTMENT_DETAIL_URL_LINK)
    }

    @Test
    fun `check url link of recommendation then should return RECOMMENDATION`() {
        assertEqualsDeepLinkMatcher(RECOMMENDATION, RECOMMENDATION_URL_LINK)
    }

    @Test
    fun `check url link of product review then should return PRODUCT_REVIEW`() {
        assertEqualsDeepLinkMatcher(PRODUCT_REVIEW,  PRODUCT_REVIEW_URL_LINK)
    }

    @Test
    fun `check url link of MYSHOP then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MYSHOP_URL_LINK)
    }

    @Test
    fun `check url link of MY-SHOP then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, MY_SHOP_URL_LINK)
    }

    @Test
    fun `check url link of deals then should return DEALS`() {
        assertEqualsDeepLinkMatcher(DEALS, DEALS_URL_LINK)
    }

    @Test
    fun `check url link of activation power merchant then should return OTHER`() {
        assertEqualsDeepLinkMatcher(OTHER, AKTIVASI_POWER_MERCHANT_URL_LINK)
    }

    @Test
    fun `check url link of shop then should return SHOP`() {
        assertEqualsDeepLinkMatcher(SHOP, SHOP_URL_LINK)
    }

    @Test
    fun `check url link of product then should return PRODUCT`() {
        assertEqualsDeepLinkMatcher(PRODUCT, PRODUCT_URL_LINK)
    }

    @Test
    fun `check url link of campaign then should return ETALASE`() {
        assertEqualsDeepLinkMatcher(ETALASE, CAMPAIGN_URL_LINK)
    }

}