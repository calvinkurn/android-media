package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class FreeOngkir implements Parcelable {

    private static final String KEY_IS_ACTIVE = "is_active";
    private static final String KEY_IMG_URL = "img_url";

    @SerializedName(KEY_IS_ACTIVE)
    @Expose
    private boolean isActive = false;

    @SerializedName(KEY_IMG_URL)
    @Expose
    private String imageUrl = "";

    public FreeOngkir() {

    }

    public FreeOngkir(boolean isActive, String imageUrl) {
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }

    public FreeOngkir(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_IS_ACTIVE)){
            this.isActive = object.getBoolean(KEY_IS_ACTIVE);
        }
        if(!object.isNull(KEY_IMG_URL)) {
            this.imageUrl = object.getString(KEY_IMG_URL);
        }
    }

    protected FreeOngkir(Parcel in) {
        isActive = in.readByte() != 0;
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FreeOngkir> CREATOR = new Creator<FreeOngkir>() {
        @Override
        public FreeOngkir createFromParcel(Parcel in) {
            return new FreeOngkir(in);
        }

        @Override
        public FreeOngkir[] newArray(int size) {
            return new FreeOngkir[size];
        }
    };

    public boolean isActive() {
        return this.isActive;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
