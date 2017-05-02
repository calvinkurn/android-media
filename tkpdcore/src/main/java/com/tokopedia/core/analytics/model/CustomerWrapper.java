package com.tokopedia.core.analytics.model;

import java.util.Map;

/**
 * @author by alvarisi on 10/27/16.
 */

public class CustomerWrapper extends BaseAnalyticsModel {
    private String mCustomerId;
    private String mFirstName;
    private String mLastName;
    private String mFullName;
    private String mEmailAddress;
    private String mMethod;
    private String phoneNumber;
    private boolean isGoldMerchant;
    private boolean isSeller;
    private String shopId;
    private String shopName;
    private String dateOfBirth;

    private Map<String, String> mAttr;

    public CustomerWrapper() {

    }

    public CustomerWrapper(CustomerWrapper.Builder builder) {
        mCustomerId     = builder.mCustomerId;
        mFirstName      = builder.mFirstName;
        mLastName       = builder.mLastName;
        mFullName       = builder.mFullName;
        mEmailAddress   = builder.mEmailAddress;
        mMethod         = builder.mMethod;
        mAttr           = builder.mAttr;
        phoneNumber     = builder.phoneNumber;
        isGoldMerchant  = builder.isGoldMerchant;
        isSeller        = builder.isSeller;
        shopId          = builder.shopId;
        shopName        = builder.shopName;
        dateOfBirth     = builder.dateOfBirth;
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

    public boolean isGoldMerchant() {
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

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
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

    @Override
    public String toString() {
        return "CustomerWrapper{" +
                "mCustomerId='" + mCustomerId + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mFullName='" + mFullName + '\'' +
                ", mEmailAddress='" + mEmailAddress + '\'' +
                ", mMethod='" + mMethod + '\'' +
                ", phonenumber='" + phoneNumber + '\'' +
                ", isgoldmerch='" + isGoldMerchant + '\'' +
                ", isseller='" + isSeller + '\'' +
                ", shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", mAttr=" + mAttr.toString() +
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
        private boolean isGoldMerchant;
        private boolean isSeller;
        private String shopId;
        private String shopName;
        private String dateOfBirth;

        private Map<String, String> mAttr;

        public Builder() {
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopName() {
            return shopName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public boolean isGoldMerchant() {
            return isGoldMerchant;
        }

        public void setGoldMerchant(boolean goldMerchant) {
            isGoldMerchant = goldMerchant;
        }

        public boolean isSeller() {
            return isSeller;
        }

        public void setSeller(boolean seller) {
            isSeller = seller;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public CustomerWrapper.Builder setCustomerId(String customerId) {
            mCustomerId = customerId;
            return this;
        }

        public CustomerWrapper.Builder setFirstName(String firstName) {
            mFirstName = firstName;
            return this;
        }

        public CustomerWrapper.Builder setLastName(String lastName) {
            mLastName = lastName;
            return this;
        }

        public CustomerWrapper.Builder setFullName(String fullName) {
            mFullName = fullName;
            return this;
        }

        public CustomerWrapper.Builder setEmailAddress(String emailAddress) {
            mEmailAddress = emailAddress;
            return this;
        }

        public CustomerWrapper.Builder setMethod(String method) {
            mMethod = method;
            return this;
        }

        public CustomerWrapper.Builder setAttr(Map<String, String> attr) {
            mAttr = attr;
            return this;
        }

        public CustomerWrapper build(){
            return new CustomerWrapper(this);
        }
    }
}
