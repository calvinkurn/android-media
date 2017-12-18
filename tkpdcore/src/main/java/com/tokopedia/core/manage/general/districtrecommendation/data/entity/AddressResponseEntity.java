package com.tokopedia.core.manage.general.districtrecommendation.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class AddressResponseEntity {

    @SerializedName("next_available")
    @Expose
    private boolean nextAvailable;
    @SerializedName("data")
    @Expose
    private ArrayList<AddressEntity> addresses;

    public boolean isNextAvailable() {
        return nextAvailable;
    }

    public void setNextAvailable(boolean nextAvailable) {
        this.nextAvailable = nextAvailable;
    }

    public ArrayList<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<AddressEntity> addresses) {
        this.addresses = addresses;
    }

}
