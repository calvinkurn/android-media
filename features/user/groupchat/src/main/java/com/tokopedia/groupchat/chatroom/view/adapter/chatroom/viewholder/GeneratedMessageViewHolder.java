package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GeneratedMessageViewModel;

/**
 * @author by nisie on 3/29/18.
 */

public class GeneratedMessageViewHolder extends BaseChatViewHolder<GeneratedMessageViewModel> {

    private TextView message;

    @LayoutRes
    public static final int LAYOUT = R.layout.generated_message_view_holder;

    public GeneratedMessageViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    @Override
    public void bind(GeneratedMessageViewModel element) {
        super.bind(element);
        message.setText(MethodChecker.fromHtml(element.getMessage()));
    }
}