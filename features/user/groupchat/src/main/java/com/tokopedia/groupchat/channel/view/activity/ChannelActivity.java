package com.tokopedia.groupchat.channel.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
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

    public static Intent getChannelCallingIntent(Context context) {
        Intent intent = new Intent(context, ChannelActivity.class);
        intent.putExtra(ACTIVE_INDICATOR_POSITION, POSITION_GROUP_CHAT);
        return intent;
    }

    @DeepLink(ApplinkConst.GROUPCHAT_LIST)
    public static TaskStackBuilder getCallingTaskStack(Context context) {
        Intent homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME);
        Intent channelListIntent = getChannelCallingIntent(context);
        channelListIntent.putExtra("title", "Play");
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(channelListIntent);
        return taskStackBuilder;
    }

}
