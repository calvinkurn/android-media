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
public class Label implements Parcelable {

    public static final String KEY_TITLE = "title";
    public static final String KEY_COLOR = "color";

    @SerializedName(KEY_TITLE)
    @Expose
    private String title = "";

    @SerializedName(KEY_COLOR)
    @Expose
    private String color = "";

    public Label(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_TITLE)) {
            setTitle(object.getString(KEY_TITLE));
        }
        if(!object.isNull(KEY_COLOR)){
            setColor(object.getString(KEY_COLOR));
        }
    }

    protected Label(Parcel in) {
        title = in.readString();
        color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
