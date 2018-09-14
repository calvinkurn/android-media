
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sharing implements Parcelable {

    @SerializedName("MetaTags")
    @Expose
    private MetaTags metaTags;
    @SerializedName("Assets")
    @Expose
    private Assets assets;
    @SerializedName("SocialTracking")
    @Expose
    private SocialTracking socialTracking;

    protected Sharing(Parcel in) {
        metaTags = in.readParcelable(MetaTags.class.getClassLoader());
        assets = in.readParcelable(Assets.class.getClassLoader());
        socialTracking = in.readParcelable(SocialTracking.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(metaTags, flags);
        dest.writeParcelable(assets, flags);
        dest.writeParcelable(socialTracking, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sharing> CREATOR = new Creator<Sharing>() {
        @Override
        public Sharing createFromParcel(Parcel in) {
            return new Sharing(in);
        }

        @Override
        public Sharing[] newArray(int size) {
            return new Sharing[size];
        }
    };

    public MetaTags getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(MetaTags metaTags) {
        this.metaTags = metaTags;
    }

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public SocialTracking getSocialTracking() {
        return socialTracking;
    }

    public void setSocialTracking(SocialTracking socialTracking) {
        this.socialTracking = socialTracking;
    }
}
