package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class AccountSettingFragment extends BaseGeneralSettingFragment {
    private static final String TAG = AccountSettingFragment.class.getSimpleName();
    private static final int REQUEST_CHANGE_PASSWORD = 1234;

    public static Fragment createInstance() {
        return new AccountSettingFragment();
    }

    @Override
    protected List<SettingItemViewModel> getSettingItems() {
        List<SettingItemViewModel> settingItems = new ArrayList<>();

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID,
                getString(R.string.title_personal_data_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_ACCOUNT_PASS_ID,
                getString(R.string.title_password_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_ACCOUNT_ADDRESS_ID,
                getString(R.string.title_address_setting)));

        return settingItems;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void onItemClicked(int settingId) {
        Intent intent;

        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            AccountHomeRouter router = (AccountHomeRouter) getActivity().getApplication();
            switch (settingId) {
                case SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID:
                    startActivityForResult(router.getManageProfileIntent(getActivity()), 0);
                    break;
                case SettingConstant.SETTING_ACCOUNT_PASS_ID:
                    if (userSession.isHasPassword()) {
                        startActivity(router.getManagePasswordIntent(getActivity()));
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
                    .getManagePasswordIntent(getActivity()), REQUEST_CHANGE_PASSWORD);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.message_success_change_profile));
        }
    }
}
