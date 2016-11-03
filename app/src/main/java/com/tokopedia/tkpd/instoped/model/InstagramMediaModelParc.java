package com.tokopedia.tkpd.instoped.model;

import android.os.Parcelable;

/**
 * Created by noiz354 on 6/22/16.
 */
public class InstagramMediaModelParc implements Parcelable {
    public String filter;
    public String link;
    public String thumbnail;
    public String standardResolution;
    public String captionText;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.filter);
        dest.writeString(this.link);
        dest.writeString(this.thumbnail);
        dest.writeString(this.standardResolution);
        dest.writeString(this.captionText);
    }

    public InstagramMediaModelParc() {
    }

    protected InstagramMediaModelParc(android.os.Parcel in) {
        this.filter = in.readString();
        this.link = in.readString();
        this.thumbnail = in.readString();
        this.standardResolution = in.readString();
        this.captionText = in.readString();
    }

    public static final Parcelable.Creator<InstagramMediaModelParc> CREATOR = new Parcelable.Creator<InstagramMediaModelParc>() {
        @Override
        public InstagramMediaModelParc createFromParcel(android.os.Parcel source) {
            return new InstagramMediaModelParc(source);
        }

        @Override
        public InstagramMediaModelParc[] newArray(int size) {
            return new InstagramMediaModelParc[size];
        }
    };
}
