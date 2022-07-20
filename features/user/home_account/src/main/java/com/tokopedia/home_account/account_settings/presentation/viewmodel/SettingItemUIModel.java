package com.tokopedia.home_account.account_settings.presentation.viewmodel;

public class SettingItemUIModel {
    private int id = -1;
    private String title = "";
    private String subtitle;
    private int iconResource = -1;
    private boolean hideArrow = false;

    public SettingItemUIModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public SettingItemUIModel(int id, String title, String subtitle) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public boolean isHideArrow() {
        return hideArrow;
    }

    public void setHideArrow(boolean hideArrow) {
        this.hideArrow = hideArrow;
    }
}
