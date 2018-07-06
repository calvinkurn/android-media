package com.tokopedia.groupchat.chatroom.domain.usecase;

import android.content.Context;
import android.util.Log;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;
import com.tokopedia.groupchat.chatroom.domain.mapper.SendMessageMapper;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler;

import javax.inject.Inject;

/**
 * @author by nisie on 2/14/18.
 */

public class SendGroupChatMessageUseCase {

    private final SendMessageMapper sendMessageMapper;

    public interface SendGroupChatMessageListener {
        void onSuccessSendMessage(ChatViewModel chatViewModel);

        void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, String errorMessage);
    }

    @Inject
    public SendGroupChatMessageUseCase(SendMessageMapper sendMessageMapper) {
        this.sendMessageMapper = sendMessageMapper;
    }

    public void execute(final Context context, final PendingChatViewModel pendingChatViewModel, final OpenChannel mChannel,
                        final SendGroupChatMessageListener listener) {

        mChannel.sendUserMessage(pendingChatViewModel.getMessage(), new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    listener.onErrorSendMessage(pendingChatViewModel, GroupChatErrorHandler
                            .getSendBirdErrorMessage(context, e, true));
                    return;
                }

                listener.onSuccessSendMessage(sendMessageMapper.map(userMessage));
            }
        });
    }


}
