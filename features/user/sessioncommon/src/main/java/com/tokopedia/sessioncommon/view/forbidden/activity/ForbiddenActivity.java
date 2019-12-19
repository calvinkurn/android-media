package com.tokopedia.sessioncommon.view.forbidden.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.sessioncommon.view.forbidden.fragment.ForbiddenFragment;

/**
 * Created by meyta on 2/22/18.
 */

public class ForbiddenActivity extends BaseSimpleActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ForbiddenActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getNewFragment() {
        return ForbiddenFragment.createInstance();
    }
}