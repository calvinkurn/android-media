package com.tokopedia.topchat.chattemplate.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics;
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment;

public class TemplateChatActivity extends BaseSimpleActivity {

    private static final String TAG = "TEMPLATE_CHAT_FRAGMENT";
    public static final String PARAM_IS_SELLER = "PARAM_IS_SELLER";

    public static Intent createInstance(Context context, Boolean isSeller) {
        Intent intent = new Intent(context, TemplateChatActivity.class);
        intent.putExtra(PARAM_IS_SELLER, isSeller);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle(getString(R.string.title_template_chat));
        toolbar.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
    }

    @Override
    protected Fragment getNewFragment() {
        return TemplateChatFragment.createInstance(getIntent().getExtras());
    }

    @Override
    public String getScreenName() {
        return ChatTemplateAnalytics.Companions.SCREEN_TEMPLATE_CHAT_SETTING;
    }
}
