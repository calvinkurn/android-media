package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmModel implements Parcelable {

    private static final String KEY_HEADER = "header";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR = "errors";

    @SerializedName(KEY_ERROR)
    private Error error = new Error();
    @SerializedName(KEY_STATUS)
    private Status status = new Status();
    @SerializedName(KEY_HEADER)
    private Header header = new Header();
    @SerializedName(KEY_DATA)
    private List<CpmData> data = new ArrayList<>();

    public CpmModel() {
    }

    public CpmModel(JSONObject object) throws JSONException {
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
                data.add(new CpmData(dataArray.getJSONObject(i)));
            }
        }
    }

    protected CpmModel(Parcel in) {
        error = in.readParcelable(Error.class.getClassLoader());
        status = in.readParcelable(Status.class.getClassLoader());
        header = in.readParcelable(Header.class.getClassLoader());
        data = in.createTypedArrayList(CpmData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(error, flags);
        dest.writeParcelable(status, flags);
        dest.writeParcelable(header, flags);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CpmModel> CREATOR = new Creator<CpmModel>() {
        @Override
        public CpmModel createFromParcel(Parcel in) {
            return new CpmModel(in);
        }

        @Override
        public CpmModel[] newArray(int size) {
            return new CpmModel[size];
        }
    };

    public List<CpmData> getData() {
        return data;
    }

    public void setData(List<CpmData> data) {
        this.data = data;
    }

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
}
