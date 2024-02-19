package com.tokopedia.cart.robot

import android.view.View
import androidx.core.view.isVisible
import com.tokopedia.cart.R
import org.junit.Assert.assertEquals

class CartItemViewHolderRobot(private val view: View) {

    fun assertGwpWidget(isVisible: Boolean) {
        assertEquals(isVisible, view.findViewById<View>(R.id.purchase_benefit_container).isVisible)
    }
}
