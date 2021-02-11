package com.tokopedia.logisticaddaddress.data;

import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;

import java.util.Map;

import rx.subscriptions.CompositeSubscription;

public interface RetrofitInteractor {

    void unSubscribe();

    void generateLatLng(Map<String, String> param, GenerateLatLongListener listener);

    void generateLatLngGeoCode(Map<String, String> param, GenerateLatLongListener listener);

    CompositeSubscription getCompositeSubscription();

    IMapsRepository getMapRepository();

    interface GenerateLatLongListener {
        void onSuccess(CoordinateViewModel model);

        void onError(String errorMessage);
    }
}
