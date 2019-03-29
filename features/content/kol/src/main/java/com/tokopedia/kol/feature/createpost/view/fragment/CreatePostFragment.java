package com.tokopedia.kol.feature.createpost.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostFragment extends BaseDaggerFragment {

    public static CreatePostFragment newInstance(Bundle bundle) {
        CreatePostFragment fragment = new CreatePostFragment();
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
