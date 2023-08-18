package tokopedia.applink.deeplinkmatcher

import android.net.Uri
import com.tokopedia.applink.DeeplinkMatcher
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before

open class DeepLinkMatcherTestFixture {

    private lateinit var deepLinkMatcher: DeeplinkMatcher

    private var hasMock = false

    @Before
    fun setup() {
        if (!hasMock) {
            mockkStatic(Uri::class)
            hasMock = true
        }
        deepLinkMatcher = DeeplinkMatcher()
    }

    protected fun assertEqualsDeepLinkMatcher(expectedIdDeepLinkChecker: Int, url: String) {
        val uri = parseToUri(url)
        val actualResult = deepLinkMatcher.match(uri)
        assertEquals(expectedIdDeepLinkChecker, actualResult)
    }

    private fun parseToUri(url: String): Uri {
        return Uri.parse(url)
    }
}