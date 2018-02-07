package com.tokopedia.core.var;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nisie on 10/06/15.
 * add parceler support by m.normansyah 31/10/2015
 */

public class ShopItem extends RecyclerViewItem implements Parcelable {


    @SerializedName("shop_name")// 1
    public String name;
    @SerializedName("shop_image")// 2
    public String iconUri;

    public String coverUri;// 3

    @SerializedName("shop_location")// 4
    public String location;

    public String isFav;// 5
    @SerializedName("shop_id")
    public String id;// 6
    @SerializedName("ad_key")
    public String adKey;// 7
    @SerializedName("ad_r")
    public String adR;// 8
    @SerializedName("shop_click_url")
    public String shopClickUrl;
    @SerializedName("shop_badge")
    public List<Badge> shopBadge;

    public ShopItem() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.iconUri);
        dest.writeString(this.coverUri);
        dest.writeString(this.location);
        dest.writeString(this.isFav);
        dest.writeString(this.id);
        dest.writeString(this.adKey);
        dest.writeString(this.adR);
        dest.writeString(this.shopClickUrl);
        dest.writeTypedList(this.shopBadge);
    }

    protected ShopItem(Parcel in) {
        super(in);
        this.name = in.readString();
        this.iconUri = in.readString();
        this.coverUri = in.readString();
        this.location = in.readString();
        this.isFav = in.readString();
        this.id = in.readString();
        this.adKey = in.readString();
        this.adR = in.readString();
        this.shopClickUrl = in.readString();
        this.shopBadge = in.createTypedArrayList(Badge.CREATOR);
    }

    public static final Creator<ShopItem> CREATOR = new Creator<ShopItem>() {
        @Override
        public ShopItem createFromParcel(Parcel source) {
            return new ShopItem(source);
        }

        @Override
        public ShopItem[] newArray(int size) {
            return new ShopItem[size];
        }
    };
}