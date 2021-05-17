package tokopedia.applink.teleporter

import android.net.Uri
import com.tokopedia.applink.teleporter.Teleporter
import com.tokopedia.applink.teleporter.TeleporterPattern
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TeleporterTest {

    @Test
    fun `teleporter replace id`() {
        val patternList = mutableListOf(
                TeleporterPattern("tokopedia",
                        "home",
                        "{id}",
                        "",
                        "",
                        "tokopedia-android-internal://test/abc/{id}/")
        )
        Assert.assertEquals("tokopedia-android-internal://test/abc/123/",
                Teleporter.switchIfNeeded(patternList, Uri.parse("tokopedia://home/123")))
    }

    @Test
    fun `teleporter not found`() {
        val patternList = mutableListOf(
                TeleporterPattern("tokopedia",
                        "home",
                        "{id}",
                        "",
                        "",
                        "tokopedia-android-internal://test/abc/{id}/")
        )
        Assert.assertEquals("", Teleporter.switchIfNeeded(patternList,
                Uri.parse("tokopedia://home/xyz/123")))
    }

    @Test
    fun `teleporter more than 1 pattern`() {
        val patternList = mutableListOf(
                TeleporterPattern("tokopedia",
                        "product",
                        "{id}",
                        "",
                        "",
                        "tokopedia-android-internal://product/abc/{id}/"),
                TeleporterPattern("tokopedia",
                        "home",
                        "{id}",
                        "",
                        "",
                        "tokopedia-android-internal://test/abc/{id}/")
        )
        Assert.assertEquals("tokopedia-android-internal://product/abc/123/",
                Teleporter.switchIfNeeded(patternList, Uri.parse("tokopedia://product/123")))
        Assert.assertEquals("tokopedia-android-internal://test/abc/123/",
                Teleporter.switchIfNeeded(patternList, Uri.parse("tokopedia://home/123")))
        Assert.assertEquals("",
                Teleporter.switchIfNeeded(patternList, Uri.parse("tokopedia://play/123")))
        Assert.assertEquals("",
                Teleporter.switchIfNeeded(patternList, Uri.parse("sellerapp://home/123")))
        Assert.assertEquals("",
                Teleporter.switchIfNeeded(patternList, Uri.parse("tokopedia://home/abc/123")))
    }

    @Test
    fun `teleporter more than 1 pattern duplicate host and scheme`() {
        val patternList = mutableListOf(
                TeleporterPattern("tokopedia",
                        "product",
                        "{id}",
                        "a=1",
                        "",
                        "tokopedia-android-internal://product/abc/{id}/"),
                TeleporterPattern("tokopedia",
                        "product",
                        "{id}",
                        "",
                        "",
                        "tokopedia-android-internal://test/abc/{id}/")
        )
        Assert.assertEquals("tokopedia-android-internal://product/abc/123/?a=1",
                Teleporter.switchIfNeeded(patternList, Uri.parse("tokopedia://product/123?a=1")))
        Assert.assertEquals("tokopedia-android-internal://test/abc/123/",
                Teleporter.switchIfNeeded(patternList, Uri.parse("tokopedia://product/123")))
    }

    @Test
    fun `teleporter replace path multiple`() {
        val patternList = mutableListOf(
                TeleporterPattern("tokopedia",
                        "home",
                        "abc/def/{id1}/{id2}/123",
                        "",
                        "",
                        "tokopedia-android-internal://test/abc/{id1}/{id2}/test")
        )
        val inputUrl = Uri.parse("tokopedia://home/abc/def/j/k/123")
        Assert.assertEquals("tokopedia-android-internal://test/abc/j/k/test",
                Teleporter.switchIfNeeded(patternList, inputUrl))
    }

    @Test
    fun `teleporter replace query optional`() {
        val patternList = mutableListOf(
                TeleporterPattern("tokopedia",
                        "home",
                        "{id1}/test",
                        "",
                        "p1={param1}&p2={param2}",
                        "tokopedia-android-internal://test/abc/{id1}/test?q={param1}&r={param2}")
        )
        var inputUrl = Uri.parse("tokopedia://home/my-house/test?p1=123&p2=ice-cream")
        Assert.assertEquals("tokopedia-android-internal://test/abc/my-house/test?q=123&r=ice-cream&p1=123&p2=ice-cream",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/mango/test")
        Assert.assertEquals("tokopedia-android-internal://test/abc/mango/test?q=&r=",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/strawberry/test?p1=juice")
        Assert.assertEquals("tokopedia-android-internal://test/abc/strawberry/test?q=juice&r=&p1=juice",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/banana/test?p2=boat")
        Assert.assertEquals("tokopedia-android-internal://test/abc/banana/test?q=&r=boat&p2=boat",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/coconut/test?p2=tree&p3=brown")
        Assert.assertEquals("tokopedia-android-internal://test/abc/coconut/test?q=&r=tree&p2=tree&p3=brown",
                Teleporter.switchIfNeeded(patternList, inputUrl))
    }

    @Test
    fun `teleporter replace query must have`() {
        val patternList = mutableListOf(
                TeleporterPattern("tokopedia",
                        "home",
                        "{id1}/test",
                        "p1={param1}&p2={param2}",
                        "p3={param3}&p4={param4}",
                        "tokopedia-android-internal://test/abc/{id1}/{param1}/test?r={param2}&s={param3}&t={param4}")
        )
        var inputUrl = Uri.parse("tokopedia://home/star/test?p1=fruit&p2=ice-cream")
        Assert.assertEquals("tokopedia-android-internal://test/abc/star/fruit/test?r=ice-cream&s=&t=&p1=fruit&p2=ice-cream",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/mango/test")
        Assert.assertEquals("",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/mango/test?p2=test")
        Assert.assertEquals("",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/star/test?p1=fruit&p2=ice-cream&p3=yellow&p4=white")
        Assert.assertEquals("tokopedia-android-internal://test/abc/star/fruit/test?r=ice-cream&s=yellow&t=white&p1=fruit&p2=ice-cream&p3=yellow&p4=white",
                Teleporter.switchIfNeeded(patternList, inputUrl))

        inputUrl = Uri.parse("tokopedia://home/star/test?p1=fruit&p2=ice-cream&p3=yellow&p4=white&p5=green")
        Assert.assertEquals("tokopedia-android-internal://test/abc/star/fruit/test?r=ice-cream&s=yellow&t=white&p1=fruit&p2=ice-cream&p3=yellow&p4=white&p5=green",
                Teleporter.switchIfNeeded(patternList, inputUrl))
    }


}