package com.tokopedia.transactiondata.entity.request;

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


        public ShopProductCheckoutRequest build() {
            return new ShopProductCheckoutRequest(this);
        }
    }
}
