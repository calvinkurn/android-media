package tokopedia.applink.util

import android.net.Uri
import android.os.Bundle
import com.tokopedia.applink.UriUtil
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UriUtilTest {

    @Test
    fun `buildUri correct case`() {
        val pattern = "sellerapp://shop/{shop_id}/feed"
        val id = "123"
        Assert.assertEquals("sellerapp://shop/123/feed",
                UriUtil.buildUri(pattern, id))
    }

    @Test
    fun `buildUri correct case 2`() {
        val pattern = "sellerapp://shop/{shop_id}/feed/{id}"
        val id = "123"
        val id2 = "1232"
        Assert.assertEquals("sellerapp://shop/123/feed/1232",
                UriUtil.buildUri(pattern, id, id2))
    }

    @Test
    fun `destructiveUriBundle null bundle`() {
        val pattern = "sellerapp://shop/{id}/feed/{id2}"
        val uri = Uri.parse("sellerapp://shop/123/feed/456")
        val result = UriUtil.destructiveUriBundle(pattern, uri, null)
        Assert.assertEquals("123", result?.getString("id"))
        Assert.assertEquals("456", result?.getString("id2"))
    }

    @Test
    fun `destructiveUriBundle bundle`() {
        val pattern = "sellerapp://shop/{id}/feed/{id2}"
        val uri = Uri.parse("sellerapp://shop/123/feed/456")
        val bundle = Bundle()
        bundle.putString("abc", "234")

        val result = UriUtil.destructiveUriBundle(pattern, uri, bundle)
        Assert.assertEquals("123", result?.getString("id"))
        Assert.assertEquals("456", result?.getString("id2"))
        Assert.assertEquals("234", result?.getString("abc"))
    }

    @Test
    fun `matchWithPattern correct case`() {
        val pattern = "sellerapp://shop/{shop_id}/feed"
        val uri = Uri.parse("sellerapp://shop/123/feed")
        Assert.assertEquals(arrayListOf("123"), UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern correct case double ids`() {
        val pattern = "tokopedia://shop/{shop_id}/etalase/{etalase_id}"
        val uri = Uri.parse("tokopedia://shop/1/etalase/2")
        Assert.assertEquals(arrayListOf("1", "2"), UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern no pattern`() {
        val pattern = "tokopedia://shop/"
        val uri = Uri.parse("tokopedia://shop")
        Assert.assertEquals(emptyList<String>(), UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern no pattern 2`() {
        val pattern = "tokopedia://shop/"
        val uri = Uri.parse("tokopedia://product")
        Assert.assertEquals(null, UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern if input has no id`() {
        val pattern = "tokopedia://digital/order/{order_id}"
        val uri = Uri.parse("tokopedia://digital/order/")
        Assert.assertEquals(null, UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern if input has missing id`() {
        val pattern = "tokopedia://sale/{slug}/{category_slug}"
        val uri = Uri.parse("tokopedia://sale/1/")
        Assert.assertEquals(null, UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern if scheme is different`() {
        val pattern = "sellerapp://shop/{shop_id}/feed"
        val uri = Uri.parse("tokopedia://shop/123/feed")
        Assert.assertEquals(null, UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern if path is different`() {
        val pattern = "tokopedia://s/umroh/agen/{slug}"
        val uri = Uri.parse("tokopedia://s/umroh/agent/1")
        Assert.assertEquals(null, UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern if host is different`() {
        val pattern = "tokopedia://s/umroh/agen/{slug}"
        val uri = Uri.parse("tokopedia://s2/umroh/agen/1")
        Assert.assertEquals(null, UriUtil.matchWithPattern(pattern, uri))
    }

    @Test
    fun `matchWithPattern if path has different size`() {
        val pattern = "tokopedia://s/umroh/agen/{slug}"
        val uri = Uri.parse("tokopedia://s2/umroh/agen/1/2")
        Assert.assertEquals(null, UriUtil.matchWithPattern(pattern, uri))
    }
}