package com.tokopedia.mitratoppers.preapprove.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * Created by nakama on 24/01/18.
 */

public class MitraToppersPreApproveWebViewActivity extends BaseSimpleActivity {
    public static final String EXTRA_PREAPPROVE_URL = "x_preapp_url";
    private String url;

    public static Intent getIntent(Context context, String url){
        Intent intent = new Intent(context, MitraToppersPreApproveWebViewActivity.class);
        intent.putExtra(EXTRA_PREAPPROVE_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(EXTRA_PREAPPROVE_URL);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(url);
    }

}
