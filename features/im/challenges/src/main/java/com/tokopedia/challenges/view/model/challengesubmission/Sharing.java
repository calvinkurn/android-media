
package com.tokopedia.challenges.view.model.challengesubmission;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sharing implements Serializable, Parcelable
{

    @SerializedName("MetaTags")
    @Expose
    private MetaTags metaTags;
    @SerializedName("Assets")
    @Expose
    private Assets assets;
    @SerializedName("SocialTracking")
    @Expose
    private SocialTracking socialTracking;
    public final static Creator<Sharing> CREATOR = new Creator<Sharing>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Sharing createFromParcel(Parcel in) {
            return new Sharing(in);
        }

        public Sharing[] newArray(int size) {
            return (new Sharing[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4987358102475114331L;

    protected Sharing(Parcel in) {
        this.metaTags = ((MetaTags) in.readValue((MetaTags.class.getClassLoader())));
        this.assets = ((Assets) in.readValue((Assets.class.getClassLoader())));
        this.socialTracking = ((SocialTracking) in.readValue((SocialTracking.class.getClassLoader())));
    }

    public Sharing() {
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(metaTags);
        dest.writeValue(assets);
        dest.writeValue(socialTracking);
    }

    public int describeContents() {
        return  0;
    }

}
