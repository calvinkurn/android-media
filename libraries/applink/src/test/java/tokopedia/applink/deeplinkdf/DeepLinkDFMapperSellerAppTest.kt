package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.internal.*
import org.junit.Assert.assertEquals
import org.junit.Test

class DeepLinkDFMapperSellerAppTest: DeepLinkDFMapperTestFixture() {

    private val dfSellerAppPath = DeepLinkDFMapperSellerAppTest::class.java.getResourceAsStream("/df_sellerapp.cfg")

    @Test
    fun `check shop settings base appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check payment setting appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalPayment.PAYMENT_SETTING
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check product manage list appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check user identification form appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check top ads dashboard seller appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check top ads dashboard internal appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check merchant shop showcase list appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check merchant shop score appLink then should return DF_SHOP_SCORE in sellerapp`() {
        val appLink = ApplinkConstInternalMechant.MERCHANT_SHOP_SCORE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_SHOP_SCORE)
    }

    @Test
    fun `check shop score detail appLink then should return DF_SHOP_SCORE in sellerapp`() {
        val appLink = ApplinkConst.SHOP_SCORE_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_SHOP_SCORE)
    }

    @Test
    fun `check shop score detail internal appLink then should return DF_SHOP_SCORE in sellerapp`() {
        val appLink = ApplinkConstInternalMarketplace.SHOP_SCORE_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_SHOP_SCORE)
    }

    @Test
    fun `check create voucher appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalSellerapp.CREATE_VOUCHER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check voucher list appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalSellerapp.VOUCHER_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check voucher detail appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalSellerapp.VOUCHER_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check merchant open product preview appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check product add appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.PRODUCT_ADD
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check welcome appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalSellerapp.WELCOME
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check seller search appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SellerApp.SELLER_SEARCH
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check comment appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.COMMENT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check internal content post detail appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check kol youtube appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.KOL_YOUTUBE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check content report appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.CONTENT_REPORT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check video detail appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.VIDEO_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check media preview appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.MEDIA_PREVIEW
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check internal content create post appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_CONTENT_CREATE_POST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check internal content draft post appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_CONTENT_DRAFT_POST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop post edit appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalContent.SHOP_POST_EDIT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check play broadcaster appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.PLAY_BROADCASTER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop page base appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConstInternalMarketplace.SHOP_PAGE_BASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop etalase appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP_ETALASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop etalase with keyword and sort appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP_ETALASE_WITH_KEYWORD_AND_SORT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop review appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP_REVIEW
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop note appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP_NOTE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop info appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP_INFO
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop home appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP_HOME
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check shop settings note appLink then should return DF_BASE_SELLER_APP in sellerapp`() {
        val appLink = ApplinkConst.SHOP_SETTINGS_NOTE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

    @Test
    fun `check merchant statistic dashboard appLink then should return DF_BASE in sellerapp`() {
        val appLink = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListSellerApp, dfSellerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE_SELLER_APP)
    }

}