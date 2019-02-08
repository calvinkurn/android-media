package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class ImageShop implements Parcelable {

    private static final String KEY_COVER = "cover";
    private static final String KEY_S_URL = "s_url";
    private static final String KEY_XS_URL = "xs_url";
    private static final String KEY_COVER_ECS = "cover_ecs";
    private static final String KEY_S_ECS = "s_ecs";
    private static final String KEY_XS_ECS = "xs_ecs";

    @SerializedName(KEY_COVER)
    private String cover = "";
    @SerializedName(KEY_S_URL)
    private String sUrl = "";
    @SerializedName(KEY_XS_URL)
    private String xsUrl = "";
    @SerializedName(KEY_COVER_ECS)
    private String coverEcs = "";
    @SerializedName(KEY_S_ECS)
    private String sEcs = "";
    @SerializedName(KEY_XS_ECS)
    private String xsEcs = "";

    public ImageShop() {
    }

    public ImageShop(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_COVER)) {
            setCover(object.getString(KEY_COVER));
        }
        if(!object.isNull(KEY_S_URL)) {
            setsUrl(object.getString(KEY_S_URL));
        }
        if(!object.isNull(KEY_XS_URL)) {
            setXsUrl(object.getString(KEY_XS_URL));
        }
        if(!object.isNull(KEY_COVER_ECS)) {
            setCoverEcs(object.getString(KEY_COVER_ECS));
        }
        if(!object.isNull(KEY_S_ECS)) {
            setsEcs(object.getString(KEY_S_ECS));
        }
        if(!object.isNull(KEY_XS_ECS)) {
            setXsEcs(object.getString(KEY_XS_ECS));
        }
    }

    protected ImageShop(Parcel in) {
        cover = in.readString();
        sUrl = in.readString();
        xsUrl = in.readString();
        coverEcs = in.readString();
        sEcs = in.readString();
        xsEcs = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cover);
        dest.writeString(sUrl);
        dest.writeString(xsUrl);
        dest.writeString(coverEcs);
        dest.writeString(sEcs);
        dest.writeString(xsEcs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageShop> CREATOR = new Creator<ImageShop>() {
        @Override
        public ImageShop createFromParcel(Parcel in) {
            return new ImageShop(in);
        }

        @Override
        public ImageShop[] newArray(int size) {
            return new ImageShop[size];
        }
    };

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public String getXsUrl() {
        return xsUrl;
    }

    public void setXsUrl(String xsUrl) {
        this.xsUrl = xsUrl;
    }

    public String getCoverEcs() {
        return coverEcs;
    }

    public void setCoverEcs(String coverEcs) {
        this.coverEcs = coverEcs;
    }

    public String getsEcs() {
        return sEcs;
    }

    public void setsEcs(String sEcs) {
        this.sEcs = sEcs;
    }

    public String getXsEcs() {
        return xsEcs;
    }

    public void setXsEcs(String xsEcs) {
        this.xsEcs = xsEcs;
    }
}
