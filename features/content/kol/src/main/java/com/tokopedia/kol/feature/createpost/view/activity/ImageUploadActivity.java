package com.tokopedia.kol.feature.createpost.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.kol.feature.createpost.view.fragment.ImageUploadFragment;

/**
 * @author by yfsx on 08/06/18.
 */
public class ImageUploadActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return ImageUploadFragment.newInstance(getIntent().getExtras());
    }
}
