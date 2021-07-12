package com.tokopedia.catalog.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog.CatalogTestUtils
import com.tokopedia.catalog.model.util.CatalogUtil
import io.mockk.MockKAnnotations
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CatalogUtilsTestCase {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(CatalogUtil::class)
    }

    @Test
    fun `Analytics Sort Filter`() {
        val hashmapSearchQuery = HashMap<String,String>()
        hashmapSearchQuery["a"] = "1"
        hashmapSearchQuery["b"] = "2"
        hashmapSearchQuery["c"] = "3"
        val result = CatalogUtil.getSortFilterAnalytics(hashmapSearchQuery)
        Assert.assertEquals(result,"a=1&b=2&c=3")
    }

    @Test
    fun `Check Deeplink Extract Catalog ID parsing using segments`(){
        val arrayOfUrl = arrayListOf<Intent>()
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/apple-63/apple-iphone-4-16gb")))
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/i-mate-63/apple-iphone-4-16gb")))
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/sony ericsson-63/apple-iphone-4-16gb/abc")))
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/i-63-mate-63/apple-iphone-4-16gb")))
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/63/apple-iphone-4-16gb")))
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/63/")))// old case handled
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/63")))
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/i-mate-63")))
        arrayOfUrl.add(Intent().setData(Uri.parse("https://tokopedia.com/catalog/i-63-mate-63/")))
        arrayOfUrl.add(Intent().setData(Uri.parse(" ")))
        arrayOfUrl.add(Intent().setData(Uri.parse("")))
        // Malformed Url
        arrayOfUrl.add(Intent().setData(Uri.parse("htps://tokopedia.com/catalog/i-63-mate-63/")))
        arrayOfUrl.forEach { intent ->
            //val pathSegments = intent.data!!.pathSegments
            val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
            if (pathSegments.size > 1) {
                val catalogId = pathSegments[1]?.split("-")?.lastOrNull()?.trim() ?: ""
                Assert.assertEquals("63", catalogId)
            }
        }
    }
}