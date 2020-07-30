package com.tokopedia.groupchat.channel.view.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.groupchat.channel.view.fragment.ChannelFragment;

public class ChannelActivity extends BaseSimpleActivity {

    public static final int RESULT_ERROR_ENTER_CHANNEL = 101;
    public static final String RESULT_MESSAGE = "result_message";

    private static final int POSITION_GROUP_CHAT = 1;
    private static final String ACTIVE_INDICATOR_POSITION = "active";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return ChannelFragment.createInstance();
    }

}
