package com.tokopedia.topchat.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class TypingChatModel implements Visitable<ChatRoomTypeFactory> {

    String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int type(ChatRoomTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

}
