package com.tokopedia.digital.widget.view.model.recentnumber;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/22/17.
 */

public class Category implements Parcelable {

    private Data data;

    protected Category(Parcel in) {
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
