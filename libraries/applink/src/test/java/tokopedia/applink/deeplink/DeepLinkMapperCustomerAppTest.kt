package tokopedia.applink.deeplink

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.communication.DeeplinkMapperCommunication
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.model.Always
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow
import com.tokopedia.applink.user.DeeplinkMapperUser
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.remoteconfig.RemoteConfigInstance
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkMapperCustomerAppTest : DeepLinkMapperTestFixture() {

    companion object {
        // This a reminder to developer.
        // If this size is modified, please also add unit test for the added deeplink.
        const val SIZE_HOST = 157
        const val SIZE_PATH = 266
    }

    override fun setup() {
        super.setup()
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    @Throws(RuntimeException::class)
    @Test
    fun `deeplinkPatternTokopediaSchemeList check`() {
        // This test is a reminder for developer.
        // If there is any mapping changed/added/deleted, developer should change the SIZE_MAPPER
        // also there developer should also add the corresponding unit test for the deeplink.
        assertEquals(SIZE_HOST, DeeplinkMapper.getTokopediaSchemeList().size)
        var totalPath = 0
        var key = ""
        var alphabeticalOrder = true
        val entryKeyNotAlphabetical = mutableListOf<String>()
        DeeplinkMapper.getTokopediaSchemeList().forEach { entry ->
            if (entry.key < key) {
                entryKeyNotAlphabetical.add(entry.key)
                alphabeticalOrder = false
            } else if (entry.key == key) {
                throw RuntimeException("There is duplicate key: " + entry.key)
            }
            key = entry.key
            totalPath += entry.value.size

            var alwaysLogicFound = false
            for (value in entry.value) {
                if (value.logic !is Always && alwaysLogicFound) {
                    throw RuntimeException("Logic goTo should always in bottom. Key: " + entry.key)
                }
                if (value.logic is Always) {
                    alwaysLogicFound = true
                }
            }
        }
        assertEquals(SIZE_PATH, totalPath)
        // alphabetical order improve readability in code
        if (!alphabeticalOrder) {
            throw RuntimeException(entryKeyNotAlphabetical.joinToString(", ") + " is not alphabetical")
        }
    }

    @Test
    fun `check home appLink then should return tokopedia internal home navigation in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME, expectedDeepLink)
    }

    @Test
    fun `check qrscan appLink then should return tokopedia internal marketplace`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/qr-scanner/{need_result}/"
        assertEqualsDeepLinkMapper(ApplinkConst.QRSCAN, expectedDeepLink)
    }

    @Test
    fun `check topchat chatbot then should return tokopedia internal chatbot`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/chatbot/123?is_chat_bot=true"
        assertEqualsDeepLinkMapper(
            ApplinkConst.TOP_CHAT + "/123?is_chat_bot=true",
            expectedDeepLink
        )
    }

    @Test
    fun `check topchat to sellerapp from mainapp then should return tokopedia internal topchat`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-chat?redirect_to_sellerapp=true"
        assertEqualsDeepLinkMapperApp(
            AppType.MAIN_APP,
            ApplinkConst.TOP_CHAT + "/123?redirect_to_sellerapp=true",
            expectedDeepLink
        )
    }

    @Test
    fun `check topchat to sellerapp in mainapp then should return tokopedia internal topchat`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-chat?redirect_to_sellerapp=true"
        assertEqualsDeepLinkMapperApp(
            AppType.SELLER_APP,
            ApplinkConst.TOP_CHAT + "/123?redirect_to_sellerapp=true",
            expectedDeepLink
        )
    }

    @Test
    fun `check topchat path then should return tokopedia internal topchat`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/topchat/test/123?a=1"
        assertEqualsDeepLinkMapper(ApplinkConst.TOP_CHAT + "/test/123?a=1", expectedDeepLink)
    }

    @Test
    fun `check account old then should return new account`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/new-home-account/abc/def/123"
        every {
            DeeplinkMapperAccount.usingOldAccount(any())
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.ACCOUNT + "/abc/def/123", expectedDeepLink)
    }

    @Test
    fun `check account new then should return tokopedia internal new account`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/new-home-account/abc/def/123"
        every {
            DeeplinkMapperAccount.usingOldAccount(any())
        } returns false
        assertEqualsDeepLinkMapper(ApplinkConst.ACCOUNT + "/abc/def/123", expectedDeepLink)
    }

    @Test
    fun `check travel subhomepage then should return tokopedia internal recharge`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://recharge/home/dynamic?platform_id=1"
        assertEqualsDeepLinkMapper(
            ApplinkConst.TRAVEL_SUBHOMEPAGE_HOME + "?platform_id=1",
            expectedDeepLink
        )
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-review/create/abc/1234/?rating=5&utm_source="
        assertEqualsDeepLinkMapper(
            ApplinkConst.PRODUCT_CREATE_REVIEW + "/abc/1234",
            expectedDeepLink
        )
    }

    @Test
    fun `check seller review then should return tokopedia internal`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/seller-review-detail?productId=123"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_REVIEW + "?productId=123", expectedDeepLink)
    }

    @Test
    fun `check oqr pin url entry link then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/ovoqrthanks/123/"
        assertEqualsDeepLinkMapper(ApplinkConst.OQR_PIN_URL_ENTRY_LINK + "/123", expectedDeepLink)
    }

    @Test
    fun `check order hisory shop then should return tokopedia internal`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/user-order-history/1932/"
        assertEqualsDeepLinkMapper(ApplinkConst.ORDER_HISTORY + "/1932", expectedDeepLink)
    }

    @Test
    fun `check home feed appLink then should return tokopedia internal home navigation tab home in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=1"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_FEED, expectedDeepLink)
    }

    @Test
    fun `check home account appLink then should return tokopedia internal new home account`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/new-home-account"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_ACCOUNT, expectedDeepLink)
    }

    @Test
    fun `check home account seller appLink then should return tokopedia internal new home account`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/new-home-account"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_ACCOUNT_SELLER, expectedDeepLink)
    }

    @Test
    fun `check seller info appLink then should empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_INFO, "")
    }

    @Test
    fun `check home recommendation appLink then should return tokopedia internal home navigation tab feed recommendation in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=5&recommend_list=true"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_RECOMMENDATION, expectedDeepLink)
    }

    @Test
    fun `check feed appLink then should return tokopedia internal home navigation tab feed content in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=1"
        assertEqualsDeepLinkMapper(ApplinkConst.FEED, expectedDeepLink)
    }

    @Test
    fun `check feed explore tab appLink then should return tokopedia internal home navigation tab feed content with explore tab position in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=1&FEED_TAB_POSITION=2"
        assertEqualsDeepLinkMapper(ApplinkConst.FEED_EXPLORE, expectedDeepLink)
    }

    @Test
    fun `check feed video tab appLink then should return tokopedia internal home navigation tab feed content with video tab position in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?TAB_POSITION=1&FEED_TAB_POSITION=3&tab="
        assertEqualsDeepLinkMapper(ApplinkConst.FEED_VIDEO, expectedDeepLink)
    }

    @Test
    fun `check amp find appLink with search query should return tokopedia internal search in customerapp`() {
        val expectedDeepLink =
            ApplinkConstInternalDiscovery.SEARCH_RESULT +
                "?q=3%20ply%20masker" +
                "&navsource=find"

        assertEqualsDeepLinkMapper(ApplinkConst.AMP_FIND + "/3-ply-masker", expectedDeepLink)
    }

    @Test
    fun `check find appLink with search query then should return tokopedia internal search in customerapp`() {
        val expectedDeepLink =
            ApplinkConstInternalDiscovery.SEARCH_RESULT +
                "?q=3%20ply%20masker" +
                "&navsource=find"

        assertEqualsDeepLinkMapper(ApplinkConst.FIND + "/3-ply-masker", expectedDeepLink)
    }

    @Test
    fun `check find appLink with query and city then should return tokopedia internal search in customerapp`() {
        val expectedDeepLink =
            ApplinkConstInternalDiscovery.SEARCH_RESULT +
                "?q=3%20ply%20masker%20di%20dki%20jakarta" +
                "&navsource=find"

        assertEqualsDeepLinkMapper(
            ApplinkConst.FIND + "/3-ply-masker-di-dki-jakarta",
            expectedDeepLink
        )
    }

    @Test
    fun `check amp find appLink with query and city then should return tokopedia internal search in customerapp`() {
        val expectedDeepLink =
            ApplinkConstInternalDiscovery.SEARCH_RESULT +
                "?q=3%20ply%20masker%20di%20dki%20jakarta" +
                "&navsource=find"

        assertEqualsDeepLinkMapper(
            ApplinkConst.AMP_FIND + "/3-ply-masker-di-dki-jakarta",
            expectedDeepLink
        )
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
    fun `check unified feed details appLink then should return tokopedia internal unified feed detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://content/detail/123123"
        assertEqualsDeepLinkMapper(
            "${ApplinkConstInternalFeed.FEED_UNIFIED_DETAILS}/123123",
            expectedDeepLink
        )
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop etalase appLink then should return tokopedia internal shop etalase in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page-product-list/1479278/etalase/wib/"
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
    fun `check shop review appLink with empty source then should return tokopedia internal shop review tab in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/review?review-source="
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_REVIEW, "1479278", "")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop review appLink with source header then should return tokopedia internal shop review fullpage in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop/1479278/review?review-source=header"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_REVIEW, "1479278", "header")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop note appLink then should return tokopedia internal shop note in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/note"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_NOTE, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop info appLink then should return tokopedia internal shop info in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/info"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_INFO, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop home appLink then should return tokopedia internal shop home in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/home"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_HOME, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop product appLink then should return tokopedia internal shop product in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/product"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_PRODUCT, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop feed appLink then should return tokopedia internal shop feed in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/feed"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_FEED, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop etalase list appLink then should return tokopedia internal shop etalase list in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/shop-showcase-list?shop_id=1479278"
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-notes"
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_SETTINGS_NOTE, expectedDeepLink)
    }

    @Test
    fun `check shop operational hour appLink then should return tokopedia internal shop operational hour in customerapp`() {
        val mockShopId = "12345"
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop/widget/operational-hour/$mockShopId/"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_OPERATIONAL_HOUR, mockShopId)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop mvc locked to product shop id then should return tokopedia internal mvc locked to product shop id in customerapp`() {
        val mockShopId = "12345"
        val mockVoucherId = "6789"
        val expectedDeepLink = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.SHOP_MVC_LOCKED_TO_PRODUCT,
            mockShopId,
            mockVoucherId
        )
        val appLink =
            UriUtil.buildUri(ApplinkConst.SHOP_MVC_LOCKED_TO_PRODUCT, mockShopId, mockVoucherId)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop settings address then should return tokopedia internal shop settings address`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-address"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_WAREHOUSE_DATA, expectedDeepLink)
    }

    @Test
    fun `check shop setting info appLink then should return tokopedia internal shop setting info in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-info"
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_SETTINGS_INFO, expectedDeepLink)
    }

    @Test
    fun `check product educational appLink internal product educational`() {
        val type = "3"

        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_EDUCATIONAL, type)
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-edu/$type/"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, type to null)
    }

    @Test
    fun `check post atc appLink internal post atc`() {
        val productId = "123"

        val appLink = UriUtil.buildUri(ApplinkConst.POST_ATC, productId)
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/post-atc/$productId/"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, productId to null)
    }

    @Test
    fun `check product info appLink with no extras then should return internal product info`() {
        val productId = "890495024"
        val keyLayoutId = "layoutID"

        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, productId)
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-detail/$productId/"

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

        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-detail/$productId/?$keyLayoutId=$layoutId"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
        assertEqualsDeeplinkParameters(appLink, keyLayoutId to layoutId)
    }

    @Test
    fun `check product info applink with extParam (encoded format) then should return expected applink`() {
        val productId = "890495024"
        val extParam =
            "fcity%3D174%2C175%2C176%2C177%2C178%2C179%26shipping%3D10%2C13%2313%26ivf%3D1TRUE"
        val applink = Uri.parse(UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, productId))
            .buildUpon()
            .appendQueryParameter("extParam", extParam.decodeToUtf8())
            .build()
            .toString()
        val expectedDeeplink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-detail/$productId/?extParam=$extParam"

        assertEqualsDeepLinkMapper(applink, expectedDeeplink)
        assertEqualsDeeplinkParameters(applink, "extParam" to extParam.decodeToUtf8())
    }

    @Test
    fun `check product ar should return product ar internal`() {
        val productId = "890495024"
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_AR, productId)
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/productar/$productId/"

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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/new-order?tab_active=new_order"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_NEW_ORDER, expectedDeepLink)
    }

    @Test
    fun `check seller shipment appLink then should return tokopedia internal seller shipment in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/ready-to-ship?tab_active=confirm_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_SHIPMENT, expectedDeepLink)
    }

    @Test
    fun `check seller status appLink then should return tokopedia internal seller status in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/shipped?tab_active=in_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_STATUS, expectedDeepLink)
    }

    @Test
    fun `check seller history appLink then should return tokopedia internal seller history in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/history?tab_active=all_order"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check create shop appLink then should return tokopedia internal create shop in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/history?tab_active=all_order"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check reputation appLink then should return tokopedia internal reputation in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/review"
        assertEqualsDeepLinkMapper(ApplinkConst.REPUTATION, expectedDeepLink)
    }

    @Test
    fun `check product review appLink then should return tokopedia internal product review in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product/890495024/review"
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_REPUTATION, "890495024")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check webview parent home appLink then return webviewbackhome`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/webviewbackhome"
        assertEqualsDeepLinkMapper(ApplinkConst.WEBVIEW_PARENT_HOME, expectedDeepLink)
    }

    @Test
    fun `check tokopedia play-notif-video then return global youtube video`() {
        val query = "?video_url=aKtb7Y3qOck" +
            "&video_cta=a&" +
            "video_title=title&" +
            "video_desc_head=head&" +
            "videoDescKey=desc&" +
            "videoLandKey=land"
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/youtube-video$query"
        assertEqualsDeepLinkMapper(ApplinkConst.PLAY_NOTIFICATION_VIDEO + query, expectedDeepLink)
    }

    @Test
    fun `check product talk appLink then should return tokopedia internal product talk in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/product-talk/890495024/"
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_TALK, "890495024")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check digital appLink then should return tokopedia internal digital in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL, expectedDeepLink)
    }

    @Test
    fun `check old digital form appLink then should return tokopedia internal digital subhome in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://recharge/home/dynamic?platform_id=31"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_PRODUCT, expectedDeepLink)
    }

    @Test
    fun `check digital telkomsel omni form appLink then should return tokopedia internal digital omni checkout in customerapp`() {
        val deeplink = "${ApplinkConst.TELKOMSEL_OMNI}?kb=43921686"
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://digital/checkout?category_id=54&operator_id=7654&client_number=43921686&product_id=20159&is_from_widget=true&kb=43921686"
        assertEqualsDeepLinkMapper(deeplink, expectedDeepLink)
    }

    @Test
    fun `check digital form appLink then should return tokopedia internal digital general in customerapp`() {
        val deeplink = "${ApplinkConst.DIGITAL_PRODUCT}?category_id=1&menu_id=1&template=general"
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://digital/general?category_id=1&menu_id=1&template=general"
        assertEqualsDeepLinkMapper(deeplink, expectedDeepLink)
    }

    @Test
    fun `check digital subhome page appLink then should return tokopedia internal recharge home in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://recharge/home/dynamic?platform_id=53&personalize=false"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME, expectedDeepLink)
    }

    @Test
    fun `check digital smartcard appLink then should return tokopedia internal recharge home in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://digital/smartcard/emoney?calling_page_check_saldo="
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_SMARTCARD, expectedDeepLink)
    }

    @Test
    fun `check digital smartcard appLink then should return tokopedia internal digital smartcard in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://digital/smartcard/emoney?calling_page_check_saldo="
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery/pengguna-baru"
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
        val expected = "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery/deals"
        assertEqualsDeepLinkMapper(ApplinkConst.PROMO, expected)
        assertEqualsDeepLinkMapper(ApplinkConst.PROMO_LIST, expected)
    }

    @Test
    fun `check promo with dash appLink then should return promo detail in customerapp`() {
        val appLink = UriUtil.buildUri(ApplinkConst.PROMO_WITH_DASH, "abc")
        val expected = UriUtil.buildUri(ApplinkConstInternalPromo.PROMO_DETAIL, "abc")
        assertEqualsDeepLinkMapper(appLink, expected)
    }

    @Test
    fun `check promo detail appLink then should return promo detail in customerapp`() {
        val appLink = UriUtil.buildUri(ApplinkConst.PROMO_DETAIL, "abc")
        val expected = UriUtil.buildUri(ApplinkConstInternalPromo.PROMO_DETAIL, "abc")
        assertEqualsDeepLinkMapper(appLink, expected)
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
    fun `check search universal search page applink then should return tokopedia internal universal search in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://discovery/universal"
        assertEqualsDeepLinkMapper(ApplinkConst.DISCOVERY_SEARCH_UNIVERSAL, expectedDeepLink)
    }

    @Test
    fun `check hotlist detail appLink then should return tokopedia internal hotlist detail in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://discovery/search-result?q=baju"
        val appLink = UriUtil.buildUri(ApplinkConst.DISCOVERY_HOTLIST_DETAIL, "baju")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check category appLink then should return tokopedia internal category in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://catalog"
        assertEqualsDeepLinkMapper(ApplinkConst.DISCOVERY_CATALOG, expectedDeepLink)
    }

    @Test
    fun `check epharmacy appLink then should return tokopedia internal category in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://epharmacy"
        assertEqualsDeepLinkMapper(ApplinkConst.EPHARMACY, expectedDeepLink)
    }

    @Test
    fun `check payment back to default appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PAYMENT_BACK_TO_DEFAULT, "")
    }

    @Test
    fun `check wishlist appLink then should return tokopedia internal wishlist collection in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://transaction/wishlist-collection"
        assertEqualsDeepLinkMapper(ApplinkConst.WISHLIST, expectedDeepLink)
    }

    @Test
    fun `check new wishlist appLink then should return tokopedia internal wishlist collection in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://transaction/wishlist-collection"
        assertEqualsDeepLinkMapper(ApplinkConst.NEW_WISHLIST, expectedDeepLink)
    }

    @Test
    fun `check external wishlist collection detail appLink then should return tokopedia internal marketplace`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://transaction/wishlist/collection/{collection_id}/"
        assertEqualsDeepLinkMapper(ApplinkConst.WISHLIST_COLLECTION_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check external wishlist collection detail appLink with affiliate unique id then should return tokopedia internal marketplace`() {
        val affUniqueId = "sSAJfRm6Gujkdy7kR8EXYhJyRl7f0Xoq-K9geactpYaJiwQWbQ1tlTg4jnDsiw=="
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://transaction/wishlist/collection/{collection_id}/?aff_unique_id=$affUniqueId"
        assertEqualsDeepLinkMapper(ApplinkConst.WISHLIST_COLLECTION_DETAIL + "?aff_unique_id=$affUniqueId", expectedDeepLink)
    }

    @Test
    fun `check login appLink then should return tokopedia internal login in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/login"
        assertEqualsDeepLinkMapper(ApplinkConst.LOGIN, expectedDeepLink)
    }

    @Test
    fun `check otp appLink then should return tokopedia internal otp in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/cotp"
        assertEqualsDeepLinkMapper(ApplinkConst.OTP, expectedDeepLink)
    }

    @Test
    fun `check otp verify appLink then should return tokopedia internal otp in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/otp-push-notif-receiver"
        assertEqualsDeepLinkMapper(ApplinkConst.OTP_PUSH_NOTIF_RECEIVER, expectedDeepLink)
    }

    @Test
    fun `check official stores appLink then should return tokopedia internal official stores in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery/sos"
        assertEqualsDeepLinkMapper(ApplinkConst.OFFICIAL_STORES, expectedDeepLink)
    }

    @Test
    fun `check official store appLink then should return tokopedia internal official store in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery/sos"
        assertEqualsDeepLinkMapper(ApplinkConst.OFFICIAL_STORE, expectedDeepLink)
    }

    @Test
    fun `check official store category appLink then should return tokopedia internal official store category in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/discovery/sos"
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
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/topchat/12345"
        val appLink = UriUtil.buildUri(ApplinkConst.TOPCHAT, "12345")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check topchat appLink then should return tokopedia internal topchat in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/topchat"
        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, ApplinkConst.TOP_CHAT, expectedDeepLink)
    }

    @Test
    fun `check topchat appLink then should return tokopedia internal topchat in sellerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-chat"
        assertEqualsDeepLinkMapperApp(AppType.SELLER_APP, ApplinkConst.TOP_CHAT, expectedDeepLink)
    }

    @Test
    fun `check topchat old appLink then should return tokopedia internal topchat old in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/topchat"
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/chat/settings/templatechat"
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/official-store/brand/0/"
        assertEqualsDeepLinkMapper(ApplinkConst.BRAND_LIST, expectedDeepLink)
    }

    @Test
    fun `check brand list with slash appLink then should return tokopedia internal brand list with slash in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/official-store/brand/0/"
        assertEqualsDeepLinkMapper(ApplinkConst.BRAND_LIST, expectedDeepLink)
    }

    @Test
    fun `check register appLink then should return empty in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/init-register"
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://people/123456?success_post=true"
        val appLink = UriUtil.buildUri(ApplinkConst.PROFILE_SUCCESS_POST, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check profile settings appLink then should return tokopedia internal profile settings in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://people/settings/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.PROFILE_SETTINGS, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check profile completion appLink then should return tokopedia internal profile completion in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/profile-completion"
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
        assertEqualsDeepLinkMapper(ApplinkConst.PAYLATER + "?q=123", expectedDeepLink)
    }

    @Test
    fun `check home credit register ktp query then should return tokopedia internal`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://fintech/home-credit-register?show_ktp=true"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_CREDIT_KTP_WITHOUT_TYPE, expectedDeepLink)
    }

    @Test
    fun `check home credit register ktp with type query then should return tokopedia internal`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://fintech/home-credit-register?show_ktp=true&type=KTP1"
        val deeplink = UriUtil.buildUri(ApplinkConst.HOME_CREDIT_KTP_WITH_TYPE, "KTP1")
        assertEqualsDeepLinkMapper(deeplink, expectedDeepLink)
    }

    @Test
    fun `check home credit register selfie query then should return tokopedia internal`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://fintech/home-credit-register?show_ktp=false"
        assertEqualsDeepLinkMapper(ApplinkConst.HOME_CREDIT_SELFIE_WITHOUT_TYPE, expectedDeepLink)
    }

    @Test
    fun `check home credit register selfie with type query then should return tokopedia internal`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://fintech/home-credit-register?show_ktp=false&type=SLFE1"
        val deeplink = UriUtil.buildUri(ApplinkConst.HOME_CREDIT_SELFIE_WITH_TYPE, "SLFE1")
        assertEqualsDeepLinkMapper(deeplink, expectedDeepLink)
    }

    @Test
    fun `check profile completion then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/profile-completion"
        assertEqualsDeepLinkMapper(ApplinkConst.PROFILE_COMPLETION, expectedDeepLink)
    }

    @Test
    fun `check feedback form then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/internal-feedback"
        assertEqualsDeepLinkMapper(ApplinkConst.FEEDBACK_FORM, expectedDeepLink)
    }

    @Test
    fun `check register init then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/init-register"
        assertEqualsDeepLinkMapper(ApplinkConst.REGISTER_INIT, expectedDeepLink)
    }

    @Test
    fun `check gold merchant statistic dashboard appLink then should return tokopedia internal gold merchant statistic dashboard in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/statistic_dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD, expectedDeepLink)
    }

    @Test
    fun `check shop score detail appLink then should return tokopedia internal shop score detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop/performance"
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_SCORE_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check shop score detail appLink with coachmark param then should return tokopedia internal shop score performance with coachmark param in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop/performance?coachmark=disabled"
        val coachMarkParam = mapOf("coachmark" to "disabled")
        val actualDeeplink =
            UriUtil.buildUriAppendParam(ApplinkConst.SHOP_SCORE_DETAIL, coachMarkParam)
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check shop admin invitation appLink then should return tokopedia internal shop admin invitation in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/invitation-page"
        assertEqualsDeepLinkMapper(ApplinkConst.ADMIN_INVITATION, expectedDeepLink)
    }

    @Test
    fun `check shop admin redirection appLink then should return tokopedia internal shop admin redirection in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/redirection-page"
        assertEqualsDeepLinkMapper(ApplinkConst.ADMIN_REDIRECTION, expectedDeepLink)
    }

    @Test
    fun `check shop admin accepted appLink with shop name param then should return tokopedia internal shop admin accepted with shop name param in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/accepted-page?shop_name=toko"
        val shopNameParam = mapOf("shop_name" to "toko")
        val actualDeeplink = UriUtil.buildUriAppendParam(ApplinkConst.ADMIN_ACCEPTED, shopNameParam)
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check shop admin accepted appLink then should return tokopedia internal shop admin accepted in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-admin/accepted-page"
        assertEqualsDeepLinkMapper(ApplinkConst.ADMIN_ACCEPTED, expectedDeepLink)
    }

    @Test
    fun `check shop penalty appLink then should return tokopedia internal shop penalty in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-penalty-old"
        assertEqualsDeepLinkMapper(ApplinkConst.SHOP_PENALTY, expectedDeepLink)
    }

    @Test
    fun `check events appLink then should return tokopedia internal events in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://event/home"
        assertEqualsDeepLinkMapper(ApplinkConst.EVENTS, expectedDeepLink)
    }

    @Test
    fun `check digital order appLink then should return tokopedia internal digital order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=digital"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_ORDER, expectedDeepLink)
    }

    @Test
    fun `check events order appLink then should return tokopedia internal events order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=events"
        assertEqualsDeepLinkMapper(ApplinkConst.EVENTS_ORDER, expectedDeepLink)
    }

    @Test
    fun `check deals order appLink then should return tokopedia internal deals order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=deals"
        assertEqualsDeepLinkMapper(ApplinkConst.DEALS_ORDER, expectedDeepLink)
    }

    @Test
    fun `check flight order appLink then should return tokopedia internal flight order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=pesawat"
        assertEqualsDeepLinkMapper(ApplinkConst.FLIGHT_ORDER, expectedDeepLink)
    }

    @Test
    fun `check train order appLink then should return tokopedia internal train order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=kereta"
        assertEqualsDeepLinkMapper(ApplinkConst.TRAIN_ORDER, expectedDeepLink)
    }

    @Test
    fun `check gift cards order appLink then should return tokopedia internal gift cards order in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=giftcards"
        assertEqualsDeepLinkMapper(ApplinkConst.GIFT_CARDS_ORDER, expectedDeepLink)
    }

    @Test
    fun `check insurance order appLink then should return tokopedia internal insurance order in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=insurance"
        assertEqualsDeepLinkMapper(ApplinkConst.INSURANCE_ORDER, expectedDeepLink)
    }

    @Test
    fun `check modal toko order appLink then should return tokopedia internal modal toko order in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=modaltoko"
        assertEqualsDeepLinkMapper(ApplinkConst.MODAL_TOKO_ORDER, expectedDeepLink)
    }

    @Test
    fun `check Plus order appLink then should return tokopedia internal Plus order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=plus"
        assertEqualsDeepLinkMapper(ApplinkConst.TOKOPEDIA_PLUS_ORDER, expectedDeepLink)
    }

    @Test
    fun `check hotel order appLink then should return tokopedia internal hotel order in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=hotel"
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace"
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_ORDER, expectedDeepLink)
    }

    @Test
    fun `check belanja order appLink then should return tokopedia internal belanja order in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace"
        assertEqualsDeepLinkMapper(ApplinkConst.BELANJA_ORDER, expectedDeepLink)
    }

    @Test
    fun `check marketplace order sub appLink then should return tokopedia internal marketplace order sub in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace_dalam_proses"
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_ORDER_SUB, expectedDeepLink)
    }

    @Test
    fun `check marketplace waiting confirmation appLink then should return tokopedia internal marketplace waiting confirmation in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_waiting_confirmation"
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION, expectedDeepLink)
    }

    @Test
    fun `check marketplace sent appLink then should return tokopedia internal marketplace sent in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_sent"
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_SENT, expectedDeepLink)
    }

    @Test
    fun `check marketplace order processed appLink then should return tokopedia internal marketplace order processed in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_processed"
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_ORDER_PROCESSED, expectedDeepLink)
    }

    @Test
    fun `check marketplace delivered appLink then should return tokopedia internal marketplace delivered in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_delivered"
        assertEqualsDeepLinkMapper(ApplinkConst.MARKETPLACE_DELIVERED, expectedDeepLink)
    }

    @Test
    fun `check buyer notif appLink then should return tokopedia internal buyer notif in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/inbox?page=notification&show_bottom_nav=false"
        assertEqualsDeepLinkMapper(ApplinkConst.BUYER_INFO, expectedDeepLink)
    }

    @Test
    fun `check seller info detail appLink then should return tokopedia internal seller info detail in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/inbox?page=notification&show_bottom_nav=false"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_INFO_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check seller info detail with url appLink then should return webview`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/webview?url=https://www.tokopedia.com/help"
        assertEqualsDeepLinkMapper(
            ApplinkConst.SELLER_INFO_DETAIL + "?url=https://www.tokopedia.com/help",
            expectedDeepLink
        )
    }

    @Test
    fun `check inbox ticket appLink then should return tokopedia internal inbox ticket in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://customercare/inbox-list"
        assertEqualsDeepLinkMapper(ApplinkConst.INBOX_TICKET, expectedDeepLink)
    }

    @Test
    fun `check shipping tracking appLink then should return tokopedia internal ticket detail in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/shipping/tracking/123456"
        val appLink = UriUtil.buildUri(ApplinkConst.ORDER_TRACKING, "123456")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check address list appLink then should return tokopedia internal manage address in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/manageaddress/"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_ADDRESS, expectedDeepLink)
    }

    @Test
    fun `check pod appLink then should return tokopedia internal proof of delivery in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/shipping/pod/123456?image_id=22222"
        val appLink = UriUtil.buildUri(ApplinkConst.ORDER_POD, "123456?image_id=22222")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check share address appLink then should return tokopedia internal logistic manage address in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/manageaddress/"
        val appLink = UriUtil.buildUri(ApplinkConst.SHARE_ADDRESS)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check flight appLink then should return tokopedia internal ticket detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://pesawat/dashboard"
        assertEqualsDeepLinkMapper(ApplinkConst.FLIGHT, expectedDeepLink)
    }

    @Test
    fun `check product manage appLink then should return tokopedia internal product manage in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/product-manage-list?filter=123"
        assertEqualsDeepLinkMapper(ApplinkConst.PRODUCT_MANAGE + "?filter=123", expectedDeepLink)
    }

    @Test
    fun `check product manage redirect appLink then should return tokopedia internal product manage`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-product-list?redirect_to_sellerapp=true"
        assertEqualsDeepLinkMapper(
            ApplinkConst.PRODUCT_MANAGE + "?redirect_to_sellerapp=true",
            expectedDeepLink
        )
    }

    @Test
    fun `check product manage from sellerapp appLink then should return tokopedia internal product manage`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/sellerhome-product-list"
        assertEqualsDeepLinkMapperApp(
            AppType.SELLER_APP,
            ApplinkConst.PRODUCT_MANAGE,
            expectedDeepLink
        )
    }

    @Test
    fun `check product edit appLink then should return tokopedia internal product edit in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/open-product-preview?id=123445&mode=edit-product"
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_EDIT, "123445")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check seller center appLink then should return to seller center webview in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/webview?url=https://seller.tokopedia.com/edu/"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_CENTER, expectedDeepLink)
    }

    @Test
    fun `check seller shipping editor appLink then should return tokopedia internal seller shipping editor in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-settings-shipping"
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
        assertEqualsDeepLinkMapper(ApplinkConst.ORDER_LIST, expectedDeepLink)
    }

    @Test
    fun `check order list webview appLink then should return tokopedia internal order list webview in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified"
        assertEqualsDeepLinkMapper(ApplinkConst.ORDER_LIST_WEBVIEW, expectedDeepLink)
    }

    @Test
    fun `check tokopoints appLink then should return tokopedia internal tokopoints in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://rewards/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TOKOPOINTS, expectedDeepLink)
    }

    @Test
    fun `check rewards appLink then should return tokopedia internal rewards in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://rewards/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TOKOPEDIA_REWARD, expectedDeepLink)
    }

    @Test
    fun `check coupon listing appLink then should return tokopedia internal coupon listing in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://rewards/kupon-saya"
        assertEqualsDeepLinkMapper(ApplinkConst.COUPON_LISTING, expectedDeepLink)
    }

    @Test
    fun `check developer options appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.DEVELOPER_OPTIONS, "")
    }

    @Test
    fun `check setting profile appLink then should return tokopedia internal setting profile in customerapp`() {
        val settingProfileApplink = ApplinkConst.SETTING_PROFILE
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/setting-profile"
        assertEqualsDeepLinkMapper(settingProfileApplink, expectedDeepLink)
    }

    @Test
    fun `check notification troubleshooter appLink then should return tokopedia internal notification troubleshooter in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/push-notification-troubleshooter"
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
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_ORDER, expectedDeepLink)
    }

    @Test
    fun `check purchase confirmed appLink then should return tokopedia internal purchase confirmed in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_waiting_confirmation"
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_CONFIRMED, expectedDeepLink)
    }

    @Test
    fun `check purchase processed appLink then should return tokopedia internal purchase processed in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_processed"
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_PROCESSED, expectedDeepLink)
    }

    @Test
    fun `check shipping confirm appLink then should return tokopedia internal shipping confirm in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=marketplace_dalam_proses"
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_SHIPPING_CONFIRM, expectedDeepLink)
    }

    @Test
    fun `check shipped appLink then should return tokopedia internal shipped in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_sent"
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_SHIPPED, expectedDeepLink)
    }

    @Test
    fun `check delivered appLink then should return tokopedia internal delivered in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_delivered"
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_DELIVERED, expectedDeepLink)
    }

    @Test
    fun `check purchase history appLink then should return tokopedia internal purchase history in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified"
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_HISTORY, expectedDeepLink)
    }

    @Test
    fun `check ongoing appLink then should return tokopedia internal ongoing in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://order/unified?filter=uoh_ongoing"
        assertEqualsDeepLinkMapper(ApplinkConst.PURCHASE_ONGOING, expectedDeepLink)
    }

    @Test
    fun `check ready to ship appLink then should return tokopedia internal ready to ship in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/ready-to-ship?tab_active=confirm_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP, expectedDeepLink)
    }

    @Test
    fun `check seller shipped appLink then should return tokopedia internal seller shipped in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/shipped?tab_active=in_shipping"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_SHIPPED, expectedDeepLink)
    }

    @Test
    fun `check seller delivered appLink then should return tokopedia internal seller delivered in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/delivered?tab_active=in_shipping&tab_status=delivered"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_DELIVERED, expectedDeepLink)
    }

    @Test
    fun `check seller canceled appLink then should return tokopedia internal seller canceled in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/cancelled?tab_active=order_canceled"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_CANCELED, expectedDeepLink)
    }

    @Test
    fun `check seller cancellation request appLink then should return tokopedia internal seller cancellation request in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/cancellationrequest?tab_active=all_order&filter_order_type=10"
        assertEqualsDeepLinkMapper(
            ApplinkConst.SELLER_PURCHASE_CANCELLATION_REQUEST,
            expectedDeepLink
        )
    }

    @Test
    fun `check seller waiting pickup appLink then should return tokopedia internal seller waiting pickup in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/waiting-pickup?tab_active=&filter_status_id=7"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_WAITING_PICKUP, expectedDeepLink)
    }

    @Test
    fun `check seller waiting awb appLink then should return tokopedia internal seller waiting awb in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/waiting-awb?tab_active=&filter_status_id=9"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_WAITING_AWB, expectedDeepLink)
    }

    @Test
    fun `check seller awb invalid appLink then should return tokopedia internal seller awb invalid in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/awb-invalid?tab_active=&filter_status_id=10"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_AWB_INVALID, expectedDeepLink)
    }

    @Test
    fun `check seller awb change appLink then should return tokopedia internal seller awb change in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/awb-change?tab_active=&filter_status_id=11"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_AWB_CHANGE, expectedDeepLink)
    }

    @Test
    fun `check seller purchase retur appLink then should return tokopedia internal seller purchase retur in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/retur?tab_active=&filter_status_id=13"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_RETUR, expectedDeepLink)
    }

    @Test
    fun `check seller purchase complaint appLink then should return tokopedia internal seller purchase complaint in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/complaint?tab_active=&filter_status_id=15"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_COMPLAINT, expectedDeepLink)
    }

    @Test
    fun `check seller purchase finished appLink then should return tokopedia internal seller purchase finished in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/finished?tab_active=done"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_PURCHASE_FINISHED, expectedDeepLink)
    }

    @Test
    fun `check seller order appLink then should return tokopedia internal seller order in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://seller/order?order_id=1234567890"
        val appLink = "${ApplinkConst.SELLER_ORDER_DETAIL}/1234567890"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check reschedule pickup applink then should return tokopedia internal reschedule pickup in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/reschedulepickup?order_id=1234567890"
        val appLink = "${ApplinkConst.LOGISTIC_SELLER_RESCHEDULE}?order_id=1234567890"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check edit address applink then should return tokopedia internal edit address revamp in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/editaddressrevamp/1234567890"
        val appLink = "${ApplinkConst.SETTING_EDIT_ADDRESS}1234567890"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check saldo appLink then should return tokopedia internal saldo in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/saldo"
        assertEqualsDeepLinkMapper(ApplinkConst.SALDO, expectedDeepLink)
    }

    @Test
    fun `check main nav appLink then should return tokopedia internal`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://navigation/main"
        assertEqualsDeepLinkMapper(ApplinkConst.Navigation.MAIN_NAV, expectedDeepLink)
    }

    @Test
    fun `create shop`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/landing-shop-creation"
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
    fun `check has password appLink then should return tokopedia internal has password in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/has-password"
        assertEqualsDeepLinkMapper(ApplinkConst.HAS_PASSWORD, expectedDeepLink)
    }

    @Test
    fun `check setting bank appLink then should return tokopedia internal setting bank in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/setting-bank"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_BANK, expectedDeepLink)
    }

    @Test
    fun `check contact us appLink then should return contact us native applink in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.CONTACT_US, ApplinkConst.CONTACT_US_NATIVE)
    }

    @Test
    fun `check contact us native appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.CONTACT_US_NATIVE, "")
    }

    @Test
    fun `check setting notification appLink then should return tokopedia internal setting notification in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/user-notification-setting"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_NOTIFICATION, expectedDeepLink)
    }

    @Test
    fun `check content explore appLink then should return tokopedia internal content explore in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://home/navigation?category_id=2&tab_name=updates&TAB_POSITION=1"
        val appLink = UriUtil.buildUri(ApplinkConst.CONTENT_EXPLORE, "updates", "2")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check content detail appLink then should return tokopedia internal content detail in customerapp`() {
        val expectedDeepLink =
            "${ApplinkConsInternalHome.HOME_NAVIGATION}?TAB_POSITION=1&FEED_TAB_POSITION=0&ARGS_FEED_SOURCE_ID=123"
        val appLink = UriUtil.buildUri(ApplinkConst.CONTENT_DETAIL, "123")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check play detail appLink then should return tokopedia internal play detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://play/1234"
        val appLink = UriUtil.buildUri(ApplinkConst.PLAY_DETAIL, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check play recom appLink then should return tokopedia internal play recom in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://play/channel_recom"
        val appLink = UriUtil.buildUri(ApplinkConst.PLAY_RECOM)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check play recom with sourceType appLink then should return tokopedia internal play recom with sourceType in customerapp`() {
        val additionalParam = "?source_type=1"
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://play/channel_recom$additionalParam"
        val appLink = UriUtil.buildUri(ApplinkConst.PLAY_RECOM + additionalParam)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check play broadcaster appLink then should return tokopedia internal play detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://play-broadcaster"
        assertEqualsDeepLinkMapper(ApplinkConst.PLAY_BROADCASTER, expectedDeepLink)
    }

    @Test
    fun `check add name profile appLink then should return tokopedia internal add name profile in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/manage-name"
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_NAME_PROFILE, expectedDeepLink)
    }

    @Test
    fun `check reset passwoord appLink then should return tokopedia internal reset password in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/forgot-password"
        assertEqualsDeepLinkMapper(ApplinkConst.RESET_PASSWORD, expectedDeepLink)
    }

    @Test
    fun `check phone verification appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.PHONE_VERIFICATION, "")
    }

    @Test
    fun `check change inactive phone appLink then should return tokopedia internal phone change inactive phone in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/change-inactive-phone"
        assertEqualsDeepLinkMapper(ApplinkConst.CHANGE_INACTIVE_PHONE, expectedDeepLink)
    }

    @Test
    fun `check privacy center appLink then should return tokopedia internal privacy center in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/privacy-center"

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(DeeplinkMapperUser.ROLLENCE_PRIVACY_CENTER)
        } returns DeeplinkMapperUser.ROLLENCE_PRIVACY_CENTER

        assertEqualsDeepLinkMapper(ApplinkConst.PRIVACY_CENTER, expectedDeepLink)
    }

    @Test
    fun `check privacy center appLink then should return tokopedia internal home navigation in customerapp`() {
        val expectedDeepLink = ApplinkConsInternalHome.HOME_NAVIGATION
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(DeeplinkMapperUser.ROLLENCE_PRIVACY_CENTER)
        } returns ""

        assertEqualsDeepLinkMapper(ApplinkConst.PRIVACY_CENTER, expectedDeepLink)
    }

    @Test
    fun `check DSAR appLink then should return tokopedia internal DSAR in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/dsar"
        assertEqualsDeepLinkMapper(ApplinkConst.User.DSAR, expectedDeepLink)
    }

    @Test
    fun `check add pin onboarding appLink then should return tokopedia internal add pin onboarding in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/add-pin-onboarding"
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_PIN_ONBOARD, expectedDeepLink)
    }

    @Test
    fun `check goto kyc param global appLink when rollence activated then should return tokopedia internal goto kyc in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/goto-kyc?projectId=7&source=Account&callback=url"
        val appLink = "tokopedia://goto-kyc?projectId=7&source=Account&callback=url"

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns DeeplinkMapperUser.ROLLENCE_GOTO_KYC
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getFilteredKeyByKeyName(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns mutableSetOf(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)

        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check goto kyc param global appLink when rollence deactivated then should return tokopedia internal toko kyc in customerapp`() {
        val expectedDeepLink =
            "${ApplinkConstInternalUserPlatform.KYC_INFO_BASE}?projectId=7&source=Account&callback=url"
        val appLink = "tokopedia://goto-kyc?projectId=7&source=Account&callback=url"

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns ""
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getFilteredKeyByKeyName(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns mutableSetOf(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)

        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check goto kyc param global appLink when failed fetch rollence then should return tokopedia internal goto kyc in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/goto-kyc?projectId=7&source=Account&callback=url"
        val appLink = "tokopedia://goto-kyc?projectId=7&source=Account&callback=url"

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getFilteredKeyByKeyName(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns mutableSetOf()

        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check goto kyc param appLink when rollence activated then should return tokopedia internal goto kyc in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/goto-kyc?projectId=7&source=Account&callback=url"
        val appLink =
            "tokopedia-android-internal://user/goto-kyc?projectId=7&source=Account&callback=url"

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns DeeplinkMapperUser.ROLLENCE_GOTO_KYC
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getFilteredKeyByKeyName(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns mutableSetOf(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)

        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check goto kyc param appLink when rollence deactivated then should return tokopedia internal toko kyc in customerapp`() {
        val expectedDeepLink =
            "${ApplinkConstInternalUserPlatform.KYC_INFO_BASE}?projectId=7&source=Account&callback=url"
        val appLink =
            "tokopedia-android-internal://user/goto-kyc?projectId=7&source=Account&callback=url"

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns ""
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getFilteredKeyByKeyName(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns mutableSetOf(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)

        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check goto kyc param appLink when failed fetch rollence then should return tokopedia internal goto kyc in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/goto-kyc?projectId=7&source=Account&callback=url"
        val appLink = "tokopedia-android-internal://user/goto-kyc?projectId=7&source=Account&callback=url"

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getFilteredKeyByKeyName(DeeplinkMapperUser.ROLLENCE_GOTO_KYC)
        } returns mutableSetOf()

        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, appLink, expectedDeepLink)
    }

    @Test
    fun `check kyc no param appLink then should return tokopedia internal kyc in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/user-identification-info"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC_NO_PARAM, expectedDeepLink)
    }

    @Test
    fun `check kyc param appLink then should return tokopedia internal kyc in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/user-identification-info?projectId=1"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC_NO_PARAM + "?projectId=1", expectedDeepLink)
    }

    @Test
    fun `check kyc appLink then appLink should return tokopedia internal kyc in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/user-identification-info?projectId=1"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC, expectedDeepLink)
    }

    @Test
    fun `check kyc form no param appLink then should return tokopedia internal kyc form in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/user-identification-form"
        assertEqualsDeepLinkMapper(ApplinkConst.KYC_FORM_NO_PARAM, expectedDeepLink)
    }

    @Test
    fun `check kyc form appLink then should return tokopedia internal kyc form in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/user-identification-form?projectId=12345"
        val appLink = UriUtil.buildUri(ApplinkConst.KYC_FORM, "12345")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check smc referral appLink then should return empty in customerapp`() {
        assertEqualsDeepLinkMapper(ApplinkConst.SMC_REFERRAL, "")
    }

    @Test
    fun `check power merchant subscribe appLink then should return tokopedia internal pmc in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/power-merchant-subscribe"
        every {
            PowerMerchantDeepLinkMapper.isEnablePMSwitchToWebView(context)
        } returns false
        every {
            PowerMerchantDeepLinkMapper.isLoginAndHasShop(context)
        } returns true
        assertEqualsDeepLinkMapper(ApplinkConst.POWER_MERCHANT_SUBSCRIBE, expectedDeepLink)
    }

    @Test
    fun `check pm benefit package appLink then should return tokopedia internal pm benefit package in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/pm-benefit-package"
        assertEqualsDeepLinkMapper(ApplinkConst.PM_BENEFIT_PACKAGE, expectedDeepLink)
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
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://deals/brand-detail-new?12345"
        val appLink = UriUtil.buildUri(ApplinkConst.DEALS_BRAND_DETAIL, "12345")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check salam umroh detail appLink then should return tokopedia internal salam umroh detail in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://order-details/umroh"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL, expectedDeepLink)
    }

    @Test
    fun `check salam umroh appLink then should return tokopedia internal salam umroh in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://s/umroh"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH, expectedDeepLink)
    }

    @Test
    fun `check salam umroh pdp appLink then should return tokopedia internal salam umroh pdp in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://s/umroh/produk/1234"
        val appLink = UriUtil.buildUri(ApplinkConst.SALAM_UMRAH_PDP, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check salam umroh shop appLink then should return tokopedia internal salam umroh shop in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://s/umroh"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH_SHOP, expectedDeepLink)
    }

    @Test
    fun `check thank you page native appLink then should return tokopedia internal thank you page native in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://payment/thankyou"
        assertEqualsDeepLinkMapper(ApplinkConst.THANK_YOU_PAGE_NATIVE, expectedDeepLink)
    }

    @Test
    fun `check salam umroh agen appLink then should return tokopedia internal salam umroh agen in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://s/umroh/agen/1234"
        val appLink = UriUtil.buildUri(ApplinkConst.SALAM_UMRAH_AGEN, "1234")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check salam umroh list agen appLink then should return tokopedia internal salam umroh list agen in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://s/umroh/agen"
        assertEqualsDeepLinkMapper(ApplinkConst.SALAM_UMRAH_LIST_AGEN, expectedDeepLink)
    }

    @Test
    fun `check merchant voucher list appLink then should return tokopedia internal merchant voucher list in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/seller-mvc/list/active/"
        assertEqualsDeepLinkMapper(ApplinkConst.MERCHANT_VOUCHER_LIST, expectedDeepLink)
    }

    @Test
    fun `check occ appLink then should return tokopedia internal occ in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/one-click-checkout"
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/gamification_gift_daily"
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
    fun `check old digital product appLink then should return tokopedia internal digital subhome in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://recharge/home/dynamic?platform_id=31"
        assertEqualsDeepLinkMapper(ApplinkConst.DIGITAL_PRODUCT, expectedDeepLink)
    }

    @Test
    fun `check digital product appLink then should return tokopedia internal digital product in customerapp`() {
        val deeplink = "${ApplinkConst.DIGITAL_PRODUCT}?category_id=1&menu_id=1&template=general"
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://digital/general?category_id=1&menu_id=1&template=general"
        assertEqualsDeepLinkMapper(deeplink, expectedDeepLink)
    }

    @Test
    fun `check tokopoints homepage appLink then should return tokopedia internal tokopoints home page in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://rewards/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.HOMEPAGE_REWARD1, expectedDeepLink)
    }

    @Test
    fun `check tokopoints homepage reward1 appLink then should return tokopedia internal tokopoints homepage rewards1 in customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://rewards/home"
        assertEqualsDeepLinkMapper(ApplinkConst.TokoPoints.HOMEPAGE_REWARD1, expectedDeepLink)
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
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://snapshot/order?order_id=1234&order_detail_id=7890"
        val appLink = UriUtil.buildUri(ApplinkConst.SNAPSHOT_ORDER + "/1234/7890")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop follower list appLink then should return tokopedia internal shop follower list in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/shop-favourites"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_FOLLOWER_LIST, "1479278")
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop setting appLink then should return tokopedia internal shop setting in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/shop-page/1479278/settings"
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
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/qr-login"
        assertEqualsDeepLinkMapper(ApplinkConst.QR_LOGIN, expectedDeepLink)
    }

    @Test
    fun `check marketplace order detail appLink then should return revamped tokopedia internal marketplace order detail in customerapp`() {
        val orderId = "123456789"
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/buyer-order-detail?order_id=$orderId"
        assertEqualsDeepLinkMapper("${ApplinkConst.MARKETPLACE_ORDER}/$orderId", expectedDeepLink)
    }

    @Test
    fun `check marketplace buyer order extension appLink then should return tokopedia internal marketplace buyer order extension in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/buyer-order-extension"
        assertEqualsDeepLinkMapper(ApplinkConst.BUYER_ORDER_EXTENSION, expectedDeepLink)
    }

    @Test
    fun `check marketplace buyer partial order fulfillment appLink then should return tokopedia internal marketplace buyer partial order fulfillment in customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/buyer-partial-order-fulfillment"
        assertEqualsDeepLinkMapper(ApplinkConst.BUYER_PARTIAL_ORDER_FULFILLMENT, expectedDeepLink)
    }

    @Test
    fun `check tokonow home appLink then should return tokopedia internal tokonow home in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokopediaNow.HOME
        val actualDeeplink = ApplinkConst.TokopediaNow.HOME
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check tokonow repurchase appLink then should return tokopedia internal tokonow repurchase in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokopediaNow.REPURCHASE
        val actualDeeplink = ApplinkConst.TokopediaNow.REPURCHASE
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check tokonow search appLink then should return tokopedia internal tokonow search in customerapp`() {
        val queryParams = "?q=keju"
        val expectedDeepLink = ApplinkConstInternalTokopediaNow.SEARCH + queryParams
        val actualDeeplink = ApplinkConst.TokopediaNow.SEARCH + queryParams
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check tokonow L1 L2 category appLink then should return tokopedia internal tokonow category in customerapp`() {
        val categoryIdL1 = "123"
        val categoryIdL2 = "456"
        val expectedDeepLink =
            "${ApplinkConstInternalTokopediaNow.OLD_CATEGORY}?category_l1=$categoryIdL1&category_l2=$categoryIdL2"
        val appLink = "${ApplinkConst.TokopediaNow.OLD_CATEGORY}/$categoryIdL1/$categoryIdL2"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow L1 category appLink then should return tokopedia internal tokonow category in customerapp`() {
        val categoryIdL1 = "123"
        val expectedDeepLink =
            "${ApplinkConstInternalTokopediaNow.OLD_CATEGORY}?category_l1=$categoryIdL1"
        val appLink = "${ApplinkConst.TokopediaNow.OLD_CATEGORY}/$categoryIdL1"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow L1 category main appLink then should return tokopedia internal tokonow category in customerapp`() {
        val categoryIdL1 = "123"
        val expectedDeepLink =
            "${ApplinkConstInternalTokopediaNow.CATEGORY}?category_l1=$categoryIdL1"
        val appLink = "${ApplinkConst.TokopediaNow.CATEGORY}/$categoryIdL1"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow L1 L2 category appLink with query param then should return tokopedia internal tokonow category in customerapp`() {
        val categoryIdL1 = "123"
        val categoryIdL2 = "456"
        val queryParam = "official=true"
        val expectedDeepLink =
            "${ApplinkConstInternalTokopediaNow.OLD_CATEGORY}?category_l1=$categoryIdL1&category_l2=$categoryIdL2&$queryParam"
        val appLink =
            "${ApplinkConst.TokopediaNow.OLD_CATEGORY}/$categoryIdL1/$categoryIdL2?$queryParam"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow L1 category appLink with query param then should return tokopedia internal tokonow category in customerapp`() {
        val categoryIdL1 = "123"
        val queryParam = "official=true"
        val expectedDeepLink =
            "${ApplinkConstInternalTokopediaNow.OLD_CATEGORY}?category_l1=$categoryIdL1&$queryParam"
        val appLink = "${ApplinkConst.TokopediaNow.OLD_CATEGORY}/$categoryIdL1?$queryParam"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow L1 category main appLink with query param then should return tokopedia internal tokonow category in customerapp`() {
        val categoryIdL1 = "123"
        val queryParam = "official=true"
        val expectedDeepLink =
            "${ApplinkConstInternalTokopediaNow.CATEGORY}?category_l1=$categoryIdL1&$queryParam"
        val appLink = "${ApplinkConst.TokopediaNow.CATEGORY}/$categoryIdL1?$queryParam"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow see all category appLink then should return tokopedia internal tokonow see all category in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY
        val actualDeeplink = ApplinkConst.TokopediaNow.SEE_ALL_CATEGORY
        assertEqualsDeepLinkMapper(actualDeeplink, expectedDeepLink)
    }

    @Test
    fun `check manual ads creation applink`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/create-manual-ads"
        assertEqualsDeepLinkMapper(
            ApplinkConst.SellerApp.TOPADS_CREATE_MANUAL_ADS,
            expectedDeepLink
        )
    }

    @Test
    fun `check gofood appLink then should return tokopedia internal tokofood home in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokoFood.HOME_OLD
        val goFoodAppLink = ApplinkConst.TokoFood.GOFOOD
        assertEqualsDeepLinkMapper(goFoodAppLink, expectedDeepLink)
    }

    @Test
    fun `check tokofood home appLink then should return tokopedia internal tokofood home in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokoFood.HOME_OLD
        val appLink = ApplinkConst.TokoFood.HOME
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokofood merchant appLink then should return tokopedia internal tokofood merchant in customerapp`() {
        val merchantId = "cbdb87be-acca-439e-ae0f-4829a608b811"
        val productId = "1111"
        val expectedDeepLink =
            "${ApplinkConstInternalTokoFood.MERCHANT_OLD}?merchantId=$merchantId&product_id=$productId"
        val appLink = UriUtil.buildUri(ApplinkConst.TokoFood.MERCHANT, merchantId, productId)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokofood category appLink then should return tokopedia internal tokofood category in customerapp`() {
        val pageTitle = "Terlaris"
        val cuisine = "Coffee"
        val option = "1"
        val sortBy = "2"
        val brandUId = "226ea0d5-c763-40dc-aad3-9a233ed04f86"
        val expectedDeepLink =
            "${ApplinkConstInternalTokoFood.CATEGORY_OLD}?pageTitle=$pageTitle&option=$option&cuisine=$cuisine&sortBy=$sortBy&brand_uid=$brandUId"
        val appLink =
            "${ApplinkConst.TokoFood.CATEGORY}?pageTitle=$pageTitle&option=$option&cuisine=$cuisine&sortBy=$sortBy&brand_uid=$brandUId"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokofood post purchase appLink then should return tokopedia internal tokofood post purchase in customerapp`() {
        val orderId = "123"
        val expectedDeepLink =
            "${ApplinkConstInternalTokoFood.POST_PURCHASE}?orderId=$orderId"
        val appLink = UriUtil.buildUri(ApplinkConst.TokoFood.POST_PURCHASE, orderId)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokofood search appLink then should return tokopedia internal tokofood home in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokoFood.SEARCH_OLD
        val appLink = ApplinkConst.TokoFood.SEARCH
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check webview mapping`() {
        val queryParam =
            "?titlebar=false&allow_override=false&need_login=false&title=abc&pull_to_refresh=false&url=https://www.tokopedia.com/help"
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/webview" + queryParam
        assertEqualsDeepLinkMapper(ApplinkConst.WEBVIEW + queryParam, expectedDeepLink)
    }

    @Test
    fun `check browser mapping`() {
        val queryParam =
            "?ext=true&titlebar=false&allow_override=false&need_login=false&title=abc&pull_to_refresh=false&url=https://www.tokopedia.com/help"
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/browser" + queryParam
        assertEqualsDeepLinkMapper(ApplinkConst.BROWSER + queryParam, expectedDeepLink)
    }

    @Test
    fun `check peduli lindungi scan qr applink then should return tokopedia internal qr in customerapp`() {
        val expectedDeepLink =
            "${ApplinkConstInternalMarketplace.QR_SCANNEER}?redirect=https://tokopedia.com/peduli-lindungi/callback?hash=yourhash"
        val appLink =
            "${ApplinkConst.QRSCAN}?redirect=https://tokopedia.com/peduli-lindungi/callback?hash=yourhash"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check feeds video tab detail page applink customerapp`() {
        val queryParam = "?widgetType=live&source_type=abc&source_id=1&filter_category=Untukmu"
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://feedplaylivedetail" + queryParam
        assertEqualsDeepLinkMapper(
            ApplinkConst.FEED_PlAY_LIVE_DETAIL + queryParam,
            expectedDeepLink
        )
    }

    @Test
    fun `check feeds user profile page applink customerapp`() {
        val queryParam = "kumamoto"
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://people/" + queryParam
        assertEqualsDeepLinkMapper(
            ApplinkConst.PROFILE.replace(
                ApplinkConst.Profile.PARAM_USER_ID,
                queryParam
            ),
            expectedDeepLink
        )
    }

    @Test
    fun `check inbox host customerapp`() {
        every {
            DeeplinkMapperCommunication.isUserLoggedIn(any())
        } returns true

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                DeeplinkMapperCommunication.KEY_ROLLENCE_UNIVERSAL_INBOX
            )
        } returns ""

        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/inbox"
        assertEqualsDeepLinkMapper(ApplinkConst.INBOX, expectedDeepLink)
    }

    @Test
    fun `check inbox host customerapp universal inbox`() {
        every {
            DeeplinkMapperCommunication.isUserLoggedIn(any())
        } returns true

        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://home/inbox"
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                DeeplinkMapperCommunication.KEY_ROLLENCE_UNIVERSAL_INBOX
            )
        } returns DeeplinkMapperCommunication.ROLLENCE_TYPE_A
        assertEqualsDeepLinkMapper(ApplinkConst.INBOX, expectedDeepLink)

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                DeeplinkMapperCommunication.KEY_ROLLENCE_UNIVERSAL_INBOX
            )
        } returns DeeplinkMapperCommunication.ROLLENCE_TYPE_B
        assertEqualsDeepLinkMapper(ApplinkConst.INBOX, expectedDeepLink)
    }

    @Test
    fun `check affiliate toko customerapp`() {
        val affiliateAppLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE, affiliateAppLink)

        val helpAppLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/help"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_TOKO_HELP, helpAppLink)

        val transactionHistoryAppLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/transaction-history"
        assertEqualsDeepLinkMapper(
            ApplinkConst.AFFILIATE_TOKO_TRANSACTION_HISTORY,
            transactionHistoryAppLink
        )

        val ssaShopListAppLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/shoplist-dipromosikan-affiliate"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_TOKO_SSA_SHOP_LIST, ssaShopListAppLink)

        val discoPageListAppLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/discopage-list"
        assertEqualsDeepLinkMapper(
            ApplinkConst.AFFILIATE_TOKO_DISCO_PAGE_LIST,
            discoPageListAppLink
        )

        val eduAppLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/edu-page"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_TOKO_EDU_PAGE, eduAppLink)

        val promoPageAppLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/promosikan"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_TOKO_PROMO_PAGE, promoPageAppLink)

        val performaPageAppLink = "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/performa"
        assertEqualsDeepLinkMapper(ApplinkConst.AFFILIATE_TOKO_PERFORMA_PAGE, performaPageAppLink)
    }

    @Test
    fun `check shop penalty detail customerapp`() {
        assertEqualsDeepLinkMapper(
            ApplinkConst.SHOP_PENALTY_DETAIL,
            ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL
        )
    }

    @Test
    fun `check power merchant pro interrupt customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/seller-menu?state="
        assertEqualsDeepLinkMapperApp(
            AppType.MAIN_APP,
            ApplinkConst.POWER_MERCHANT_PRO_INTERRUPT,
            expectedDeepLink
        )
    }

    @Test
    fun `check marketplace onboarding customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/onboarding"
        assertEqualsDeepLinkMapper("tokopedia://marketplace/onboarding", expectedDeepLink)
    }

    @Test
    fun `check SELLER_CUSTOM_PRODUCT_LOGISTIC customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://logistic/customproductlogistic"
        assertEqualsDeepLinkMapper(ApplinkConst.SELLER_CUSTOM_PRODUCT_LOGISTIC, expectedDeepLink)
    }

    @Test
    fun `check SETTING_PAYMENT customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/payment-setting"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_PAYMENT, expectedDeepLink)
    }

    @Test
    fun `check SETTING_ACCOUNT customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/account-setting"
        assertEqualsDeepLinkMapper(ApplinkConst.SETTING_ACCOUNT, expectedDeepLink)
    }

    @Test
    fun `check GOPAY_KYC customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://payment/gopayKyc"
        assertEqualsDeepLinkMapper(ApplinkConst.GOPAY_KYC, expectedDeepLink)
    }

    @Test
    fun `check KYC_FORM_ONLY_NO_PARAM customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/user-identification-only"
        assertEqualsDeepLinkMapper(
            ApplinkConst.KYC_FORM_ONLY_NO_PARAM,
            expectedDeepLink
        )
    }

    @Test
    fun `check KYC_FORM_ONLY_WITH_PARAM`() {
        val dl = UriUtil.buildUri(ApplinkConst.KYC_FORM_ONLY, "1", "true", "true", "1")
        assertEqualsDeepLinkMapper(
            dl,
            "${DeeplinkConstant.SCHEME_INTERNAL}://user/user-identification-only?projectId=1&showIntro=true&redirectUrl=true&type=1"
        )
    }

    @Test
    fun `check NOTIFICATION customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://marketplace/inbox?page=notification&show_bottom_nav=false"
        assertEqualsDeepLinkMapperApp(AppType.MAIN_APP, ApplinkConst.NOTIFICATION, expectedDeepLink)
    }

    @Test
    fun `check ACTIVATION_GOPAY customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://fintech/activate_gopay"
        assertEqualsDeepLinkMapper(ApplinkConst.ACTIVATION_GOPAY + "/abc/def", expectedDeepLink)
    }

    @Test
    fun `check OPTIMIZED_CHECKOUT customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://fintech/opt-checkout"
        assertEqualsDeepLinkMapper(ApplinkConst.OPTIMIZED_CHECKOUT + "/abc/def", expectedDeepLink)
    }

    @Test
    fun `check SHARING_HOST customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/sharing/abc/def"
        assertEqualsDeepLinkMapper("tokopedia://sharing/abc/def", expectedDeepLink)
    }

    @Test
    fun `check LINK_ACCOUNT customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/link-account-webview"
        assertEqualsDeepLinkMapper(ApplinkConst.LINK_ACCOUNT, expectedDeepLink)
    }

    @Test
    fun `check EXPLICIT_PROFILE customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/explicit-profile"
        assertEqualsDeepLinkMapper(ApplinkConst.EXPLICIT_PROFILE, expectedDeepLink)
    }

    @Test
    fun `check TELEPHONY_MASKING customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/telephony-masking"
        assertEqualsDeepLinkMapper(ApplinkConst.TELEPHONY_MASKING + "/abc/def", expectedDeepLink)
    }

    @Test
    fun `check PRODUCT_BUNDLE customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/product-bundle/123/"
        assertEqualsDeepLinkMapper("tokopedia://product-bundle/123", expectedDeepLink)
    }

    @Test
    fun `check GIFTING customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://merchant/gifting/213/"
        assertEqualsDeepLinkMapper("tokopedia://gifting/213", expectedDeepLink)
    }

    @Test
    fun `check TOPADS_CREATE_MANUAL_ADS customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://topads/create-manual-ads"
        assertEqualsDeepLinkMapper(
            ApplinkConst.SellerApp.TOPADS_CREATE_MANUAL_ADS + "/abc/def",
            expectedDeepLink
        )
    }

    @Test
    fun `check WEBVIEW_DOWNLOAD_HOST customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/webviewdownload"
        assertEqualsDeepLinkMapper("tokopedia://webviewdownload/abc/def", expectedDeepLink)
    }

    @Test
    fun `check AFFILIATE_DEFAULT_CREATE_POST_V2 customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://affiliate/create_post_v2/abc/def"
        assertEqualsDeepLinkMapper(
            ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2 + "/abc/def",
            expectedDeepLink
        )
    }

    @Test
    fun `check REVIEW_REMINDER_PREVIOUS customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://sellerapp/review-reminder"
        assertEqualsDeepLinkMapper(ApplinkConst.REVIEW_REMINDER_PREVIOUS, expectedDeepLink)
    }

    @Test
    fun `check IMAGE_PICKER_V2 customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/image-picker/v2/"
        assertEqualsDeepLinkMapper("tokopedia://image-picker/v2", expectedDeepLink)
    }

    @Test
    fun `check MEDIA_PICKER customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/media-picker"
        assertEqualsDeepLinkMapper(ApplinkConst.MediaPicker.MEDIA_PICKER, expectedDeepLink)
    }

    @Test
    fun `check MEDIA_PICKER_PREVIEW customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://global/media-picker-preview"
        assertEqualsDeepLinkMapper(ApplinkConst.MediaPicker.MEDIA_PICKER_PREVIEW, expectedDeepLink)
    }

    @Test
    fun `check WEB_HOST customerapp`() {
        val expectedDeepLink = "https://www.tokopedia.com/abc/def"
        assertEqualsDeepLinkMapper("tokopedia://www.tokopedia.com/abc/def", expectedDeepLink)
    }

    @Test
    fun `check INPUT_INACTIVE_NUMBER customerapp`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_INTERNAL}://user/input-old-phone-number"
        assertEqualsDeepLinkMapper(ApplinkConst.INPUT_INACTIVE_NUMBER, expectedDeepLink)
    }

    @Test
    fun `check add phone page applink customerapp`() {
        val expectedDeepLink = ApplinkConstInternalUserPlatform.ADD_PHONE
        assertEqualsDeepLinkMapper(ApplinkConst.ADD_PHONE, expectedDeepLink)
    }

    @Test
    fun `check feed creation product search applink customerapp`() {
        val applink = ApplinkConst.FEED_CREATION_PRODUCT_SEARCH
        val internalApplink = ApplinkConstInternalContent.INTERNAL_FEED_CREATION_PRODUCT_SEARCH

        assertEqualsDeepLinkMapper(applink, internalApplink)
    }

    @Test
    fun `check feed creation shop search applink customerapp`() {
        val applink = ApplinkConst.FEED_CREATION_SHOP_SEARCH
        val internalApplink = ApplinkConstInternalContent.INTERNAL_FEED_CREATION_SHOP_SEARCH

        assertEqualsDeepLinkMapper(applink, internalApplink)
    }

    @Test
    fun `check resolution success applink customerapp`() {
        val expectedDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://resolution/success-create?url=https://tokopedia.com/resolution-center/6932"
        val appLink = UriUtil.buildUri(
            ApplinkConst.RESOLUTION_SUCCESS,
            "https://tokopedia.com/resolution-center/6932"
        )

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe detail deeplink should return recipe detail internal applink in customerapp`() {
        val recipeId = "100"
        val expectedDeepLink = UriUtil.buildUriAppendParam(
            ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
            mapOf(DeeplinkMapperTokopediaNow.PARAM_RECIPE_ID to recipeId)
        )
        val appLink = UriUtil.buildUri(ApplinkConst.TokopediaNow.RECIPE_DETAIL, recipeId)
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe detail deeplink should return recipe detail internal applink with query param in customerapp`() {
        val recipeId = "101"
        val queryParam = "autoplay=true"
        val deepLink = UriUtil.buildUriAppendParam(
            ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
            mapOf(DeeplinkMapperTokopediaNow.PARAM_RECIPE_ID to recipeId)
        )
        val expectedDeepLink = "$deepLink&$queryParam"
        val appLink =
            "${UriUtil.buildUri(ApplinkConst.TokopediaNow.RECIPE_DETAIL, recipeId)}&$queryParam"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe bookmark deeplink should return recipe bookmark internal applink in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokopediaNow.RECIPE_BOOKMARK
        val appLink = ApplinkConst.TokopediaNow.RECIPE_BOOKMARK
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe bookmark deeplink should return recipe bookmark internal applink with query param in customerapp`() {
        val queryParam = "page=1"
        val expectedDeepLink = "${ApplinkConstInternalTokopediaNow.RECIPE_BOOKMARK}?$queryParam"
        val appLink = "${ApplinkConst.TokopediaNow.RECIPE_BOOKMARK}?$queryParam"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe home deeplink should return recipe home internal applink in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokopediaNow.RECIPE_HOME
        val appLink = ApplinkConst.TokopediaNow.RECIPE_HOME
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe home deeplink should return recipe home internal applink with query param in customerapp`() {
        val queryParam = "page=1"
        val expectedDeepLink = "${ApplinkConstInternalTokopediaNow.RECIPE_HOME}?$queryParam"
        val appLink = "${ApplinkConst.TokopediaNow.RECIPE_HOME}?$queryParam"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe search deeplink should return recipe search internal applink in customerapp`() {
        val expectedDeepLink = ApplinkConstInternalTokopediaNow.RECIPE_SEARCH
        val appLink = ApplinkConst.TokopediaNow.RECIPE_SEARCH
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokonow recipe search deeplink should return recipe search internal applink with query param in customerapp`() {
        val queryParam = "page=1"
        val expectedDeepLink = "${ApplinkConstInternalTokopediaNow.RECIPE_SEARCH}?$queryParam"
        val appLink = "${ApplinkConst.TokopediaNow.RECIPE_SEARCH}?$queryParam"
        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check tokochat applink customerapp`() {
        val expectedDeepLink = ApplinkConstInternalCommunication.TOKO_CHAT
        setRemoteConfig(true)
        assertEqualsDeepLinkMapper(ApplinkConst.TOKO_CHAT, expectedDeepLink)
    }

    @Test
    fun `check tokochat applink customerapp with query params`() {
        val queryParams = "?orderId=F-123&tokochatSource=tokofood"
        val deepLink = "${ApplinkConst.TOKO_CHAT}$queryParams"
        val expectedDeepLink = "${ApplinkConstInternalCommunication.TOKO_CHAT}$queryParams"
        setRemoteConfig(true)
        assertEqualsDeepLinkMapper(deepLink, expectedDeepLink)
    }

    @Test
    fun `check tokochat applink customerapp with remote config turn off`() {
        val queryParams = "orderId=F-123&tokochatSource=tokofood"
        val deepLink = "${ApplinkConst.TOKO_CHAT}?$queryParams"
        val expectedDeepLink = "${ApplinkConstInternalOrder.UNIFY_ORDER_TOKOFOOD}&$queryParams"
        setRemoteConfig(false)
        assertEqualsDeepLinkMapper(deepLink, expectedDeepLink)
    }

    @Test
    fun `check topchat settings bubble activation applink`() {
        val deepLink = ApplinkConst.TOPCHAT_BUBBLE_ACTIVATION
        val expectedDeepLink = ApplinkConstInternalMarketplace.TOPCHAT_BUBBLE_ACTIVATION
        assertEqualsDeepLinkMapper(deepLink, expectedDeepLink)
    }

    @Test
    fun `check product review gallery applink`() {
        val deepLink = ApplinkConst.PRODUCT_REVIEW_GALLERY
        val expectedDeepLink = ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY
        assertEqualsDeepLinkMapper(deepLink, expectedDeepLink)
    }

    @Test
    fun `check dilayani tokopedia applink`() {
        val deepLink = ApplinkConst.DilayaniTokopedia.HOME
        val expectedDeepLink = ApplinkConstInternalDilayaniTokopedia.HOME
        assertEqualsDeepLinkMapper(deepLink, expectedDeepLink)
    }

    @Test
    fun `check youtube player appLink then should return tokopedia internal youtube player in customerapp`() {
        val expectedInternalDeepLink =
            "${DeeplinkConstant.SCHEME_INTERNAL}://global/youtube-player/123/"
        val appLink = UriUtil.buildUri(ApplinkConst.YOUTUBE_PLAYER, "123")
        assertEqualsDeepLinkMapper(appLink, expectedInternalDeepLink)
    }

    @Test
    fun `check feed relevant post applink customerapp`() {
        val postId = "12345"
        val source = "detail-play"
        val query = "source=$source"
        val expectedDeepLink = ApplinkConsInternalHome.HOME_NAVIGATION +
            "?${DeeplinkMapperHome.EXTRA_TAB_POSITION}=${DeeplinkMapperHome.TAB_POSITION_FEED}" +
            "&${ApplinkConstInternalContent.EXTRA_FEED_TAB_POSITION}=${ApplinkConstInternalContent.UF_TAB_POSITION_FOR_YOU}" +
            "&${ApplinkConstInternalContent.UF_EXTRA_FEED_SOURCE_ID}=$postId" +
            "&${ApplinkConstInternalContent.UF_EXTRA_FEED_SOURCE_NAME}=$source" +
            "&$query"

        val appLink = "tokopedia://content/$postId?$query"

        assertEqualsDeepLinkMapper(appLink, expectedDeepLink)
    }

    @Test
    fun `check shop nib applink`() {
        assertEqualsDeepLinkMapper(
            ApplinkConst.SHOP_NIB_CUSTOMER_APP,
            ApplinkConstInternalMechant.SHOP_NIB_CUSTOMER_APP
        )
    }
}
