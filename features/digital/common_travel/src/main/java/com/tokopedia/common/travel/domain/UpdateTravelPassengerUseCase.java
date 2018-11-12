package com.tokopedia.common.travel.domain;

import com.tokopedia.common.travel.data.TravelPassengerDataStoreFactory;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/11/18.
 */
public class UpdateTravelPassengerUseCase extends UseCase<Boolean> {

    public static final String ID_PASSENGER = "id_passenger";
    public static final String IS_SELECTED = "is_selected";

    private TravelPassengerDataStoreFactory travelPassengerDataStoreFactory;

    @Inject
    public UpdateTravelPassengerUseCase(TravelPassengerDataStoreFactory travelPassengerDataStoreFactory) {
        this.travelPassengerDataStoreFactory = travelPassengerDataStoreFactory;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return travelPassengerDataStoreFactory.updatePassenger(requestParams.getString(ID_PASSENGER, ""), requestParams.getBoolean(IS_SELECTED, false));
    }

    public RequestParams createRequestParams(String idPassenger, boolean isSelected) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ID_PASSENGER, idPassenger);
        requestParams.putBoolean(IS_SELECTED, isSelected);
        return requestParams;
    }
}
