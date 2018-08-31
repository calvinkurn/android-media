package com.tokopedia.common_digital.cart.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class Relationships implements Parcelable {
    private Relation relationProduct;
    private Relation relationCategory;
    private Relation relationOperator;

    public Relation getRelationProduct() {
        return relationProduct;
    }

    public void setRelationProduct(Relation relationProduct) {
        this.relationProduct = relationProduct;
    }

    public Relation getRelationCategory() {
        return relationCategory;
    }

    public void setRelationCategory(Relation relationCategory) {
        this.relationCategory = relationCategory;
    }

    public Relation getRelationOperator() {
        return relationOperator;
    }

    public void setRelationOperator(Relation relationOperator) {
        this.relationOperator = relationOperator;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.relationProduct, flags);
        dest.writeParcelable(this.relationCategory, flags);
        dest.writeParcelable(this.relationOperator, flags);
    }

    public Relationships() {
    }

    protected Relationships(Parcel in) {
        this.relationProduct = in.readParcelable(Relation.class.getClassLoader());
        this.relationCategory = in.readParcelable(Relation.class.getClassLoader());
        this.relationOperator = in.readParcelable(Relation.class.getClassLoader());
    }

    public static final Creator<Relationships> CREATOR =
            new Creator<Relationships>() {
                @Override
                public Relationships createFromParcel(Parcel source) {
                    return new Relationships(source);
                }

                @Override
                public Relationships[] newArray(int size) {
                    return new Relationships[size];
                }
            };
}
