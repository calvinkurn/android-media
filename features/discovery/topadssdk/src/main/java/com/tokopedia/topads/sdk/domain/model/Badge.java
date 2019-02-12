package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class Badge implements Parcelable {

    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_SHOW = "show";

    @SerializedName(KEY_TITLE)
    @Expose
    private String title;

    @SerializedName(KEY_IMAGE_URL)
    @Expose
    private String imageUrl;

    @SerializedName(KEY_SHOW)
    @Expose
    private Boolean show;

    public Badge(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Badge(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_TITLE)) {
            setTitle(object.getString(KEY_TITLE));
        }
        if(!object.isNull(KEY_IMAGE_URL)) {
            setImageUrl(object.getString(KEY_IMAGE_URL));
        }
        if(!object.isNull(KEY_SHOW)) {
            setShow(object.getBoolean(KEY_SHOW));
        }
    }

    protected Badge(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        byte tmpShow = in.readByte();
        show = tmpShow == 0 ? null : tmpShow == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (show == null ? 0 : show ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel in) {
            return new Badge(in);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean isShow() {
        return show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
