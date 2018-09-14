
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialTracking implements Parcelable
{

    @SerializedName("facebook")
    @Expose
    private Facebook facebook;
    @SerializedName("twitter")
    @Expose
    private Twitter twitter;
    @SerializedName("instagram")
    @Expose
    private Instagram instagram;
    @SerializedName("youtube")
    @Expose
    private Youtube youtube;

    protected SocialTracking(Parcel in) {
        facebook = in.readParcelable(Facebook.class.getClassLoader());
        twitter = in.readParcelable(Twitter.class.getClassLoader());
        instagram = in.readParcelable(Instagram.class.getClassLoader());
        youtube = in.readParcelable(Youtube.class.getClassLoader());
    }

    public static final Creator<SocialTracking> CREATOR = new Creator<SocialTracking>() {
        @Override
        public SocialTracking createFromParcel(Parcel in) {
            return new SocialTracking(in);
        }

        @Override
        public SocialTracking[] newArray(int size) {
            return new SocialTracking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(facebook, i);
        parcel.writeParcelable(twitter, i);
        parcel.writeParcelable(instagram, i);
        parcel.writeParcelable(youtube, i);
    }

    public Facebook getFacebook() {
        return facebook;
    }

    public void setFacebook(Facebook facebook) {
        this.facebook = facebook;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public Instagram getInstagram() {
        return instagram;
    }

    public void setInstagram(Instagram instagram) {
        this.instagram = instagram;
    }

    public Youtube getYoutube() {
        return youtube;
    }

    public void setYoutube(Youtube youtube) {
        this.youtube = youtube;
    }

    public static Creator<SocialTracking> getCREATOR() {
        return CREATOR;
    }
}
