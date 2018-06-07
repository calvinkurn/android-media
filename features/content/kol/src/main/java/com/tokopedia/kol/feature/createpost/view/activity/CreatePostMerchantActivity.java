package com.tokopedia.kol.feature.createpost.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.kol.feature.createpost.view.fragment.CreatePostMerchantFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostMerchantActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return CreatePostMerchantFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
