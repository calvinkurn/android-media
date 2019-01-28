package com.tokopedia.logisticgeolocation.data;

import android.content.Context;

import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;

import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
public interface RetrofitInteractor {

    void generateAddress(Context context, GenerateAddressListener listener);

    void unSubscribe();

    void generateLatLng(Map<String, String> param, GenerateLatLongListener listener);

    void generateLatLngGeoCode(Map<String, String> param, GenerateLatLongListener listener);

    CompositeSubscription getCompositeSubscription();

    IMapsRepository getMapRepository();

    interface GenerateLatLongListener {
        void onSuccess(CoordinateViewModel model);

        void onError(String errorMessage);
    }

    interface GenerateAddressListener {

        void onSuccess(LocationPass model);

        void onError(Throwable e);

        void onPreConnection();

        String getAddress(double latitude, double longitude);

        LocationPass convertData(double latitude, double longitude);
    }
}
