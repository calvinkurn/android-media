
package com.tokopedia.core.shopinfo.models.productmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Deprecated
public class ProductModel implements Parcelable {
    @SerializedName("list")
    @Expose
    public java.util.List<com.tokopedia.core.shopinfo.models.productmodel.List> list = new ArrayList<com.tokopedia.core.shopinfo.models.productmodel.List>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
    }

    public ProductModel() {
    }

    protected ProductModel(Parcel in) {
        this.list = in.createTypedArrayList(List.CREATOR);
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel source) {
            return new ProductModel(source);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };
}
