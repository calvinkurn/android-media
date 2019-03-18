package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.country.database.FlightAirportCountryTable;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingGetPhoneCodeUseCase extends UseCase<List<FlightBookingPhoneCodeViewModel>> {
    private static final String PARAM_QUERY = "query";
    private static final String DEFAULT_PARAM = "";


    private final FlightRepository flightRepository;

    @Inject
    public FlightBookingGetPhoneCodeUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightBookingPhoneCodeViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getPhoneCodeList(requestParams.getString(PARAM_QUERY, DEFAULT_PARAM))
                .flatMap(flightAirportCountryTables -> {
                    List<FlightBookingPhoneCodeViewModel> flightBookingPhoneCodeViewModels = new ArrayList<>();
                    for (FlightAirportCountryTable flightAirportDB : flightAirportCountryTables) {
                        FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel = new FlightBookingPhoneCodeViewModel();
                        flightBookingPhoneCodeViewModel.setCountryId(flightAirportDB.getCountryId());
                        flightBookingPhoneCodeViewModel.setCountryName(flightAirportDB.getCountryName());
                        flightBookingPhoneCodeViewModel.setCountryPhoneCode(String.valueOf(flightAirportDB.getPhoneCode()));
                        flightBookingPhoneCodeViewModels.add(flightBookingPhoneCodeViewModel);
                    }
                    return Observable.just(flightBookingPhoneCodeViewModels);
                });
    }

    public RequestParams createRequest(String query) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_QUERY, query);
        return requestParams;
    }
}
