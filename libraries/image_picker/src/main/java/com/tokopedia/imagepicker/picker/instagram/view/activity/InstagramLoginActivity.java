package com.tokopedia.imagepicker.picker.instagram.view.activity;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.imagepicker.picker.instagram.view.fragment.InstagramLoginFragment;

/**
 * Created by zulfikarrahman on 6/8/18.
 */

public class InstagramLoginActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return new InstagramLoginFragment();
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
