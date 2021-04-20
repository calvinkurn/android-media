package com.tokopedia.topchat.chattemplate.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics;
import com.tokopedia.topchat.chattemplate.view.fragment.EditTemplateChatFragment;
import com.tokopedia.topchat.common.InboxMessageConstant;

public class EditTemplateChatActivity extends BaseSimpleActivity {

    private static final String TAG = "EDIT_TEMPLATE_CHAT_FRAGMENT";

    public static Intent createInstance(Context context) {
        Intent intent = new Intent(context, EditTemplateChatActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras() != null) {
            String message = getIntent().getExtras().getString(InboxMessageConstant.PARAM_MESSAGE);
            setToolbarTitle(message == null);
        }
        toolbar.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
    }

    @Override
    protected Fragment getNewFragment() {
        return EditTemplateChatFragment.createInstance(getIntent().getExtras());
    }

    private void setToolbarTitle(boolean isAdd) {
        if (isAdd) {
            toolbar.setTitle(getString(R.string.add_template_chat_title));
        } else {
            toolbar.setTitle(getString(R.string.edit_template_chat_title));
        }
    }

    @Override
    public String getScreenName() {
        return ChatTemplateAnalytics.Companions.SCREEN_TEMPLATE_CHAT_SET;
    }
}
