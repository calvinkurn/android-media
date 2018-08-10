package com.tokopedia.shop.common.graphql.data.shopetalase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopEtalaseViewModel implements Parcelable{
    private String id = "";
    private String name = "";
    private int count = 0;

    public ShopEtalaseViewModel(ShopEtalaseModel shopEtalaseModel){
        this.id = shopEtalaseModel.getId();
        this.name = shopEtalaseModel.getName();
        this.count = shopEtalaseModel.getCount();
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
    }

    public ShopEtalaseViewModel() {
    }

    protected ShopEtalaseViewModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<ShopEtalaseViewModel> CREATOR = new Creator<ShopEtalaseViewModel>() {
        @Override
        public ShopEtalaseViewModel createFromParcel(Parcel source) {
            return new ShopEtalaseViewModel(source);
        }

        @Override
        public ShopEtalaseViewModel[] newArray(int size) {
            return new ShopEtalaseViewModel[size];
        }
    };
}
