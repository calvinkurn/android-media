package tokopedia.applink.deeplink

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.order.DeeplinkMapperUohOrder
import com.tokopedia.applink.penalty.DeepLinkMapperPenalty
import com.tokopedia.applink.shopscore.DeepLinkMapperShopScore
import com.tokopedia.config.GlobalConfig
import io.mockk.every
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkMapperCustomerAppTest : DeepLinkMapperTestFixture() {

    override fun setup() {
        super.setup()
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    @Test
    fun `check home appLink then should return tokopedia internal home navigation in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME, expectedDeepLink)
    }

    @Test
    fun `check inbox appLink and new navigation then should return tokopedia internal home navigation in customerapp`() {
        every {
            DeeplinkMapperHome.useNewInbox()
        } returns true
        val expectedDeepLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/inbox?a=2"
        assertEqualsDeepLinkMapper(ApplinkConst.INBOX + "?a=2", expectedDeepLink)
    }

    @Test
    fun `check inbox appLink and old navigation then should return tokopedia internal home navigation in customerapp`() {
        every {
            DeeplinkMapperHome.useNewInbox()
        } returns false
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/inbox"
        assertEqualsDeepLinkMapper(ApplinkConst.INBOX, expectedDeepLink)
    }

    @Test
    fun `check qrscan appLink then should return tokopedia internal marketplace`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/qr-scanner/{need_result}/"
        assertEqualsDeepLinkMapper(ApplinkConst.QRSCAN, expectedDeepLink)
    }

    @Test
    fun `check topchat chatbot then should return tokopedia internal chatbot`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/chatbot/123?is_chat_bot=true"
        assertEqualsDeepLinkMapper(ApplinkConst.TOP_CHAT + "/123?is_chat_bot=true", expectedDeepLink)
    }

    @Test
    fun `check topchat to sellerapp from mainapp then should return tokopedia internal topchat`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-chat?redirect_to_sellerapp=true"
        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, ApplinkConst.TOP_CHAT + "/123?redirect_to_sellerapp=true", expectedDeepLink)
    }

    @Test
    fun `check topchat to sellerapp in mainapp then should return tokopedia internal topchat`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-chat?redirect_to_sellerapp=true"
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.TOP_CHAT + "/123?redirect_to_sellerapp=true", expectedDeepLink)
    }

    @Test
    fun `check topchat path then should return tokopedia internal topchat`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/topchat/test/123?a=1"
        assertEqualsDeepLinkMapper(ApplinkConst.TOP_CHAT + "/test/123?a=1", expectedDeepLink)
    }

    @Test
    fun `check account old then should return tokopedia internal account`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/old-home-account/abc/def/123"
        every {
            DeeplinkMapperAccount.usingOldAccount(any())
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.ACCOUNT + "/abc/def/123", expectedDeepLink)
    }

    @Test
    fun `check account new then should return tokopedia internal account`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/new-home-account/abc/def/123"
        every {
            DeeplinkMapperAccount.usingOldAccount(any())
        } returns false
        assertEqualsDeepLinkMapper(ApplinkConst.ACCOUNT + "/abc/def/123", expectedDeepLink)
    }

    @Test
    fun `check travel subhomepage then should return tokopedia internal recharge`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://recharge/home/dynamic?platform_id=1"
        assertEqualsDeepLinkMapper(ApplinkConst.TRAVEL_SUBHOMEPAGE_HOME + "?platform_id=1", expectedDeepLink)
    }

    @Test
    fun `check cart then should return tokopedia internal card`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/cart?test=1"
        assertEqualsDeepLinkMapper(ApplinkConst.CART + "?test=1", expectedDeepLink)
    }

    @Test
    fun `check tradein then should return tokopedia internal category tradein`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://category/tradein/abc"
        assertEqualsDeepLinkMapper(ApplinkConst.TRADEIN + "/abc", expectedDeepLink)
    }

    @Test
    fun `check product-review create then should return tokopedia internal create review`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-review/create/abc/1234/?rating=5&utm_source="
        assertEqualsDeepLinkMapper(ApplinkConst.PRODUCT_CREATE_REVIEW + "/abc/1234", expectedDeepLink)
    }

    @Test
    fun `check review reminder then should return tokopedia internal review reminder`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/review-reminder"
        assertEqualsDeepLinkMapper(ApplinkConst.REVIEW_REMINDER , expectedDeepLink)
    }

    @Test
    fun `check seller review then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/seller-review-detail?productId=123"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_REVIEW + "?productId=123", expectedDeepLink)
    }

    @Test
    fun `check oqr pin url entry link then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/ovoqrthanks/123/"
        assertEqualsDeepLinkMapper(ApplinkConst.OQR_PIN_URL_ENTRY_LINK + "/123", expectedDeepLink)
    }

    @Test
    fun `check order hisory shop then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/user-order-history/1932/"
        assertEqualsDeepLinkMapper(ApplinkConst.ORDER_HISTORY + "/1932", expectedDeepLink)
    }

    @Test
    fun `check home feed appLink then should return tokopedia internal home navigation tab home in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=1"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_FEED, expectedDeepLink)
    }

    @Test
    fun `check home account appLink then should return tokopedia internal home navigation tab account in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=4"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_ACCOUNT, expectedDeepLink)
    }

    @Test
    fun `check home account seller appLink then should return tokopedia internal home navigation tab seller in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=4&ACCOUNT_TAB=ACCOUNT_TAB_SELLER"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_ACCOUNT_SELLER, expectedDeepLink)
    }

    @Test
    fun `check seller info appLink then should empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_INFO, "")
    }

    @Test
    fun `check home recommendation appLink then should return tokopedia internal home navigation tab feed recommendation in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=5&recommend_list=true"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_RECOMMENDATION, expectedDeepLink)
    }

    @Test
    fun `check feed appLink then should return tokopedia internal home navigation tab feed content in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=1"
        assertEqualsDeepLinkMapper(ApplinkConst.FEED, expectedDeepLink)
    }

    @Test
    fun `check find appLink then should return tokopedia internal home find in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://find"
        assertEqualsDeepLinkMapper(ApplinkConst.FIND, expectedDeepLink)
    }

    @Test
    fun `check amp find appLink then should return tokopedia internal home find in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://find"
        assertEqualsDeepLinkMapper(ApplinkConst.AMP_FIND, expectedDeepLink)
    }

    @Test
    fun `check feed hashtag appLink then should return tokopedia internal feed hashtag in customerapp`() {
        val appLink = UriUtil.buildUri(ApplinkConst.FEED_HASHTAG, "baju")
        assertEqualsDeepLinkMapper(appLink, "")
    }

    @Test
    fun `check feed details appLink then should return tokopedia internal feed communication detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://feedcommunicationdetail"
        assertEqualsDeepLinkMapper(ApplinkConst.FEED_DETAILS, expectedDeepLink)
    }

    @Test
    fun `check home category appLink then should return tokopedia internal home category in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_CATEGORY, expectedDeepLink)
    }

    @Test
    fun `check message appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.MESSAGE, "")
    }

    @Test
    fun `check talk appLink then should return tokopedia internal talk in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/inbox-talk"
        assertEqualsDeepLinkMapper(ApplinkConst.TALK, expectedDeepLink)
    }

    @Test
    fun `check shop appLink then should return tokopedia internal shop in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop etalase appLink then should return tokopedia internal shop etalase in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page-product-list/1479278/etalase/wib/"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_ETALASE, "1479278", "wib")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop talk appLink then should return tokopedia internal shop talk in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/shop-talk/1479278/"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_TALK, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop review appLink then should return tokopedia internal shop review in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/review"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_REVIEW, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop note appLink then should return tokopedia internal shop note in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/note"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_NOTE, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop info appLink then should return tokopedia internal shop info in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/info"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_INFO, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop home appLink then should return tokopedia internal shop home in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/home"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_HOME, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop product appLink then should return tokopedia internal shop product in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/product"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_PRODUCT, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop feed appLink then should return tokopedia internal shop feed in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/feed"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_FEED, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop etalase list appLink then should return tokopedia internal shop etalase list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/shop-showcase-list?shop_id=1479278"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_ETALASE_LIST, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check my shop etalase list appLink then should return tokopedia internal my shop etalase list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/shop-showcase-list"
        assertEqualsDeepLinkMapper(ApplinkConst.MY_SHOP_ETALASE_LIST, expectedDeepLink)
    }

    @Test
    fun `check shop settings note appLink then should return tokopedia internal shop settings note in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-notes"
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_SETTINGS_NOTE, expectedDeepLink)
    }

    @Test
    fun `check shop settings address then should return tokopedia internal shop settings address`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-address"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_WAREHOUSE_DATA, expectedDeepLink)
    }

    @Test
    fun `check shop setting info appLink then should return tokopedia internal shop setting info in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-info"
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_SETTINGS_INFO, expectedDeepLink)
    }

    @Test
    fun `check product info appLink with no extras then should return internal product info`() {
        val productId = "890495024"
        val keyLayoutId = "layoutID"

        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, productId)
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-detail/${productId}/"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, keyLayoutId to null)
    }

    @Test
    fun `check product info appLink with layoutId extras then should return internal product info`() {
        val productId = "890495024"
        val layoutId = "4"
        val keyLayoutId = "layoutID"

        val appLink = Uri.parse(UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, productId))
                .buildUpon()
                .appendQueryParameter(keyLayoutId, layoutId)
                .build()
                .toString()

        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-detail/${productId}/?${keyLayoutId}=${layoutId}"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, keyLayoutId to layoutId)
    }

    @Test
    fun `check product info from affiliate then should return internal product info`() {
        val productId = "890495024"

        val appLink = UriUtil.buildUri(ApplinkConst.AFFILIATE_PRODUCT, productId)
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-detail/${productId}/?is_from_explore_affiliate=isAffiliate"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check product add appLink then should return tokopedia internal product add in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/open-product-preview"
        assertEqualsDeepLinkMapper(ApplinkConst.PRODUCT_ADD, expectedDeepLink)
    }

    @Test
    fun `check default recommendation page appLink then should return tokopedia internal default recommendation page in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/rekomendasi"
        assertEqualsDeepLinkMapper(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE, expectedDeepLink)
    }

    @Test
    fun `check add credit cart appLink then should return tokopedia internal add credit card in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://payment/add-credit-card"
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_CREDIT_CARD, expectedDeepLink)
    }

    @Test
    fun `check cart appLink then should return tokopedia internal card in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://payment/add-credit-card"
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_CREDIT_CARD, expectedDeepLink)
    }

    @Test
    fun `check checkout appLink then should return tokopedia internal checkout in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/checkout"
        assertEqualsDeepLinkMapper(ApplinkConst.CHECKOUT, expectedDeepLink)
    }

    @Test
    fun `check seller new order appLink then should return tokopedia internal seller new order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/new-order?tab_active=new_order"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_NEW_ORDER, expectedDeepLink)
    }

    @Test
    fun `check seller shipment appLink then should return tokopedia internal seller shipment in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/ready-to-ship?tab_active=confirm_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_SHIPMENT, expectedDeepLink)
    }

    @Test
    fun `check seller status appLink then should return tokopedia internal seller status in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/shipped?tab_active=in_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_STATUS, expectedDeepLink)
    }

    @Test
    fun `check seller history appLink then should return tokopedia internal seller history in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/history?tab_active=all_order"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check create shop appLink then should return tokopedia internal create shop in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/history?tab_active=all_order"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check reputation appLink then should return tokopedia internal reputation in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/review"
        every {
            DeeplinkMapperMerchant.goToInboxUnified()
        } returns false
        assertEqualsDeepLinkMapper(ApplinkConst.REPUTATION, expectedDeepLink)
    }

    @Test
    fun `check reputation appLink and ab test then should route to inbox unified in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/inbox?page=review&role=buyer"
        every {
            DeeplinkMapperMerchant.goToInboxUnified()
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.REPUTATION, expectedDeepLink)
    }

    @Test
    fun `check product review appLink then should return tokopedia internal product review in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product/890495024/review"
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_REPUTATION, "890495024")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check webview parent home appLink then should empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.WEBVIEW_PARENT_HOME, "")
    }

    @Test
    fun `check product talk appLink then should return tokopedia internal product talk in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/product-talk/890495024/"
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_TALK, "890495024")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check digital appLink then should return tokopedia internal digital in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL, expectedDeepLink)
    }

    @Test
    fun `check digital form appLink then should return tokopedia internal digital form in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://digital/form"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_PRODUCT, expectedDeepLink)
    }

    @Test
    fun `check digital subhome page appLink then should return tokopedia internal recharge home in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://recharge/home"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME, expectedDeepLink)
    }

    @Test
    fun `check digital smartcard appLink then should return tokopedia internal recharge home in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://digital/smartcard/emoney?calling_page_check_saldo="
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_SMARTCARD, expectedDeepLink)
    }

    @Test
    fun `check digital smartcard appLink then should return tokopedia internal digital smartcard in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://digital/smartcard/emoney?calling_page_check_saldo="
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_SMARTCARD, expectedDeepLink)
    }

    @Test
    fun `check digital smart bills appLink then should return tokopedia internal digital smart bills in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://recharge/bayarsekaligus"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_SMARTBILLS, expectedDeepLink)
    }

    @Test
    fun `check digital cart appLink then should return tokopedia internal digital cart in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://digital/checkout"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_CART, expectedDeepLink)
    }

    @Test
    fun `check digital category appLink then should return tokopedia internal digital category in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/category"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_CATEGORY, expectedDeepLink)
    }

    @Test
    fun `check recharge appLink then should return tokopedia internal recharge in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://recharge"
        assertEqualsDeepLinkMapper(ApplinkConst.RECHARGE, expectedDeepLink)
    }

    @Test
    fun `check discovery page appLink then should return tokopedia internal discovery page in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery/1"
        val appLink = UriUtil.buildUri(ApplinkConst.DISCOVERY_PAGE, "1")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check discovery page appLink then should empty in customerapp`() {
        val appLink = UriUtil.buildUri(ApplinkConst.REACT_DISCOVERY_PAGE, "1")
        assertEqualsDeepLinkMapper(appLink, "")
    }

    @Test
    fun `check discovery new user appLink then should return tokopedia internal discovery new user in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery/pengguna-baru"
        val appLink = UriUtil.buildUri(ApplinkConst.DISCOVERY_NEW_USER, "1")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check jump appLink then should return tokopedia internal jump in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/jump"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_EXPLORE, expectedDeepLink)
    }

    @Test
    fun `check promo appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PROMO, "")
    }

    @Test
    fun `check promo with dash appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PROMO_WITH_DASH, "")
    }

    @Test
    fun `check promo detail appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PROMO_DETAIL, "")
    }

    @Test
    fun `check discovery category detail appLink then should return tokopedia internal discovery category detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://category/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.DISCOVERY_CATEGORY_DETAIL, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check discovery p then should return tokopedia internal category`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://category/abc_def_123"
        assertEqualsDeepLinkMapper("tokopedia://p/abc/def/123", expectedDeepLink)
    }

    @Test
    fun `check search appLink then should return tokopedia internal search in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://discovery/search-result"
        assertEqualsDeepLinkMapper(ApplinkConst.DISCOVERY_SEARCH, expectedDeepLink)
    }

    @Test
    fun `check search autocomplete appLink then should return tokopedia internal search autocomplete in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://discovery/autocomplete"
        assertEqualsDeepLinkMapper(ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE, expectedDeepLink)
    }

    @Test
    fun `check hotlist detail appLink then should return tokopedia internal hotlist detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://discovery/search-result?q=baju"
        val appLink = UriUtil.buildUri(ApplinkConst.DISCOVERY_HOTLIST_DETAIL, "baju")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check category appLink then should return tokopedia internal category in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://catalog"
        assertEqualsDeepLinkMapper(ApplinkConst.DISCOVERY_CATALOG, expectedDeepLink)
    }

    @Test
    fun `check payment back to default appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PAYMENT_BACK_TO_DEFAULT, "")
    }

    @Test
    fun `check wishlist appLink then should return tokopedia internal wish list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/wishlist"
        assertEqualsDeepLinkMapper(ApplinkConst.WISHLIST, expectedDeepLink)
    }

    @Test
    fun `check new wishlist appLink then should return tokopedia internal new wish list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/wishlist"
        assertEqualsDeepLinkMapper(ApplinkConst.NEW_WISHLIST, expectedDeepLink)
    }

    @Test
    fun `check recently viewed appLink then should return tokopedia internal recently viewed in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/recentlyviewed"
        assertEqualsDeepLinkMapper(ApplinkConst.RECENT_VIEW, expectedDeepLink)
    }

    @Test
    fun `check login appLink then should return tokopedia internal login in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/login"
        assertEqualsDeepLinkMapper(ApplinkConst.LOGIN, expectedDeepLink)
    }

    @Test
    fun `check otp appLink then should return tokopedia internal otp in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/cotp"
        assertEqualsDeepLinkMapper(ApplinkConst.OTP, expectedDeepLink)
    }

    @Test
    fun `check otp verify appLink then should return tokopedia internal otp in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/otp-push-notif-receiver"
        assertEqualsDeepLinkMapper(ApplinkConst.OTP_PUSH_NOTIF_RECEIVER, expectedDeepLink)
    }

    @Test
    fun `check official stores appLink then should return tokopedia internal official stores in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=2"
        assertEqualsDeepLinkMapper(ApplinkConst.OFFICIAL_STORES, expectedDeepLink)
    }

    @Test
    fun `check official store appLink then should return tokopedia internal official store in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=2"
        assertEqualsDeepLinkMapper(ApplinkConst.OFFICIAL_STORE, expectedDeepLink)
    }

    @Test
    fun `check official store category appLink then should return tokopedia internal official store category in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?key_category=21&TAB_POSITION=2"
        val appLink = UriUtil.buildUri(ApplinkConst.OFFICIAL_STORE_CATEGORY, "21")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check resolution center buyer appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.RESCENTER_SELLER, "")
    }

    @Test
    fun `check order history appLink then should return tokopedia internal order history in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.ORDER_HISTORY, "")
    }

    @Test
    fun `check topchat with id appLink then should return tokopedia internal topchat in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/topchat/12345"
        val appLink = UriUtil.buildUri(ApplinkConst.TOPCHAT, "12345")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check topchat appLink then should return tokopedia internal topchat in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/topchat"
        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, ApplinkConst.TOP_CHAT, expectedDeepLink)
    }

    @Test
    fun `check topchat appLink then should return tokopedia internal topchat in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-chat"
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.TOP_CHAT, expectedDeepLink)
    }

    @Test
    fun `check topchat old appLink then should return tokopedia internal topchat old in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/topchat"
        assertEqualsDeepLinkMapper(ApplinkConst.TOPCHAT_OLD, expectedDeepLink)
    }

    @Test
    fun `check chatbot with id appLink then should return tokopedia internal chatbot in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/chatbot/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.CHATBOT, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check chatbot appLink then should return tokopedia internal chatbot in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/chatbot"
        assertEqualsDeepLinkMapper(ApplinkConst.CHAT_BOT, expectedDeepLink)
    }

    @Test
    fun `check chat template appLink then should return tokopedia internal chat template in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/chat/settings/templatechat"
        assertEqualsDeepLinkMapper(ApplinkConst.CHAT_TEMPLATE, expectedDeepLink)
    }

    @Test
    fun `check referral appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.REFERRAL, "")
    }

    @Test
    fun `check favorite appLink then should return tokopedia internal favorite in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://home/favorite"
        assertEqualsDeepLinkMapper(ApplinkConst.FAVORITE, expectedDeepLink)
    }

    @Test
    fun `check brand list appLink then should return tokopedia internal brand list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/official-store/brand/0/"
        assertEqualsDeepLinkMapper(ApplinkConst.BRAND_LIST, expectedDeepLink)
    }

    @Test
    fun `check brand list with slash appLink then should return tokopedia internal brand list with slash in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/official-store/brand/0/"
        assertEqualsDeepLinkMapper(ApplinkConst.BRAND_LIST, expectedDeepLink)
    }

    @Test
    fun `check register appLink then should return empty in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/init-register"
        assertEqualsDeepLinkMapper(ApplinkConst.REGISTER, expectedDeepLink)
    }

    @Test
    fun `check profile appLink then should return tokopedia internal profile in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://people/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.PROFILE, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check profile after post appLink then should return tokopedia internal profile after post in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://people/123456?after_post=true"
        val appLink = UriUtil.buildUri(ApplinkConst.PROFILE_AFTER_POST, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check profile success post appLink then should return tokopedia internal profile success post in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://people/123456?success_post=true"
        val appLink = UriUtil.buildUri(ApplinkConst.PROFILE_SUCCESS_POST, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check profile completion appLink then should return tokopedia internal profile completion in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/profile-completion"
        assertEqualsDeepLinkMapper(ApplinkConst.PROFILE_COMPLETION, expectedDeepLink)
    }

    @Test
    fun `check how to pay appLink then should return tokopedia internal how to pay in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://howtopay"
        assertEqualsDeepLinkMapper(ApplinkConst.HOWTOPAY, expectedDeepLink)
    }

    @Test
    fun `check pay later query then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://fintech/paylater?q=123"
        assertEqualsDeepLinkMapper(ApplinkConst.PAYLATER +"?q=123", expectedDeepLink)
    }

    @Test
    fun `check profile completion then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/profile-completion"
        assertEqualsDeepLinkMapper(ApplinkConst.PROFILE_COMPLETION, expectedDeepLink)
    }

    @Test
    fun `check feedback form then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/internal-feedback"
        assertEqualsDeepLinkMapper(ApplinkConst.FEEDBACK_FORM, expectedDeepLink)
    }

    @Test
    fun `check ovo register init then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/ovo-reg-init"
        assertEqualsDeepLinkMapper(ApplinkConst.OVO_REGISTER_INIT, expectedDeepLink)
    }

    @Test
    fun `check register init then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/init-register"
        assertEqualsDeepLinkMapper(ApplinkConst.REGISTER_INIT, expectedDeepLink)
    }

    @Test
    fun `check ovo final page then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/ovo-final-page"
        assertEqualsDeepLinkMapper(ApplinkConst.OVO_FINAL_PAGE, expectedDeepLink)
    }

    @Test
    fun `check gold merchant statistic dashboard appLink then should return tokopedia internal gold merchant statistic dashboard in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/statistic_dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD, expectedDeepLink)
    }

    @Test
    fun `check shop score detail appLink then should return tokopedia internal shop score detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop/performance"
        every {
            DeepLinkMapperShopScore.isEnableNewShopScore(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_SCORE_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check shop penalty appLink then should return tokopedia internal shop penalty in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-penalty"
        every {
            DeepLinkMapperPenalty.isEnableNewPenalty(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_PENALTY, expectedDeepLink)
    }

    @Test
    fun `check events appLink then should return tokopedia internal events in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://event/home"
        assertEqualsDeepLinkMapper(ApplinkConst.EVENTS, expectedDeepLink)
    }

    @Test
    fun `check promo native appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PROMO_LIST, "")
    }

    @Test
    fun `check digital order appLink then should return tokopedia internal digital order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=digital"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_ORDER, expectedDeepLink)
    }

    @Test
    fun `check events order appLink then should return tokopedia internal events order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=events"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.EVENTS_ORDER, expectedDeepLink)
    }

    @Test
    fun `check deals order appLink then should return tokopedia internal deals order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=deals"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.DEALS_ORDER, expectedDeepLink)
    }

    @Test
    fun `check flight order appLink then should return tokopedia internal flight order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=pesawat"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.FLIGHT_ORDER, expectedDeepLink)
    }

    @Test
    fun `check train order appLink then should return tokopedia internal train order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=kereta"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.TRAIN_ORDER, expectedDeepLink)
    }

    @Test
    fun `check gift cards order appLink then should return tokopedia internal gift cards order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=giftcards"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.GIFT_CARDS_ORDER, expectedDeepLink)
    }

    @Test
    fun `check insurance order appLink then should return tokopedia internal insurance order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=insurance"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.INSURANCE_ORDER, expectedDeepLink)
    }

    @Test
    fun `check modal toko order appLink then should return tokopedia internal modal toko order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=modaltoko"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.MODAL_TOKO_ORDER, expectedDeepLink)
    }

    @Test
    fun `check hotel order appLink then should return tokopedia internal hotel order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=hotel"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.HOTEL_ORDER, expectedDeepLink)
    }

    @Test
    fun `check hotel appLink then should return tokopedia internal hotel in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://hotel"
        assertEqualsDeepLinkMapper(ApplinkConst.HOTEL, expectedDeepLink)
    }

    @Test
    fun `check oms order detail appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.OMS_ORDER_DETAIL, "")
    }

    @Test
    fun `check marketplace order appLink then should return tokopedia internal marketplace order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_ORDER, expectedDeepLink)
    }

    @Test
    fun `check belanja order appLink then should return tokopedia internal belanja order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.BELANJA_ORDER, expectedDeepLink)
    }

    @Test
    fun `check marketplace order sub appLink then should return tokopedia internal marketplace order sub in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace_dalam_proses"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_ORDER_SUB, expectedDeepLink)
    }

    @Test
    fun `check marketplace waiting confirmation appLink then should return tokopedia internal marketplace waiting confirmation in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_waiting_confirmation"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION, expectedDeepLink)
    }

    @Test
    fun `check marketplace sent appLink then should return tokopedia internal marketplace sent in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_sent"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_SENT, expectedDeepLink)
    }

    @Test
    fun `check marketplace order processed appLink then should return tokopedia internal marketplace order processed in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_processed"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_ORDER_PROCESSED, expectedDeepLink)
    }

    @Test
    fun `check marketplace delivered appLink then should return tokopedia internal marketplace delivered in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_delivered"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_DELIVERED, expectedDeepLink)
    }

    @Test
    fun `check buyer notif appLink then should return tokopedia internal buyer notif in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/notif-center"
        assertEqualsDeepLinkMapper(ApplinkConst.BUYER_INFO, expectedDeepLink)
    }

    @Test
    fun `check buyer info with id appLink then should return tokopedia internal buyer info with id in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/notif-center/12345/"
        val appLink = UriUtil.buildUri(ApplinkConst.BUYER_INFO_WITH_ID, "12345")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check seller info detail appLink then should return tokopedia internal seller info detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/notif-center"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_INFO_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check inbox ticket appLink then should return tokopedia internal inbox ticket in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://customercare/inbox-list"
        assertEqualsDeepLinkMapper(ApplinkConst.INBOX_TICKET, expectedDeepLink)
    }

    @Test
    fun `check shipping tracking appLink then should return tokopedia internal ticket detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/shipping/tracking/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.ORDER_TRACKING, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check flight appLink then should return tokopedia internal ticket detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://pesawat/dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.FLIGHT, expectedDeepLink)
    }

    @Test
    fun `check product manage appLink then should return tokopedia internal product manage in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-manage-list?filter=123"
        assertEqualsDeepLinkMapper(ApplinkConst.PRODUCT_MANAGE + "?filter=123", expectedDeepLink)
    }

    @Test
    fun `check product manage redirect appLink then should return tokopedia internal product manage`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-product-list?redirect_to_sellerapp=true"
        assertEqualsDeepLinkMapper(ApplinkConst.PRODUCT_MANAGE + "?redirect_to_sellerapp=true", expectedDeepLink)
    }

    @Test
    fun `check product manage from sellerapp appLink then should return tokopedia internal product manage`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-product-list"
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.PRODUCT_MANAGE, expectedDeepLink)
    }

    @Test
    fun `check product edit appLink then should return tokopedia internal product edit in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/open-product-preview?id=123445&mode=edit-product"
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_EDIT, "123445")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check seller center appLink then should return to seller center webview in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/webview?url=https://seller.tokopedia.com/edu/"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_CENTER, expectedDeepLink)
    }

    @Test
    fun `check seller shipping editor appLink then should return tokopedia internal seller shipping editor in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_SHIPPING_EDITOR, expectedDeepLink)
    }

    @Test
    fun `check seller cod activation appLink then should return tokopedia internal seller cod activation in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-cod"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_COD_ACTIVATION, expectedDeepLink)
    }

    @Test
    fun `check order list appLink then should return tokopedia internal order list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.ORDER_LIST, expectedDeepLink)
    }

    @Test
    fun `check order list webview appLink then should return tokopedia internal order list webview in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.ORDER_LIST_WEBVIEW, expectedDeepLink)
    }

    @Test
    fun `check tokopoints appLink then should return tokopedia internal tokopoints in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TOKOPOINTS, expectedDeepLink)
    }

    @Test
    fun `check rewards appLink then should return tokopedia internal rewards in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TOKOPEDIA_REWARD, expectedDeepLink)
    }

    @Test
    fun `check coupon listing appLink then should return tokopedia internal coupon listing in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/kupon-saya"
        assertEqualsDeepLinkMapper(ApplinkConst.COUPON_LISTING, expectedDeepLink)
    }

    @Test
    fun `check developer options appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.DEVELOPER_OPTIONS, "")
    }

    @Test
    fun `check setting profile appLink then should return tokopedia internal setting profile in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/setting-profile"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_PROFILE, expectedDeepLink)
    }

    @Test
    fun `check notification appLink then should return tokopedia internal notification in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/notification"
        assertEqualsDeepLinkMapper(ApplinkConst.NOTIFICATION, expectedDeepLink)
    }

    @Test
    fun `check notification troubleshooter appLink then should return tokopedia internal notification troubleshooter in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/push-notification-troubleshooter"
        assertEqualsDeepLinkMapper(ApplinkConst.NOTIFICATION_TROUBLESHOOTER, expectedDeepLink)
    }

    @Test
    fun `check buyer payment appLink then should return tokopedia internal buyer payment in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://buyer/payment"
        assertEqualsDeepLinkMapper(ApplinkConst.PMS, expectedDeepLink)
    }

    @Test
    fun `check purchase order appLink then should return tokopedia internal purchase order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_ORDER, expectedDeepLink)
    }

    @Test
    fun `check purchase confirmed appLink then should return tokopedia internal purchase confirmed in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_waiting_confirmation"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_CONFIRMED, expectedDeepLink)
    }

    @Test
    fun `check purchase processed appLink then should return tokopedia internal purchase processed in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_processed"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_PROCESSED, expectedDeepLink)
    }

    @Test
    fun `check shipping confirm appLink then should return tokopedia internal shipping confirm in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace_dalam_proses"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_SHIPPING_CONFIRM, expectedDeepLink)
    }

    @Test
    fun `check shipped appLink then should return tokopedia internal shipped in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_sent"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_SHIPPED, expectedDeepLink)
    }

    @Test
    fun `check delivered appLink then should return tokopedia internal delivered in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_delivered"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_DELIVERED, expectedDeepLink)
    }

    @Test
    fun `check purchase history appLink then should return tokopedia internal purchase history in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check ongoing appLink then should return tokopedia internal ongoing in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_ongoing"
        every {
            DeeplinkMapperUohOrder.useUoh(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_ONGOING, expectedDeepLink)
    }

    @Test
    fun `check ready to ship appLink then should return tokopedia internal ready to ship in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/ready-to-ship?tab_active=confirm_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP, expectedDeepLink)
    }

    @Test
    fun `check seller shipped appLink then should return tokopedia internal seller shipped in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/shipped?tab_active=in_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_SHIPPED, expectedDeepLink)
    }

    @Test
    fun `check seller delivered appLink then should return tokopedia internal seller delivered in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/delivered?tab_active=in_shipping&tab_status=delivered"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_DELIVERED, expectedDeepLink)
    }

    @Test
    fun `check seller canceled appLink then should return tokopedia internal seller canceled in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/cancelled?tab_active=order_canceled"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_CANCELED, expectedDeepLink)
    }

    @Test
    fun `check seller cancellation request appLink then should return tokopedia internal seller cancellation request in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/cancellationrequest?tab_active=all_order&filter_order_type=10"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_CANCELLATION_REQUEST, expectedDeepLink)
    }

    @Test
    fun `check seller waiting pickup appLink then should return tokopedia internal seller waiting pickup in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/waiting-pickup?tab_active=&filter_status_id=7"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_WAITING_PICKUP, expectedDeepLink)
    }

    @Test
    fun `check seller waiting awb appLink then should return tokopedia internal seller waiting awb in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/waiting-awb?tab_active=&filter_status_id=9"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_WAITING_AWB, expectedDeepLink)
    }

    @Test
    fun `check seller awb invalid appLink then should return tokopedia internal seller awb invalid in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/awb-invalid?tab_active=&filter_status_id=10"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_AWB_INVALID, expectedDeepLink)
    }

    @Test
    fun `check seller awb change appLink then should return tokopedia internal seller awb change in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/awb-change?tab_active=&filter_status_id=11"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_AWB_CHANGE, expectedDeepLink)
    }

    @Test
    fun `check seller purchase retur appLink then should return tokopedia internal seller purchase retur in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/retur?tab_active=&filter_status_id=13"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_RETUR, expectedDeepLink)
    }

    @Test
    fun `check seller purchase complaint appLink then should return tokopedia internal seller purchase complaint in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/complaint?tab_active=&filter_status_id=15"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_COMPLAINT, expectedDeepLink)
    }

    @Test
    fun `check seller purchase finished appLink then should return tokopedia internal seller purchase finished in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/finished?tab_active=done"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_FINISHED, expectedDeepLink)
    }

    @Test
    fun `check seller order appLink then should return tokopedia internal seller order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://seller/order"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_ORDER_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check saldo appLink then should return tokopedia internal saldo in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/saldo"
        assertEqualsDeepLinkMapper(ApplinkConst.SALDO, expectedDeepLink)
    }

    @Test
    fun `check instant debit bca appLink then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/instantdebitbca"
        assertEqualsDeepLinkMapper(ApplinkConst.DigitalInstantDebit.INSTANT_DEBIT_BCA_APPLINK, expectedDeepLink)
    }

    @Test
    fun `check instant debit bca edit limit appLink then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/editbcaoneklik"
        assertEqualsDeepLinkMapper(ApplinkConst.DigitalInstantDebit.INSTANT_DEBIT_BCA_EDITLIMIT_APPLINK, expectedDeepLink)
    }

    @Test
    fun `check main nav appLink then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://navigation/main"
        assertEqualsDeepLinkMapper(ApplinkConst.Navigation.MAIN_NAV, expectedDeepLink)
    }

    @Test
    fun `create shop`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/landing-shop-creation"
        assertEqualsDeepLinkMapper(ApplinkConst.CREATE_SHOP, expectedDeepLink)
    }

    @Test
    fun `check layanan finansial appLink then should return tokopedia internal layanan finansial in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://layanan-finansial/"
        assertEqualsDeepLinkMapper(ApplinkConst.LAYANAN_FINANSIAL, expectedDeepLink)
    }

    @Test
    fun `check saldo intro appLink then should return tokopedia internal saldo intro in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/saldo-intro"
        assertEqualsDeepLinkMapper(ApplinkConst.SALDO_INTRO, expectedDeepLink)
    }

    @Test
    fun `check change password appLink then should return tokopedia internal change password in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/change-password"
        assertEqualsDeepLinkMapper(ApplinkConst.CHANGE_PASSWORD, expectedDeepLink)
    }

    @Test
    fun `check has password appLink then should return tokopedia internal has password in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/has-password"
        assertEqualsDeepLinkMapper(ApplinkConst.HAS_PASSWORD, expectedDeepLink)
    }

    @Test
    fun `check setting bank appLink then should return tokopedia internal setting bank in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/setting-bank"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_BANK, expectedDeepLink)
    }

    @Test
    fun `check contact us appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.CONTACT_US, "")
    }

    @Test
    fun `check contact us native appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.CONTACT_US_NATIVE, "")
    }

    @Test
    fun `check setting notification appLink then should return tokopedia internal setting notification in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/user-notification-setting"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_NOTIFICATION, expectedDeepLink)
    }

    @Test
    fun `check content explore appLink then should return tokopedia internal content explore in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?category_id=2&tab_name=updates&TAB_POSITION=1"
        val appLink = UriUtil.buildUri(ApplinkConst.CONTENT_EXPLORE, "updates", "2")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check content detail appLink then should return tokopedia internal content detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://content/post-detail/123"
        val appLink = UriUtil.buildUri(ApplinkConst.CONTENT_DETAIL, "123")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check content create post appLink then should return tokopedia internal content create post in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://content/create_post/"
        assertEqualsDeepLinkMapper(ApplinkConst.CONTENT_CREATE_POST, expectedDeepLink)
    }

    @Test
    fun `check content draft post appLink then should return tokopedia internal content draft post in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://content/draft/1234"
        val appLink = UriUtil.buildUri(ApplinkConst.CONTENT_DRAFT_POST, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check interest pick appLink then should return tokopedia internal interest pick in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://interestpick"
        assertEqualsDeepLinkMapper(ApplinkConst.INTEREST_PICK, expectedDeepLink)
    }

    @Test
    fun `check kol comment appLink then should return tokopedia internal kol comment in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://content/comment/1234?isFromApplink=true"
        val appLink = UriUtil.buildUri(ApplinkConst.KOL_COMMENT, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check kol youtube appLink then should return tokopedia internal kol youtube in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://kolyoutube/https://www.youtube.com/watch?v=U5BwfqBpiWU"
        val appLink = UriUtil.buildUri(ApplinkConst.KOL_YOUTUBE, "https://www.youtube.com/watch?v=U5BwfqBpiWU")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check affiliate create post appLink then should return tokopedia internal affiliate create post in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/create_post/123456/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.AFFILIATE_CREATE_POST, "123456", "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check affiliate draft post appLink then should return tokopedia internal affiliate draft post in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/draft/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.AFFILIATE_DRAFT_POST, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check affiliate default create post appLink then should return tokopedia internal affiliate default create post in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/create_post/"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST, expectedDeepLink)
    }

    @Test
    fun `check affiliate dashboard appLink then should return tokopedia internal affiliate dashboard in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_DASHBOARD, expectedDeepLink)
    }

    @Test
    fun `check affiliate education appLink then should return tokopedia internal affiliate education in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/education"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_EDUCATION, expectedDeepLink)
    }

    @Test
    fun `check affiliate explore appLink then should return tokopedia internal affiliate explore in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/explore"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_EXPLORE, expectedDeepLink)
    }

    @Test
    fun `check play detail appLink then should return tokopedia internal play detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://play/1234"
        val appLink = UriUtil.buildUri(ApplinkConst.PLAY_DETAIL, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check play broadcaster appLink then should return tokopedia internal play detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://play-broadcaster"
        assertEqualsDeepLinkMapper(ApplinkConst.PLAY_BROADCASTER, expectedDeepLink)
    }

    @Test
    fun `check add name profile appLink then should return tokopedia internal add name profile in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/manage-name"
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_NAME_PROFILE, expectedDeepLink)
    }

    @Test
    fun `check reset passwoord appLink then should return tokopedia internal reset password in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/forgot-password"
        assertEqualsDeepLinkMapper(ApplinkConst.RESET_PASSWORD, expectedDeepLink)
    }

    @Test
    fun `check phone verification appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PHONE_VERIFICATION, "")
    }

    @Test
    fun `check change inactive phone appLink then should return tokopedia internal phone change inactive phone in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/change-inactive-phone"
        assertEqualsDeepLinkMapper(ApplinkConst.CHANGE_INACTIVE_PHONE, expectedDeepLink)
    }

    @Test
    fun `check add pin onboarding appLink then should return tokopedia internal add pin onboarding in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/add-pin-onboarding"
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_PIN_ONBOARD, expectedDeepLink)
    }

    @Test
    fun `check add fingerprint onboarding appLink then should return tokopedia internal add fingerprint onboarding in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/add-fingerprint-onboarding"
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_FINGERPRINT_ONBOARDING, expectedDeepLink)
    }

    @Test
    fun `check kyc no param appLink then should return tokopedia internal kyc in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/user-identification-info"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC_NO_PARAM, expectedDeepLink)
    }

    @Test
    fun `check kyc param appLink then should return tokopedia internal kyc in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/user-identification-info?projectId=1"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC_NO_PARAM +"?projectId=1", expectedDeepLink)
    }

    @Test
    fun `check kyc appLink then appLink should return tokopedia internal kyc in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/user-identification-info?projectId=1"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC, expectedDeepLink)
    }

    @Test
    fun `check kyc form no param appLink then should return tokopedia internal kyc form in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/user-identification-form"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC_FORM_NO_PARAM, expectedDeepLink)
    }

    @Test
    fun `check kyc form appLink then should return tokopedia internal kyc form in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/user-identification-form?projectId=12345"
        val appLink = UriUtil.buildUri(ApplinkConst.KYC_FORM, "12345")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check consumer splash screen appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.CONSUMER_SPLASH_SCREEN, "")
    }

    @Test
    fun `check home credit ktp without type appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_CREDIT_KTP_WITHOUT_TYPE, "")
    }

    @Test
    fun `check home credit ktp with type appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_CREDIT_KTP_WITH_TYPE, "")
    }

    @Test
    fun `check home credit selfie without type appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_CREDIT_SELFIE_WITHOUT_TYPE, "")
    }

    @Test
    fun `check home credit selfie with type appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_CREDIT_SELFIE_WITH_TYPE, "")
    }

    @Test
    fun `check smc referral appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SMC_REFERRAL, "")
    }

    @Test
    fun `check power merchant subscribe appLink then should return tokopedia internal pmc in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/power-merchant-subscribe"
        assertEqualsDeepLinkMapper(ApplinkConst.POWER_MERCHANT_SUBSCRIBE, expectedDeepLink)
    }

    @Test
    fun `check ovo p2 transfer appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.OVOP2PTRANSFERFORM_SHORT, "")
    }

    @Test
    fun `check ovo wallet appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.OVO_WALLET, "")
    }

    @Test
    fun `check deals appLink then should return tokopedia internal deals in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://deals/home-new"
        assertEqualsDeepLinkMapper(ApplinkConst.DEALS_HOME, expectedDeepLink)
    }

    @Test
    fun `check deals detail appLink then should return tokopedia internal deals detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/deals-brand/12345/"
        val appLink = UriUtil.buildUri(ApplinkConst.DEALS_BRAND_DETAIL, "12345")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check salam umroh detail appLink then should return tokopedia internal salam umroh detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://order-details/umroh"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check salam umroh appLink then should return tokopedia internal salam umroh in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://s/umroh"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH, expectedDeepLink)
    }

    @Test
    fun `check salam umroh pdp appLink then should return tokopedia internal salam umroh pdp in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://s/umroh/produk/1234"
        val appLink = UriUtil.buildUri(ApplinkConst.SALAM_UMRAH_PDP, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check salam umroh shop appLink then should return tokopedia internal salam umroh shop in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://s/umroh"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH_SHOP, expectedDeepLink)
    }

    @Test
    fun `check thank you page native appLink then should return tokopedia internal thank you page native in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://payment/thankyou"
        assertEqualsDeepLinkMapper(ApplinkConst.THANK_YOU_PAGE_NATIVE, expectedDeepLink)
    }

    @Test
    fun `check salam umroh agen appLink then should return tokopedia internal salam umroh agen in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://s/umroh/agen/1234"
        val appLink = UriUtil.buildUri(ApplinkConst.SALAM_UMRAH_AGEN, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check salam umroh list agen appLink then should return tokopedia internal salam umroh list agen in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://s/umroh/agen"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH_LIST_AGEN, expectedDeepLink)
    }

    @Test
    fun `check merchant voucher list appLink then should return tokopedia internal merchant voucher list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/voucher-list"
        assertEqualsDeepLinkMapper(ApplinkConst.MERCHANT_VOUCHER_LIST, expectedDeepLink)
    }

    @Test
    fun `check occ appLink then should return tokopedia internal occ in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/one-click-checkout"
        assertEqualsDeepLinkMapper(ApplinkConst.OCC, expectedDeepLink)
    }

    @Test
    fun `check seller migration appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_MIGRATION, "")
    }

    @Test
    fun `check gamification crack appLink then should return tokopedia internal gamification crack in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/gamification"
        assertEqualsDeepLinkMapper(ApplinkConst.Gamification.CRACK, expectedDeepLink)
    }

    @Test
    fun `check gamification tap tap appLink then should return tokopedia internal gamification tap tap in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/gamification2"
        assertEqualsDeepLinkMapper(ApplinkConst.Gamification.TAP_TAP_MANTAP, expectedDeepLink)
    }

    @Test
    fun `check gamification daily gift box appLink then should return tokopedia internal gamification daily gift box in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/gamification_gift_daily"
        assertEqualsDeepLinkMapper(ApplinkConst.Gamification.DAILY_GIFT_BOX, expectedDeepLink)
    }

    @Test
    fun `check gamification daily gift tap tap appLink then should return tokopedia internal gamification daily gift tap tap in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/gamification_gift_60s"
        assertEqualsDeepLinkMapper(ApplinkConst.Gamification.GIFT_TAP_TAP, expectedDeepLink)
    }

    @Test
    fun `check digital browse appLink then should return tokopedia internal digital browse in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://category-explore"
        assertEqualsDeepLinkMapper(ApplinkConst.Digital.DIGITAL_BROWSE, expectedDeepLink)
    }

    @Test
    fun `check digital product appLink then should return tokopedia internal digital product in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://digital/form"
        assertEqualsDeepLinkMapper(ApplinkConst.Digital.DIGITAL_PRODUCT, expectedDeepLink)
    }

    @Test
    fun `check tokopoints homepage appLink then should return tokopedia internal tokopoints home page in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.HOMEPAGE, expectedDeepLink)
    }

    @Test
    fun `check tokopoints homepage slash appLink then should return tokopedia internal tokopoints home page in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.HOMEPAGE2, expectedDeepLink)
    }

    @Test
    fun `check tokopoints homepage reward1 appLink then should return tokopedia internal tokopoints homepage rewards1 in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.HOMEPAGE_REWARD1, expectedDeepLink)
    }

    @Test
    fun `check tokopoints homepage reward2 appLink then should return tokopedia internal tokopoints homepage rewards2 in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.HOMEPAGE_REWARD2, expectedDeepLink)
    }

    @Test
    fun `check coupon detail appLink then should return kupon saya detail in customerapp`() {
        val expectedDeepLink = "kupon-saya/detail"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.COUPON_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check coupon detail value appLink then should return kupon detail in customerapp`() {
        val expectedDeepLink = "kupon-detail"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.COUPON_DETAIL_VALUE, expectedDeepLink)
    }

    @Test
    fun `check catalog detail appLink then should return tukar point detail in customerapp`() {
        val expectedDeepLink = "tukar-point/detail"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.CATALOG_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check catalog detail value appLink then should return tukar detail in customerapp`() {
        val expectedDeepLink = "tukar-detail"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.CATALOG_DETAIL_VALUE, expectedDeepLink)
    }

    @Test
    fun `check catalog detail new appLink then should return kupon detail in customerapp`() {
        val expectedDeepLink = "kupon/detail"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.CATALOG_DETAIL_NEW, expectedDeepLink)
    }

    @Test
    fun `check catalog list new appLink then should return kupon in customerapp`() {
        val expectedDeepLink = "kupon"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.CATALOG_LIST_NEW, expectedDeepLink)
    }

    @Test
    fun `check catalog list value appLink then should return tukar point in customerapp`() {
        val expectedDeepLink = "tukar-point"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.CATALOG_LIST_VALUE, expectedDeepLink)
    }

    @Test
    fun `check history tokopoints appLink then should return tokopedia internal history tokopoint in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://tokopoints/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.HISTORY, expectedDeepLink)
    }

    @Test
    fun `check discovery appLink then should return tokopedia internal discovery in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery"
        assertEqualsDeepLinkMapper(ApplinkConst.DISCOVERY, expectedDeepLink)
    }

    @Test
    fun `check moneyin appLink then should return tokopedia internal moneyin in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://money_in/device_validation"
        assertEqualsDeepLinkMapper(ApplinkConst.MONEYIN, expectedDeepLink)
    }

    @Test
    fun `check snapshot order appLink then should return tokopedia internal snapshot order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://snapshot/order?order_id=1234&order_detail_id=7890"
        val appLink = UriUtil.buildUri(ApplinkConst.SNAPSHOT_ORDER + "/1234/7890")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop follower list appLink then should return tokopedia internal shop follower list in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/shop-favourites"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_FOLLOWER_LIST, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop setting appLink then should return tokopedia internal shop setting in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/settings"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_SETTINGS_CUSTOMER_APP, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check buyer cancellation request appLink should return tokopedia internal buyer cancellation request applink in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://buyer/cancellationrequest"
        val appLink = ApplinkConst.ORDER_BUYER_CANCELLATION_REQUEST_PAGE
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }
    
    @Test
    fun `check login by qr appLink then should return tokopedia internal login by qr in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/qr-login"
        assertEqualsDeepLinkMapper(ApplinkConst.MONEYIN, expectedDeepLink)
    }
}