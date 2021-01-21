package tokopedia.applink.deeplinkdf

import android.net.Uri
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.DeeplinkMatcher
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before

open class DeepLinkDFMapperTestFixture {

    private val mockUri = mockk<Uri>()
    private val mockBuilder = mockk<Uri.Builder>()
    private val mockHost = ""
    private val mockPaths = listOf<String>()

    @Before
    fun setup() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockUri
        every { Uri.parse(any()).buildUpon() } returns mockBuilder
        every { Uri.parse(any()).buildUpon().build() } returns mockUri
        every { Uri.parse(any()).buildUpon().build().host } returns mockHost
        every { Uri.parse(any()).buildUpon().build().pathSegments } returns mockPaths
        every { Uri.parse(any()).pathSegments } returns mockPaths
    }

    @After
    fun finish()  {
        unmockkStatic(Uri::class)
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