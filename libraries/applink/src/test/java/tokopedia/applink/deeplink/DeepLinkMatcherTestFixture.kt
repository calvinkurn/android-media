package tokopedia.applink.deeplink

import android.net.Uri
import com.tokopedia.applink.DeeplinkMatcher
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before

open class DeepLinkMatcherTestFixture {

    private lateinit var deepLinkMatcher: DeeplinkMatcher

    @Before
    fun setup() {
        mockkStatic(Uri::class)
        deepLinkMatcher = DeeplinkMatcher()
    }

    @After
    fun finish()  {
        unmockkStatic(Uri::class)
    }

    protected fun assertEqualsDeepLinkMatcher(idDeepLinkChecker: Int, url: String) {
        val uri = parseToUri(url)
        val expectedResult = deepLinkMatcher.match(uri)
        assertEquals(idDeepLinkChecker, expectedResult)
    }

    private fun parseToUri(url: String): Uri {
        return Uri.parse(url)
    }

    companion object {
        const val PLAY_URL_LINK = "https://www.tokopedia.com/play"
        const val GROUP_CHAT_URL_LINK = "https://www.tokopedia.com/groupchat"
        const val FLIGHT_URL_LINK = "https://www.tokopedia.com/flight"
        const val PROMO_DETAIL_URL_LINK = "https://www.tokopedia.com/promo/finansial-citibank/?cta_src=0"
        const val PROMO_LIST_URL_LINK = "https://www.tokopedia.com/promo"
        const val SALE_URL_LINK = "https://www.tokopedia.com/sale"
        const val INVOICE_URL_LINK = "https://www.tokopedia.com/invoice.pl?id=601307061"
        const val BLOG_URL_LINK = "https://www.tokopedia.com/blog/"
        const val NEW_CATEGORY_URL_LINK = "https://www.tokopedia.com/category"
        const val OLD_CATEGORY_URL_LINK = "https://www.tokopedia.com/p"
        const val SEARCH_URL_LINK = "https://www.tokopedia.com/search?st=product&q=baju"
        const val HOT_LIST_URL_LINK = "https://www.tokopedia.com/hot/"
        const val HOT_URL_LINK = "https://www.tokopedia.com/hot/abc/"
        const val FIND_URL_LINK = "https://www.tokopedia.com/find"
        const val CATALOG_URL_INK = "https://www.tokopedia.com/catalog"
        const val DISCOVERY_B_URL_LINK = "https://www.tokopedia.com/b/everyday"
        const val DISCOVERY_PAGE_URL_LINK = "https://www.tokopedia.com/discovery/everyday"
        const val PULSA_URL_LINK = "https://www.tokopedia.com/pulsa/"
        const val ETALASE_URL_LINK = "https://www.tokopedia.com/unilever/etalase/wib"
        const val REFERALL_URL_LINK = "https://www.tokopedia.com/referral/"
        const val TOKO_POINTS_URL_LINK = "https://www.tokopedia.com/tokopoints"
        const val OVO_URL_LINK = "https://www.tokopedia.com/ovo/"
        const val PEOPLE_URL_LINK = "https://www.tokopedia.com/people/5208397"
        const val CONTENT_URL_LINK = "https://m.tokopedia.com/content/new"
        const val KUPON_THR_URL_LINK = "https://www.tokopedia.com/kupon-thr"
        const val SERU_URL_LINK = "https://www.tokopedia.com/seru/abc1234"
        const val GOLD_URL_LINK = "https://www.tokopedia.com/emas/"
        const val REKSA_DANA_URL_LINK = "https://www.tokopedia.com/reksa-dana/"
        const val HELP_URL_LINK = "https://www.tokopedia.com/bantuan"
        const val EVENTS_URL_LINK = "https://www.tokopedia.com/events"
        const val MERCHANT_KYC_URL_LINK = "https://www.tokopedia.com/terms/merchantkyc"
        const val CONTACT_US_URL_LINK =  "https://www.tokopedia.com/contact-us"
        const val ABOUT_URL_LINK = "https://www.tokopedia.com/about"
        const val RESET_LINK = "https://www.tokopedia.com/reset.pl"
        const val ORDER_LIST_URL_LINK = "https://www.tokopedia.com/order-list?filter=18&tab=0"
        const val ACTIVATION_URL_LINK = "https://www.tokopedia.com/activation.pl"
        const val CREDIT_MOTOR_URL_LINK = "https://www.tokopedia.com/kredit-motor/132"
        const val MODAL_TOKO_URL_LINK = "https://www.tokopedia.com/fm/modal-toko/"
        const val HOTEL_URL_LINK = "https://www.tokopedia.com/hotel/"
        const val TRAVEL_ENTERTAINMENT_URL_LINK =  "https://m.tokopedia.com/travel-entertainment/"
        const val TRAVEL_ENTERTAINTMENT_DETAIL_URL_LINK = "https://m.tokopedia.com/travel-entertainment/bandung/"
        const val RECOMMENDATION_URL_LINK = "https://m.tokopedia.com/rekomendasi/"
        const val PRODUCT_REVIEW_URL_LINK = "https://www.tokopedia.com/product-review/2/3/4"
        const val MYSHOP_URL_LINK = "https://www.tokopedia.com/myshop/123/"
        const val MY_SHOP_URL_LINK = "https://www.tokopedia.com/my-shop"
        const val DEALS_URL_LINK = "https://www.tokopedia.com/deals/"
        const val AKTIVASI_POWER_MERCHANT_URL_LINK = "https://www.tokopedia.com/terms/aktivasi-powermerchant"
        const val SHOP_URL_LINK = "https://www.tokopedia.com/nicestuff88"
        const val PRODUCT_URL_LINK = "https://www.tokopedia.com/nicestuff88/magic-tas-kantong-belanja-shopping-bag-roll-up-berkualitas"
        const val CAMPAIGN_URL_LINK = "https://www.tokopedia.com/dettol/campaign/123"
    }
}