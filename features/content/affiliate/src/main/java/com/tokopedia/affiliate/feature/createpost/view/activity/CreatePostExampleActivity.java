package com.tokopedia.affiliate.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.createpost.view.fragment.CreatePostExampleFragment;

public class CreatePostExampleActivity extends BaseSimpleActivity {

    public static final String PARAM_TITLE = "title";
    public static final String PARAM_IMAGE = "image";

    public static Intent createIntent(Context context, String image, String title) {
        Intent intent = new Intent(context, CreatePostExampleActivity.class);
        intent.putExtra(PARAM_IMAGE, image);
        intent.putExtra(PARAM_TITLE, title);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return CreatePostExampleFragment.createInstance(bundle);
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
