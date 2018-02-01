package com.tokopedia.tkpdchat.common;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpdchat.common.di.ChatComponent;
import com.tokopedia.tkpdchat.common.di.DaggerChatComponent;


/**
 * @author by nisie on 2/1/18.
 */

public abstract class BaseChatActivity extends BaseSimpleActivity {

    private ChatComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    private void initInjector() {
        getChatComponent().inject(this);
    }

    protected ChatComponent getChatComponent() {
        if (component == null) {
            component = DaggerChatComponent.getFlightComponent(getApplication());
        }
        return component;
    }
}
