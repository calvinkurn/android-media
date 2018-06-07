package com.tokopedia.kol.feature.createpost.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.kol.feature.createpost.view.fragment.CreatePostKolFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostKolActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return CreatePostKolFragment.newInstance(getIntent().getExtras());
    }
}
