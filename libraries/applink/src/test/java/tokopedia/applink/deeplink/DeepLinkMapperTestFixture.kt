package tokopedia.applink.deeplink

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before

open class DeepLinkMapperTestFixture {

    protected lateinit var context: Context

    private var hasMock = false

    @Before
    open fun setup() {
        context = ApplicationProvider.getApplicationContext()
        if (!hasMock) {
            mockkObject(DeeplinkMapperUoh)
            mockkObject(DeeplinkMapperMerchant)
            mockkObject(DeeplinkMapperHome)
            mockkObject(DeeplinkMapperAccount)
            mockkObject(DeeplinkMapper)
            mockkObject(PowerMerchantDeepLinkMapper)
            mockkClass(GlobalConfig::class)
            mockkStatic(RemoteConfigInstance::class)
            mockkObject(FirebaseRemoteConfigInstance)
            hasMock = true
        }
        setAllowingDebugToolsFalse()
    }

    open fun setAllowingDebugToolsFalse() {
        GlobalConfig.DEBUG = false
        GlobalConfig.ENABLE_DISTRIBUTION = false
    }

    protected fun assertEqualsDeepLinkMapper(deepLink: String, expectedDeepLink: String) {
        val actualResult = DeeplinkMapper.getRegisteredNavigation(context, deepLink)
        assertEquals(expectedDeepLink, actualResult)
    }

    protected fun assertEqualsDeepLinkMapperApp(appType: AppType, deepLink: String, expectedDeepLink: String) {
        GlobalConfig.APPLICATION_TYPE = if (appType == AppType.MAIN_APP) {
            GlobalConfig.CONSUMER_APPLICATION
        } else {
            GlobalConfig.SELLER_APPLICATION
        }
        assertEqualsDeepLinkMapper(deepLink, expectedDeepLink)
        val actualResultReverse = DeeplinkMapper.getRegisteredNavigation(context, deepLink)
        assertEquals(expectedDeepLink, actualResultReverse)
    }

    protected fun assertEqualsDeeplinkParameters(deeplink: String, vararg extras: Pair<String, String?>) {
        val actualResult = DeeplinkMapper.getRegisteredNavigation(context, deeplink)
        val uri = Uri.parse(actualResult)
        extras.forEach {
            assertEquals(uri.getQueryParameter(it.first), it.second)
        }
    }

    protected fun setRemoteConfig(result: Boolean) {
        every {
            FirebaseRemoteConfigInstance.get(any() as Context)
                .getBoolean(any() as String, any() as Boolean)
        } returns result
    }
}

enum class AppType(val isMainApp: Boolean) {
    MAIN_APP(true),
    SELLER_APP(false)
}
