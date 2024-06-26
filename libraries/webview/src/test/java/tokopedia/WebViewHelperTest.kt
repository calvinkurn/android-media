package tokopedia

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.webview.WebViewHelper
import com.tokopedia.webview.WhiteListedDomains
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce
import com.tokopedia.webview.ext.encodeQueryNested
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class WebViewHelperTest {

    protected lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        mockkObject(WebViewHelper)
        every {
            WebViewHelper.getWhiteListedDomains(any())
        } answers {
            WhiteListedDomains(
                true,
                listOf(
                    "gojek.co.id",
                    "web.stage.halodoc.com"
                )
            )
        }
    }

    @Test
    fun getUrlQueryIfHasSymbol() {
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification/#/tokopedia/",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/#/tokopedia/"))
        )
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification/#/tokopedia/?title=123",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/#/tokopedia/?title=123"))
        )
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification/#/tokopedia/?title=123&a=b",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/#/tokopedia/?title=123&a=b"))
        )
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification/#/tokopedia/",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/#/tokopedia/&a=b"))
        )
    }

    @Test
    fun getUrlQueryIfHasNoSymbol() {
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification/",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/"))
        )
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification?title=123",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification?title=123"))
        )
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification?title=123",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification?title=123&a=b"))
        )
        assertEquals(
            "https://registeruat.dbank.co.id/web-verification",
            WebViewHelper.getUrlQuery(Uri.parse("tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification&a=b"))
        )
    }

    @Test
    fun testPlainUrl() {
        val url = "https://www.tokopedia.com/help"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        assertEquals("https://www.tokopedia.com/help", result)
    }

    @Test
    fun testSimpleUrl() {
        val url = "tokopedia://webview?url=https://www.tokopedia.com/help"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        assertEquals("https://www.tokopedia.com/help", result)
    }

    @Test
    fun testSimpleEncodedUrl() {
        val url = "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fhelp%2F"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        assertEquals("https://www.tokopedia.com/help/", result)
    }

    @Test
    fun testSimpleInternalUrl() {
        val url = "tokopedia-android-internal://webview?url=https://www.tokopedia.com/help"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        assertEquals("https://www.tokopedia.com/help", result)
    }

    @Test
    fun testDoubleUrl() {
        val url =
            "tokopedia://webview?url=https://js.tokopedia.com?url=https://www.tokopedia.com/help"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        val resultUri = Uri.parse(result)
        assertEquals("https", resultUri.scheme)
        assertEquals("js.tokopedia.com", resultUri.host)
        assertEquals(
            "https://www.tokopedia.com/help".encodeOnce(),
            resultUri.getQueryParameter("url")
        )
    }

    @Test
    fun testDoubleUrlWithParameter() {
        val url =
            "tokopedia://webview?url=https://js.tokopedia.com?url=https://www.tokopedia.com/help?id=4&target=5&title=3"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        val resultUri = Uri.parse(result)
        assertEquals("https", resultUri.scheme)
        assertEquals("js.tokopedia.com", resultUri.host)

        val secondUrl = resultUri.getQueryParameter("url")
        assertEquals("https://www.tokopedia.com/help?id=4&target=5&title=3".encodeOnce(), secondUrl)

        val secondUrlUri = Uri.parse(secondUrl!!.decode())
        assertEquals("4", secondUrlUri.getQueryParameter("id"))
        assertEquals("5", secondUrlUri.getQueryParameter("target"))
        assertEquals("3", secondUrlUri.getQueryParameter("title"))

        assertEquals("5", resultUri.getQueryParameter("target"))
        assertEquals("3", resultUri.getQueryParameter("title"))
    }

    @Test
    fun redirecturl_instead_url() {
        val url =
            "tokopedia://webview?url=https%3A%2F%2Ftokopedia.com%2Fovo%2Fapi%2Fv1%2Factivate%3Fredirect_url%3Dtokopedia%3A%2F%2Fback"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        assertEquals(
            "https://tokopedia.com/ovo/api/v1/activate?redirect_url=tokopedia://back",
            result
        )
    }

    @Test
    fun testDoubleUrlWithParameterAnd() {
        val url =
            "tokopedia://webview?url=https://js.tokopedia.com?url=https://www.tokopedia.com/help&target=5&title=3"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        val resultUri = Uri.parse(result)
        assertEquals("https", resultUri.scheme)
        assertEquals("js.tokopedia.com", resultUri.host)

        val secondUrl = resultUri.getQueryParameter("url")!!.decode()
        val secondUrlUri = Uri.parse(secondUrl)
        assertEquals("www.tokopedia.com", secondUrlUri.host)
        assertEquals("/help", secondUrlUri.path)

        assertEquals("5", resultUri.getQueryParameter("target"))
        assertEquals("3", resultUri.getQueryParameter("title"))
    }

    @Test
    fun testUrlWithParameterAnd() {
        val url = "tokopedia://webview?url=https://www.tokopedia.com/abc&target=5&title=3"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        val resultUri = Uri.parse(result)
        assertEquals("https", resultUri.scheme)
        assertEquals("www.tokopedia.com", resultUri.host)
        assertEquals("/abc", resultUri.path)
        Assert.assertNull(resultUri.getQueryParameter("target"))
        Assert.assertNull(resultUri.getQueryParameter("title"))
    }

    @Test
    fun testEncodeNested1() {
        val url = "tokopedia://back"
        val result = url.encodeQueryNested()
        assertEquals("tokopedia://back", result)
    }

    @Test
    fun testEncodeNested2() {
        val url =
            "https://web.stage.halodoc.com/tanya-dokter/konsultasi-resep?title=Chat-Dokter&source=tokopedia&consultation_id=448153&_single=true&redirect_tokopedia=tokopedia://back"
        val result = url.encodeQueryNested()
        assertEquals(
            "https://web.stage.halodoc.com/tanya-dokter/konsultasi-resep?title=Chat-Dokter&source=tokopedia&consultation_id=448153&_single=true&redirect_tokopedia=tokopedia%3A%2F%2Fback",
            result
        )
    }

    @Test
    fun testEncodeNested3() {
        val url =
            "https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https://web.stage.halodoc.com/tanya-dokter/konsultasi-resep?title=Chat-Dokter&source=tokopedia&consultation_id=448153&_single=true&redirect_tokopedia=tokopedia://back"
        val result = url.encodeQueryNested()
        assertEquals(
            "https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https%3A%2F%2Fweb.stage.halodoc.com%2Ftanya-dokter%2Fkonsultasi-resep%3Ftitle%3DChat-Dokter%26source%3Dtokopedia%26consultation_id%3D448153%26_single%3Dtrue%26redirect_tokopedia%3Dtokopedia%253A%252F%252Fback",
            result
        )
    }

    @Test
    fun testEncodeNested4() {
        val url =
            "https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https%3A%2F%2Fweb.stage.halodoc.com%2Ftanya-dokter%2Fkonsultasi-resep%3Ftitle%3DChat-Dokter%26source%3Dtokopedia%26consultation_id%3D448153%26_single%3Dtrue%26redirect_tokopedia%3Dtokopedia%253A%252F%252Fback"
        val result = url.encodeQueryNested()
        assertEquals(url, result)
    }

    @Test
    fun testEncodeNested5() {
        val url =
            "https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https%3A%2F%2Fweb.stage.halodoc.com%2Ftanya-dokter%2Fkonsultasi-resep%3Ftitle%3DChat-Dokter%26source%3Dtokopedia%26consultation_id%3D448153%26_single%3Dtrue%26redirect_tokopedia%3Dtokopedia%253A%252F%252Fback"
        val result = url.encodeQueryNested()
        assertEquals(url, result)
    }

    @Test
    fun testWhitelistTokopedia1() {
        val url = "https://www.tokopedia.com/help"
        assertEquals(true, WebViewHelper.isUrlWhitelisted(context, url))
    }

    @Test
    fun testWhitelistTokopedia2() {
        val url = "https://test.halo.tokopedia.com/help/123"
        assertEquals(true, WebViewHelper.isUrlWhitelisted(context, url))
    }

    @Test
    fun testWhitelistTokopediaFake() {
        val url = "https://www.tokopedia.com.fake/poc/test"
        assertEquals(false, WebViewHelper.isUrlWhitelisted(context, url))
    }

    @Test
    fun testWhitelistGojek1() {
        val url = "https://gojek.co.id/test"
        assertEquals(true, WebViewHelper.isUrlWhitelisted(context, url))
    }

    @Test
    fun testWhitelistGojek2() {
        val url = "https://test.gojek.co.id/test"
        assertEquals(true, WebViewHelper.isUrlWhitelisted(context, url))
    }

    @Test
    fun testWhitelistNotRegistered() {
        val url = "https://halodoc.com/test"
        assertEquals(false, WebViewHelper.isUrlWhitelisted(context, url))
    }
}
