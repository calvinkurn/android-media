package com.tokopedia.webview


import android.net.Uri
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WebviewUrlParsingTest {

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
        val url = "tokopedia://webview?url=https://js.tokopedia.com?url=https://www.tokopedia.com/help"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        val resultUri = Uri.parse(result)
        assertEquals("https", resultUri.scheme)
        assertEquals("js.tokopedia.com", resultUri.host)
        assertEquals("https://www.tokopedia.com/help".encodeOnce(), resultUri.getQueryParameter("url"))
    }

    @Test
    fun testDoubleUrlWithParameter() {
        val url = "tokopedia://webview?url=https://js.tokopedia.com?url=https://www.tokopedia.com/help?id=4&target=5&title=3"
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
        val url = "tokopedia://webview?url=https%3A%2F%2Ftokopedia.com%2Fovo%2Fapi%2Fv1%2Factivate%3Fredirect_url%3Dtokopedia%3A%2F%2Fback"
        val result = WebViewHelper.getEncodedUrlCheckSecondUrl(Uri.parse(url), url)
        val resultUri = Uri.parse(result)
        assertEquals("https", resultUri.scheme)
        assertEquals("tokopedia.com", resultUri.host)
        assertEquals("/ovo/api/v1/activate", resultUri.path)
        assertEquals("tokopedia://back", resultUri.getQueryParameter("redirect_url"))
    }

    @Test
    fun testDoubleUrlWithParameterAnd() {
        val url = "tokopedia://webview?url=https://js.tokopedia.com?url=https://www.tokopedia.com/help&target=5&title=3"
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
        assertNull(resultUri.getQueryParameter("target"))
        assertNull(resultUri.getQueryParameter("title"))
    }


}