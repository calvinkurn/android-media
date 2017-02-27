package com.tokopedia.core.router.digitalmodule.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public class ExampPassData implements Parcelable {
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
    }

    public ExampPassData() {
    }

    protected ExampPassData(Parcel in) {
        this.categoryId = in.readString();
    }

    public static final Parcelable.Creator<ExampPassData> CREATOR =
            new Parcelable.Creator<ExampPassData>() {
                @Override
                public ExampPassData createFromParcel(Parcel source) {
                    return new ExampPassData(source);
                }

                @Override
                public ExampPassData[] newArray(int size) {
                    return new ExampPassData[size];
                }
            };
}
