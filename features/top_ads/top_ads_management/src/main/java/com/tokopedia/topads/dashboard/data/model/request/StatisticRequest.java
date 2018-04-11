package com.tokopedia.topads.dashboard.data.model.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class StatisticRequest implements Parcelable {

    private String shopId;
    private int type;
    private Date startDate;
    private Date endDate;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFormattedStartDate() {
        return new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate);
    }

    public String getFormattedEndDate() {
        return new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate);
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        params.put(TopAdsNetworkConstant.PARAM_TYPE, String.valueOf(type));
        params.put(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        params.put(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeInt(this.type);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
    }

    public StatisticRequest() {
    }

    protected StatisticRequest(Parcel in) {
        this.shopId = in.readString();
        this.type = in.readInt();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
    }

    public static final Parcelable.Creator<StatisticRequest> CREATOR = new Parcelable.Creator<StatisticRequest>() {
        @Override
        public StatisticRequest createFromParcel(Parcel source) {
            return new StatisticRequest(source);
        }

        @Override
        public StatisticRequest[] newArray(int size) {
            return new StatisticRequest[size];
        }
    };
}