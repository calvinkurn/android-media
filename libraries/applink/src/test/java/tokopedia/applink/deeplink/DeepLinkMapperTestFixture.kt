package tokopedia.applink.deeplink

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.communication.DeeplinkMapperCommunication
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.model.Always
import com.tokopedia.applink.model.DLP
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh
import com.tokopedia.applink.user.DeeplinkMapperUser
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.mockkStatic
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
            mockkObject(DeeplinkMapperCommunication)
            mockkObject(DeeplinkMapper)
            mockkObject(DeeplinkMapperUser)
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

    open fun mockReverseList() {
        every {
            DeeplinkMapper.getList(any<Map<String, MutableList<DLP>>>(), any<Uri>())
        } answers {
            val map = firstArg<Map<String, MutableList<DLP>>>()
            val uri = secondArg<Uri>()
            val list = map[uri.host ?: ""]
            if (list == null) {
                null
            } else {
                val newList = mutableListOf<DLP>()
                val (listAlways, listNotAlways) = list.partition { it.logic is Always }
                newList.addAll(listNotAlways.reversed())
                newList.addAll(listAlways)
                newList
            }
        }
    }

    protected fun assertEqualsDeepLinkMapper(deepLink: String, expectedDeepLink: String) {
        val actualResult = DeeplinkMapper.getRegisteredNavigation(context, deepLink)
        assertEquals(expectedDeepLink, actualResult)
        mockReverseList()
        val actualResultReversed = DeeplinkMapper.getRegisteredNavigation(context, deepLink)
        assertEquals(expectedDeepLink, actualResultReversed)
    }

    protected fun assertEqualsDeepLinkMapperApp(
        appType: AppType,
        deepLink: String,
        expectedDeepLink: String
    ) {
        GlobalConfig.APPLICATION_TYPE = if (appType == AppType.MAIN_APP) {
            GlobalConfig.CONSUMER_APPLICATION
        } else {
            GlobalConfig.SELLER_APPLICATION
        }
        assertEqualsDeepLinkMapper(deepLink, expectedDeepLink)
        mockReverseList()
        val actualResultReversed = DeeplinkMapper.getRegisteredNavigation(context, deepLink)
        assertEquals(expectedDeepLink, actualResultReversed)
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    protected fun assertEqualsDeeplinkParameters(
        deeplink: String,
        vararg extras: Pair<String, String?>
    ) {
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
