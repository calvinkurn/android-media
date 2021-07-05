package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
@Entity(tableName = "inapp_data")
public class CMInApp {

    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("notificationId")
    public long id;

    @Expose
    @ColumnInfo(name = "campaignId")
    @SerializedName("campaignId")
    public String campaignId;

    @Expose
    @ColumnInfo(name = "freq")
    @SerializedName(value = "freq")
    public int freq = 1;

    @Expose
    @ColumnInfo(name = "notificationType")
    @SerializedName("notificationType")
    public String type = "";

    @Expose
    @ColumnInfo(name = "campaignUserToken")
    @SerializedName("campaignUserToken")
    public String campaignUserToken = "";

    @Expose
    @ColumnInfo(name = "parentId")
    @SerializedName("parentId")
    public String parentId = "";

    @Expose
    @ColumnInfo(name = "e")
    @SerializedName("e")
    public long expiry = 0;

    @Expose
    @ColumnInfo(name = "inAnim")
    @SerializedName("inAnim")
    public String inAnim = "";

    @Expose
    @ColumnInfo(name = "s")
    @SerializedName("s")
    public String screen = "*";

    @Expose
    @ColumnInfo(name = "campaignCode")
    @SerializedName("campaignCode")
    public String campaignCode = "";

    @Expose
    @SerializedName("d")
    @ColumnInfo(name = "d")
    public boolean cancelable = false;

    @Expose
    @SerializedName("ui")
    @Embedded(prefix = "ui_")
    public CMLayout cmLayout = new CMLayout();

    @Expose
    @ColumnInfo(name = "st")
    @SerializedName(value = "st")
    public long startTime = 0;

    @Expose
    @SerializedName(value = "et")
    @ColumnInfo(name = "et")
    public long endTime = 0;

    @Expose
    @SerializedName(value = "ct")
    @ColumnInfo(name = "ct")
    public long currentTime = 0;

    @Expose
    @SerializedName(value = "buf_time")
    @ColumnInfo(name = "buf_time")
    public long bufTime = 0;

    @ColumnInfo(name = "shown")
    public boolean isShown = false;

    @ColumnInfo(name = "last_shown")
    public long lastShownTime = 0;

    @ColumnInfo(name = "is_test")
    private boolean isTest = false;

    @ColumnInfo(name = "perst_on")
    private boolean persistentToggle = true;

    @ColumnInfo(name = "is_interacted")
    private boolean isInteracted = false;

    @ColumnInfo(name = "is_amplification")
    private boolean isAmplification = false;

    @ColumnInfo(name = "customValues")
    private String customValues = "";

    public CMInApp(){}

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

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
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


    public String getCampaignUserToken() {
        return campaignUserToken;
    }

    public void setCampaignUserToken(String campaignUserToken) {
        this.campaignUserToken = campaignUserToken;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getBufTime() {
        return bufTime;
    }

    public void setBufTime(long bufTime) {
        this.bufTime = bufTime;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public long getLastShownTime() {
        return lastShownTime;
    }

    public void setLastShownTime(long lastShownTime) {
        this.lastShownTime = lastShownTime;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public boolean isPersistentToggle() {
        return persistentToggle;
    }

    public void setPersistentToggle(boolean persistentToggle) {
        this.persistentToggle = persistentToggle;
    }

    public boolean isInteracted() {
        return isInteracted;
    }

    public void setInteracted(boolean interacted) {
        isInteracted = interacted;
    }

    public void setCustomValues(String customValues) {
        this.customValues = customValues;
    }

    public String getCustomValues() {
        return customValues;
    }

    public boolean isAmplification() {
        return isAmplification;
    }

    public void setAmplification(boolean amplification) {
        isAmplification = amplification;
    }
}