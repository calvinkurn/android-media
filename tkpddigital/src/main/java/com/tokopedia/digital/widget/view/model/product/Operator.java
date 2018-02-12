package com.tokopedia.digital.widget.view.model.product;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Operator implements Parcelable {

    private Data data;

    public Operator() {
    }

    protected Operator(Parcel in) {
        data = in.readParcelable(Data.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Operator> CREATOR = new Creator<Operator>() {
        @Override
        public Operator createFromParcel(Parcel in) {
            return new Operator(in);
        }

        @Override
        public Operator[] newArray(int size) {
            return new Operator[size];
        }
    };

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
