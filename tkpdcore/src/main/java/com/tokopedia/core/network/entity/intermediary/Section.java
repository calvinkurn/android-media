
package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Section implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("ids")
    @Expose
    private List<Integer> ids = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }


    protected Section(Parcel in) {
        title = in.readString();
        if (in.readByte() == 0x01) {
            products = new ArrayList<Product>();
            in.readList(products, Product.class.getClassLoader());
        } else {
            products = null;
        }
        if (in.readByte() == 0x01) {
            ids = new ArrayList<Integer>();
            in.readList(ids, Integer.class.getClassLoader());
        } else {
            ids = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        if (products == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(products);
        }
        if (ids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ids);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };
}