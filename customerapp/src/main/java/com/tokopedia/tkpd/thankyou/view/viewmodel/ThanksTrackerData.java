package com.tokopedia.tkpd.thankyou.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThanksTrackerData implements Parcelable {
    private String platform;
    private String template;
    private String id;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.platform);
        dest.writeString(this.template);
        dest.writeString(this.id);
    }

    public ThanksTrackerData() {
    }

    protected ThanksTrackerData(Parcel in) {
        this.platform = in.readString();
        this.template = in.readString();
        this.id = in.readString();
    }

    public static final Creator<ThanksTrackerData> CREATOR = new Creator<ThanksTrackerData>() {
        @Override
        public ThanksTrackerData createFromParcel(Parcel source) {
            return new ThanksTrackerData(source);
        }

        @Override
        public ThanksTrackerData[] newArray(int size) {
            return new ThanksTrackerData[size];
        }
    };
}