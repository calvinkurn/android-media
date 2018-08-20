package com.tokopedia.shop.common.graphql.data.shopetalase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopEtalaseModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("count")
    @Expose
    private int count = 0;

    public ShopEtalaseModel(){}

    protected ShopEtalaseModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        count = in.readInt();
    }

    public static final Creator<ShopEtalaseModel> CREATOR = new Creator<ShopEtalaseModel>() {
        @Override
        public ShopEtalaseModel createFromParcel(Parcel in) {
            return new ShopEtalaseModel(in);
        }

        @Override
        public ShopEtalaseModel[] newArray(int size) {
            return new ShopEtalaseModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(count);
    }
}
