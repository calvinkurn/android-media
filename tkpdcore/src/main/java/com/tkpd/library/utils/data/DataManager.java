package com.tkpd.library.utils.data;

import android.content.Context;

import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by m.normansyah on 2/2/16.
 * DAO Pattern
 * Singleton
 */
public interface DataManager {
    int CITY = 0;
    int DISTRICT = 1;
    int PROVINCE = 2;
    int BANK = 3;
    int DEPARTMENT = 4;

    void getListProvince(Context context, DataReceiver dataReceiver);
    void getListCity(Context context, DataReceiver dataReceiver, String provinceId);
    void getListDistrict(Context context, DataReceiver dataReceiver, String cityId);

    /**
     * always fetch List Bank from Network, because it will update the last one or not.
     * @param context
     * @param dataReceiver
     */
    void getListBank(Context context, DataReceiver dataReceiver);

    /**
     * get all list of shipping city used {@link com.tokopedia.core.discovery.fragment.FragmentBrowseProduct}
     * or any browse related to it.
     * @param context
     * @param dataReceiver
     */
    void getListShippingCity(Context context, DataReceiver dataReceiver);

    /**
     * use this if don't want to implement all receiver
     */
    abstract class DefaultDataReceiver implements DataReceiver{
        public CompositeSubscription getSubscription(){ return null;}
        public void setDistricts(List<District> districts){}
        public void setCities(List<City> cities){}
        public void setProvinces(List<Province> provinces){}
        public void setBank(List<Bank> banks){}
        public void setShippingCity(List<District> districts){}
        public void onNetworkError(String message){}
        public void onMessageError(String message){}
        public void onUnknownError(String message){}
        public void onTimeout(){}
        public void onFailAuth(){}
    }
}
