package com.tokopedia.digital.widget.view.model.recentnumber;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class Data implements Parcelable {

    private String type;
    private int id;

    protected Data(Parcel in) {
        type = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}