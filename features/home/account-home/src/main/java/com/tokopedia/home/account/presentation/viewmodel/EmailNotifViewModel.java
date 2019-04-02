package com.tokopedia.home.account.presentation.viewmodel;

import com.tokopedia.home.account.constant.SettingType;

public class EmailNotifViewModel {
    private String title;
    private String summary;
    private String id;

    public EmailNotifViewModel(String title, String summary, @SettingType String id) {
        this.title = title;
        this.summary = summary;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
