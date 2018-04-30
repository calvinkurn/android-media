package com.tokopedia.groupchat.chatroom.domain.usecase;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

/**
 * @author by nisie on 2/14/18.
 */

public class LoginGroupChatUseCase {

    private static final String GROUP_CHAT_SP = "GROUP_CHAT";
    private static final String GROUP_CHAT_ANON_USER_ID = "GROUP_CHAT_ANON_USER_ID";

    public interface LoginGroupChatListener {
        void onSuccessEnterChannel(OpenChannel openChannel);

        void onErrorEnterChannel(String errorMessage);

        void onUserBanned(String errorMessage);

        void onChannelNotFound(String sendBirdErrorMessage);
    }

    @Inject
    public LoginGroupChatUseCase() {
    }

    public void execute(final Context context, final String channelUrl,
                        String userId, final String userName, final String userAvatar,
                        final LoginGroupChatListener listener, String sendBirdToken) {
        if (TextUtils.isEmpty(userId)) {
            userId = getAnonymousSendbirdUserId(context);
        }
        SendBird.connect(userId, sendBirdToken, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    listener.onErrorEnterChannel(GroupChatErrorHandler
                            .getSendBirdErrorMessage(context, e, false));
                    return;
                }

                SendBird.updateCurrentUserInfo(userName, userAvatar, new SendBird.UserInfoUpdateHandler() {
                    @Override
                    public void onUpdated(SendBirdException e) {
                        if (e != null) {
                            listener.onErrorEnterChannel(GroupChatErrorHandler
                                    .getSendBirdErrorMessage(context, e, false));
                            return;
                        }

                        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
                            @Override
                            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                                if (e != null && e.getCode() == GroupChatErrorHandler
                                        .CHANNEL_NOT_FOUND) {
                                    listener.onChannelNotFound(GroupChatErrorHandler
                                            .getSendBirdErrorMessage(context, e, false));
                                    return;
                                } else if (e != null) {
                                    listener.onErrorEnterChannel(GroupChatErrorHandler
                                            .getSendBirdErrorMessage(context, e, false));
                                    return;
                                }

                                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {

                                    @Override
                                    public void onResult(SendBirdException e) {
                                        if (e != null && e.getCode() == GroupChatErrorHandler
                                                .USER_IS_BANNED) {
                                            listener.onUserBanned(GroupChatErrorHandler
                                                    .getSendBirdErrorMessage(context, e, false));
                                            return;
                                        } else if (e != null) {
                                            listener.onErrorEnterChannel(GroupChatErrorHandler
                                                    .getSendBirdErrorMessage(context, e, false));
                                            return;
                                        }

                                        listener.onSuccessEnterChannel(openChannel);

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    private String getAnonymousSendbirdUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUP_CHAT_SP, Context
                .MODE_PRIVATE);

        String anonUserId = sharedPreferences.getString(GROUP_CHAT_ANON_USER_ID, "");

        if (TextUtils.isEmpty(anonUserId)) {
            anonUserId = context.getString(R.string.anonymous) + UUID.randomUUID() + new
                    Date().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(GROUP_CHAT_ANON_USER_ID, anonUserId);
            editor.apply();

            return anonUserId;
        } else {
            return anonUserId;
        }
    }
}
