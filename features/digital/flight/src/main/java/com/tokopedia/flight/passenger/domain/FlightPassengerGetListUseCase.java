package com.tokopedia.flight.passenger.domain;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.passenger.domain.model.ListPassengerViewModelMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightPassengerGetListUseCase extends UseCase<List<FlightBookingPassengerViewModel>> {

    private static final String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";
    private static final String DEFAULT_STRING_VALUE = "";

    private final FlightRepository flightRepository;
    private final ListPassengerViewModelMapper listPassengerViewModelMapper;

    @Inject
    public FlightPassengerGetListUseCase(FlightRepository flightRepository, ListPassengerViewModelMapper listPassengerViewModelMapper) {
        this.flightRepository = flightRepository;
        this.listPassengerViewModelMapper = listPassengerViewModelMapper;
    }

    @Override
    public Observable<List<FlightBookingPassengerViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getPassengerList(requestParams.getString(PARAM_PASSENGER_ID, DEFAULT_STRING_VALUE))

                .flatMap(flightPassengerDbs -> Observable.just(listPassengerViewModelMapper.transform(flightPassengerDbs)))
                .flatMap(flightBookingPassengerViewModelList -> Observable.from(flightBookingPassengerViewModelList)
                        .flatMap(this::getPassportData)
                        .toList());
    }

    public RequestParams createEmptyRequestParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, DEFAULT_STRING_VALUE);
        return requestParams;
    }

    public RequestParams generateRequestParams(String passengerId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        return requestParams;
    }

    private Observable<FlightBookingPassengerViewModel> getPassportData(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        return Observable.just(flightBookingPassengerViewModel)
                .flatMap((Func1<FlightBookingPassengerViewModel, Observable<FlightBookingPassengerViewModel>>) flightBookingPassengerViewModel1 -> {
                    if (flightBookingPassengerViewModel1.getPassportNationality() != null) {
                        return Observable.zip(Observable.just(flightBookingPassengerViewModel1),
                                flightRepository.getAirportByCountryId(flightBookingPassengerViewModel1.getPassportNationality().getCountryId()),
                                (flightBookingPassengerViewModel11, nationality) -> {
                                    FlightBookingPhoneCodeViewModel passportNationality = new FlightBookingPhoneCodeViewModel();
                                    passportNationality.setCountryId(nationality.getCountryId());
                                    passportNationality.setCountryName(nationality.getCountryName());
                                    passportNationality.setCountryPhoneCode(String.valueOf(nationality.getPhoneCode()));

                                    flightBookingPassengerViewModel11.setPassportNationality(passportNationality);
                                    return flightBookingPassengerViewModel11;
                                });
                    } else {
                        return Observable.just(flightBookingPassengerViewModel1);
                    }
                })
                .flatMap((Func1<FlightBookingPassengerViewModel, Observable<FlightBookingPassengerViewModel>>) flightBookingPassengerViewModel12 -> {
                    if (flightBookingPassengerViewModel12.getPassportIssuerCountry() != null) {
                        return Observable.zip(Observable.just(flightBookingPassengerViewModel12), flightRepository.getAirportByCountryId(flightBookingPassengerViewModel12.getPassportIssuerCountry().getCountryId()),
                                (flightBookingPassengerViewModel121, issuerCountry) -> {
                                    FlightBookingPhoneCodeViewModel passportIssuerCountry = new FlightBookingPhoneCodeViewModel();
                                    passportIssuerCountry.setCountryId(issuerCountry.getCountryId());
                                    passportIssuerCountry.setCountryName(issuerCountry.getCountryName());
                                    passportIssuerCountry.setCountryPhoneCode(String.valueOf(issuerCountry.getPhoneCode()));

                                    flightBookingPassengerViewModel121.setPassportIssuerCountry(passportIssuerCountry);
                                    return flightBookingPassengerViewModel121;
                                });
                    }
                    return Observable.just(flightBookingPassengerViewModel12);
                });
    }
}
