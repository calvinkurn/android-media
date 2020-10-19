package tokopedia.applink.deeplink

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.order.DeeplinkMapperUohOrder
import com.tokopedia.config.GlobalConfig
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before

open class DeepLinkMapperTestFixture {

    protected lateinit var context: Context

    @Before
    open fun setup() {
        context = ApplicationProvider.getApplicationContext()
        mockkObject(DeeplinkMapperUohOrder)
        mockkClass(GlobalConfig::class)
    }

    @After
    open fun finish() {
        unmockkAll()
    }

    protected fun assertEqualsDeepLinkMapper(deepLink: String, expectedDeepLink: String) {
        val actualResult = DeeplinkMapper.getRegisteredNavigation(context, deepLink)
        assertEquals(expectedDeepLink, actualResult)
    }
}