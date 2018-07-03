package com.tokopedia.navigation.presentation.fragment;

import android.view.View;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.base.ParentFragment;

/**
 * Created by meta on 19/06/18.
 */
public class AccountFragment extends ParentFragment {

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public int resLayout() {
        return R.layout.fragment_account;
    }

    @Override
    public void initView(View view) {
        setTitle("");
    }

    @Override
    public void loadData() {

    }

    @Override
    protected String getScreenName() {
        return "";
    }
}
