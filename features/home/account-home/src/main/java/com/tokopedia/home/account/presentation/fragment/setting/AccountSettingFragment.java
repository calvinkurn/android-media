package com.tokopedia.home.account.presentation.fragment.setting;

import android.support.v4.app.Fragment;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class AccountSettingFragment extends BaseGeneralSettingFragment {
    private static final String TAG = AccountSettingFragment.class.getSimpleName();

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

    }
}
