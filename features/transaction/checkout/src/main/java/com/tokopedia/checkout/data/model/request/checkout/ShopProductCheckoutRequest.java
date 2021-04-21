package com.tokopedia.checkout.data.model.request.checkout;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class ShopProductCheckoutRequest implements Parcelable {

    @SuppressLint("Invalid Data Type")
    @SerializedName("shop_id")
    @Expose
    public int shopId;
    @SerializedName("is_preorder")
    @Expose
    public int isPreorder;
    @SerializedName("finsurance")
    @Expose
    public int finsurance;
    @SerializedName("shipping_info")
    @Expose
    public ShippingInfoCheckoutRequest shippingInfo;
    @SerializedName("is_dropship")
    @Expose
    public int isDropship;
    @SerializedName("dropship_data")
    @Expose
    public DropshipDataCheckoutRequest dropshipData;
    @SerializedName("product_data")
    @Expose
    public List<ProductDataCheckoutRequest> productData = new ArrayList<>();
    @SerializedName("fcancel_partial")
    @Expose
    public int fcancelPartial;
    @SuppressLint("Invalid Data Type")
    @SerializedName("warehouse_id")
    @Expose
    public int warehouseId;
    @SerializedName("promo_codes")
    @Expose
    public ArrayList<String> promoCodes;
    @SerializedName("promos")
    @Expose
    public List<PromoRequest> promos;
    @SerializedName("is_order_priority")
    @Expose
    public int isOrderPriority;

    // Additional data, won't be dispatched over network
    public String cartString = "";

    public ShopProductCheckoutRequest() {
    }

    private ShopProductCheckoutRequest(Builder builder) {
        shopId = builder.shopId;
        isPreorder = builder.isPreorder;
        finsurance = builder.finsurance;
        shippingInfo = builder.shippingInfo;
        isDropship = builder.isDropship;
        dropshipData = builder.dropshipData;
        productData = builder.productData;
        fcancelPartial = builder.fcancelPartial;
        warehouseId = builder.warehouseId;
        promoCodes = builder.promoCodes;
        promos = builder.promos;
        isOrderPriority = builder.isOrderPriority;
        cartString = builder.cartString;
    }


    protected ShopProductCheckoutRequest(Parcel in) {
        shopId = in.readInt();
        isPreorder = in.readInt();
        finsurance = in.readInt();
        shippingInfo = in.readParcelable(ShippingInfoCheckoutRequest.class.getClassLoader());
        isDropship = in.readInt();
        dropshipData = in.readParcelable(DropshipDataCheckoutRequest.class.getClassLoader());
        productData = in.createTypedArrayList(ProductDataCheckoutRequest.CREATOR);
        fcancelPartial = in.readInt();
        warehouseId = in.readInt();
        promoCodes = in.createStringArrayList();
        isOrderPriority = in.readInt();
        cartString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shopId);
        dest.writeInt(isPreorder);
        dest.writeInt(finsurance);
        dest.writeParcelable(shippingInfo, flags);
        dest.writeInt(isDropship);
        dest.writeParcelable(dropshipData, flags);
        dest.writeTypedList(productData);
        dest.writeInt(fcancelPartial);
        dest.writeInt(warehouseId);
        dest.writeStringList(promoCodes);
        dest.writeInt(isOrderPriority);
        dest.writeString(cartString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopProductCheckoutRequest> CREATOR = new Creator<ShopProductCheckoutRequest>() {
        @Override
        public ShopProductCheckoutRequest createFromParcel(Parcel in) {
            return new ShopProductCheckoutRequest(in);
        }

        @Override
        public ShopProductCheckoutRequest[] newArray(int size) {
            return new ShopProductCheckoutRequest[size];
        }
    };

    public int getShopId() {
        return shopId;
    }

    public static final class Builder {
        private int shopId;
        private int isPreorder;
        private int finsurance;
        private ShippingInfoCheckoutRequest shippingInfo;
        private int isDropship;
        private DropshipDataCheckoutRequest dropshipData;
        private List<ProductDataCheckoutRequest> productData;
        private int fcancelPartial;
        private int warehouseId;
        private ArrayList<String> promoCodes;
        private List<PromoRequest> promos;
        private int isOrderPriority;
        private String cartString;

        public Builder() {
        }

        public Builder shopId(int val) {
            shopId = val;
            return this;
        }

        public Builder isPreorder(int val) {
            isPreorder = val;
            return this;
        }

        public Builder finsurance(int val) {
            finsurance = val;
            return this;
        }

        public Builder shippingInfo(ShippingInfoCheckoutRequest val) {
            shippingInfo = val;
            return this;
        }

        public Builder isDropship(int val) {
            isDropship = val;
            return this;
        }

        public Builder dropshipData(DropshipDataCheckoutRequest val) {
            dropshipData = val;
            return this;
        }

        public Builder productData(List<ProductDataCheckoutRequest> val) {
            productData = val;
            return this;
        }

        public Builder fcancelPartial(int val) {
            fcancelPartial = val;
            return this;
        }

        public Builder warehouseId(int id) {
            warehouseId = id;
            return this;
        }

        public Builder promoCodes(ArrayList<String> val) {
            promoCodes = val;
            return this;
        }

        public Builder promos(List<PromoRequest> val) {
            promos = val;
            return this;
        }

        public Builder isOrderPriority(int val) {
            isOrderPriority = val;
            return this;
        }

        public Builder cartString(String val) {
            cartString = val;
            return this;
        }

        public ShopProductCheckoutRequest build() {
            return new ShopProductCheckoutRequest(this);
        }
    }
}
