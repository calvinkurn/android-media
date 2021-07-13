package com.tokopedia.topchat.chatroom.view.adapter.util

import android.text.Selection
import android.text.Spannable
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener

class MessageOnTouchListener(
    private val viewListener: ChatLinkHandlerListener
) : View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) return false
        val widget = v as? TextView ?: return false
        val action = event.action
        if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_DOWN
        ) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val buffer = Spannable.Factory.getInstance().newSpannable(widget.text)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val link = buffer.getSpans(off, off, URLSpan::class.java)
            if (link.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    val clickedUrl = link[0].url
                    when {
                        viewListener.shouldHandleUrlManually(clickedUrl) -> {
                            viewListener.onGoToWebView(clickedUrl, clickedUrl)
                            notifyLinkHit(widget)
                            return true
                        }
                        viewListener.isBranchIOLink(clickedUrl) -> {
                            viewListener.handleBranchIOLinkClick(clickedUrl)
                            notifyLinkHit(widget)
                            return true
                        }
                    }
                } else {
                    Selection.setSelection(
                        buffer,
                        buffer.getSpanStart(link[0]),
                        buffer.getSpanEnd(link[0])
                    )
                    notifyLinkHit(widget)
                    return true
                }
            }
        }
        return false
    }

    private fun notifyLinkHit(widget: TextView) {
        Toast.makeText(widget.context, "LInk Hit True", Toast.LENGTH_SHORT).show()
    }
}