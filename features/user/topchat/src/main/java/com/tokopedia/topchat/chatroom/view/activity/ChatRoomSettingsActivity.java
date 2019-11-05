package com.tokopedia.topchat.chatroom.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomSettingsFragment;
import com.tokopedia.topchat.common.InboxChatConstant;

public class ChatRoomSettingsActivity extends BaseSimpleActivity {


    public static Intent getIntent(Context context, String messageId,
                                   ChatSettingsResponse chatSettingsResponse,
                                   boolean isChatEnabled, String role, String senderName, int shopId) {
        Intent intent = new Intent(context, ChatRoomSettingsActivity.class);
        intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId);
        intent.putExtra(ApplinkConst.Chat.SHOP_ID, shopId);
        intent.putExtra(InboxChatConstant.CHATRESPONSEMODEL, chatSettingsResponse);
        intent.putExtra(InboxChatConstant.CHAT_ENABLED, isChatEnabled);
        intent.putExtra(InboxChatConstant.CHAT_ROLE, role);
        intent.putExtra(InboxChatConstant.SENDER_NAME, senderName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(com.tokopedia.topchat.R.string.chat_incoming_settings) + " "
                + getIntent().getStringExtra(InboxChatConstant.SENDER_NAME));
    }

    @Override
    protected Fragment getNewFragment() {
        return ChatRoomSettingsFragment.createInstance(getIntent().getExtras());
    }
}

