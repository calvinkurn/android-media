package com.tokopedia.kol.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.kol.feature.createpost.view.fragment.CreatePostFragment;
import com.tokopedia.kol.feature.createpost.view.fragment.CreatePostWebviewFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostActivity extends BaseSimpleActivity {

    public static final String IMAGE_URL = "image_url";
    public static final String FORM_URL = "form_url";

    public static Intent getInstanceWebView(Context context, String formUrl) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FORM_URL,formUrl);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent().getExtras() != null) {
            if (!TextUtils.isEmpty(getIntent().getExtras().getString(FORM_URL))) {
                return CreatePostWebviewFragment.newInstance(getIntent().getExtras());
            }
        }
        return CreatePostFragment.newInstance(getIntent().getExtras());
    }
}
