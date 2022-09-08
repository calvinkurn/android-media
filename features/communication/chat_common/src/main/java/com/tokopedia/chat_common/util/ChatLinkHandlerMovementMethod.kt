package com.tokopedia.chat_common.util

import android.text.Selection
import android.text.Spannable
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import java.lang.ref.WeakReference

/**
 * Created by Hendri on 12/04/18.
 */
class ChatLinkHandlerMovementMethod(
    viewListener: ChatLinkHandlerListener?
) : SelectableSpannedMovementMethod() {

    private val viewListener: WeakReference<ChatLinkHandlerListener?>?

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
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
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val link = buffer.getSpans(off, off, URLSpan::class.java)
            if (link.isNotEmpty() && viewListener != null && viewListener.get() != null) {
                return if (action == MotionEvent.ACTION_UP) {
                    val clickedUrl = link[0].url
                    val reference = viewListener.get()
                    if (viewListener.get() != null &&
                        reference?.shouldHandleUrlManually(clickedUrl) == true
                    ) {
                        reference.onGoToWebView(clickedUrl, clickedUrl)
                        true
                    } else if (reference?.isBranchIOLink(clickedUrl) == true) {
                        reference.handleBranchIOLinkClick(clickedUrl)
                        true
                    } else {
                        super.onTouchEvent(widget, buffer, event)
                    }
                } else {
                    Selection.setSelection(
                        buffer,
                        buffer.getSpanStart(link[0]),
                        buffer.getSpanEnd(link[0])
                    )
                    true
                }
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    init {
        this.viewListener = WeakReference(viewListener)
    }
}