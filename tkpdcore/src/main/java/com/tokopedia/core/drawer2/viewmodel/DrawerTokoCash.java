package com.tokopedia.core.drawer2.viewmodel;

/**
 * Created by nisie on 1/24/17.
 */

public class DrawerTokoCash {
    private String tokoCash;
    private String tokoCashUrl;
    private boolean isHasTokoCash;
    private String tokoCashLabel;

    public DrawerTokoCash() {
        this.tokoCash = "";
        this.tokoCashUrl = "";
        this.isHasTokoCash = false;
        this.tokoCashLabel = "";
    }

    public String getTokoCash() {
        return tokoCash;
    }

    public void setTokoCash(String tokoCash) {
        this.tokoCash = tokoCash;
    }

    public String getTokoCashUrl() {
        return tokoCashUrl;
    }

    public void setTokoCashUrl(String tokoCashUrl) {
        this.tokoCashUrl = tokoCashUrl;
    }

    public boolean isHasTokoCash() {
        return isHasTokoCash;
    }

    public void setHasTokoCash(boolean hasTokoCash) {
        isHasTokoCash = hasTokoCash;
    }

    public String getTokoCashLabel() {
        return tokoCashLabel;
    }

    public void setTokoCashLabel(String tokoCashLabel) {
        this.tokoCashLabel = tokoCashLabel;
    }
}
