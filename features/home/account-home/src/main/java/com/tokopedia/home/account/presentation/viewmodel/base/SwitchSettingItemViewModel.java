package com.tokopedia.home.account.presentation.viewmodel.base;

import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

public class SwitchSettingItemViewModel extends SettingItemViewModel {
    private boolean useOnClick = false;
    private String labelType = "";

    public void setUseOnClick(boolean useOnClick) {
        this.useOnClick = useOnClick;
    }

    public String labelType() {
        return this.labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
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

    public SwitchSettingItemViewModel(
            int id, String title, String subtitle, boolean isUseOnClick, String labelType) {
        super(id, title, subtitle);
        this.useOnClick = isUseOnClick;
        this.labelType = labelType;
    }
}
