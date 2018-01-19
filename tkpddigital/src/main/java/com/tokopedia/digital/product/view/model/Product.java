package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Product implements Parcelable {
    public static final int STATUS_OUT_OF_STOCK = 3;
    public static final int STATUS_INACTIVE = 2;

    private String productId;
    private String productType;

    private String desc;
    private String detail;
    private String detailUrl;
    private String detailUrlText;
    private String info;
    private String price;
    private long pricePlain;
    private Promo promo;
    private int status;

    private Product(Builder builder) {
        setProductId(builder.productId);
        setProductType(builder.productType);
        setDesc(builder.desc);
        setDetail(builder.detail);
        setDetailUrl(builder.detailUrl);
        setDetailUrlText(builder.detailUrlText);
        setInfo(builder.info);
        setPrice(builder.price);
        setPricePlain(builder.pricePlain);
        setPromo(builder.promo);
        setStatus(builder.status);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getDetailUrlText() {
        return detailUrlText;
    }

    public void setDetailUrlText(String detailUrlText) {
        this.detailUrlText = detailUrlText;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getPricePlain() {
        return pricePlain;
    }

    public void setPricePlain(long pricePlain) {
        this.pricePlain = pricePlain;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.productType);
        dest.writeString(this.desc);
        dest.writeString(this.detail);
        dest.writeString(this.detailUrl);
        dest.writeString(this.detailUrlText);
        dest.writeString(this.info);
        dest.writeString(this.price);
        dest.writeLong(this.pricePlain);
        dest.writeParcelable(this.promo, flags);
        dest.writeInt(this.status);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.productId = in.readString();
        this.productType = in.readString();
        this.desc = in.readString();
        this.detail = in.readString();
        this.detailUrl = in.readString();
        this.detailUrlText = in.readString();
        this.info = in.readString();
        this.price = in.readString();
        this.pricePlain = in.readLong();
        this.promo = in.readParcelable(Promo.class.getClassLoader());
        this.status = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static final class Builder {
        private String productId;
        private String productType;
        private String desc;
        private String detail;
        private String detailUrl;
        private String detailUrlText;
        private String info;
        private String price;
        private long pricePlain;
        private Promo promo;
        private int status;

        public Builder() {
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder productType(String val) {
            productType = val;
            return this;
        }

        public Builder desc(String val) {
            desc = val;
            return this;
        }

        public Builder detail(String val) {
            detail = val;
            return this;
        }

        public Builder detailUrl(String val) {
            detailUrl = val;
            return this;
        }

        public Builder detailUrlText(String val) {
            detailUrlText = val;
            return this;
        }

        public Builder info(String val) {
            info = val;
            return this;
        }

        public Builder price(String val) {
            price = val;
            return this;
        }

        public Builder pricePlain(long val) {
            pricePlain = val;
            return this;
        }

        public Builder promo(Promo val) {
            promo = val;
            return this;
        }

        public Builder status(int val) {
            status = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    @Override
    public String toString() {
        return desc;
    }

}
