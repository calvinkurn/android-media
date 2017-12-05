package com.tokopedia.tkpd.home.thankyou.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThanksAnalyticsData implements Parcelable {
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

    public ThanksAnalyticsData() {
    }

    protected ThanksAnalyticsData(Parcel in) {
        this.platform = in.readString();
        this.template = in.readString();
        this.id = in.readString();
    }

    public static final Creator<ThanksAnalyticsData> CREATOR = new Creator<ThanksAnalyticsData>() {
        @Override
        public ThanksAnalyticsData createFromParcel(Parcel source) {
            return new ThanksAnalyticsData(source);
        }

        @Override
        public ThanksAnalyticsData[] newArray(int size) {
            return new ThanksAnalyticsData[size];
        }
    };
}