package com.tokopedia.home.account.presentation.fragment.setting;

import android.support.v4.app.Fragment;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class TkpdPaySettingFragment extends BaseGeneralSettingFragment{
    public static Fragment createInstance() {
        return new TkpdPaySettingFragment();
    }

    private static final String TAG = TkpdPaySettingFragment.class.getSimpleName();

    @Override
    protected List<SettingItemViewModel> getSettingItems() {
        List<SettingItemViewModel> settingItems = new ArrayList<>();

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TOKOCASH_ID,
                getString(R.string.title_tokocash_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SALDO_ID,
                getString(R.string.title_saldo_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_BANK_ACCOUNT_ID,
                getString(R.string.title_bank_account_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_CREDIT_CARD_ID,
                getString(R.string.title_credit_card_setting)));

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
