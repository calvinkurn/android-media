package com.tokopedia.topads.keyword.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.topads.common.view.adapter.TopAdsListAdapterTypeFactory;
import com.tokopedia.topads.dashboard.view.model.Ad;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public class KeywordAd implements Ad, Parcelable, ItemType, Visitable<TopAdsListAdapterTypeFactory<KeywordAd>> {
    public static final int TYPE = 192929201;
    private String id;
    private String keywordTag;
    private String groupId;
    private String groupName;
    private int status;
    private String statusDesc;
    private String keywordTypeId;
    private String keywordTypeDesc;
    private int statusToogle;
    private String priceBidFmt;
    private int groupBid;
    private String statAvgClick;
    private String statTotalSpent;
    private String statTotalImpression;
    private String statTotalClick;
    private String statTotalCtr;
    private String statTotalConversion;
    private String labelPerClick;

    public KeywordAd() {
    }

    public int getGroupBid() {
        return groupBid;
    }

    public void setGroupBid(int groupBid) {
        this.groupBid = groupBid;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Override
    public int getStatusToogle() {
        return statusToogle;
    }

    public void setStatusToogle(int statusToogle) {
        this.statusToogle = statusToogle;
    }

    @Override
    public String getPriceBidFmt() {
        return priceBidFmt;
    }

    public void setPriceBidFmt(String priceBidFmt) {
        this.priceBidFmt = priceBidFmt;
    }

    @Override
    public String getPriceDailyFmt() {
        return null;
    }

    @Override
    public String getPriceDailySpentFmt() {
        return null;
    }

    @Override
    public String getPriceDailyBar() {
        return null;
    }

    @Override
    public String getStartDate() {
        return null;
    }

    @Override
    public String getStartTime() {
        return null;
    }

    @Override
    public String getEndDate() {
        return null;
    }

    @Override
    public String getEndTime() {
        return null;
    }

    @Override
    public String getStatAvgClick() {
        return statAvgClick;
    }

    public void setStatAvgClick(String statAvgClick) {
        this.statAvgClick = statAvgClick;
    }

    @Override
    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    public void setStatTotalSpent(String statTotalSpent) {
        this.statTotalSpent = statTotalSpent;
    }

    @Override
    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    public void setStatTotalImpression(String statTotalImpression) {
        this.statTotalImpression = statTotalImpression;
    }

    @Override
    public String getStatTotalClick() {
        return statTotalClick;
    }

    public void setStatTotalClick(String statTotalClick) {
        this.statTotalClick = statTotalClick;
    }

    @Override
    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    public void setStatTotalCtr(String statTotalCtr) {
        this.statTotalCtr = statTotalCtr;
    }

    @Override
    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    public void setStatTotalConversion(String statTotalConversion) {
        this.statTotalConversion = statTotalConversion;
    }

    @Override
    public String getLabelEdit() {
        return null;
    }

    @Override
    public String getLabelPerClick() {
        return labelPerClick;
    }

    public void setLabelPerClick(String labelPerClick) {
        this.labelPerClick = labelPerClick;
    }

    @Override
    public String getLabelOf() {
        return null;
    }

    @Override
    public String getName() {
        return keywordTag;
    }

    public String getkeywordTypeDesc() {
        return keywordTypeDesc;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public String getKeywordTypeDesc() {
        return keywordTypeDesc;
    }

    public void setKeywordTypeDesc(String keywordTypeDesc) {
        this.keywordTypeDesc = keywordTypeDesc;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.groupId);
        dest.writeString(this.keywordTypeId);
        dest.writeString(this.groupName);
        dest.writeString(this.keywordTag);
        dest.writeInt(this.status);
        dest.writeInt(this.statusToogle);
        dest.writeString(this.statusDesc);
        dest.writeString(this.statAvgClick);
        dest.writeString(this.statTotalSpent);
        dest.writeString(this.statTotalImpression);
        dest.writeString(this.statTotalClick);
        dest.writeString(this.statTotalCtr);
        dest.writeString(this.statTotalConversion);
        dest.writeString(this.priceBidFmt);
        dest.writeString(this.labelPerClick);
        dest.writeString(this.keywordTypeDesc);
        dest.writeInt(this.groupBid);
    }

    protected KeywordAd(Parcel in) {
        this.id = in.readString();
        this.groupId = in.readString();
        this.keywordTypeId = in.readString();
        this.groupName = in.readString();
        this.keywordTag = in.readString();
        this.status = in.readInt();
        this.statusToogle = in.readInt();
        this.statusDesc = in.readString();
        this.statAvgClick = in.readString();
        this.statTotalSpent = in.readString();
        this.statTotalImpression = in.readString();
        this.statTotalClick = in.readString();
        this.statTotalCtr = in.readString();
        this.statTotalConversion = in.readString();
        this.priceBidFmt = in.readString();
        this.labelPerClick = in.readString();
        this.keywordTypeDesc = in.readString();
        this.groupBid = in.readInt();
    }

    public static final Creator<KeywordAd> CREATOR = new Creator<KeywordAd>() {
        @Override
        public KeywordAd createFromParcel(Parcel source) {
            return new KeywordAd(source);
        }

        @Override
        public KeywordAd[] newArray(int size) {
            return new KeywordAd[size];
        }
    };

    @Override
    public int type(TopAdsListAdapterTypeFactory<KeywordAd> typeFactory) {
        return typeFactory.type(this);
    }
}
