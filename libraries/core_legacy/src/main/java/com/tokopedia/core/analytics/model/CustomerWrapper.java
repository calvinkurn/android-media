package com.tokopedia.core.analytics.model;

import java.util.Map;

/**
 * @author by alvarisi on 10/27/16.
 *
 * Modified by : Hafizh Herdi
 *
 */

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

    public CustomerWrapper() {

    }

    public CustomerWrapper(Builder builder) {
        mCustomerId             = builder.mCustomerId;
        mFirstName              = builder.mFirstName;
        mLastName               = builder.mLastName;
        mFullName               = builder.mFullName;
        mEmailAddress           = builder.mEmailAddress;
        mMethod                 = builder.mMethod;
        mAttr                   = builder.mAttr;
        phoneNumber             = builder.phoneNumber;
        isGoldMerchant          = builder.isGoldMerchant;
        isSeller                = builder.isSeller;
        shopId                  = builder.shopId;
        shopName                = builder.shopName;
        dateOfBirth             = builder.dateOfBirth;
        totalItemSold           = builder.totalItemSold;
        regDate                 = builder.regDate;
        dateShopCreated         = builder.dateShopCreated;
        shopLocation            = builder.shopLocation;
        tokocashAmt             = builder.tokocashAmt;
        saldoAmt                = builder.saldoAmt;
        topAdsAmt               = builder.topAdsAmt;
        isTopadsUser            = builder.isTopadsUser;
        hasPurchasedMarketplace = builder.hasPurchasedMarketplace;
        hasPurchasedDigital     = builder.hasPurchasedDigital;
        hasPurchasedTiket       = builder.hasPurchasedTiket;
        lastTransactionDate     = builder.lastTransactionDate;
        totalActiveProduct      = builder.totalActiveProduct;
        shopScore               = builder.shopScore;
        gender                  = builder.gender;
    }

    public String getShopScore() {
        return shopScore;
    }

    public void setShopScore(String shopScore) {
        this.shopScore = shopScore;
    }

    public String getTotalActiveProduct() {
        return totalActiveProduct;
    }

    public void setTotalActiveProduct(String totalActiveProduct) {
        this.totalActiveProduct = totalActiveProduct;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Boolean isHasPurchasedTiket() {
        return hasPurchasedTiket;
    }

    public void setHasPurchasedTiket(Boolean hasPurchasedTiket) {
        this.hasPurchasedTiket = hasPurchasedTiket;
    }

    public void setHasPurchasedDigital(Boolean hasPurchasedDigital) {
        this.hasPurchasedDigital = hasPurchasedDigital;
    }

    public Boolean isHasPurchasedDigital() {
        return hasPurchasedDigital;
    }

    public void setHasPurchasedMarketplace(Boolean hasPurchasedMarketplace) {
        this.hasPurchasedMarketplace = hasPurchasedMarketplace;
    }

    public Boolean isHasPurchasedMarketplace() {
        return hasPurchasedMarketplace;
    }

    public Boolean isTopadsUser() {
        return isTopadsUser;
    }

    public void setTopadsUser(Boolean topadsUser) {
        isTopadsUser = topadsUser;
    }

    public String getTopAdsAmt() {
        return topAdsAmt;
    }

    public void setTopAdsAmt(String topAdsAmt) {
        this.topAdsAmt = topAdsAmt;
    }

    public String getSaldoAmt() {
        return saldoAmt;
    }

    public void setSaldoAmt(String saldoAmt) {
        this.saldoAmt = saldoAmt;
    }

    public String getTokocashAmt() {
        return tokocashAmt;
    }

    public void setTokocashAmt(String tokocashAmt) {
        this.tokocashAmt = tokocashAmt;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getDateShopCreated() {
        return dateShopCreated;
    }

    public void setDateShopCreated(String dateShopCreated) {
        this.dateShopCreated = dateShopCreated;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getTotalItemSold() {
        return totalItemSold;
    }

    public void setTotalItemSold(String totalItemSold) {
        this.totalItemSold = totalItemSold;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomerId() {
        return this.mCustomerId;
    }

    public String getFirstName() {
        return this.mFirstName;
    }

    public String getLastName() {
        return this.mLastName;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    @Override
    public void setExtraAttr(Map<String, String> extraAttr) {
        this.extraAttr = extraAttr;
    }

    public Map<String, String> getExtraAttr() {
        return this.extraAttr;
    }

    public String getmCustomerId() {
        return mCustomerId;
    }

    public void setCustomerId(String mCustomerId) {
        this.mCustomerId = mCustomerId;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
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

        public Builder() {
        }

        public Builder setShopScore(String shopScore) {
            this.shopScore = shopScore;
            return this;
        }

        public Builder setTotalActiveProduct(String totalActiveProduct) {
            this.totalActiveProduct = totalActiveProduct;
            return this;
        }

        public Builder setLastTransactionDate(String lastTransactionDate) {
            this.lastTransactionDate = lastTransactionDate;
            return this;
        }

        public Builder setHasPurchasedTiket(Boolean hasPurchasedTiket) {
            this.hasPurchasedTiket = hasPurchasedTiket;
            return this;
        }

        public Builder setHasPurchasedMarketplace(Boolean hasPurchasedMarketplace) {
            this.hasPurchasedMarketplace = hasPurchasedMarketplace;
            return this;
        }

        public Builder setHasPurchasedDigital(Boolean hasPurchasedDigital) {
            this.hasPurchasedDigital = hasPurchasedDigital;
            return this;
        }

        public Builder setTopadsUser(Boolean topadsUser) {
            isTopadsUser = topadsUser;
            return this;
        }

        public Builder setTopAdsAmt(String topAdsAmt) {
            this.topAdsAmt = topAdsAmt;
            return this;
        }

        public Builder setSaldoAmt(String saldoAmt) {
            this.saldoAmt = saldoAmt;
            return this;
        }

        public Builder setTokocashAmt(String tokocashAmt) {
            this.tokocashAmt = tokocashAmt;
            return this;
        }

        public Builder setShopLocation(String shopLocation) {
            this.shopLocation = shopLocation;
            return this;
        }

        public Builder setDateShopCreated(String dateShopCreated) {
            this.dateShopCreated = dateShopCreated;
            return this;
        }

        public Builder setRegDate(String regDate) {
            this.regDate = regDate;
            return this;
        }

        public Builder setTotalItemSold(String totalItemSold) {
            this.totalItemSold = totalItemSold;
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

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public Builder setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
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

        public Builder setCustomerId(String customerId) {
            mCustomerId = customerId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            mFirstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            mLastName = lastName;
            return this;
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
