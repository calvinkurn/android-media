package com.tokopedia.phoneverification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
import com.tokopedia.network.constant.TkpdBaseURL;

/**
 * @author by nisie on 6/9/17.
 */

public class TokoCashWebViewActivity extends BaseWebViewActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getIntentCall(Context context) {
        return new Intent(context, TokoCashWebViewActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(TkpdBaseURL.URL_TOKOCASH);
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }
}
