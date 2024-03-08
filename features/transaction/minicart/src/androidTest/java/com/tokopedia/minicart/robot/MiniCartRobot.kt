package com.tokopedia.minicart.robot

import android.view.View
import com.tokopedia.minicart.v2.MiniCartV2Widget
import org.junit.Assert.assertEquals

fun miniCartWidget(widget: MiniCartV2Widget, func: MiniCartRobot.() -> Unit) {
    MiniCartRobot(widget).apply(func)
}

class MiniCartRobot(private val widget: MiniCartV2Widget) {

    fun assertWidgetVisible() {
        assertEquals(View.VISIBLE, widget.visibility)
    }

    fun assertWidgetTotalAmount(total: String) {
        assertEquals(total, widget.binding?.miniCartTotalAmount?.amountView?.text?.toString())
    }

    fun assertButtonEnabled(isEnabled: Boolean) {
        assertEquals(isEnabled, widget.binding?.miniCartTotalAmount?.amountCtaView?.isEnabled)
    }
}
