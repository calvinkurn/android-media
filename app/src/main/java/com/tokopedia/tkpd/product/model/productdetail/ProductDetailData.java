package com.tokopedia.tkpd.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
public class ProductDetailData implements Parcelable{
    private static final String TAG = ProductDetailData.class.getSimpleName();

    @SerializedName("info")
    @Expose
    private ProductInfo info;
    @SerializedName("statistic")
    @Expose
    private ProductStatistic statistic;
    @SerializedName("shop_info")
    @Expose
    private ProductShopInfo shopInfo;
    @SerializedName("wholesale_price")
    @Expose
    private List<ProductWholesalePrice> wholesalePrice = new ArrayList<ProductWholesalePrice>();
    @SerializedName("breadcrumb")
    @Expose
    private List<ProductBreadcrumb> breadcrumb = new ArrayList<ProductBreadcrumb>();
    @SerializedName("rating")
    @Expose
    private ProductRating rating;
    @SerializedName("preorder")
    @Expose
    private ProductPreOrder preOrder;
    @SerializedName("cashback")
    @Expose
    private ProductCashback cashBack;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = new ArrayList<ProductImage>();

    public ProductDetailData() {
    }

    public ProductInfo getInfo() {
        return info;
    }

    public void setInfo(ProductInfo info) {
        this.info = info;
    }

    public ProductStatistic getStatistic() {
        return statistic;
    }

    public void setStatistic(ProductStatistic statistic) {
        this.statistic = statistic;
    }

    public ProductShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ProductShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public List<ProductWholesalePrice> getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(List<ProductWholesalePrice> wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public List<ProductBreadcrumb> getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(List<ProductBreadcrumb> breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductRating getRating() {
        return rating;
    }

    public void setCashBack(ProductCashback cashBack) {
        this.cashBack = cashBack;
    }

    public ProductCashback getCashBack() {
        return cashBack;
    }

    public ProductPreOrder getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(ProductPreOrder preOrder) {
        this.preOrder = preOrder;
    }

    public void setRating(ProductRating rating) {
        this.rating = rating;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    protected ProductDetailData(Parcel in) {
        info = (ProductInfo) in.readValue(ProductInfo.class.getClassLoader());
        statistic = (ProductStatistic) in.readValue(ProductStatistic.class.getClassLoader());
        shopInfo = (ProductShopInfo) in.readValue(ProductShopInfo.class.getClassLoader());
        if (in.readByte() == 0x01) {
            wholesalePrice = new ArrayList<ProductWholesalePrice>();
            in.readList(wholesalePrice, ProductWholesalePrice.class.getClassLoader());
        } else {
            wholesalePrice = null;
        }
        if (in.readByte() == 0x01) {
            breadcrumb = new ArrayList<ProductBreadcrumb>();
            in.readList(breadcrumb, ProductBreadcrumb.class.getClassLoader());
        } else {
            breadcrumb = null;
        }
        rating = (ProductRating) in.readValue(ProductRating.class.getClassLoader());
        preOrder = (ProductPreOrder) in.readValue(ProductPreOrder.class.getClassLoader());
        cashBack = (ProductCashback) in.readValue(ProductCashback.class.getClassLoader());
        if (in.readByte() == 0x01) {
            productImages = new ArrayList<ProductImage>();
            in.readList(productImages, ProductImage.class.getClassLoader());
        } else {
            productImages = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(info);
        dest.writeValue(statistic);
        dest.writeValue(shopInfo);
        if (wholesalePrice == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(wholesalePrice);
        }
        if (breadcrumb == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(breadcrumb);
        }
        dest.writeValue(rating);
        dest.writeValue(preOrder);
        dest.writeValue(cashBack);
        if (productImages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(productImages);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductDetailData> CREATOR = new Parcelable.Creator<ProductDetailData>() {
        @Override
        public ProductDetailData createFromParcel(Parcel in) {
            return new ProductDetailData(in);
        }

        @Override
        public ProductDetailData[] newArray(int size) {
            return new ProductDetailData[size];
        }
    };


    public static class Builder {
        private ProductInfo info;
        private ProductStatistic statistic;
        private ProductShopInfo shopInfo;
        private List<ProductWholesalePrice> wholesalePrice = new ArrayList<ProductWholesalePrice>();
        private List<ProductBreadcrumb> breadcrumb = new ArrayList<ProductBreadcrumb>();
        private ProductRating rating;
        private List<ProductImage> productImages = new ArrayList<ProductImage>();
        private ProductCashback cashBack;

        private Builder() {
        }

        public static Builder aProductInfoData() {
            return new Builder();
        }

        public Builder setInfo(ProductInfo info) {
            this.info = info;
            return this;
        }

        public Builder setStatistic(ProductStatistic statistic) {
            this.statistic = statistic;
            return this;
        }

        public Builder setShopInfo(ProductShopInfo shopInfo) {
            this.shopInfo = shopInfo;
            return this;
        }

        public Builder setWholesalePrice(List<ProductWholesalePrice> wholesalePrice) {
            this.wholesalePrice = wholesalePrice;
            return this;
        }

        public Builder setBreadcrumb(List<ProductBreadcrumb> breadcrumb) {
            this.breadcrumb = breadcrumb;
            return this;
        }

        public Builder setRating(ProductRating rating) {
            this.rating = rating;
            return this;
        }

        public Builder setProductImages(List<ProductImage> productImages) {
            this.productImages = productImages;
            return this;
        }

        public Builder setCashBack(ProductCashback cashBack) {
            this.cashBack = cashBack;
            return this;
        }

        public Builder but() {
            return aProductInfoData().setInfo(info).setStatistic(statistic).setShopInfo(shopInfo).setWholesalePrice(wholesalePrice).setBreadcrumb(breadcrumb).setRating(rating).setProductImages(productImages).setCashBack(cashBack);
        }

        public ProductDetailData build() {
            ProductDetailData productDetailData = new ProductDetailData();
            productDetailData.setInfo(info);
            productDetailData.setStatistic(statistic);
            productDetailData.setShopInfo(shopInfo);
            productDetailData.setWholesalePrice(wholesalePrice);
            productDetailData.setBreadcrumb(breadcrumb);
            productDetailData.setRating(rating);
            productDetailData.setProductImages(productImages);
            productDetailData.setCashBack(cashBack);
            return productDetailData;
        }
    }
}
