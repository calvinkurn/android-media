package com.tokopedia.common_digital.common.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class DigitalCategoryDetailPassData implements Parcelable {
    public static final String PARAM_CATEGORY_ID = "category_id";
    public static final String PARAM_MENU_ID = "menu_id";
    public static final String PARAM_OPERATOR_ID = "operator_id";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_CLIENT_NUMBER = "client_number";
    public static final String PARAM_IS_FROM_WIDGET = "is_from_widget";

    private String categoryId;
    private String operatorId;
    private String productId;
    private String clientNumber;
    private String menuId;
    private boolean isFromWidget;
    private boolean isCouponApplied;
    private String url;
    private String appLinks;
    private String categoryName;
    private String additionalETollBalance;
    private String additionalETollLastUpdatedDate;
    private String additionalETollOperatorName;

    private DigitalCategoryDetailPassData(Builder builder) {
        setCategoryId(builder.categoryId);
        setOperatorId(builder.operatorId);
        setMenuId(builder.menuId);
        setProductId(builder.productId);
        setClientNumber(builder.clientNumber);
        setFromWidget(builder.isFromWidget);
        setCouponApplied(builder.isCouponApplied);
        setUrl(builder.url);
        setAppLinks(builder.appLinks);
        setCategoryName(builder.categoryName);
        setAdditionalETollBalance(builder.additionalETollLastBalance);
        setAdditionalETollLastUpdatedDate(builder.additionalETollLastUpdatedDate);
        setAdditionalETollOperatorName(builder.additionalETollOperatorName);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public boolean isFromWidget() {
        return isFromWidget;
    }

    public void setFromWidget(boolean fromWidget) {
        isFromWidget = fromWidget;
    }

    public boolean isCouponApplied() {
        return isCouponApplied;
    }

    public void setCouponApplied(boolean couponApplied) {
        isCouponApplied = couponApplied;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppLinks() {
        return appLinks;
    }

    public void setAppLinks(String appLinks) {
        this.appLinks = appLinks;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAdditionalETollBalance() {
        return additionalETollBalance;
    }

    public void setAdditionalETollBalance(String additionalETollBalance) {
        this.additionalETollBalance = additionalETollBalance;
    }

    public String getAdditionalETollLastUpdatedDate() {
        return additionalETollLastUpdatedDate;
    }

    public void setAdditionalETollLastUpdatedDate(String additionalETollLastUpdatedDate) {
        this.additionalETollLastUpdatedDate = additionalETollLastUpdatedDate;
    }

    public String getAdditionalETollOperatorName() {
        return additionalETollOperatorName;
    }

    public void setAdditionalETollOperatorName(String additionalETollOperatorName) {
        this.additionalETollOperatorName = additionalETollOperatorName;
    }

    protected DigitalCategoryDetailPassData(Parcel in) {
        categoryId = in.readString();
        operatorId = in.readString();
        productId = in.readString();
        clientNumber = in.readString();
        menuId = in.readString();
        isFromWidget = in.readByte() != 0;
        isCouponApplied = in.readByte() != 0;
        url = in.readString();
        appLinks = in.readString();
        categoryName = in.readString();
        additionalETollBalance = in.readString();
        additionalETollLastUpdatedDate = in.readString();
        additionalETollOperatorName = in.readString();
    }

    public static final Creator<DigitalCategoryDetailPassData> CREATOR = new Creator<DigitalCategoryDetailPassData>() {
        @Override
        public DigitalCategoryDetailPassData createFromParcel(Parcel in) {
            return new DigitalCategoryDetailPassData(in);
        }

        @Override
        public DigitalCategoryDetailPassData[] newArray(int size) {
            return new DigitalCategoryDetailPassData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryId);
        parcel.writeString(operatorId);
        parcel.writeString(productId);
        parcel.writeString(clientNumber);
        parcel.writeString(menuId);
        parcel.writeByte((byte) (isFromWidget ? 1 : 0));
        parcel.writeByte((byte) (isCouponApplied ? 1 : 0));
        parcel.writeString(url);
        parcel.writeString(appLinks);
        parcel.writeString(categoryName);
        parcel.writeString(additionalETollBalance);
        parcel.writeString(additionalETollLastUpdatedDate);
        parcel.writeString(additionalETollOperatorName);
    }

    public static final class Builder {
        private String categoryId;
        private String operatorId;
        private String productId;
        private String menuId;
        private String clientNumber;
        private boolean isFromWidget;
        private boolean isCouponApplied;
        private String url;
        private String appLinks;
        private String categoryName;
        private String additionalETollLastBalance;
        private String additionalETollLastUpdatedDate;
        private String additionalETollOperatorName;

        public Builder() {
        }

        public Builder menuId(String val) {
            menuId = val;
            return this;
        }

        public Builder categoryId(String val) {
            categoryId = val;
            return this;
        }

        public Builder operatorId(String val) {
            operatorId = val;
            return this;
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder clientNumber(String val) {
            clientNumber = val;
            return this;
        }

        public Builder isFromWidget(boolean val) {
            isFromWidget = val;
            return this;
        }

        public Builder isCouponApplied(boolean val) {
            isCouponApplied = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder appLinks(String val) {
            appLinks = val;
            return this;
        }

        public Builder categoryName(String val) {
            categoryName = val;
            return this;
        }

        public Builder additionalETollLastBalance(String val) {
            additionalETollLastBalance = val;
            return this;
        }

        public Builder additionalETollLastUpdatedDate(String val) {
            additionalETollLastUpdatedDate = val;
            return this;
        }

        public Builder additionalETollOperatorName(String val) {
            additionalETollOperatorName = val;
            return this;
        }

        public DigitalCategoryDetailPassData build() {
            return new DigitalCategoryDetailPassData(this);
        }
    }
}
