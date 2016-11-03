package com.tokopedia.tkpd.analytics.model;

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

    public CustomerWrapper() {

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

    @Override
    public void setName(String name) {

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
}
