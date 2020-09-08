package tokopedia.applink

import android.content.Context
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DeepLinkDFMapperTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var deepLinkDFMapperTest: DeeplinkDFMapper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `check applink onboarding, should return DF_BASE`() {
        val DF_BASE = "df_base"
        val actualResult = deepLinkDFMapperTest.getDFDeeplinkIfNotInstalled(context, ApplinkConstInternalMarketplace.ONBOARDING)
        assertTrue(actualResult?.equals(DF_BASE) ?: false)
    }


    class DeepLinkDF(applink: String,
                     moduleId: String
    )

}