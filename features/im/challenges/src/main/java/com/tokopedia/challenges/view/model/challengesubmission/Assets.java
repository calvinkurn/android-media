
package com.tokopedia.challenges.view.model.challengesubmission;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Assets implements Serializable, Parcelable
{

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("video")
    @Expose
    private String video;
    public final static Creator<Assets> CREATOR = new Creator<Assets>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Assets createFromParcel(Parcel in) {
            return new Assets(in);
        }

        public Assets[] newArray(int size) {
            return (new Assets[size]);
        }

    }
    ;
    private final static long serialVersionUID = -2872052661629100915L;

    protected Assets(Parcel in) {
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Assets() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeValue(video);
    }

    public int describeContents() {
        return  0;
    }

}
