package tokopedia

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.webview.WebViewHelper
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class AppendClientIdTest {

    protected lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        mockkObject(WebViewHelper)
    }

    @Test
    fun testEncodeNestedJsTokopedia() {
        val url =
            "https://js.tokopedia.com?url=https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https%3A%2F%2Fweb.stage.halodoc.com%2Ftanya-dokter%2Fkonsultasi-resep%3Ftitle%3DChat-Dokter%26source%3Dtokopedia%26consultation_id%3D448153%26_single%3Dtrue%26redirect_tokopedia%3Dtokopedia%253A%252F%252Fback"
        every {
            WebViewHelper.getClientId()
        } returns "123"
        val result = WebViewHelper.appendGAClientIdAsQueryParam(url, context)
        val uriResult = Uri.parse(result)
        assertEquals(uriResult.scheme, "https")
        assertEquals(uriResult.host, "js.tokopedia.com")
        val urlQuery = uriResult.getQueryParameter("url")
        assertEquals(
            "https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https%3A%2F%2Fweb.stage.halodoc.com%2Ftanya-dokter%2Fkonsultasi-resep%3Ftitle%3DChat-Dokter%26source%3Dtokopedia%26consultation_id%3D448153%26_single%3Dtrue%26redirect_tokopedia%3Dtokopedia%253A%252F%252Fback&appClientId=123",
            urlQuery
        )
        assertEquals(
            "123",
            Uri.parse(urlQuery).getQueryParameter("appClientId")
        )
    }

    @Test
    fun testEncodeNestedJsTokopedia2() {
        val url =
            "https://js.tokopedia.com?url=https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https://web.stage.halodoc.com/tanya-dokter/konsultasi-resep?title=Chat-Dokter&source=tokopedia&consultation_id=448153&_single=true&redirect_tokopedia=tokopedia://back"
        every {
            WebViewHelper.getClientId()
        } returns "123"
        val result = WebViewHelper.appendGAClientIdAsQueryParam(url, context)
        val uriResult = Uri.parse(result)
        assertEquals(uriResult.scheme, "https")
        assertEquals(uriResult.host, "js.tokopedia.com")
        val urlQuery = uriResult.getQueryParameter("url")
        assertEquals(
            "https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https%3A%2F%2Fweb.stage.halodoc.com%2Ftanya-dokter%2Fkonsultasi-resep%3Ftitle%3DChat-Dokter%26source%3Dtokopedia%26consultation_id%3D448153%26_single%3Dtrue%26redirect_tokopedia%3Dtokopedia%253A%252F%252Fback&appClientId=123",
            urlQuery
        )
        assertEquals(
            "123",
            Uri.parse(urlQuery).getQueryParameter("appClientId")
        )
    }

    @Test
    fun testAppendClientIdTokopediacom() {
        val url = "https://www.tokopedia.com/help"
        every {
            WebViewHelper.getClientId()
        } returns "123"
        val result = WebViewHelper.appendGAClientIdAsQueryParam(url, context)
        val uriResult = Uri.parse(result)
        assertEquals("https", uriResult.scheme)
        assertEquals("www.tokopedia.com", uriResult.host)
        assertEquals("/help", uriResult.path)
        assertEquals("123", uriResult.getQueryParameter("appClientId"))
    }

    @Test
    fun testAppendClientIdHasAndAfterUrl() {
        val url = "https://js.tokopedia.com/seamless?token=abc&os_type=1&uid=123&url=https%3A%2F%2Fpay.tokopedia.com%2Fv2%2Fovo%2Ftopup%2Fform%3Famount%3D0%26back_url%3Dhttps%3A%2F%2Fwww.tokopedia.com%2Fovo%26customer_email%3D%26customer_msisdn%3D123"
        every {
            WebViewHelper.getClientId()
        } returns "456"
        val result = WebViewHelper.appendGAClientIdAsQueryParam(url, context)
        val uriResult = Uri.parse(result)
        assertEquals(uriResult.scheme, "https")
        assertEquals(uriResult.host, "js.tokopedia.com")
        val urlQuery = uriResult.getQueryParameter("url")
        val uriQuery = Uri.parse(urlQuery)
        assertEquals(uriQuery.scheme, "https")
        assertEquals(uriQuery.host, "pay.tokopedia.com")
        assertEquals(uriQuery.path, "/v2/ovo/topup/form")
        assertEquals(
            "0",
            uriQuery.getQueryParameter("amount")
        )
        assertEquals(
            "https://www.tokopedia.com/ovo",
            uriQuery.getQueryParameter("back_url")
        )
        assertEquals(
            "123",
            uriQuery.getQueryParameter("customer_msisdn")
        )
        assertEquals(
            "456",
            uriQuery.getQueryParameter("appClientId")
        )
    }
}
