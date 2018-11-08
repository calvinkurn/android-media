package com.tokopedia.home.account.presentation.fragment.setting;

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
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;

import static com.tokopedia.home.account.AccountConstants.Analytics.ADDRESS_LIST;
import static com.tokopedia.home.account.AccountConstants.Analytics.PASSWORD;
import static com.tokopedia.home.account.AccountConstants.Analytics.PERSONAL_DATA;

public class AccountSettingFragment extends TkpdBaseV4Fragment {

    private static final String TAG = AccountSettingFragment.class.getSimpleName();
    private static final int REQUEST_CHANGE_PASSWORD = 123;
    private static int REQUEST_ADD_PASSWORD = 1234;
    private UserSession userSession;
    private AccountAnalytics accountAnalytics;

    public static Fragment createInstance() {
        return new AccountSettingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userSession = ((AbstractionRouter) context.getApplicationContext()).getSession();
        accountAnalytics = new AccountAnalytics(getActivity());
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
            Intent intent;
            switch (settingId) {
                case SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID:
                    accountAnalytics.eventClickAccountSetting(PERSONAL_DATA);
                    intent = RouteManager.getIntent(getActivity(), ApplinkConst.SETTING_PROFILE);
                    getActivity().startActivityForResult(intent, 0);
                    break;
                case SettingConstant.SETTING_ACCOUNT_PASS_ID:
                    accountAnalytics.eventClickAccountSetting(PASSWORD);
                    if (userSession.isHasPassword()) {
                        intent = RouteManager.getIntent(getActivity(), ApplinkConst.CHANGE_PASSWORD);
                        getActivity().startActivityForResult(intent, REQUEST_CHANGE_PASSWORD);
                    } else {
                        intentToAddPassword();
                    }
                    break;
                case SettingConstant.SETTING_ACCOUNT_ADDRESS_ID:
                    accountAnalytics.eventClickAccountSetting(ADDRESS_LIST);
                    startActivity(router.getManageAddressIntent(getActivity()));
                    break;
                default:
                    break;
            }
        }
    }

    private void intentToAddPassword() {
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            startActivityForResult(
                    ((AccountHomeRouter)getActivity().getApplicationContext())
                            .getAddPasswordIntent(getActivity()), REQUEST_CHANGE_PASSWORD);

        }
    }
}
