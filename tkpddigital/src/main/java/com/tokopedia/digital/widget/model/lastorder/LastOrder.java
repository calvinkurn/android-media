package com.tokopedia.digital.widget.model.lastorder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class LastOrder implements Parcelable {

    private String type;
    private int id;
    private Attributes attributes;

    public LastOrder() {
    }

    protected LastOrder(Parcel in) {
        type = in.readString();
        id = in.readInt();
        attributes = in.readParcelable(Attributes.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(id);
        dest.writeParcelable(attributes, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LastOrder> CREATOR = new Creator<LastOrder>() {
        @Override
        public LastOrder createFromParcel(Parcel in) {
            return new LastOrder(in);
        }

        @Override
        public LastOrder[] newArray(int size) {
            return new LastOrder[size];
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

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
