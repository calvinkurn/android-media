package com.tokopedia.topads.sdk.domain.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsModel implements Parcelable {

    private static final String KEY_HEADER = "header";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR = "errors";

    @SerializedName(KEY_ERROR)
    @Expose
    private Error error;

    @SerializedName(KEY_STATUS)
    @Expose
    private Status status;

    @SerializedName(KEY_HEADER)
    @Expose
    private Header header;

    @SerializedName(KEY_DATA)
    @Expose
    private List<Data> data = new ArrayList<>();
    private int adsPosition = 0;

    public TopAdsModel() {
    }

    public TopAdsModel(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ERROR)){
            JSONObject error = object.getJSONArray(KEY_ERROR).getJSONObject(0);
            setError(new Error(error));
        }
        if(!object.isNull(KEY_HEADER)) {
            setHeader(new Header(object.getJSONObject(KEY_HEADER)));
        }
        if(!object.isNull(KEY_STATUS)) {
            setStatus(new Status(object.getJSONObject(KEY_STATUS)));
        }
        if(!object.isNull(KEY_DATA)) {
            JSONArray dataArray = object.getJSONArray(KEY_DATA);
            for (int i = 0; i < dataArray.length(); i++) {
                data.add(new Data(dataArray.getJSONObject(i)));
            }
        }
    }

    protected TopAdsModel(Parcel in) {
        data = in.createTypedArrayList(Data.CREATOR);
        adsPosition = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeInt(adsPosition);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TopAdsModel> CREATOR = new Creator<TopAdsModel>() {
        @Override
        public TopAdsModel createFromParcel(Parcel in) {
            return new TopAdsModel(in);
        }

        @Override
        public TopAdsModel[] newArray(int size) {
            return new TopAdsModel[size];
        }
    };

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getAdsPosition() {
        return adsPosition;
    }

    public void setAdsPosition(int adsPosition) {
        this.adsPosition = adsPosition;
    }
}
