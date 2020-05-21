package com.tokopedia.product.detail.data.util

import android.net.Uri
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView
import com.tokopedia.product.detail.view.listener.ProductFullDescriptionListener

/**
 * Created by Yehezkiel on 05/05/20
 */
class ProductCustomMovementMethod(val listener: (String) -> Unit) : LinkMovementMethod() {

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x)

            val link = buffer.getSpans(off, off, URLSpan::class.java)
            if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                val url = link[0].url

                return if (isBranchIoLink(url)) {
                    listener.invoke(url)
                    true
                } else {
                    super.onTouchEvent(widget, buffer, event);
                }
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    private fun isBranchIoLink(url: String): Boolean {
        val BRANCH_IO_HOST = "tokopedia.link"
        val uri = Uri.parse(url)
        return uri.host != null && uri.host == BRANCH_IO_HOST
    }

}