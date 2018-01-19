package com.tokopedia.digital.widget.view.model.product;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Product implements Parcelable {
    public static final int STATUS_OUT_OF_STOCK = 3;
    public static final int STATUS_INACTIVE = 2;

    private Attributes attributes;
    private String id;
    private Relationship relationships;
    private String type;

    public Product() {
    }

    protected Product(Parcel in) {
        attributes = in.readParcelable(Attributes.class.getClassLoader());
        id = in.readString();
        relationships = in.readParcelable(Relationship.class.getClassLoader());
        type = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(attributes, flags);
        dest.writeString(id);
        dest.writeParcelable(relationships, flags);
        dest.writeString(type);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Relationship getRelationships() {
        return relationships;
    }

    public void setRelationships(Relationship relationships) {
        this.relationships = relationships;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return attributes.getDesc();
    }
}
