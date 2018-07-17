package com.tokopedia.home.account.presentation.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;

/**
 * @author okasurya on 7/16/18.
 */
public class SellerAccountFragment extends TkpdBaseV4Fragment {
    public static final String TAG = SellerAccountFragment.class.getSimpleName();

    public static Fragment newInstance() {
        return new SellerAccountFragment();
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }
}
