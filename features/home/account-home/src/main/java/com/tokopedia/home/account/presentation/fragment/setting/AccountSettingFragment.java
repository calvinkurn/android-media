package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class AccountSettingFragment extends TkpdBaseV4Fragment {
    private static final String TAG = AccountSettingFragment.class.getSimpleName();
    private static int REQUEST_ADD_PASSWORD = 1234;
    private static final int REQUEST_CHANGE_PASSWORD = 123;
    private UserSession userSession;

    public static Fragment createInstance() {
        return new AccountSettingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userSession = ((AbstractionRouter)context.getApplicationContext()).getSession();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.label_view_identity).setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID));
        view.findViewById(R.id.label_view_address).setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_ADDRESS_ID));
        view.findViewById(R.id.label_view_password).setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_PASS_ID));
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    public void onItemClicked(int settingId) {
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            AccountHomeRouter router = (AccountHomeRouter) getActivity().getApplication();
            switch (settingId) {
                case SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID:
                    startActivityForResult(router.getManageProfileIntent(getActivity()), 0);
                    break;
                case SettingConstant.SETTING_ACCOUNT_PASS_ID:
                    if (userSession.isHasPassword()) {
                        startActivity(router.getManagePasswordIntent(getActivity()));
                        startActivityForResult(router.getManagePasswordIntent(getActivity()), REQUEST_CHANGE_PASSWORD);
                    } else {
                        intentToAddPassword();
                    }
                    break;
                case SettingConstant.SETTING_ACCOUNT_ADDRESS_ID:
                    startActivity(router.getManageAddressIntent(getActivity()));
                    break;
                default:
                    break;
            }
        }
    }

    private void intentToAddPassword() {
        if (getActivity().getApplication() instanceof AccountHomeRouter){
            startActivityForResult(((AccountHomeRouter) getActivity().getApplication())
                    .getManagePasswordIntent(getActivity()), REQUEST_ADD_PASSWORD);
        }
    }
}
