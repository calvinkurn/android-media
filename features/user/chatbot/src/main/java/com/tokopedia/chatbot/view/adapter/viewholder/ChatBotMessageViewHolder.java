package com.tokopedia.chatbot.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.chat_common.view.adapter.viewholder.MessageViewHolder;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener;
import com.tokopedia.chatbot.R;
import com.tokopedia.chatbot.view.customview.ReadMoreBottomSheet;

public class ChatBotMessageViewHolder extends MessageViewHolder {

    public static final int MESSAGE_LENGTH = 170;
    private TextView mesageBottom;
    @LayoutRes
    public static final int LAYOUT = R.layout.layout_message_chat_chatbot;
    public ChatBotMessageViewHolder(View itemView, ChatLinkHandlerListener listener) {
        super(itemView, listener);
        mesageBottom = itemView.findViewById(R.id.bottom_view);
    }

    @Override
    protected void setChatLeft(View chatBalloon) {
        super.setChatLeft(chatBalloon);
        if(message.getText().toString().length()>MESSAGE_LENGTH){
            MethodChecker.setBackground(chatBalloon,itemView.getContext().getResources().getDrawable(R.drawable.left_bubble_with_stroke));
            mesageBottom.setVisibility(View.VISIBLE);
            message.scrollTo(0,0);
            mesageBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReadMoreBottomSheet.createInstance(message.getText().toString())
                            .show(((FragmentActivity)itemView.getContext()).getSupportFragmentManager(),"read_more_bottom_sheet");
                }
            });

        } else {
            mesageBottom.setVisibility(View.GONE);
            chatBalloon.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.left_bubble));
        }
    }

    @Override
    protected void setChatRight(View chatBalloon) {
        super.setChatRight(chatBalloon);
        mesageBottom.setVisibility(View.GONE);

    }
}
