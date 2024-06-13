package com.tokopedia.productcard

import android.content.Context
import android.os.Build
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnNextLayout
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.productcard.ProductConstraintLayoutUtilTest.getPrivateField
import com.tokopedia.productcard.ProductConstraintLayoutUtilTest.mockPrivateMethod
import com.tokopedia.productcard.layout.ProductConstraintLayout
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ProductConstraintLayoutTest {

    private lateinit var context: Context

    private lateinit var productConstraintLayout: ProductConstraintLayout

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        productConstraintLayout = ProductConstraintLayout(context)
        productConstraintLayout.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    @Test
    fun testSizePercentage_100_percent_ProductConstraintLayout() {
        testCalculateVisibilityAdsProductCard(left = 0, top = 0, right = 100, bottom = 100, expectedValue = 100)
    }

    @Test
    fun testSizePercentage_73_percent_ProductConstraintLayout() {
        testCalculateVisibilityAdsProductCard(left = 0, top = 10, right = 19, bottom = 0, expectedValue = 73)
    }

    @Test
    fun testSizePercentage_50_percent_ProductConstraintLayout() {
        testCalculateVisibilityAdsProductCard(50, 0, 50, 0, 50)
    }

    @Test
    fun testSizePercentage_20_percent_ProductConstraintLayout() {
        testCalculateVisibilityAdsProductCard(0, 50, 60, 0, 20)
    }

    @Test
    fun testSizePercentage_0_percent_ProductConstraintLayout() {
        testCalculateVisibilityAdsProductCard(0, 110, 0, 0, 0)
    }

    private fun testCalculateVisibilityAdsProductCard(left: Int, top: Int, right: Int, bottom: Int, expectedValue: Int) {
        productConstraintLayout.apply {
            measure(
                View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY), // WidthMeasureSpec
                View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY)  // HeightMeasureSpec
            )

            // Lay out the view
            layout(0, 0, measuredWidth, measuredHeight)
        }

        productConstraintLayout.rectf.set(left, top, right, bottom)
        productConstraintLayout.mockPrivateMethod("calculateVisibility", true, Boolean::class.java)
        productConstraintLayout.doOnNextLayout {
            val maxAreaPercentage = productConstraintLayout.getPrivateField<Int>("maxAreaPercentage")
            assertEquals(expectedValue, maxAreaPercentage)
        }
    }
}
