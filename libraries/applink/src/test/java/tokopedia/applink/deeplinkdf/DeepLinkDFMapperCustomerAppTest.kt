package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.internal.*
import org.junit.Assert.assertEquals
import org.junit.Test

class DeepLinkDFMapperCustomerAppTest: DeepLinkDFMapperTestFixture() {

    private val dfCustomerAppPath = DeepLinkDFMapperCustomerAppTest::class.java.getResourceAsStream("/df_customerapp.cfg")

    @Test
    fun `check onboarding appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.ONBOARDING
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check age restriction appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalCategory.AGE_RESTRICTION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check tradein appLink then should return DF_CATEGORY_TRADE_IN in customerapp`() {
        val appLink = ApplinkConstInternalCategory.TRADEIN
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_CATEGORY_TRADE_IN)
    }

    @Test
    fun `check internal belanja category appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalCategory.INTERNAL_BELANJA_CATEGORY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal hotlist revamp applink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalCategory.INTERNAL_HOTLIST_REVAMP
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check final price appLink then should return DF_CATEGORY_TRADE_IN in customerapp`() {
        val appLink = ApplinkConstInternalCategory.FINAL_PRICE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_CATEGORY_TRADE_IN)
    }

    @Test
    fun `check money in internal appLink then should return DF_CATEGORY_TRADE_IN in customerapp`() {
        val appLink = ApplinkConstInternalCategory.MONEYIN_INTERNAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_CATEGORY_TRADE_IN)
    }

    @Test
    fun `check internal explore category appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalCategory.INTERNAL_EXPLORE_CATEGORY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal catalog appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalCategory.INTERNAL_CATALOG
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal find appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalCategory.INTERNAL_FIND
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal category appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalCategory.INTERNAL_CATEGORY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check profile appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.PROFILE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal affiliate appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_AFFILIATE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check play detail appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.PLAY_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check comment appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.COMMENT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal content post detail appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check kol youtube appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.KOL_YOUTUBE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check content report appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.CONTENT_REPORT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check video detail appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.VIDEO_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check media preview appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.MEDIA_PREVIEW
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check interest pick appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.INTEREST_PICK
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal affiliate create post appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal affiliate draft post appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_AFFILIATE_DRAFT_POST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check affiliate edit appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.AFFILIATE_EDIT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal content create post appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_CONTENT_CREATE_POST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal content draft post appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.INTERNAL_CONTENT_DRAFT_POST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop post edit appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalContent.SHOP_POST_EDIT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check digital sub homepage home appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check telco postpaid digital appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.TELCO_POSTPAID_DIGITAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check telco prepaid digital appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.TELCO_PREPAID_DIGITAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check digital product form appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.DIGITAL_PRODUCT_FORM
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check general template appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.GENERAL_TEMPLATE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check camera ocr appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.CAMERA_OCR
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check voucher game appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.VOUCHER_GAME
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check cart digital appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.CART_DIGITAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check digital cart external appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.DIGITAL_CART
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal smartcard emoney appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal smartcard brizzy appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check global internal digital deal appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check global internal digital deal slug appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check global internal digital deal category appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check global internal digital deal all brands appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check global internal digital deal brand detail appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check similiar search result base appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check search result appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalDiscovery.SEARCH_RESULT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check autocomplete appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalDiscovery.AUTOCOMPLETE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check home wishlist appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalHome.HOME_WISHLIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check default home recommendation appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check home recent view appLink then should return DF_MERCHANT_LOGIN in customerapp`() {
        val appLink = ApplinkConsInternalHome.HOME_RECENT_VIEW
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check ovo pay with qr entry appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check oqr pin url entry appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check ovo wallet appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.OVO_WALLET
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check saldo deposit appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.SALDO_DEPOSIT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check saldo intro appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.SALDO_INTRO
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check ovo p2 transfer form short appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.OVOP2PTRANSFERFORM_SHORT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check referral appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.REFERRAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check drop off picker appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalLogistic.DROPOFF_PICKER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shipping confirmation appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalLogistic.SHIPPING_CONFIRMATION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check order tracking appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.ORDER_TRACKING
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check manage address appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalLogistic.MANAGE_ADDRESS
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add address v1 appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalLogistic.ADD_ADDRESS_V1
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add address v2 appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalLogistic.ADD_ADDRESS_V2
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check district recommendation shop settings then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check geolocation appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.GEOLOCATION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check open shop appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.OPEN_SHOP
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check favorite appLink then should return DF_MERCHANT_LOGIN in customerapp`() {
        val appLink = ApplinkConst.FAVORITE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check report product appLink then should return DF_MERCHANT_LOGIN in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.REPORT_PRODUCT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_LOGIN)
    }

    @Test
    fun `check internal seller appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalOrder.INTERNAL_SELLER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check product manage appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConst.PRODUCT_MANAGE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check product manage list appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check seller home product manage list appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalSellerapp.SELLER_HOME_PRODUCT_MANAGE_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check power merchant subscribe appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConst.POWER_MERCHANT_SUBSCRIBE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check power merchant subscribe internal appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check shop setting base appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check topads dashboard customer appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check topads dashboard internal appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check seller transaction appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConst.SELLER_TRANSACTION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check merchant shop showcase list appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist internal appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMechant.BRANDLIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist search appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMechant.BRANDLIST_SEARCH
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist external appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.BRAND_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check brandlist with slash appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.BRAND_LIST_WITH_SLASH
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check merchant open product review appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check product add appLink then should return DF_MERCHANT_SELLER in customerapp`() {
        val appLink = ApplinkConst.PRODUCT_ADD
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_MERCHANT_SELLER)
    }

    @Test
    fun `check shop page base appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.SHOP_PAGE_BASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop etalase appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_ETALASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop etalase with keyword and sort appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_ETALASE_WITH_KEYWORD_AND_SORT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop review appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_REVIEW
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop note appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_NOTE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop info appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_INFO
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop home appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_HOME
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop settings note appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_SETTINGS_NOTE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check contact us native appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = ApplinkConst.CONTACT_US_NATIVE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check contact us appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = ApplinkConst.CONTACT_US
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check ticket detail appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = ApplinkConst.TICKET_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check internal inbox list appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = ApplinkConstInternalOperational.INTERNAL_INBOX_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check chat bot appLink then should return DF_OPERATIONAL_CONTACT_US in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.CHAT_BOT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
    }

    @Test
    fun `check payment setting appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalPayment.PAYMENT_SETTING
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check install debit bca entry pattern appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.INSTANT_DEBIT_BCA_ENTRY_PATTERN
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check edit bca one click entry pattern appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.EDIT_BCA_ONE_KLICK_ENTRY_PATTERN
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check internal toko points appLink then should return DF_PROMO_TOKOPOINTS in customerapp`() {
        val appLink = ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_PROMO_TOKOPOINTS)
    }

    @Test
    fun `check internal gamification crack appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_CRACK
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification tap tap mantap appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_TAP_TAP_MANTAP
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification smc referal appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_SMC_REFERRAL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification daily gift appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_DAILY_GIFT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check internal gamification tap tap gift appLink then should return DF_PROMO_GAMIFICATION in customerapp`() {
        val appLink = ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_TAP_TAP_GIFT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
    }

    @Test
    fun `check event home appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = ApplinkConstInternalEntertainment.EVENT_HOME
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check event pdp appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = ApplinkConstInternalEntertainment.EVENT_PDP
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check deals homepage appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = ApplinkConstInternalDeals.DEALS_HOMEPAGE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check deals brand page appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = ApplinkConstInternalDeals.DEALS_BRAND_PAGE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check deals category appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = ApplinkConst.DEALS_CATEGORY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check deals detail appLink then should return DF_ENTERTAINMENT in customerapp`() {
        val appLink = ApplinkConst.DEALS_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_ENTERTAINMENT)
    }

    @Test
    fun `check salam umrah home page appLink then should return DF_SALAM_UMRAH in customerapp`() {
        val appLink = ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_SALAM_UMRAH)
    }

    @Test
    fun `check salam order detail appLink then should return DF_SALAM_UMRAH in customerapp`() {
        val appLink = ApplinkConstInternalSalam.SALAM_ORDER_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_SALAM_UMRAH)
    }

    @Test
    fun `check travel sub homepage appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.TRAVEL_SUBHOMEPAGE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check flight external appLink then should return DF_TRAVEL in customerapp`() {
        val appLink = ApplinkConst.FLIGHT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_TRAVEL)
    }

    @Test
    fun `check flight internal appLink then should return DF_TRAVEL in customerapp`() {
        val appLink = ApplinkConstInternalTravel.INTERNAL_FLIGHT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_TRAVEL)
    }

    @Test
    fun `check hotel external appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.HOTEL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check group chat list internal play appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalPlay.GROUPCHAT_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check group chat detail internal play appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalPlay.GROUPCHAT_DETAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check promo campaign shake landing prefix appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalPromo.PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check setting profile appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.SETTING_PROFILE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add phone appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_PHONE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add email appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_EMAIL
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add bod appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_BOD
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change name appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.CHANGE_NAME
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change gender appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.CHANGE_GENDER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add name register appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change pin appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.CHANGE_PIN
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add pin onboarding appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add pin appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_PIN
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check add pin complete appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_PIN_COMPLETE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check profile completion appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.PROFILE_COMPLETION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check change phone number appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check change password appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.CHANGE_PASSWORD
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check setting bank appLink then should return DF_USER_SETTINGS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.SETTING_BANK
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_SETTINGS)
    }

    @Test
    fun `check user notification setting appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check user identification form appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check attach invoice appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.ATTACH_INVOICE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check attach voucher appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.ATTACH_VOUCHER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check order history appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.ORDER_HISTORY
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check topchat idless appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.TOPCHAT_IDLESS
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check topchat appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.TOPCHAT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check inbox talk appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.INBOX_TALK
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check shop talk appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.SHOP_TALK
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check product talk appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.PRODUCT_TALK
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check detail talk base appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.DETAIL_TALK_BASE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add talk appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.ADD_TALK
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check add fingerprint onboarding appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.ADD_FINGERPRINT_ONBOARDING
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check liveness detection appLink then should return DF_USER_LIVENESS in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.LIVENESS_DETECTION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_USER_LIVENESS)
    }

    @Test
    fun `check notification appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalNotification.NOTIFICATION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check notification buyer appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalNotification.NOTIFICATION_BUYER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check push notification troubleshooter appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalGlobal.PUSH_NOTIFICATION_TROUBLESHOOTER
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check otp appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConst.OTP
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check checkout appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.CHECKOUT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check checkout address selection appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.CHECKOUT_ADDRESS_SELECTION
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check one click checkout appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check preference list appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.PREFERENCE_LIST
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check preference edit appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.PREFERENCE_EDIT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check promo checkout marketplace appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }

    @Test
    fun `check normal checkout marketplace appLink then should return DF_BASE in customerapp`() {
        val appLink = ApplinkConstInternalMarketplace.NORMAL_CHECKOUT
        val expectedResult = getDeepLinkIdFromDeepLink(DeeplinkDFMapper.deeplinkDFPatternListCustomerApp, dfCustomerAppPath)?.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedResult?.moduleId, DeeplinkDFMapper.DF_BASE)
    }
}