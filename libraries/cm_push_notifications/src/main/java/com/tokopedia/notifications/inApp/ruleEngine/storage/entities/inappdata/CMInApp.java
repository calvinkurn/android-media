package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
@Entity(tableName = "inapp_data")
public class CMInApp {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose
    @SerializedName("notificationId")
    public long id;

    @ColumnInfo(name = "freq")
    @Expose
    @SerializedName(value = "freq")
    public int freq = 1;

    @ColumnInfo(name = "notificationType")
    @SerializedName("notificationType")
    @Expose
    public String type;

    @ColumnInfo(name = "e")
    @SerializedName("e")
    @Expose
    public long expiry;

    @Expose
    @ColumnInfo(name = "inAnim")
    @SerializedName("inAnim")
    public String inAnim;

    @ColumnInfo(name = "s")
    @SerializedName("s")
    @Expose
    public String screen;

    @SerializedName("d")
    @ColumnInfo(name = "d")
    @Expose
    public boolean cancelable;
    @SerializedName("ui")
    @Embedded(prefix = "ui_")
    @Expose
    public CMLayout cmLayout;
    @Expose
    @ColumnInfo(name = "st")
    @SerializedName(value = "st")
    public long startTime = 0l;
    @Expose
    @SerializedName(value = "et")
    @ColumnInfo(name = "et")
    public long endTime;
    @Expose
    @SerializedName(value = "ct")
    @ColumnInfo(name = "ct")
    public long currentTime = 0l;
    @Expose
    @SerializedName(value = "buf_time")
    @ColumnInfo(name = "buf_time")
    public long bufTime = 0l;
    @ColumnInfo(name = "shown")
    public boolean isShown = false;
    @ColumnInfo(name = "last_shown")
    public long lastShownTime;

    @Ignore
    public View cmInAppView;



    public CMInApp(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public CMLayout getCmLayout() {
        return cmLayout;
    }

    public void setCmLayout(CMLayout cmLayout) {
        this.cmLayout = cmLayout;
    }

    public View getCmInAppView() {
        return cmInAppView;
    }

    public void setCmInAppView(View cmInAppView) {
        this.cmInAppView = cmInAppView;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public String getInAnim() {
        return inAnim;
    }

    public void setInAnim(String inAnim) {
        this.inAnim = inAnim;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}