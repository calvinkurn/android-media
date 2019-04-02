package com.tokopedia.core.gcm.model;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * @author  by alvarisi on 1/11/17.
 */

public class NotificationPass {
    public String title;
    public String description;
    public String ticker;
    public Class<?> classParentStack;
    public ArrayList<String> savedNotificationContents;
    public ArrayList<Integer> savedNotificationCodes;
    public boolean isAllowedBigStyle;
    public Bundle extraData;
    public Intent mIntent;

    public NotificationPass() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Class<?> getClassParentStack() {
        return classParentStack;
    }

    public void setClassParentStack(Class<?> classParentStack) {
        this.classParentStack = classParentStack;
    }

    public ArrayList<String> getSavedNotificationContents() {
        return savedNotificationContents;
    }

    public void setSavedNotificationContents(ArrayList<String> savedNotificationContents) {
        this.savedNotificationContents = savedNotificationContents;
    }

    public ArrayList<Integer> getSavedNotificationCodes() {
        return savedNotificationCodes;
    }

    public void setSavedNotificationCodes(ArrayList<Integer> savedNotificationCodes) {
        this.savedNotificationCodes = savedNotificationCodes;
    }

    public boolean isAllowedBigStyle() {
        return isAllowedBigStyle;
    }

    public void setAllowedBigStyle(boolean allowedBigStyle) {
        isAllowedBigStyle = allowedBigStyle;
    }

    public Bundle getExtraData() {
        return extraData;
    }

    public void setExtraData(Bundle extraData) {
        this.extraData = extraData;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }
}
