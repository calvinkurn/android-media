package com.tokopedia.core.router.transactionmodule.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by Angga.Prasetiyo on 11/03/2016.
 */
public class ProductCartPass implements Parcelable {
    private static final String TAG = ProductCartPass.class.getSimpleName();

    private String productId;
    private String productName;
    private String imageUri;
    private int minOrder;
    private String productCategory;
    private String weight;
    private String shopId;
    private String price;
    private String notes;
    private String categoryId;
    private String shopName;
    private String categoryLevelName;
    private String shopType;

    public ProductCartPass() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCategoryLevelName() {
        return categoryLevelName;
    }

    public void setCategoryLevelName(String categoryLevelName) {
        this.categoryLevelName = categoryLevelName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    protected ProductCartPass(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        imageUri = in.readString();
        minOrder = in.readInt();
        productCategory = in.readString();
        weight = in.readString();
        shopId = in.readString();
        price = in.readString();
        notes = in.readString();
        categoryId = in.readString();
        shopName = in.readString();
        categoryLevelName = in.readString();
        shopType = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(imageUri);
        dest.writeInt(minOrder);
        dest.writeString(productCategory);
        dest.writeString(weight);
        dest.writeString(shopId);
        dest.writeString(price);
        dest.writeString(notes);
        dest.writeString(categoryId);
        dest.writeString(shopName);
        dest.writeString(categoryLevelName);
        dest.writeString(shopType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductCartPass> CREATOR =
            new Parcelable.Creator<ProductCartPass>() {
                @Override
                public ProductCartPass createFromParcel(Parcel in) {
                    return new ProductCartPass(in);
                }

                @Override
                public ProductCartPass[] newArray(int size) {
                    return new ProductCartPass[size];
                }
            };


    public static class Builder {
        private String productId;
        private String productName;
        private String imageUri;
        private int minOrder;
        private String productCategory;
        private String weight;
        private String shopId;
        private String price;
        private String notes;
        private String categoryId;
        private String shopName;
        private String categoryLevelName;
        private String shopType;

        private Builder() {
        }

        public static Builder aProductCartPass() {
            return new Builder();
        }

        public Builder setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setImageUri(String imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        public Builder setMinOrder(int minOrder) {
            this.minOrder = minOrder;
            return this;
        }

        public Builder setProductCategory(String productCategory) {
            this.productCategory = productCategory;
            return this;
        }

        public Builder setWeight(String weight) {
            this.weight = weight;
            return this;
        }

        public Builder setShopId(String shopId) {
            this.shopId = shopId;
            return this;
        }

        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }

        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder setCategoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public Builder setCategoryLevelName(String categoryLevelName) {
            this.categoryLevelName = categoryLevelName;
            return this;
        }

        public Builder setShopType(String shopType) {
            this.shopType = shopType;
            return this;
        }

        public Builder but() {
            return aProductCartPass()
                    .setProductId(productId)
                    .setProductName(productName)
                    .setImageUri(imageUri)
                    .setMinOrder(minOrder)
                    .setProductCategory(productCategory)
                    .setWeight(weight)
                    .setShopId(shopId)
                    .setPrice(price)
                    .setNotes(notes)
                    .setCategoryId(categoryId)
                    .setShopName(shopName)
                    .setCategoryLevelName(categoryLevelName)
                    .setShopType(shopType);

        }

        public ProductCartPass build() {
            ProductCartPass productCartPass = new ProductCartPass();
            productCartPass.setProductId(productId);
            productCartPass.setProductName(productName);
            productCartPass.setImageUri(imageUri);
            productCartPass.setMinOrder(minOrder);
            productCartPass.setProductCategory(productCategory);
            productCartPass.setWeight(weight);
            productCartPass.setShopId(shopId);
            productCartPass.setPrice(price);
            productCartPass.setNotes(notes);
            productCartPass.setCategoryId(categoryId);
            productCartPass.setShopName(shopName);
            productCartPass.setCategoryLevelName(categoryLevelName);
            productCartPass.setShopType(shopType);
            return productCartPass;
        }
    }
}
