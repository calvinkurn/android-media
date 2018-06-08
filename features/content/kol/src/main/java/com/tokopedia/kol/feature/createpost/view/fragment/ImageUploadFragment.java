package com.tokopedia.kol.feature.createpost.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class ImageUploadFragment extends BaseDaggerFragment {

    public static ImageUploadFragment newInstance(Bundle bundle) {
        ImageUploadFragment fragment = new ImageUploadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
