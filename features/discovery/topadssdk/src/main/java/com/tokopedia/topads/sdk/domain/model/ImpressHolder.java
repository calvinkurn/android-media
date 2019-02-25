package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author errysuprayogi on 29,January,2019
 */
public class ImpressHolder implements Parcelable {

    @Expose(serialize = false, deserialize = false)
    private boolean invoke;

    protected ImpressHolder(Parcel in) {
        invoke = in.readByte() != 0;
    }

    public ImpressHolder() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (invoke ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImpressHolder> CREATOR = new Creator<ImpressHolder>() {
        @Override
        public ImpressHolder createFromParcel(Parcel in) {
            return new ImpressHolder(in);
        }

        @Override
        public ImpressHolder[] newArray(int size) {
            return new ImpressHolder[size];
        }
    };

    public boolean isInvoke() {
        return invoke;
    }

    public void invoke(){
        this.invoke = true;
    }
}
