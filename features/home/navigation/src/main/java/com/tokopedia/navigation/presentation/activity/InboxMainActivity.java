package com.tokopedia.navigation.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;

/**
 * Created by meta on 10/01/19.
 */
public class InboxMainActivity extends BaseSimpleActivity {

    public static Intent start(Context context) {
        return new Intent(context, InboxMainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return InboxFragment.newInstance(false);
    }
}