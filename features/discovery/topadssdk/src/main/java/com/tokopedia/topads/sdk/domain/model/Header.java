package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Header implements Parcelable {
    @SerializedName(KEY_TOTAL_DATA)
    private int totalData;
    @SerializedName(KEY_PROCESS_TIME)
    private double processTime;
    @SerializedName(KEY_META_DATA)
    private MetaData metaData = new MetaData();

    private static final String KEY_META_DATA = "meta";
    private static final String KEY_TOTAL_DATA = "total_data";
    private static final String KEY_PROCESS_TIME = "process_time";

    public Header() {
    }

    public Header(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_META_DATA)) {
            setMetaData(new MetaData(object.getJSONObject(KEY_META_DATA)));
        }
        if(!object.isNull(KEY_PROCESS_TIME)) {
            setProcessTime(object.getDouble(KEY_PROCESS_TIME));
        }
        if(!object.isNull(KEY_TOTAL_DATA)) {
            setTotalData(object.getInt(KEY_TOTAL_DATA));
        }
    }

    protected Header(Parcel in) {
        totalData = in.readInt();
        processTime = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalData);
        dest.writeDouble(processTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

}
