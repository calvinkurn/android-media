package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class LabelGroup implements Parcelable {

    private static final String KEY_POSITION = "position";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TITLE = "title";

    @SerializedName(KEY_POSITION)
    @Expose
    private String position;

    @SerializedName(KEY_TYPE)
    @Expose
    private String type;

    @SerializedName(KEY_TITLE)
    @Expose
    private String title;

    public LabelGroup(String position, String type, String title) {
        this.position = position;
        this.type = type;
        this.title = title;
    }

    public LabelGroup(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_POSITION)){
            this.position = object.getString(KEY_POSITION);
        }
        if(!object.isNull(KEY_TYPE)){
            this.type = object.getString(KEY_TYPE);
        }
        if(!object.isNull(KEY_TITLE)){
            this.title = object.getString(KEY_TITLE);
        }
    }

    protected LabelGroup(Parcel in) {
        position = in.readString();
        type = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(position);
        dest.writeString(type);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LabelGroup> CREATOR = new Creator<LabelGroup>() {
        @Override
        public LabelGroup createFromParcel(Parcel in) {
            return new LabelGroup(in);
        }

        @Override
        public LabelGroup[] newArray(int size) {
            return new LabelGroup[size];
        }
    };

    public String getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}