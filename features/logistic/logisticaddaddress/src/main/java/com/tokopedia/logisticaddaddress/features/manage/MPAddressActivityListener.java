package com.tokopedia.logisticaddaddress.features.manage;

/**
 * Created on 5/18/16.
 */
public interface MPAddressActivityListener {

    void startServiceSetDefaultAddress(String addressId);

    void startServiceDeleteAddress(String addressId);

    void setFilterViewVisibility(boolean isAble);
}
