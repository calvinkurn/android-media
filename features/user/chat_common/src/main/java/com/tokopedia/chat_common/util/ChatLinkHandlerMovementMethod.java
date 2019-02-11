package com.tokopedia.chat_common.util;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener;
import com.tokopedia.design.component.ticker.SelectableSpannedMovementMethod;

import java.lang.ref.WeakReference;

/**
 * Created by Hendri on 12/04/18.
 */
public class ChatLinkHandlerMovementMethod extends SelectableSpannedMovementMethod {

    private WeakReference<ChatLinkHandlerListener> viewListener;

    public ChatLinkHandlerMovementMethod(ChatLinkHandlerListener viewListener) {
        this.viewListener = new WeakReference<>(viewListener);
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0 && viewListener != null && viewListener.get() != null) {
                if (action == MotionEvent.ACTION_UP) {
                    String clickedUrl = link[0].getURL();
                    if (viewListener.get() != null && viewListener.get().shouldHandleUrlManually
                            (clickedUrl)) {
                        viewListener.get().onGoToWebView(clickedUrl, clickedUrl);
                        return true;
                    } else if (viewListener.get().isBranchIOLink(clickedUrl)) {
                        viewListener.get().handleBranchIOLinkClick(clickedUrl);
                        return true;
                    } else {
                        return super.onTouchEvent(widget, buffer, event);
                    }
                } else {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                    return true;
                }
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }
}
