package com.tokopedia.topchat.chattemplate.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics;
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment;

public class TemplateChatActivity extends BaseSimpleActivity {

    private static final String TAG = "TEMPLATE_CHAT_FRAGMENT";

    public static Intent createInstance(Context context) {
        Intent intent = new Intent(context, TemplateChatActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle(getString(R.string.drawer_title_setting));
    }

    @Override
    protected Fragment getNewFragment() {
        return TemplateChatFragment.createInstance(getIntent().getExtras());
    }

    @Override
    public String getScreenName() {
        return ChatTemplateAnalytics.Companion.SCREEN_TEMPLATE_CHAT_SETTING;
    }
}
