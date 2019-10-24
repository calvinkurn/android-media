package com.tokopedia.common.travel.domain;

import com.tokopedia.common.travel.data.PhoneCodeRepository;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;
import com.tokopedia.flight.country.database.CountryPhoneCodeTable;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class GetPhoneCodeUseCase extends UseCase<List<CountryPhoneCode>> {
    private static final String PARAM_QUERY = "query";
    private static final String DEFAULT_PARAM = "";


    private final PhoneCodeRepository phoneCodeRepository;

    @Inject
    public GetPhoneCodeUseCase(PhoneCodeRepository phoneCodeRepository) {
        this.phoneCodeRepository = phoneCodeRepository;
    }

    @Override
    public Observable<List<CountryPhoneCode>> createObservable(RequestParams requestParams) {
        return phoneCodeRepository.getPhoneCodeList(requestParams.getString(PARAM_QUERY, DEFAULT_PARAM))
                .flatMap(flightAirportCountryTables -> {
                    List<CountryPhoneCode> flightBookingPhoneCodes = new ArrayList<>();
                    for (CountryPhoneCodeTable flightAirportDB : flightAirportCountryTables) {
                        CountryPhoneCode flightBookingPhoneCode = new CountryPhoneCode();
                        flightBookingPhoneCode.setCountryId(flightAirportDB.getCountryId());
                        flightBookingPhoneCode.setCountryName(flightAirportDB.getCountryName());
                        flightBookingPhoneCode.setCountryPhoneCode(String.valueOf(flightAirportDB.getPhoneCode()));
                        flightBookingPhoneCodes.add(flightBookingPhoneCode);
                    }
                    return Observable.just(flightBookingPhoneCodes);
                });
    }

    public RequestParams createRequest(String query) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_QUERY, query);
        return requestParams;
    }
}
