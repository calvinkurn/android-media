package com.tokopedia.home.account.presentation.viewmodel.base;

import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

public class SwitchSettingItemViewModel extends SettingItemViewModel {

    public SwitchSettingItemViewModel(int id, String title) {
        super(id, title);
    }

    public SwitchSettingItemViewModel(int id, String title, String subtitle) {
        super(id, title, subtitle);
    }
}
