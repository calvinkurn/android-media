package com.tokopedia.topads.dashboard.data.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.topads.dashboard.view.model.Ad;


public class ProductAd implements Ad, Parcelable {

    public static final int TYPE = 1;

    @SerializedName("ad_id")
    @Expose
    private long id;
    @SerializedName("ad_status")
    @Expose
    private int status;
    @SerializedName("ad_status_desc")
    @Expose
    private String statusDesc;
    @SerializedName("ad_status_toogle")
    @Expose
    private int statusToogle;
    @SerializedName("ad_price_bid_fmt")
    @Expose
    private String priceBidFmt;
    @SerializedName("ad_price_daily_fmt")
    @Expose
    private String priceDailyFmt;
    @SerializedName("ad_price_daily_spent_fmt")
    @Expose
    private String priceDailySpentFmt;
    @SerializedName("ad_price_daily_bar")
    @Expose
    private String priceDailyBar;
    @SerializedName("ad_editable")
    @Expose
    private int editable;
    @SerializedName("ad_start_date")
    @Expose
    private String startDate;
    @SerializedName("ad_start_time")
    @Expose
    private String startTime;
    @SerializedName("ad_end_date")
    @Expose
    private String endDate;
    @SerializedName("ad_end_time")
    @Expose
    private String endTime;

    @SerializedName("stat_avg_click")
    @Expose
    private String statAvgClick;
    @SerializedName("stat_total_spent")
    @Expose
    private String statTotalSpent;
    @SerializedName("stat_total_impression")
    @Expose
    private String statTotalImpression;
    @SerializedName("stat_total_click")
    @Expose
    private String statTotalClick;
    @SerializedName("stat_total_ctr")
    @Expose
    private String statTotalCtr;
    @SerializedName("stat_total_conversion")
    @Expose
    private String statTotalConversion;
    @SerializedName("label_edit")
    @Expose
    private String labelEdit;
    @SerializedName("label_per_click")
    @Expose
    private String labelPerClick;
    @SerializedName("label_of")
    @Expose
    private String labelOf;

    @SerializedName("product_name")
    @Expose
    private String name;

    @SerializedName("ad_moderated")
    @Expose
    private int adModerated;
    @SerializedName("ad_moderated_reason")
    @Expose
    private String adModeratedReason;

    @SerializedName("item_id")
    @Expose
    private int itemId;
    @SerializedName("product_uri")
    @Expose
    private String productUri;
    @SerializedName("product_active")
    @Expose
    private int productActive;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_id")
    @Expose
    private long groupId;

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusDesc() {
        return statusDesc;
    }

    @Override
    public int getStatusToogle() {
        return statusToogle;
    }

    @Override
    public String getPriceBidFmt() {
        return priceBidFmt;
    }

    @Override
    public String getPriceDailyFmt() {
        return priceDailyFmt;
    }

    @Override
    public String getPriceDailySpentFmt() {
        return priceDailySpentFmt;
    }

    @Override
    public String getPriceDailyBar() {
        return priceDailyBar;
    }

    public int getEditable() {
        return editable;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public String getStatAvgClick() {
        return statAvgClick;
    }

    @Override
    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    @Override
    public String getStatTotalClick() {
        return statTotalClick;
    }

    @Override
    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    @Override
    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    @Override
    public String getLabelEdit() {
        return labelEdit;
    }

    @Override
    public String getLabelPerClick() {
        return labelPerClick;
    }

    @Override
    public String getLabelOf() {
        return labelOf;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAdModerated() {
        return adModerated;
    }

    public String getAdModeratedReason() {
        return adModeratedReason;
    }

    public int getItemId() {
        return itemId;
    }

    public String getProductUri() {
        return productUri;
    }

    public int getProductActive() {
        return productActive;
    }

    public String getGroupName() {
        return groupName;
    }

    public long getGroupId() {
        return groupId;
    }

    public ProductAd() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.status);
        dest.writeString(this.statusDesc);
        dest.writeInt(this.statusToogle);
        dest.writeString(this.priceBidFmt);
        dest.writeString(this.priceDailyFmt);
        dest.writeString(this.priceDailySpentFmt);
        dest.writeString(this.priceDailyBar);
        dest.writeInt(this.editable);
        dest.writeString(this.startDate);
        dest.writeString(this.startTime);
        dest.writeString(this.endDate);
        dest.writeString(this.endTime);
        dest.writeString(this.statAvgClick);
        dest.writeString(this.statTotalSpent);
        dest.writeString(this.statTotalImpression);
        dest.writeString(this.statTotalClick);
        dest.writeString(this.statTotalCtr);
        dest.writeString(this.statTotalConversion);
        dest.writeString(this.labelEdit);
        dest.writeString(this.labelPerClick);
        dest.writeString(this.labelOf);
        dest.writeString(this.name);
        dest.writeInt(this.adModerated);
        dest.writeString(this.adModeratedReason);
        dest.writeInt(this.itemId);
        dest.writeString(this.productUri);
        dest.writeInt(this.productActive);
        dest.writeString(this.groupName);
        dest.writeLong(this.groupId);
    }

    protected ProductAd(Parcel in) {
        this.id = in.readLong();
        this.status = in.readInt();
        this.statusDesc = in.readString();
        this.statusToogle = in.readInt();
        this.priceBidFmt = in.readString();
        this.priceDailyFmt = in.readString();
        this.priceDailySpentFmt = in.readString();
        this.priceDailyBar = in.readString();
        this.editable = in.readInt();
        this.startDate = in.readString();
        this.startTime = in.readString();
        this.endDate = in.readString();
        this.endTime = in.readString();
        this.statAvgClick = in.readString();
        this.statTotalSpent = in.readString();
        this.statTotalImpression = in.readString();
        this.statTotalClick = in.readString();
        this.statTotalCtr = in.readString();
        this.statTotalConversion = in.readString();
        this.labelEdit = in.readString();
        this.labelPerClick = in.readString();
        this.labelOf = in.readString();
        this.name = in.readString();
        this.adModerated = in.readInt();
        this.adModeratedReason = in.readString();
        this.itemId = in.readInt();
        this.productUri = in.readString();
        this.productActive = in.readInt();
        this.groupName = in.readString();
        this.groupId = in.readLong();
    }

    public static final Parcelable.Creator<ProductAd> CREATOR = new Parcelable.Creator<ProductAd>() {
        @Override
        public ProductAd createFromParcel(Parcel source) {
            return new ProductAd(source);
        }

        @Override
        public ProductAd[] newArray(int size) {
            return new ProductAd[size];
        }
    };

    @Override
    public int getType() {
        return TYPE;
    }
}