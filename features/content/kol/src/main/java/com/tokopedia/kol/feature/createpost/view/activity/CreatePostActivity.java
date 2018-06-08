package com.tokopedia.kol.feature.createpost.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.kol.feature.createpost.view.fragment.CreatePostFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return CreatePostFragment.newInstance(getIntent().getExtras());
    }
}
