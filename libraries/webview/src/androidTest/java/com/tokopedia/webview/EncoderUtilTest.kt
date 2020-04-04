package com.tokopedia.webview


import androidx.test.runner.AndroidJUnit4
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncoderUtilTest {

    @Test
    fun testEncodeOnce() {
        val url = "https://www.tokopedia.com/help"
        val encodedUrl = url.encodeOnce()
        assertEquals("https%3A%2F%2Fwww.tokopedia.com%2Fhelp", encodedUrl)
    }

    @Test
    fun testEncodeOnceTwice() {
        val url = "https://www.tokopedia.com/help"
        val encodedUrl = url.encodeOnce().encodeOnce()
        assertEquals("https%3A%2F%2Fwww.tokopedia.com%2Fhelp", encodedUrl)
    }

    @Test
    fun testDecode() {
        val url = "https%3A%2F%2Fwww.tokopedia.com%2Fhelp"
        val decoded = url.decode()
        assertEquals("https://www.tokopedia.com/help", decoded)
    }

    @Test
    fun testDecodeTwice() {
        val url = "https%3A%2F%2Fwww.tokopedia.com%2Fhelp"
        val decoded = url.decode().decode()
        assertEquals("https://www.tokopedia.com/help", decoded)
    }



}