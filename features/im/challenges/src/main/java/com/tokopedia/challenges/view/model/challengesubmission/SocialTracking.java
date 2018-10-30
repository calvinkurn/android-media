
package com.tokopedia.challenges.view.model.challengesubmission;

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

    public final static Creator<SocialTracking> CREATOR = new Creator<SocialTracking>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SocialTracking createFromParcel(Parcel in) {
            return new SocialTracking(in);
        }

        public SocialTracking[] newArray(int size) {
            return (new SocialTracking[size]);
        }

    }
    ;

    protected SocialTracking(Parcel in) {
        this.facebook = ((Facebook) in.readValue((Facebook.class.getClassLoader())));
        this.twitter = ((Twitter) in.readValue((Twitter.class.getClassLoader())));
        this.instagram = ((Instagram) in.readValue((Instagram.class.getClassLoader())));
        this.youtube = ((Youtube) in.readValue((Youtube.class.getClassLoader())));
    }

    public SocialTracking() {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(facebook);
        dest.writeValue(twitter);
        dest.writeValue(instagram);
        dest.writeValue(youtube);
    }

    public int describeContents() {
        return  0;
    }

}
