package com.tokopedia.challenges.view.model.challengesubmission;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

    @SerializedName("Duration")
    @Expose
    private int duration;
    @SerializedName("Sources")
    @Expose
    private List<Source> sources = null;
    public final static Parcelable.Creator<Video> CREATOR = new Creator<Video>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return (new Video[size]);
        }

    };

    protected Video(Parcel in) {
        this.duration = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.sources, (Source.class.getClassLoader()));
    }

    public Video() {
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(duration);
        dest.writeList(sources);
    }

    public int describeContents() {
        return 0;
    }

}