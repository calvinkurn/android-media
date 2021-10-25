package com.tokopedia.topchat.chatroom.view.adapter.util

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener

class MessageOnTouchListener(
    private val viewListener: ChatLinkHandlerListener
) : View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) return false
        val textView = v as? TextView ?: return false
        val action = event.action
        if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_DOWN
        ) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= textView.totalPaddingLeft
            y -= textView.totalPaddingTop
            x += textView.scrollX
            y += textView.scrollY
            val layout = textView.layout
            val line = layout.getLineForVertical(y)
            val buffer = Spannable.Factory.getInstance().newSpannable(textView.text)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            if (buffer.length >= off) {
                val link = buffer.getSpans(off, off, URLSpan::class.java)
                if (link.isNotEmpty()) {
                    if (action == MotionEvent.ACTION_UP) {
                        val clickedUrl = link[0].url
                        when {
                            viewListener.shouldHandleUrlManually(clickedUrl) -> {
                                viewListener.onGoToWebView(clickedUrl, clickedUrl)
                                return true
                            }
                            viewListener.isBranchIOLink(clickedUrl) -> {
                                viewListener.handleBranchIOLinkClick(clickedUrl)
                                return true
                            }
                            else -> {
                                link[0].onClick(textView)
                                return true
                            }
                        }
                    }
                    return true
                }
            }
        }
        return false
    }

}