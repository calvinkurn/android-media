package com.tokopedia.digital.widget.model.recentnumber;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class RecentNumber implements Parcelable {

    private String type;
    private Attributes attributes;
    private Relationship relationships;

    public RecentNumber() {
    }

    protected RecentNumber(Parcel in) {
        type = in.readString();
        attributes = in.readParcelable(Attributes.class.getClassLoader());
        relationships = in.readParcelable(Relationship.class.getClassLoader());
    }

    public static final Creator<RecentNumber> CREATOR = new Creator<RecentNumber>() {
        @Override
        public RecentNumber createFromParcel(Parcel in) {
            return new RecentNumber(in);
        }

        @Override
        public RecentNumber[] newArray(int size) {
            return new RecentNumber[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeParcelable(attributes, flags);
        dest.writeParcelable(relationships, flags);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Relationship getRelationships() {
        return relationships;
    }

    public void setRelationships(Relationship relationships) {
        this.relationships = relationships;
    }
}
