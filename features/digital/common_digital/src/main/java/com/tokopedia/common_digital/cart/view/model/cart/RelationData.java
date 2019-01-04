package com.tokopedia.common_digital.cart.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class RelationData implements Parcelable {
    private String type;
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.id);
    }

    public RelationData() {
    }

    protected RelationData(Parcel in) {
        this.type = in.readString();
        this.id = in.readString();
    }

    public static final Creator<RelationData> CREATOR =
            new Creator<RelationData>() {
                @Override
                public RelationData createFromParcel(Parcel source) {
                    return new RelationData(source);
                }

                @Override
                public RelationData[] newArray(int size) {
                    return new RelationData[size];
                }
            };
}
