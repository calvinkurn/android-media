package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

/**
 * @author by nisie on 8/28/17.
 */

public class SmileyModel {
    int ResId;
    String name;
    String value;
    boolean isChange;

    public SmileyModel(int resId, String name, String value) {
        ResId = resId;
        this.name = name;
        this.value = value;
        this.isChange = false;
    }

    public SmileyModel(int resId, String name, String value, boolean isChange) {
        ResId = resId;
        this.name = name;
        this.value = value;
    }

    public boolean isChange() {
        return isChange;
    }

    public int getResId() {
        return ResId;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return value;
    }
}
