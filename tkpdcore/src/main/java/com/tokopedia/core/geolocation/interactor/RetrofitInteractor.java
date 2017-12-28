package com.tokopedia.core.geolocation.interactor;

import android.content.Context;

import com.tokopedia.core.geolocation.domain.IMapsRepository;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by hangnadi on 1/31/16.
 */
public interface RetrofitInteractor {

    void generateAddress(Context context, GenerateAddressListener listener);

    void unSubscribe();

    void generateLatLng(Context context,
                        TKPDMapParam<String, String> param,
                        GenerateLatLongListener listener);

    CompositeSubscription getCompositeSubscription();

    MapService getMapService();

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
