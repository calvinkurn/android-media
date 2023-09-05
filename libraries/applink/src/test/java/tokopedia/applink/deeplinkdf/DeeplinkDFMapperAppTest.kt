package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AFFILIATE
import com.tokopedia.applink.ApplinkConst.DEALS_ALL_BRANDS
import com.tokopedia.applink.ApplinkConst.DEALS_CATEGORY
import com.tokopedia.applink.ApplinkConst.DEALS_HOME
import com.tokopedia.applink.ApplinkConst.FEEDBACK_FORM
import com.tokopedia.applink.ApplinkConst.Gamification.DAILY_GIFT_BOX
import com.tokopedia.applink.ApplinkConst.Gamification.GIFT_TAP_TAP
import com.tokopedia.applink.ApplinkConst.PRODUCT_AR
import com.tokopedia.applink.ApplinkConst.PRODUCT_CREATE_REVIEW
import com.tokopedia.applink.ApplinkConst.REPUTATION_DETAIL
import com.tokopedia.applink.ApplinkConst.REVIEW_DETAIL
import com.tokopedia.applink.ApplinkConst.SELLER_REVIEW
import com.tokopedia.applink.ApplinkConst.TOKOPEDIA_REWARD
import com.tokopedia.applink.ApplinkConst.TOKOPOINTS
import com.tokopedia.applink.DeeplinkDFMapper.DF_ALPHA_TESTING
import com.tokopedia.applink.DeeplinkDFMapper.DF_CATEGORY_AFFILIATE
import com.tokopedia.applink.DeeplinkDFMapper.DF_CATEGORY_TRADE_IN
import com.tokopedia.applink.DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER
import com.tokopedia.applink.DeeplinkDFMapper.DF_DIGITAL
import com.tokopedia.applink.DeeplinkDFMapper.DF_DILAYANI_TOKOPEDIA
import com.tokopedia.applink.DeeplinkDFMapper.DF_ENTERTAINMENT
import com.tokopedia.applink.DeeplinkDFMapper.DF_FEED_CONTENT_CREATION
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_LOGIN
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_NONLOGIN
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_PRODUCT_AR
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_SELLER
import com.tokopedia.applink.DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US
import com.tokopedia.applink.DeeplinkDFMapper.DF_PEOPLE
import com.tokopedia.applink.DeeplinkDFMapper.DF_PROMO_GAMIFICATION
import com.tokopedia.applink.DeeplinkDFMapper.DF_PROMO_TOKOPOINTS
import com.tokopedia.applink.DeeplinkDFMapper.DF_TOKOCHAT
import com.tokopedia.applink.DeeplinkDFMapper.DF_TOKOFOOD
import com.tokopedia.applink.DeeplinkDFMapper.DF_TOKOPEDIA_NOW
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.DROPOFF_PICKER
import com.tokopedia.applink.internal.ApplinkConstInternalMedia
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import io.mockk.every
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkDFMapperTest : DeepLinkDFMapperTestFixture() {
    @Test
    fun `Deeplink DF Customerapp`() {
        checkDeeplink(false)
    }

    @Test
    fun `Deeplink DF Sellerapp`() {
        checkDeeplink(true)
    }

    @Test
    fun `MA df_alpha_testing`() {
        assertEqualDeepLinkMA(FEEDBACK_FORM, DF_ALPHA_TESTING)
    }

    @Test
    fun `MA df_category_affiliate`() {
        assertEqualDeepLinkMA(AFFILIATE, DF_CATEGORY_AFFILIATE)
    }

    @Test
    fun `MA df_category_trade_in`() {
        assertEqualDeepLinkMA(ApplinkConst.TRADEIN, DF_CATEGORY_TRADE_IN)
        assertEqualDeepLinkMA(ApplinkConst.TRADEIN + "/abc", DF_CATEGORY_TRADE_IN)
        assertEqualDeepLinkMA(ApplinkConst.MONEYIN, DF_CATEGORY_TRADE_IN)
        assertEqualDeepLinkMA(DROPOFF_PICKER, DF_CATEGORY_TRADE_IN)
    }

    @Test
    fun `MA df_comm_tokochat`() {
        assertEqualDeepLinkMA(ApplinkConst.TOKO_CHAT, DF_TOKOCHAT)
    }

    @Test
    fun `MA df_content_play_broadcaster`() {
        assertEqualDeepLinkMA(ApplinkConst.PLAY_BROADCASTER, DF_CONTENT_PLAY_BROADCASTER)
        assertEqualDeepLinkMA(ApplinkConst.PLAY_SHORTS, DF_CONTENT_PLAY_BROADCASTER)
        assertEqualDeepLinkMA(
            ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER,
            DF_CONTENT_PLAY_BROADCASTER
        )
    }

    @Test
    fun `MA df_digital`() {
        assertEqualDeepLinkMA(ApplinkConsInternalDigital.SMART_BILLS, DF_DIGITAL)
        assertEqualDeepLinkMA(ApplinkConsInternalDigital.ADD_TELCO, DF_DIGITAL)
    }

    @Test
    fun `MA df_dilayanitokopedia`() {
        assertEqualDeepLinkMA(ApplinkConst.DilayaniTokopedia.HOME, DF_DILAYANI_TOKOPEDIA)
    }

    @Test
    fun `MA df_entertainment`() {
        assertEqualDeepLinkMA(ApplinkConst.EVENTS, DF_ENTERTAINMENT)
        assertEqualDeepLinkMA(ApplinkConst.EVENTS_CATEGORY, DF_ENTERTAINMENT)

        assertEqualDeepLinkMA(DEALS_HOME, DF_ENTERTAINMENT)
        assertEqualDeepLinkMA(DEALS_CATEGORY, DF_ENTERTAINMENT)
        assertEqualDeepLinkMA(DEALS_ALL_BRANDS, DF_ENTERTAINMENT)
    }

    @Test
    fun `MA df_feed_content_creation`() {
        assertEqualDeepLinkMA(ApplinkConst.IMAGE_PICKER_V2, DF_FEED_CONTENT_CREATION)
        assertEqualDeepLinkMA(
            ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2 + "/",
            DF_FEED_CONTENT_CREATION
        )
        assertEqualDeepLinkMA(
            ApplinkConst.AFFILIATE_PRODUCT_PICKER_FROM_SHOP_NO_PARAM + "/",
            DF_FEED_CONTENT_CREATION
        )
        assertEqualDeepLinkMA(ApplinkConst.FEED_CREATION_PRODUCT_SEARCH, DF_FEED_CONTENT_CREATION)

        assertEqualDeepLinkMA(ApplinkConst.MediaEditor.MEDIA_EDITOR, DF_FEED_CONTENT_CREATION)

    }

    @Test
    fun `MA df_merchant_login`() {
        assertEqualDeepLinkMA(ApplinkConst.FAVORITE, DF_MERCHANT_LOGIN)
        assertEqualDeepLinkMA(ApplinkConst.ORDER_HISTORY + "/shopid", DF_MERCHANT_LOGIN)
    }

    @Test
    fun `MA df_merchant_nonlogin`() {
        assertEqualDeepLinkMA(SELLER_REVIEW, DF_MERCHANT_NONLOGIN)
        assertEqualDeepLinkMA("$PRODUCT_CREATE_REVIEW/a/b", DF_MERCHANT_NONLOGIN)
        assertEqualDeepLinkMA(REVIEW_DETAIL, DF_MERCHANT_NONLOGIN)
        assertEqualDeepLinkMA(REPUTATION_DETAIL, DF_MERCHANT_NONLOGIN)
        assertEqualDeepLinkMA(ApplinkConst.PRODUCT_REVIEW_GALLERY, DF_MERCHANT_NONLOGIN)
        assertEqualDeepLinkMA(ApplinkConst.PRODUCT_BULK_CREATE_REVIEW, DF_MERCHANT_NONLOGIN)
    }

    @Test
    fun `MA df_merchant_product_ar`() {
        assertEqualDeepLinkMA(PRODUCT_AR, DF_MERCHANT_PRODUCT_AR)
    }


    @Test
    fun `MA df_merchant_seller`() {
        assertEqualDeepLinkMA(ApplinkConst.PRODUCT_MANAGE, DF_MERCHANT_SELLER)
        every {
            PowerMerchantDeepLinkMapper.isEnablePMSwitchToWebView(context)
        } returns false
        every {
            PowerMerchantDeepLinkMapper.isLoginAndHasShop(context)
        } returns true
        assertEqualDeepLinkMA(ApplinkConst.POWER_MERCHANT_SUBSCRIBE, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.PM_BENEFIT_PACKAGE, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.SHOP_SCORE_DETAIL, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.SellerApp.SHOP_SCORE_DETAIL, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.SHOP_PENALTY, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.SHOP_PENALTY_DETAIL, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.SHOP_SETTINGS_CUSTOMER_APP, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(TOPADS_DASHBOARD_CUSTOMER, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.PRODUCT_ADD, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.ADMIN_INVITATION, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.ADMIN_ACCEPTED, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.ADMIN_REDIRECTION, DF_MERCHANT_SELLER)
        assertEqualDeepLinkMA(ApplinkConst.SELLER_TRANSACTION, DF_MERCHANT_SELLER)
    }

    @Test
    fun `MA df_operational_contact_us`() {
        assertEqualDeepLinkMA(ApplinkConst.CONTACT_US, DF_OPERATIONAL_CONTACT_US)
        assertEqualDeepLinkMA(ApplinkConst.INBOX_TICKET, DF_OPERATIONAL_CONTACT_US)
        assertEqualDeepLinkMA(ApplinkConst.CHAT_BOT + "/123", DF_OPERATIONAL_CONTACT_US)
        assertEqualDeepLinkMA(ApplinkConst.TELEPHONY_MASKING, DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `MA df_people`() {
        assertEqualDeepLinkMA("tokopedia://people/settings/123", DF_PEOPLE)
        assertEqualDeepLinkMA("tokopedia://people/123", DF_PEOPLE)
    }

    @Test
    fun `MA df_promo_gamification`() {
        assertEqualDeepLinkMA(DAILY_GIFT_BOX, DF_PROMO_GAMIFICATION)
        assertEqualDeepLinkMA(GIFT_TAP_TAP, DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `MA df_promo_tokopoints`() {
        assertEqualDeepLinkMA(TOKOPEDIA_REWARD, DF_PROMO_TOKOPOINTS)
        assertEqualDeepLinkMA(TOKOPOINTS, DF_PROMO_TOKOPOINTS)
        assertEqualDeepLinkMA("$TOKOPEDIA_REWARD/tukar-point", DF_PROMO_TOKOPOINTS)
        assertEqualDeepLinkMA("$TOKOPEDIA_REWARD/kupon-saya/123", DF_PROMO_TOKOPOINTS)
        assertEqualDeepLinkMA("$TOKOPEDIA_REWARD/tukar-detail", DF_PROMO_TOKOPOINTS)
        assertEqualDeepLinkMA("$TOKOPEDIA_REWARD/kupon-detail", DF_PROMO_TOKOPOINTS)
        assertEqualDeepLinkMA("$TOKOPEDIA_REWARD/introduction", DF_PROMO_TOKOPOINTS)
    }

    @Test
    fun `MA df_tokofood`() {
        assertEqualDeepLinkMA(ApplinkConst.TokoFood.HOME, DF_TOKOFOOD)
        assertEqualDeepLinkMA(ApplinkConst.TokoFood.CATEGORY, DF_TOKOFOOD)
        assertEqualDeepLinkMA(ApplinkConst.TokoFood.MERCHANT, DF_TOKOFOOD)
        assertEqualDeepLinkMA(ApplinkConst.TokoFood.POST_PURCHASE, DF_TOKOFOOD)
        assertEqualDeepLinkMA(ApplinkConst.TokoFood.SEARCH, DF_TOKOFOOD)
        assertEqualDeepLinkMA(ApplinkConst.TokoFood.GOFOOD, DF_TOKOFOOD)
    }

    @Test
    fun `MA df_tokopedianow`() {
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.HOME, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.CATEGORY, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.OLD_CATEGORY, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.SEE_ALL_CATEGORY, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.REPURCHASE, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.RECIPE_DETAIL, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.RECIPE_BOOKMARK, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.RECIPE_BOOKMARK, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.RECIPE_HOME, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.RECIPE_SEARCH, DF_TOKOPEDIA_NOW)
        assertEqualDeepLinkMA(ApplinkConst.TokopediaNow.RECIPE_AUTO_COMPLETE, DF_TOKOPEDIA_NOW)
    }


}