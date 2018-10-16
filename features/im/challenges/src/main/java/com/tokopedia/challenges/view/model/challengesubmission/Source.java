package com.tokopedia.challenges.view.model.challengesubmission;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Source implements Parcelable {

    @SerializedName("Format")
    @Expose
    private String format;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("Bitrate")
    @Expose
    private int bitrate;
    @SerializedName("Quality")
    @Expose
    private int quality;
    public final static Parcelable.Creator<Source> CREATOR = new Creator<Source>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        public Source[] newArray(int size) {
            return (new Source[size]);
        }

    };

    protected Source(Parcel in) {
        this.format = ((String) in.readValue((String.class.getClassLoader())));
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this.bitrate = ((int) in.readValue((int.class.getClassLoader())));
        this.quality = ((int) in.readValue((int.class.getClassLoader())));
    }

    public Source() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(format);
        dest.writeValue(source);
        dest.writeValue(bitrate);
        dest.writeValue(quality);
    }

    public int describeContents() {
        return 0;
    }

}