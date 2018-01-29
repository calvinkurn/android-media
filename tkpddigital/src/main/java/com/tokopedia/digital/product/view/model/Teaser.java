package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/3/17.
 */

public class Teaser implements Parcelable {


    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
    }

    public Teaser() {
    }

    protected Teaser(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Teaser> CREATOR = new Parcelable.Creator<Teaser>() {
        @Override
        public Teaser createFromParcel(Parcel source) {
            return new Teaser(source);
        }

        @Override
        public Teaser[] newArray(int size) {
            return new Teaser[size];
        }
    };
}
