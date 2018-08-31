package com.tokopedia.digital.cart.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class Relation implements Parcelable {

    private RelationData data;

    public Relation(RelationData data) {
        this.data = data;
    }

    public RelationData getData() {
        return data;
    }

    public void setData(RelationData data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }

    protected Relation(Parcel in) {
        this.data = in.readParcelable(RelationData.class.getClassLoader());
    }

    public static final Parcelable.Creator<Relation> CREATOR =
            new Parcelable.Creator<Relation>() {
                @Override
                public Relation createFromParcel(Parcel source) {
                    return new Relation(source);
                }

                @Override
                public Relation[] newArray(int size) {
                    return new Relation[size];
                }
            };
}
