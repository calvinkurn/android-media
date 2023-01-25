package com.tokopedia.cart.robot

import android.view.View
import com.tokopedia.cart.R
import com.tokopedia.unifyprinciples.Typography
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

class CartShopViewHolderRobot(private val view: View) {

    // Bebas Ongkir Extra
    fun assertShowTokoCabangInfo() {
        assertEquals("TokoCabang", view.findViewById<Typography>(R.id.tv_fulfill_district).text)
        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.iu_image_fulfill).visibility)
    }

    fun assertNotShowTokoCabangInfo() {
        assertNotEquals("TokoCabang", view.findViewById<Typography>(R.id.tv_fulfill_district).text)
        assertEquals(View.GONE, view.findViewById<View>(R.id.iu_image_fulfill).visibility)
    }

    fun assertShowFreeShippingImage() {
        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.img_free_shipping).visibility)
        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.separator_free_shipping).visibility)
    }

    fun assertNotShowFreeShippingImage() {
        assertEquals(View.GONE, view.findViewById<View>(R.id.img_free_shipping).visibility)
        assertEquals(View.GONE, view.findViewById<View>(R.id.separator_free_shipping).visibility)
    }

    // BO Affordability
    fun assertShowBoAffordabilityTicker() {
        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.layout_cart_shop_group_ticker).visibility)
    }

    fun assertNotShowBoAffordabilityTicker() {
        assertEquals(View.GONE, view.findViewById<View>(R.id.layout_cart_shop_group_ticker).visibility)
    }
}
