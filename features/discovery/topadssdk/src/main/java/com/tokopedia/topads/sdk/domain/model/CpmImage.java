package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmImage extends ImpressHolder implements Parcelable {

    private static final String KEY_FULL_URL = "full_url";
    private static final String KEY_FULL_ECS = "full_ecs";
    private static final String KEY_ILUSTRATION_URL = "illustration_url";

    @SerializedName(KEY_FULL_URL)
    private String fullUrl;
    @SerializedName(KEY_FULL_ECS)
    private String fullEcs;
    @SerializedName(KEY_ILUSTRATION_URL)
    private String ilustrationUrl;

    public CpmImage(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_FULL_URL)){
            setFullUrl(object.getString(KEY_FULL_URL));
        }
        if(!object.isNull(KEY_FULL_ECS)){
            setFullEcs(object.getString(KEY_FULL_ECS));
        }
        if(!object.isNull(KEY_ILUSTRATION_URL)){
            setIlustrationUrl(object.getString(KEY_ILUSTRATION_URL));
        }
    }

    protected CpmImage(Parcel in) {
        fullUrl = in.readString();
        fullEcs = in.readString();
        ilustrationUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullUrl);
        dest.writeString(fullEcs);
        dest.writeString(ilustrationUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CpmImage> CREATOR = new Creator<CpmImage>() {
        @Override
        public CpmImage createFromParcel(Parcel in) {
            return new CpmImage(in);
        }

        @Override
        public CpmImage[] newArray(int size) {
            return new CpmImage[size];
        }
    };

    public void setIlustrationUrl(String ilustrationUrl) {
        this.ilustrationUrl = ilustrationUrl;
    }

    public String getIlustrationUrl() {
        return ilustrationUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getFullEcs() {
        return fullEcs;
    }

    public void setFullEcs(String fullEcs) {
        this.fullEcs = fullEcs;
    }
}

