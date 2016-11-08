package com.tokopedia.core.geolocation.interactor;

import com.tokopedia.core.geolocation.model.LocationPass;

/**
 * Created by hangnadi on 1/31/16.
 */
public interface RetrofitInteractor {

    void generateAddress(GenerateAddressListener listener);

    void unSubscribe();

    interface GenerateAddressListener {

        void onSuccess(LocationPass model);

        void onError(Throwable e);

        void onPreConnection();

        String getAddress(double latitude, double longitude);

        LocationPass convertData(double latitude, double longitude);
    }
}
