package com.tokopedia.core.people.model;

import java.util.Map;

/**
 * Created on 6/1/16.
 */
public class InputOutputData {

    private PeopleInfoData peopleInfoData;

    private PeoplePrivacyData peoplePrivacyData;

    private PeopleAddressData peopleAddressData;

    private Map<String, String> params;

    private boolean byPass;

    private int errorType;

    private String errorMessage;

    private int errorCode;
    private PeopleFavShop peopleFavShopData;

    public PeopleInfoData getPeopleInfoData() {
        return peopleInfoData;
    }

    public void setPeopleInfoData(PeopleInfoData peopleInfoData) {
        this.peopleInfoData = peopleInfoData;
    }

    public PeoplePrivacyData getPeoplePrivacyData() {
        return peoplePrivacyData;
    }

    public void setPeoplePrivacyData(PeoplePrivacyData peoplePrivacyData) {
        this.peoplePrivacyData = peoplePrivacyData;
    }

    public PeopleAddressData getPeopleAddressData() {
        return peopleAddressData;
    }

    public void setPeopleAddressData(PeopleAddressData peopleAddressData) {
        this.peopleAddressData = peopleAddressData;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isByPass() {
        return byPass;
    }

    public void setByPass(boolean byPass) {
        this.byPass = byPass;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setPeopleFavShopData(PeopleFavShop peopleFavShopData) {
        this.peopleFavShopData = peopleFavShopData;
    }

    public PeopleFavShop getPeopleFavShopData() {
        return peopleFavShopData;
    }
}
