package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/2/17.
 */

public class BannerTitle implements Parcelable {
    private String title;

    public BannerTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
    }

    protected BannerTitle(Parcel in) {
        this.title = in.readString();
    }

    public static final Parcelable.Creator<BannerTitle> CREATOR = new Parcelable.Creator<BannerTitle>() {
        @Override
        public BannerTitle createFromParcel(Parcel source) {
            return new BannerTitle(source);
        }

        @Override
        public BannerTitle[] newArray(int size) {
            return new BannerTitle[size];
        }
    };
}
