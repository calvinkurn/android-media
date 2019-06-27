package com.tokopedia.topads.sdk.domain.model;

/**
 * Author errysuprayogi on 08,March,2019
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Template implements Parcelable {


    private static final String KEY_NAME = "name";
    private static final String KEY_IS_AD = "is_ad";

    @SerializedName(KEY_NAME)
    private String name = "";
    @SerializedName(KEY_IS_AD)
    private boolean isAd;

    public Template(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_NAME)) {
            setName(object.getString(KEY_NAME));
        }
        if(!object.isNull(KEY_IS_AD)) {
            setIsAd(object.getBoolean(KEY_IS_AD));
        }
    }

    protected Template(Parcel in) {
        name = in.readString();
        isAd = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isAd ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Template> CREATOR = new Creator<Template>() {
        @Override
        public Template createFromParcel(Parcel in) {
            return new Template(in);
        }

        @Override
        public Template[] newArray(int size) {
            return new Template[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsAd() {
        return isAd;
    }

    public void setIsAd(boolean isAd) {
        this.isAd = isAd;
    }
}