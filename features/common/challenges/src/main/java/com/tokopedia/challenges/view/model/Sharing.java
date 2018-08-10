
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

    protected Sharing(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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

}
