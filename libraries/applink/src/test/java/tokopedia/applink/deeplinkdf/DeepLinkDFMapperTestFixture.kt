package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.DeeplinkDFMapper
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before

open class DeepLinkDFMapperTestFixture {

    @Before
    fun setup() {
        mockkObject(DeeplinkDFMapper)
    }

    @After
    fun finish() {
        unmockkObject(DeeplinkDFMapper)
    }

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