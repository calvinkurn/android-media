package com.tokopedia.digital.widget.view.model.status;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class Version implements Parcelable {

    private int category;
    private int operator;
    private int product;
    private String minimumAndroidBuild;

    public Version() {
    }

    protected Version(Parcel in) {
        category = in.readInt();
        operator = in.readInt();
        product = in.readInt();
        minimumAndroidBuild = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category);
        dest.writeInt(operator);
        dest.writeInt(product);
        dest.writeString(minimumAndroidBuild);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Version> CREATOR = new Creator<Version>() {
        @Override
        public Version createFromParcel(Parcel in) {
            return new Version(in);
        }

        @Override
        public Version[] newArray(int size) {
            return new Version[size];
        }
    };

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public String getMinimumAndroidBuild() {
        return minimumAndroidBuild;
    }

    public void setMinimumAndroidBuild(String minimumAndroidBuild) {
        this.minimumAndroidBuild = minimumAndroidBuild;
    }
}