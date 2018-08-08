package com.tokopedia.challenges.view.model.challengesubmission;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media implements Parcelable {

    @SerializedName("MediaType")
    @Expose
    private String mediaType;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Video")
    @Expose
    private Video video;
    public final static Parcelable.Creator<Media> CREATOR = new Creator<Media>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        public Media[] newArray(int size) {
            return (new Media[size]);
        }

    };

    protected Media(Parcel in) {
        this.mediaType = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((Video) in.readValue((Video.class.getClassLoader())));
    }

    public Media() {
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mediaType);
        dest.writeValue(imageUrl);
        dest.writeValue(video);
    }

    public int describeContents() {
        return 0;
    }

}