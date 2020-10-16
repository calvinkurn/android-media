package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.DeeplinkDFMapper
import org.junit.Assert.assertEquals

open class DeepLinkDFMapperTestFixture {

    protected fun assertEqualDeepLinkCustomerApp(appLink: String, expectedModuleId: String) {
        val actualResult = DeeplinkDFMapper.deeplinkDFPatternListCustomerApp.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedModuleId, actualResult?.moduleId)
    }

    protected fun assertEqualDeepLinkSellerApp(appLink: String, expectedModuleId: String) {
        val actualResult = DeeplinkDFMapper.deeplinkDFPatternListSellerApp.firstOrNull {
            it.logic(appLink)
        }
        assertEquals(expectedModuleId, actualResult?.moduleId)
    }
}