package com.tokopedia.kol.feature.createpost.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostKolFragment extends BaseDaggerFragment {

    public static CreatePostKolFragment newInstance(Bundle bundle) {
        CreatePostKolFragment fragment = new CreatePostKolFragment();
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
