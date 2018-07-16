package com.tokopedia.home.account.presentation.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;

/**
 * @author okasurya on 7/16/18.
 */
public class BuyerAccountFragment extends TkpdBaseV4Fragment {
    @Override
    protected String getScreenName() {
        return null;
    }

    public static Fragment newInstance() {
        return new BuyerAccountFragment();
    }
}
