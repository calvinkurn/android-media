package com.tokopedia.common.travel.domain;

import com.tokopedia.common.travel.constant.TravelPlatformType;
import com.tokopedia.common.travel.constant.TravelType;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

/**
 * Created by nabillasabbaha on 27/11/18.
 */
public abstract class BaseTravelPassengerUseCase<T> extends UseCase<T> {

    public static final String TRAVEL_ID = "travelId";
    public static final String ID_PASSENGER = "id";
    public static final String NAME_USER = "name";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String ID_NUMBER = "idNumber";
    public static final String PAX_TYPE = "paxType";
    public static final String TITLE = "title";
    public static final String BIRTHDATE = "birthDate";
    private static final String INPUT_GQL = "input";
    private static final String INSTANCE_TYPE = "instanceType";

    public RequestParams create(RequestParams requestParams, int travelPlatformType) {
        RequestParams requestParamsInput = RequestParams.create();
        requestParamsInput.putObject(INPUT_GQL, requestParams.getParameters());
        if (travelPlatformType == TravelPlatformType.TRAIN) {
            requestParamsInput.putObject(INSTANCE_TYPE, TravelType.TRAIN);
        } else if (travelPlatformType == TravelPlatformType.FLIGHT) {
            requestParamsInput.putObject(INSTANCE_TYPE, TravelType.FLIGHT);
        }
        return requestParamsInput;
    }
}
