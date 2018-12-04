package com.tokopedia.chat_common.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel;
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener;
import com.tokopedia.chat_common.R;

/**
 * @author by nisie on 5/9/18.
 */
public class FallbackAttachmentViewHolder extends BaseChatViewHolder<FallbackAttachmentViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_fallback_attachment;
    private final ChatLinkHandlerListener listener;

    private TextView message;

    public FallbackAttachmentViewHolder(View itemView, ChatLinkHandlerListener listener) {
        super(itemView);
        this.listener = listener;
        message = itemView.findViewById(R.id.message);
    }

    @Override
    public void bind(FallbackAttachmentViewModel viewModel) {
        super.bind(viewModel);
        setMessage(viewModel);
        setClickableUrl();
    }

    private void setMessage(FallbackAttachmentViewModel element) {
        if (!element.getMessage().isEmpty()) {
            message.setText(MethodChecker.fromHtml(element.getMessage()));
        }
    }

    private void setClickableUrl() {
        message.setMovementMethod(new ChatLinkHandlerMovementMethod(listener));
    }
}
