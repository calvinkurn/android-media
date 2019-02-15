package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author errysuprayogi on 29,January,2019
 */
public class ImageHolder implements Parcelable {

    @Expose(serialize = false)
    private boolean invoke;

    protected ImageHolder(Parcel in) {
        invoke = in.readByte() != 0;
    }

    public ImageHolder() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (invoke ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageHolder> CREATOR = new Creator<ImageHolder>() {
        @Override
        public ImageHolder createFromParcel(Parcel in) {
            return new ImageHolder(in);
        }

        @Override
        public ImageHolder[] newArray(int size) {
            return new ImageHolder[size];
        }
    };

    public boolean isInvoke() {
        return invoke;
    }

    public void invoke(){
        this.invoke = true;
    }
}
