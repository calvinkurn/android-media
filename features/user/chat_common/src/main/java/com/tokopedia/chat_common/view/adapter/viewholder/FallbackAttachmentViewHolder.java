package com.tokopedia.chat_common.view.adapter.viewholder;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel;
import com.tokopedia.chat_common.data.MessageViewModel;
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

    private Context context;
    private TextView message;

    public FallbackAttachmentViewHolder(View itemView, ChatLinkHandlerListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        message = itemView.findViewById(R.id.message);
    }

    @Override
    public void bind(FallbackAttachmentViewModel viewModel) {
        super.bind(viewModel);
        setMessage(viewModel);
        setClickableUrl();
        setupChatBubbleAlignment(message, viewModel);
    }

    private void setupChatBubbleAlignment(View chatBalloon, FallbackAttachmentViewModel element) {
        if (element.isSender()) {
            setChatRight(chatBalloon);
        } else {
            setChatLeft(chatBalloon);
        }
    }

    private void setChatLeft(View chatBalloon) {
        chatBalloon.setBackground(context.getResources().getDrawable(R.drawable
                .left_bubble));
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon);
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, hour);
        message.setTextColor(MethodChecker.getColor(context, R.color.black_70));
        message.setLinkTextColor(MethodChecker.getColor(context, R.color.black_70));
    }

    private void setChatRight(View chatBalloon) {
        chatBalloon.setBackground(context.getResources().getDrawable(R.drawable
                .right_bubble));
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon);
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, hour);
        message.setTextColor(MethodChecker.getColor(context, R.color.Unify_N0));
        message.setLinkTextColor(MethodChecker.getColor(context, R.color.Unify_N0));
    }

    private void setAlignParent(int alignment, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        params.addRule(alignment);
        view.setLayoutParams(params);
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
