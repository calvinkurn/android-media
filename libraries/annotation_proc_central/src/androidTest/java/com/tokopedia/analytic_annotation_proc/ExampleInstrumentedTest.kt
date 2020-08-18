package com.tokopedia.analytic_annotation_proc

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.abstraction.processor.ProductListClickBundler
import com.tokopedia.abstraction.processor.ProductListClickProduct
import com.tokopedia.abstraction.processor.ProductListImpressionBundler
import com.tokopedia.abstraction.processor.ProductListImpressionProduct
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.String
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.tokopedia.analytic_annotation_proc.test", appContext.packageName)
    }

    @Test
    fun demoProductListImpression(){

        val products = ArrayList<ProductListImpressionProduct>()
        val product1 = ProductListImpressionProduct(
                "1234",
                "product name",
                null,
                "category",
                "variant",
                10500.0,
                null,
                1,
                "blast_id",
                "blast_id",
                null,
                null
        )
        products.add(product1)
        val bundle = ProductListImpressionBundler.getBundle(
                "blast_id",
                products,
                null,
                "view_item_list",
                "chat detail",
                "view on product thumbnail",
                null,
                null
        )

        System.out.println(bundle.getString("eventAction"))
        assert(bundle != null)
        assertEquals(bundle.getString("eventAction"),"view on product thumbnail")
    }

    @Test
    fun testClickProduct(){
        val products = ArrayList<ProductListClickProduct>()
        val topChatProduct = ProductListClickProduct(
                "1234",
                "product name",
                "category",
                "variant",
                null,
                10000.0,
                null,
                "/chat",
                1,
                HashMap()
        )
        products.add(topChatProduct)

        var bundle = ProductListClickBundler.getBundle(
                "blast_id",
                products,
                "chat detail",
                "view on product thumbnail",
                ProductListClickBundler.KEY,
                null,
                null,
                null
        )

        assert(bundle != null)
        assertEquals(bundle.getString("eventAction"), null)

        bundle = ProductListClickBundler.getBundle(
                "blast_id",
                products,
                "chat detail",
                "click on product thumbnail",
                ProductListClickBundler.KEY,
                null,
                null,
                null
        )

        assert(bundle != null)
        assertEquals(bundle.getString("eventAction"),"click on product thumbnail")
        assertNotEquals(bundle.getString("event"), "select_content2")
    }
}
