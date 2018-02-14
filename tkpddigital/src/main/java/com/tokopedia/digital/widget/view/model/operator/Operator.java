package com.tokopedia.digital.widget.view.model.operator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Operator implements Parcelable {

    private Attributes attributes;
    private int id;
    private String type;

    public Operator() {
    }

    protected Operator(Parcel in) {
        attributes = in.readParcelable(Attributes.class.getClassLoader());
        id = in.readInt();
        type = in.readString();
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

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(attributes, flags);
        dest.writeInt(id);
        dest.writeString(type);
    }

    @Override
    public String toString() {
        return attributes.getName();
    }
}
