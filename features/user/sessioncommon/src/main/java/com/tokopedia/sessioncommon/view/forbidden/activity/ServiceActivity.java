package com.tokopedia.sessioncommon.view.forbidden.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.sessioncommon.view.forbidden.fragment.ServiceFragment;

/**
 * Created by nakama on 28/02/18.
 */

public class ServiceActivity extends BaseSimpleActivity {

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, ServiceActivity.class);
        intent.putExtra(ServiceActivity.class.getSimpleName(), url);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getNewFragment() {
        String url = getIntent().getStringExtra(ServiceActivity.class.getSimpleName());
        return ServiceFragment.createInstance(url);
    }
}
