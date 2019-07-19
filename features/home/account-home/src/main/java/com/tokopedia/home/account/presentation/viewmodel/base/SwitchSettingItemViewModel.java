package com.tokopedia.home.account.presentation.viewmodel.base;

import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

public class SwitchSettingItemViewModel extends SettingItemViewModel {
    private boolean useOnClick = false;

    public void setUseOnClick(boolean useOnClick) {
        this.useOnClick = useOnClick;
    }

    public boolean isUseOnClick() {
        return useOnClick;
    }

    public SwitchSettingItemViewModel(int id, String title) {
        super(id, title);
    }

    public SwitchSettingItemViewModel(int id, String title, String subtitle, boolean isUseOnClick) {
        super(id, title, subtitle);
        this.useOnClick = isUseOnClick;
    }
}
