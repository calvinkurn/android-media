package tokopedia

import android.os.Build
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class EncoderTest {
    @Test
    fun testEncodeOnce() {
        val url = "https://www.tokopedia.com/help"
        val encodedUrl = url.encodeOnce()
        Assert.assertEquals("https%3A%2F%2Fwww.tokopedia.com%2Fhelp", encodedUrl)
    }

    @Test
    fun testEncodeOnceTwice() {
        val url = "https://www.tokopedia.com/help"
        val encodedUrl = url.encodeOnce().encodeOnce()
        Assert.assertEquals("https%3A%2F%2Fwww.tokopedia.com%2Fhelp", encodedUrl)
    }

    @Test
    fun testDecode() {
        val url = "https%3A%2F%2Fwww.tokopedia.com%2Fhelp"
        val decoded = url.decode()
        Assert.assertEquals("https://www.tokopedia.com/help", decoded)
    }

    @Test
    fun testDecodeTwice() {
        val url = "https%3A%2F%2Fwww.tokopedia.com%2Fhelp"
        val decoded = url.decode().decode()
        Assert.assertEquals("https://www.tokopedia.com/help", decoded)
    }
}