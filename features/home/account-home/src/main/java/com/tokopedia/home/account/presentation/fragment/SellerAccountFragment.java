package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.home.account.presentation.viewmodel.AccountViewModel;

/**
 * @author okasurya on 7/16/18.
 */
public class SellerAccountFragment extends TkpdBaseV4Fragment {
    public static final String ACCOUNT_DATA = "account_data";

    public static final String TAG = SellerAccountFragment.class.getSimpleName();

    public static Fragment newInstance(AccountViewModel accountViewModel) {
        Fragment fragment = new SellerAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACCOUNT_DATA, accountViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }
}
