package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ShopStats implements Parcelable {
    private static final String TAG = ShopStats.class.getSimpleName();
    @SerializedName("shop_service_rate")
    @Expose
    private Integer shopServiceRate;
    @SerializedName("shop_speed_rate")
    @Expose
    private Integer shopSpeedRate;
    @SerializedName("shop_total_transaction")
    @Expose
    private String shopTotalTransaction;
    @SerializedName("shop_speed_description")
    @Expose
    private String shopSpeedDescription;
    @SerializedName("shop_total_etalase")
    @Expose
    private Integer shopTotalEtalase;
    @SerializedName("shop_service_description")
    @Expose
    private String shopServiceDescription;
    @SerializedName("shop_item_sold")
    @Expose
    private String shopItemSold;
    @SerializedName("shop_total_product")
    @Expose
    private String shopTotalProduct;
    @SerializedName("shop_accuracy_rate")
    @Expose
    private Integer shopAccuracyRate;
    @SerializedName("shop_accuracy_description")
    @Expose
    private String shopAccuracyDescription;
    @SerializedName("shop_badge_level")
    @Expose
    private ShopBadge shopBadge;

    public ShopStats() {
    }

    public Integer getShopServiceRate() {
        return shopServiceRate;
    }

    public void setShopServiceRate(Integer shopServiceRate) {
        this.shopServiceRate = shopServiceRate;
    }

    public Integer getShopSpeedRate() {
        return shopSpeedRate;
    }

    public void setShopSpeedRate(Integer shopSpeedRate) {
        this.shopSpeedRate = shopSpeedRate;
    }

    public String getShopTotalTransaction() {
        return shopTotalTransaction;
    }

    public void setShopTotalTransaction(String shopTotalTransaction) {
        this.shopTotalTransaction = shopTotalTransaction;
    }

    public String getShopSpeedDescription() {
        return shopSpeedDescription;
    }

    public void setShopSpeedDescription(String shopSpeedDescription) {
        this.shopSpeedDescription = shopSpeedDescription;
    }

    public Integer getShopTotalEtalase() {
        return shopTotalEtalase;
    }

    public void setShopTotalEtalase(Integer shopTotalEtalase) {
        this.shopTotalEtalase = shopTotalEtalase;
    }

    public String getShopServiceDescription() {
        return shopServiceDescription;
    }

    public void setShopServiceDescription(String shopServiceDescription) {
        this.shopServiceDescription = shopServiceDescription;
    }

    public String getShopItemSold() {
        return shopItemSold;
    }

    public void setShopItemSold(String shopItemSold) {
        this.shopItemSold = shopItemSold;
    }

    public String getShopTotalProduct() {
        return shopTotalProduct;
    }

    public void setShopTotalProduct(String shopTotalProduct) {
        this.shopTotalProduct = shopTotalProduct;
    }

    public Integer getShopAccuracyRate() {
        return shopAccuracyRate;
    }

    public void setShopAccuracyRate(Integer shopAccuracyRate) {
        this.shopAccuracyRate = shopAccuracyRate;
    }

    public String getShopAccuracyDescription() {
        return shopAccuracyDescription;
    }

    public void setShopAccuracyDescription(String shopAccuracyDescription) {
        this.shopAccuracyDescription = shopAccuracyDescription;
    }

    public ShopBadge getShopBadge() {
        return shopBadge;
    }

    public void setShopBadge(ShopBadge shopBadge) {
        this.shopBadge = shopBadge;
    }

    protected ShopStats(Parcel in) {
        shopServiceRate = in.readByte() == 0x00 ? null : in.readInt();
        shopSpeedRate = in.readByte() == 0x00 ? null : in.readInt();
        shopTotalTransaction = in.readString();
        shopSpeedDescription = in.readString();
        shopTotalEtalase = in.readByte() == 0x00 ? null : in.readInt();
        shopServiceDescription = in.readString();
        shopItemSold = in.readString();
        shopTotalProduct = in.readString();
        shopAccuracyRate = in.readByte() == 0x00 ? null : in.readInt();
        shopAccuracyDescription = in.readString();
        shopBadge = (ShopBadge) in.readValue(ShopBadge.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (shopServiceRate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopServiceRate);
        }
        if (shopSpeedRate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopSpeedRate);
        }
        dest.writeString(shopTotalTransaction);
        dest.writeString(shopSpeedDescription);
        if (shopTotalEtalase == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopTotalEtalase);
        }
        dest.writeString(shopServiceDescription);
        dest.writeString(shopItemSold);
        dest.writeString(shopTotalProduct);
        if (shopAccuracyRate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopAccuracyRate);
        }
        dest.writeString(shopAccuracyDescription);
        dest.writeValue(shopBadge);
    }

    @SuppressWarnings("unused")
    public static final Creator<ShopStats> CREATOR = new Creator<ShopStats>() {
        @Override
        public ShopStats createFromParcel(Parcel in) {
            return new ShopStats(in);
        }

        @Override
        public ShopStats[] newArray(int size) {
            return new ShopStats[size];
        }
    };


    public static class Builder {
        private Integer shopServiceRate;
        private Integer shopSpeedRate;
        private String shopTotalTransaction;
        private String shopSpeedDescription;
        private Integer shopTotalEtalase;
        private String shopServiceDescription;
        private String shopItemSold;
        private String shopTotalProduct;
        private Integer shopAccuracyRate;
        private String shopAccuracyDescription;
        private ShopBadge shopBadge;

        private Builder() {
        }

        public static Builder aShopStats() {
            return new Builder();
        }

        public Builder setShopServiceRate(Integer shopServiceRate) {
            this.shopServiceRate = shopServiceRate;
            return this;
        }

        public Builder setShopSpeedRate(Integer shopSpeedRate) {
            this.shopSpeedRate = shopSpeedRate;
            return this;
        }

        public Builder setShopTotalTransaction(String shopTotalTransaction) {
            this.shopTotalTransaction = shopTotalTransaction;
            return this;
        }

        public Builder setShopSpeedDescription(String shopSpeedDescription) {
            this.shopSpeedDescription = shopSpeedDescription;
            return this;
        }

        public Builder setShopTotalEtalase(Integer shopTotalEtalase) {
            this.shopTotalEtalase = shopTotalEtalase;
            return this;
        }

        public Builder setShopServiceDescription(String shopServiceDescription) {
            this.shopServiceDescription = shopServiceDescription;
            return this;
        }

        public Builder setShopItemSold(String shopItemSold) {
            this.shopItemSold = shopItemSold;
            return this;
        }

        public Builder setShopTotalProduct(String shopTotalProduct) {
            this.shopTotalProduct = shopTotalProduct;
            return this;
        }

        public Builder setShopAccuracyRate(Integer shopAccuracyRate) {
            this.shopAccuracyRate = shopAccuracyRate;
            return this;
        }

        public Builder setShopAccuracyDescription(String shopAccuracyDescription) {
            this.shopAccuracyDescription = shopAccuracyDescription;
            return this;
        }

        public Builder setShopBadge(ShopBadge shopBadge) {
            this.shopBadge = shopBadge;
            return this;
        }

        public Builder but() {
            return aShopStats().setShopServiceRate(shopServiceRate).setShopSpeedRate(shopSpeedRate).setShopTotalTransaction(shopTotalTransaction).setShopSpeedDescription(shopSpeedDescription).setShopTotalEtalase(shopTotalEtalase).setShopServiceDescription(shopServiceDescription).setShopItemSold(shopItemSold).setShopTotalProduct(shopTotalProduct).setShopAccuracyRate(shopAccuracyRate).setShopAccuracyDescription(shopAccuracyDescription).setShopBadge(shopBadge);
        }

        public ShopStats build() {
            ShopStats shopStats = new ShopStats();
            shopStats.setShopServiceRate(shopServiceRate);
            shopStats.setShopSpeedRate(shopSpeedRate);
            shopStats.setShopTotalTransaction(shopTotalTransaction);
            shopStats.setShopSpeedDescription(shopSpeedDescription);
            shopStats.setShopTotalEtalase(shopTotalEtalase);
            shopStats.setShopServiceDescription(shopServiceDescription);
            shopStats.setShopItemSold(shopItemSold);
            shopStats.setShopTotalProduct(shopTotalProduct);
            shopStats.setShopAccuracyRate(shopAccuracyRate);
            shopStats.setShopAccuracyDescription(shopAccuracyDescription);
            shopStats.setShopBadge(shopBadge);
            return shopStats;
        }
    }
}
