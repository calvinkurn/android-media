package com.tokopedia.analytic_annotation_proc

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.abstraction.processor.ProductListImpressionBundler
import com.tokopedia.abstraction.processor.ProductListImpressionProduct
import org.junit.Assert.assertEquals
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
                ProductListImpressionBundler.KEY,
                "chat detail",
                "view on product thumbnail",
                null,
                null
        )

        assert(bundle.getString("eventCategory").equals("view on product thumbnail")){
            "wrong eventCategory checker"
        }

        assert(bundle.getString("eventCategory").equals("view on product thumbnail")){
            "wrong eventCategory checker"
        }
    }
}
