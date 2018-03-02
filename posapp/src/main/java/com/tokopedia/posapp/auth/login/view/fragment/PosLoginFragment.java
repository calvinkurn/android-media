package com.tokopedia.posapp.auth.login.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * @author okasurya on 2/23/18.
 */

public class PosLoginFragment extends BaseDaggerFragment {
    public static Fragment newInstance() {
        return null;
    }

    @Override
    protected String getScreenName() {
        return PosLoginFragment.class.getSimpleName();
    }

    @Override
    protected void initInjector() {

    }
}
