package com.tokopedia.core.analytics.model;

import java.util.Map;

/**
 * @author by alvarisi on 10/27/16.
 *
 * Modified by : Hafizh Herdi
 *
 * use map
 */
@Deprecated
public class CustomerWrapper extends BaseAnalyticsModel {
    private String mCustomerId;
    private String mFirstName;
    private String mLastName;
    private String mFullName;
    private String mEmailAddress;
    private String mMethod;
    private String phoneNumber;
    private Boolean isGoldMerchant;
    private Boolean isSeller;
    private String shopId;
    private String shopName;
    private String dateOfBirth;
    private String totalItemSold;
    private String regDate;
    private String dateShopCreated;
    private String shopLocation;
    private String tokocashAmt;
    private String saldoAmt;
    private String topAdsAmt;
    private Boolean isTopadsUser;
    private Boolean hasPurchasedMarketplace;
    private Boolean hasPurchasedDigital;
    private Boolean hasPurchasedTiket;
    private String lastTransactionDate;
    private String totalActiveProduct;
    private String shopScore;
    private String gender;

    private Map<String, String> mAttr;

    public CustomerWrapper(Builder builder) {
        mFullName               = builder.mFullName;
        mEmailAddress           = builder.mEmailAddress;
        mMethod                 = builder.mMethod;
        mAttr                   = builder.mAttr;
        phoneNumber             = builder.phoneNumber;
        isGoldMerchant          = builder.isGoldMerchant;
        isSeller                = builder.isSeller;
        shopId                  = builder.shopId;
        shopName                = builder.shopName;
        shopLocation            = builder.shopLocation;
        gender                  = builder.gender;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomerId() {
        return this.mCustomerId;
    }

    public String getFullName() {
        return this.mFullName;
    }

    public String getEmailAddress() {
        return this.mEmailAddress;
    }

    @Override
    public String getName() {
        return null;
    }

    public Boolean isGoldMerchant() {
        return isGoldMerchant;
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

    public void setGoldMerchant(Boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public Boolean isSeller() {
        return isSeller;
    }

    public void setSeller(Boolean seller) {
        isSeller = seller;
    }

    @Override
    public void setName(String name) {

    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Map<String, String> getAttr() {
        return null;
    }

    public void setFullName(String mFullName) {
        this.mFullName = mFullName;
    }


    public void setEmailAddress(String mEmailAddress) {
        this.mEmailAddress = mEmailAddress;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String mMethod) {
        this.mMethod = mMethod;
    }

    public void setAttr(Map<String, String> attr) {
        mAttr = attr;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "CustomerWrapper{" +
                "mCustomerId='" + mCustomerId + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mFullName='" + mFullName + '\'' +
                ", mEmailAddress='" + mEmailAddress + '\'' +
                ", mMethod='" + mMethod + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isGoldMerchant=" + isGoldMerchant +
                ", isSeller=" + isSeller +
                ", shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", totalItemSold='" + totalItemSold + '\'' +
                ", regDate='" + regDate + '\'' +
                ", dateShopCreated='" + dateShopCreated + '\'' +
                ", shopLocation='" + shopLocation + '\'' +
                ", tokocashAmt='" + tokocashAmt + '\'' +
                ", saldoAmt='" + saldoAmt + '\'' +
                ", topAdsAmt='" + topAdsAmt + '\'' +
                ", isTopadsUser=" + isTopadsUser +
                ", hasPurchasedMarketplace=" + hasPurchasedMarketplace +
                ", hasPurchasedDigital=" + hasPurchasedDigital +
                ", hasPurchasedTiket=" + hasPurchasedTiket +
                ", lastTransactionDate='" + lastTransactionDate + '\'' +
                ", totalActiveProduct='" + totalActiveProduct + '\'' +
                ", shopScore='" + shopScore + '\'' +
                ", mAttr=" + mAttr +
                '}';
    }

    public static class Builder{
        private String mFullName;
        private String mEmailAddress;
        private String mMethod;
        private String phoneNumber;
        private Boolean isGoldMerchant;
        private Boolean isSeller;
        private String shopId;
        private String shopName;
        private String shopLocation;
        private String gender;

        private Map<String, String> mAttr;

        public Builder() {
        }

        public Builder setShopLocation(String shopLocation) {
            this.shopLocation = shopLocation;
            return this;
        }

        public Builder setShopId(String shopId) {
            this.shopId = shopId;
            return this;
        }

        public String getShopId() {
            return shopId;
        }

        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public String getShopName() {
            return shopName;
        }

        public Boolean isGoldMerchant() {
            return isGoldMerchant;
        }

        public Builder setGoldMerchant(Boolean goldMerchant) {
            isGoldMerchant = goldMerchant;
            return this;
        }

        public Boolean isSeller() {
            return isSeller;
        }

        public Builder setSeller(Boolean seller) {
            isSeller = seller;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public Builder setFullName(String fullName) {
            mFullName = fullName;
            return this;
        }

        public Builder setEmailAddress(String emailAddress) {
            mEmailAddress = emailAddress;
            return this;
        }

        public Builder setMethod(String method) {
            mMethod = method;
            return this;
        }

        public Builder setAttr(Map<String, String> attr) {
            mAttr = attr;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public CustomerWrapper build(){
            return new CustomerWrapper(this);
        }
    }
}
