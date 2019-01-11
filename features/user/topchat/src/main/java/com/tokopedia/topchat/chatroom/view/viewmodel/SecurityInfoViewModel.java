package com.tokopedia.topchat.chatroom.view.viewmodel;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;

/**
 * @author by nisie on 10/24/17.
 */

public class SecurityInfoViewModel implements Visitable<ChatRoomTypeFactory> {

    private String url = TkpdBaseURL.User.URL_SECURITY_INFO;

    public SecurityInfoViewModel(String url) {
        if (!TextUtils.isEmpty(url))
            this.url = url;
    }

    @Override
    public int type(ChatRoomTypeFactory chatRoomTypeFactory) {
        return chatRoomTypeFactory.type(this);
    }

    public String getUrl() {
        return url;
    }
}
