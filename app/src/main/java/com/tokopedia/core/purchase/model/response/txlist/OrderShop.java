package com.tokopedia.core.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderShop implements Parcelable {
    private static final String TAG = OrderShop.class.getSimpleName();

    @SerializedName("shop_uri")
    @Expose
    private String shopUri;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_pic")
    @Expose
    private String shopPic;

    protected OrderShop(Parcel in) {
        shopUri = in.readString();
        shopId = in.readString();
        shopName = in.readString();
        shopPic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopUri);
        dest.writeString(shopId);
        dest.writeString(shopName);
        dest.writeString(shopPic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderShop> CREATOR = new Creator<OrderShop>() {
        @Override
        public OrderShop createFromParcel(Parcel in) {
            return new OrderShop(in);
        }

        @Override
        public OrderShop[] newArray(int size) {
            return new OrderShop[size];
        }
    };

    public String getShopUri() {
        return shopUri;
    }

    public void setShopUri(String shopUri) {
        this.shopUri = shopUri;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }
}
