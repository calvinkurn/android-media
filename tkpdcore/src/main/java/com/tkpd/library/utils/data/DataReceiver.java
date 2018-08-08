package com.tkpd.library.utils.data;


import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by noiz354 on 2/2/16.
 */
public interface DataReceiver {
    CompositeSubscription getSubscription();
    void setDistricts(List<District> districts);
    void setCities(List<City> cities);
    void setProvinces(List<Province> provinces);
    void setBank(List<Bank> banks);
    void setShippingCity(List<District> districts);
    void onNetworkError(String message);
    void onMessageError(String message);
    void onUnknownError(String message);
    void onTimeout();
    void onFailAuth();
}
