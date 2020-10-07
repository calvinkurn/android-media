package tokopedia.applink.deeplink

import android.app.Activity
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.order.DeeplinkMapperUohOrder
import com.tokopedia.config.GlobalConfig
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

open class DeepLinkMapperTestFixture {

    private lateinit var activityController: ActivityController<Activity>

    @Before
    open fun setup() {
        activityController = Robolectric.buildActivity(Activity::class.java)
        mockkObject(DeeplinkMapper)
        mockkObject(DeeplinkMapperUohOrder)
        mockkClass(GlobalConfig::class)
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun assertEqualsDeepLinkMapper(deepLink: String, actualDeepLink: String) {
        val expectedResult = DeeplinkMapper.getRegisteredNavigation(activityController.get(), deepLink)
        assertEquals(expectedResult, actualDeepLink)
    }
}