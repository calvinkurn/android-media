package com.tokopedia.chatbot.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.chat_common.data.MessageViewModel;
import com.tokopedia.chat_common.view.adapter.viewholder.MessageViewHolder;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener;
import com.tokopedia.chatbot.EllipsizeMaker;
import com.tokopedia.chatbot.R;
import com.tokopedia.chatbot.util.ChatBotTimeConverter;
import com.tokopedia.chatbot.view.customview.ReadMoreBottomSheet;

import static com.tokopedia.chatbot.EllipsizeMaker.MESSAGE_LINE_COUNT;

public class ChatBotMessageViewHolder extends MessageViewHolder {

    private TextView mesageBottom;
    private String htmlMessage;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_message_chat_chatbot;
    public ChatBotMessageViewHolder(View itemView, ChatLinkHandlerListener listener) {
        super(itemView, listener);
        mesageBottom = itemView.findViewById(R.id.bottom_view);
    }

    @Override
    public void bind(MessageViewModel element) {
        super.bind(element);
        htmlMessage = element.getMessage();
    }

    @Override
    protected void setChatLeft(View chatBalloon) {
        super.setChatLeft(chatBalloon);
        message.post(() -> {
            if (message.getLineCount() >= MESSAGE_LINE_COUNT) {
                message.setMaxLines(MESSAGE_LINE_COUNT);
                message.setText(EllipsizeMaker.INSTANCE.getTruncatedMsg(message));
                MethodChecker.setBackground(chatBalloon, ContextCompat.getDrawable(itemView.getContext(),R.drawable.left_bubble_with_stroke));
                mesageBottom.setVisibility(View.VISIBLE);
                mesageBottom.setOnClickListener((View v) -> {
                    ReadMoreBottomSheet.createInstance(htmlMessage)
                            .show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), "read_more_bottom_sheet");
                });

            } else {
                mesageBottom.setVisibility(View.GONE);
                MethodChecker.setBackground(chatBalloon, ContextCompat.getDrawable(itemView.getContext(),com.tokopedia.chat_common.R.drawable.left_bubble));
            }
        });

    }

    @Override
    protected boolean alwaysShowTime() {
        return true;
    }

    @Override
    protected String getHourTime(String replyTime) {
        return ChatBotTimeConverter.INSTANCE.getHourTime(replyTime);
    }

    @Override
    protected void setChatRight(View chatBalloon) {
        super.setChatRight(chatBalloon);
        message.setMaxLines(Integer.MAX_VALUE);
        mesageBottom.setVisibility(View.GONE);

    }

    @Override
    protected int getMessageId() {
        return R.id.message;
    }

    @Override
    protected int getChatStatusId() {
        return R.id.chat_status;
    }

    @Override
    protected int getNameId() {
        return R.id.name;
    }

    @Override
    protected int getLabelId() {
        return R.id.label;
    }

    @Override
    protected int getDotId() {
        return R.id.dot;
    }

    @Override
    protected int getMainId() {
        return R.id.main;
    }

    @Override
    protected int getHourId() {
        return R.id.hour;
    }

    @Override
    protected int getDateId() {
        return R.id.date;
    }
}
