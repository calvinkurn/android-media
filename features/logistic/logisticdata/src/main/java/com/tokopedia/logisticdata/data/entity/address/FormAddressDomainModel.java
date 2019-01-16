package com.tokopedia.logisticdata.data.entity.address;

import com.tokopedia.logisticdata.data.entity.address.db.City;
import com.tokopedia.logisticdata.data.entity.address.db.District;

import java.util.List;

/**
 * Created by hangnadi on 2/23/17.
 */

public class FormAddressDomainModel {
    private List<City> cities;
    private boolean valid;
    private int errorCode;
    private String errorMessage;
    private List<District> districts;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }


    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
