package com.tokopedia.purchase_platform.common.utils;

import android.widget.CompoundButton;

/**
 * Created by fwidjaja on 10/03/20.
 */
public class CheckboxWatcher implements CompoundButton.OnCheckedChangeListener {

    private Boolean prevIsChecked = false;
    public static final int CHECKBOX_WATCHER_DEBOUNCE_TIME = 1000;
    public static CheckboxWatcherListener checkboxWatcherListener;

    public CheckboxWatcher(CheckboxWatcherListener checkboxWatcherListener) {
        this.checkboxWatcherListener = checkboxWatcherListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        prevIsChecked = isChecked;
        if (checkboxWatcherListener != null) {
            checkboxWatcherListener.onCheckboxChanged(isChecked);
        }
    }

    public interface CheckboxWatcherListener {
        void onCheckboxChanged(Boolean isChecked);
    }
}
