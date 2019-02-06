package com.tokopedia.useridentification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.useridentification.view.fragment.UserIdentificationCameraFragment;

/**
 * @author by alvinatin on 07/11/18.
 */

public class UserIdentificationCameraActivity extends BaseSimpleActivity {

    private final static String EXTRA_VIEW_MODE = "view_mode";
    private int viewMode;

    public static Intent createIntent(Context context, int viewMode) {
        Intent intent = new Intent(context, UserIdentificationCameraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_VIEW_MODE, viewMode);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null){
            viewMode = getIntent().getIntExtra(EXTRA_VIEW_MODE, 1);
        }
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        return UserIdentificationCameraFragment.createInstance(viewMode);
    }

}
