package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public class Error implements Parcelable {

    @SerializedName(KEY_CODE)
    @Expose
    private int code;

    @SerializedName(KEY_TITLE)
    @Expose
    private String title;

    @SerializedName(KEY_DETAIL)
    @Expose
    private String detail;

    private static final String KEY_CODE = "code";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DETAIL = "detail";

    public Error() {
    }

    public Error(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_CODE)) {
            setCode(object.getInt(KEY_CODE));
        }
        if(!object.isNull(KEY_TITLE)){
            setTitle(object.getString(KEY_TITLE));
        }
        if(!object.isNull(KEY_DETAIL)){
            setDetail(object.getString(KEY_DETAIL));
        }
    }

    protected Error(Parcel in) {
        code = in.readInt();
        title = in.readString();
        detail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(title);
        dest.writeString(detail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Error> CREATOR = new Creator<Error>() {
        @Override
        public Error createFromParcel(Parcel in) {
            return new Error(in);
        }

        @Override
        public Error[] newArray(int size) {
            return new Error[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
