package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import org.junit.Test

class DeepLinkDFMapperSellerAppTest : DeepLinkDFMapperTestFixture() {

    @Test
    fun `check shop settings base appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/shop-settings"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SHOP_SETTINGS_SELLER_APP)
    }

    @Test
    fun `check payment setting appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalPayment.INTERNAL_PAYMENT}/setting"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check product manage list appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/product-manage-list"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check user identification form appLink then should return DF_KYC_SELLERAPP in sellerapp`() {
        val appLink = "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/user-identification-form?projectId=123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_KYC_SELLERAPP)
    }

    @Test
    fun `check user identification form appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/liveness-detection?projectId=123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_KYC_SELLERAPP)
    }

    @Test
    fun `check top ads dashboard seller appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://topads"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check top ads dashboard internal appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalTopAds.INTERNAL_TOPADS}/dashboard"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check merchant shop showcase list appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/shop-showcase-list"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check merchant shop score appLink then should return DF_SELLER_FRONT_FUNNEL in sellerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check shop score detail appLink then should return DF_SELLER_FRONT_FUNNEL in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check shop score detail appLink with sellerapp scheme then should return DF_SELLER_FRONT_FUNNEL in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check shop score detail internal appLink then should return DF_SELLER_FRONT_FUNNEL in sellerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check create voucher appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP}/seller-mvc/create/{voucher_type}/"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check voucher list appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP}/seller-mvc/list/{voucher_status}/"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check voucher detail appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP}/seller-mvc/detail/{voucher_id}/"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check merchant open product preview appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/open-product-preview"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check product add appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://product/add"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check welcome appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP}/welcome"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check seller search appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP}/seller-search"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check comment appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/comment/123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check internal content post detail appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/post-detail/"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check content report appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/content-report/123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check video detail appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalContent.INTERNAL_CONTENT}/video-detail/123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check play broadcaster appLink then should return DF_CONTENT_PLAY_BROADCASTER in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://play-broadcaster"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER)
    }

    @Test
    fun `check play shorts appLink then should return DF_CONTENT_PLAY_BROADCASTER in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://play-shorts"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER)
    }

    @Test
    fun `check media picker appLink then should return DF_CONTENT_PLAY_BROADCASTER in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/media-picker"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER)
    }

    @Test
    fun `check media picker album appLink then should return DF_CONTENT_PLAY_BROADCASTER in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/media-picker-album"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER)
    }

    @Test
    fun `check media picker preview appLink then should return DF_CONTENT_PLAY_BROADCASTER in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/media-picker-preview"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER)
    }

    @Test
    fun `check shop page base appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/shop-page"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop etalase appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/etalase/123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop etalase with keyword and sort appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/etalase/123456/?search=car&sort=1"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop review appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/review"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop note appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/note"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop info appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop/12345/info"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop home appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val mockShopId = "12345"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_HOME, mockShopId)
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop product appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val mockShopId = "12345"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_PRODUCT, mockShopId)
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop feed appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val mockShopId = "12345"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_FEED, mockShopId)
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop mvc locked to product shop id appLink then should return DF_BASE_SELLERAPP in sellerapp`() {
        val mockShopId = "12345"
        val mockVoucherId = "6789"
        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_MVC_LOCKED_TO_PRODUCT, mockShopId, mockVoucherId)
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop settings note appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://setting/shop/note"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop operational hour bottom sheet appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val internalAppLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/shop/widget/operational-hour/12345/"
        assertEqualDeepLinkSellerApp(internalAppLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check admin invitation internal appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://shop-admin/invitation-page"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check admin accepted internal appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://shop-admin/accepted-page"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check campaign list applink then should return DF_CAMPAIGN_LIST in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://campaign-list"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_CAMPAIGN_LIST)
    }

    @Test
    fun `check flash sale tokopedia feature appLink then should return DF_FS_TOKOPEDIA in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://tokopedia-flash-sale"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA)
    }

    @Test
    fun `check upcoming flash sale tokopedia feature appLink then should return DF_FS_TOKOPEDIA in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://tokopedia-flash-sale/upcoming"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA)
    }

    @Test
    fun `check registered flash sale tokopedia feature appLink then should return DF_FS_TOKOPEDIA in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://tokopedia-flash-sale/registered"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA)
    }

    @Test
    fun `check ongoing flash sale tokopedia feature appLink then should return DF_FS_TOKOPEDIA in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://tokopedia-flash-sale/ongoing"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA)
    }

    @Test
    fun `check finished flash sale tokopedia feature appLink then should return DF_FS_TOKOPEDIA in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://tokopedia-flash-sale/finished"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA)
    }

    @Test
    fun `check flash sale tokopedia detail page appLink then should return DF_FS_TOKOPEDIA in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://tokopedia-flash-sale/campaign-detail/1"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA)
    }

    @Test
    fun `check seller shop nib public applink should return DF_BASE in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://shop-nib"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check seller shop nib internal applink should return DF_BASE in sellerapp`() {
        val appLink = ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP + "/shop-nib"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check power merchant subscribe appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://power_merchant/subscribe"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check power merchant subscribe internal appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/power-merchant-subscribe"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check power merchant benefit package appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://power_merchant/benefit-package"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check power merchant benefit package internal appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/pm-benefit-package"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check seller feedback appLink should return DF_SELLER_FEEDBACK in seller app`() {
        val appLink = ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP + "/seller-feedback"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FEEDBACK)
    }

    @Test
    fun `check external statistic appLink should return DF_SELLER_FRONT_FUNNEL in seller app`() {
        val statisticExternal = ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD
        assertEqualDeepLinkSellerApp(statisticExternal, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check sellerapp external statistic appLink should return DF_SELLER_FRONT_FUNNEL in seller app`() {
        val appLink = ApplinkConst.SellerApp.STATISTIC_DASHBOARD
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check internal statistic appLink should return DF_SELLER_FRONT_FUNNEL in seller app`() {
        val appLink = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check seller persona internal appLink should return DF_SELLER_FRONT_FUNNEL in seller app`() {
        val appLink = ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP + "/seller-persona"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check seller persona external appLink should return DF_SELLER_FRONT_FUNNEL in seller app`() {
        val appLink = "sellerapp://seller-persona"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL)
    }

    @Test
    fun `check goto kyc from applink then should return DF_KYC_SELLERAPP in seller app`() {
        val appLink = "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/goto-kyc?projectId=123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_KYC_SELLERAPP)
    }

    @Test
    fun `check user identification form appLink then should return DF_KYC_SELLERAPP in seller app`() {
        val appLink = "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/user-identification-form?projectId=123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_KYC_SELLERAPP)
    }

    @Test
    fun `check user identification info appLink then should return DF_KYC_SELLERAPP in seller app`() {
        val appLink = "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/user-identification-info?projectId=123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_KYC_SELLERAPP)
    }

    @Test
    fun `check user identification only appLink then should return DF_KYC_SELLERAPP in seller app`() {
        val appLink = "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/user-identification-only?projectId=123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_KYC_SELLERAPP)
    }

    @Test
    fun `check inbox talk appLink then should return DF_SELLER_TALK in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/inbox-talk"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_TALK)
    }

    @Test
    fun `check product talk appLink then should return DF_SELLER_TALK in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/product-talk/123"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_TALK)
    }

    @Test
    fun `check reply talk appLink then should return DF_SELLER_TALK in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/reply-talk/123"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_TALK)
    }

    @Test
    fun `check add talk appLink then should return DF_SELLER_TALK in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/add-talk"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_TALK)
    }

    @Test
    fun `check talk seller settings appLink then should return DF_SELLER_TALK in sellerapp`() {
        val appLink = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/talk-seller-settings/"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_TALK)
    }
}
