package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.*
import org.junit.Test

class DeepLinkDFMapperSellerAppTest : DeepLinkDFMapperTestFixture() {

    @Test
    fun `check shop settings base appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/shop-settings"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
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
    fun `check user identification form appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/user-identification-form?projectId=123456"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
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
    fun `check merchant shop score appLink then should return DF_SHOP_SCORE in sellerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SHOP_SCORE)
    }

    @Test
    fun `check shop score detail appLink then should return DF_SHOP_SCORE in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SHOP_SCORE)
    }

    @Test
    fun `check shop score detail appLink with sellerapp scheme then should return DF_SHOP_SCORE in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SHOP_SCORE)
    }

    @Test
    fun `check shop score detail internal appLink then should return DF_SHOP_SCORE in sellerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/shop-score-detail"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SHOP_SCORE)
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
    fun `check play broadcaster appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://play-broadcaster"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
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
    fun `check merchant statistic dashboard appLink then should return DF_BASE in sellerapp`() {
        val appLink = "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/statistic_dashboard"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check admin invitation internal appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://shop-admin/invitation-page"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check admin accepted internal appLink then should return DF_BASE_SELLER_APP in customerapp`() {
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
    fun `check flash sale tokopedia detail page appLink then should return DF_FS_TOKOPEDIA in sellerapp`() {
        val appLink = "${DeeplinkConstant.SCHEME_SELLERAPP}://tokopedia-flash-sale/campaign-detail/1"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA)
    }

    @Test
    fun `check seller feedback appLink should return DF_SELLER_FEEDBACK in seller app`() {
        val appLink = ApplinkConstInternalSellerapp.INTERNAL_SELLERAPP + "/seller-feedback"
        assertEqualDeepLinkSellerApp(appLink, DeeplinkDFMapper.DF_SELLER_FEEDBACK)
    }
}
