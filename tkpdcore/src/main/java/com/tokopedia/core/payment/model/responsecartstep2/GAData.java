package com.tokopedia.core.payment.model.responsecartstep2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * GAData
 * Created by Angga.Prasetiyo on 15/07/2016.
 */
public class GAData implements Parcelable{
    @SerializedName("detail")
    @Expose
    private List<Detail> detail = new ArrayList<Detail>();
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("shipping")
    @Expose
    private String shipping;
    @SerializedName("shop_name")
    @Expose
    private String shopName;

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    protected GAData(Parcel in) {
        if (in.readByte() == 0x01) {
            detail = new ArrayList<Detail>();
            in.readList(detail, Detail.class.getClassLoader());
        } else {
            detail = null;
        }
        city = in.readString();
        itemPrice = in.readString();
        shopId = in.readString();
        orderId = in.readString();
        province = in.readString();
        shipping = in.readString();
        shopName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (detail == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(detail);
        }
        dest.writeString(city);
        dest.writeString(itemPrice);
        dest.writeString(shopId);
        dest.writeString(orderId);
        dest.writeString(province);
        dest.writeString(shipping);
        dest.writeString(shopName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GAData> CREATOR = new Parcelable.Creator<GAData>() {
        @Override
        public GAData createFromParcel(Parcel in) {
            return new GAData(in);
        }

        @Override
        public GAData[] newArray(int size) {
            return new GAData[size];
        }
    };
}
