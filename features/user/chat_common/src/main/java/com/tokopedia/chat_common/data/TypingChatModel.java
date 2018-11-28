package com.tokopedia.chat_common.data;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class TypingChatModel implements Visitable<BaseChatTypeFactory> {

    String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int type(BaseChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
