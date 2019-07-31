package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.library.baseadapter.BaseItem;

public class PointHistoryItem extends BaseItem {
    @SerializedName("id")
    public long id;

    @SerializedName("createTime")
    public String createTime;

    @SerializedName("createTimeDesc")
    public String createTimeDesc;

    @SerializedName("title")
    public String title;

    @SerializedName("notes")
    public String notes;

    @SerializedName("memberPoints")
    public int memberPoints;

    @SerializedName("rewardPoints")
    public int rewardPoints;

    @SerializedName("historyType")
    public int historyType;

    @SerializedName("icon")
    public String icon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeDesc() {
        return createTimeDesc;
    }

    public void setCreateTimeDesc(String createTimeDesc) {
        this.createTimeDesc = createTimeDesc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(int memberPoints) {
        this.memberPoints = memberPoints;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public int getHistoryType() {
        return historyType;
    }

    public void setHistoryType(int historyType) {
        this.historyType = historyType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "PointHistoryItem{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", createTimeDesc='" + createTimeDesc + '\'' +
                ", title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", memberPoints=" + memberPoints +
                ", rewardPoints=" + rewardPoints +
                ", historyType=" + historyType +
                ", icon='" + icon + '\'' +
                '}';
    }
}
