package com.tokopedia.flight.orderlist.domain;

import com.tokopedia.flight_dbflow.FlightAirlineDB;
import com.tokopedia.flight_dbflow.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func4;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightGetOrderUseCase extends UseCase<FlightOrder> {
    private static final String PARAM_ID = "invoice_id";
    private static final String DEFAULT_EMPTY_VALUE = "";
    private FlightRepository flightRepository;

    @Inject
    public FlightGetOrderUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightOrder> createObservable(RequestParams requestParams) {
        return flightRepository.getOrder(requestParams.getString(PARAM_ID, DEFAULT_EMPTY_VALUE));
//                .flatMap(new Func1<FlightOrder, Observable<FlightOrder>>() {
//                    @Override
//                    public Observable<FlightOrder> call(FlightOrder flightOrder) {
//                        return Observable.zip(
//                                Observable.just(flightOrder),
//                                Observable.from(flightOrder.getJourneys())
//                                        .flatMap(new Func1<FlightOrderJourney, Observable<FlightOrderJourney>>() {
//                                            @Override
//                                            public Observable<FlightOrderJourney> call(FlightOrderJourney flightOrderJourney) {
//                                                return Observable.zip(Observable.just(flightOrderJourney),
//                                                        flightRepository.getAirportById(flightOrderJourney.getDepartureAiportId()),
//                                                        flightRepository.getAirportById(flightOrderJourney.getArrivalAirportId()),
//                                                        Observable
//                                                                .from(flightOrderJourney.getRouteViewModels())
//                                                                .flatMap(new Func1<FlightDetailRouteViewModel, Observable<FlightDetailRouteViewModel>>() {
//                                                                    @Override
//                                                                    public Observable<FlightDetailRouteViewModel> call(FlightDetailRouteViewModel flightDetailRouteViewModel) {
//                                                                        return Observable.zip(Observable.just(flightDetailRouteViewModel),
//                                                                                flightRepository.getAirportById(flightDetailRouteViewModel.getDepartureAirportCode()),
//                                                                                flightRepository.getAirportById(flightDetailRouteViewModel.getArrivalAirportCode()),
//                                                                                flightRepository.getAirlineList(flightDetailRouteViewModel.getAirlineCode()),
//                                                                                new Func4<FlightDetailRouteViewModel,
//                                                                                        FlightAirportDB,
//                                                                                        FlightAirportDB,
//                                                                                        List<FlightAirlineDB>,
//                                                                                        FlightDetailRouteViewModel>() {
//                                                                                    @Override
//                                                                                    public FlightDetailRouteViewModel call(FlightDetailRouteViewModel flightDetailRouteViewModel,
//                                                                                                                           FlightAirportDB departureAirports,
//                                                                                                                           FlightAirportDB arrivalAirports,
//                                                                                                                           List<FlightAirlineDB> flightAirlineDBS) {
//                                                                                        if (departureAirports != null) {
//                                                                                            flightDetailRouteViewModel.setDepartureAirportCity(departureAirports.getCityName());
//                                                                                            flightDetailRouteViewModel.setDepartureAirportName(departureAirports.getAirportName());
//                                                                                        }
//                                                                                        if (arrivalAirports != null) {
//                                                                                            flightDetailRouteViewModel.setArrivalAirportCity(arrivalAirports.getCityName());
//                                                                                            flightDetailRouteViewModel.setArrivalAirportName(arrivalAirports.getAirportName());
//                                                                                        }
//
//                                                                                        if (flightAirlineDBS != null && flightAirlineDBS.size() > 0) {
//                                                                                            FlightAirlineDB flightAirlineDB = flightAirlineDBS.get(0);
//                                                                                            flightDetailRouteViewModel.setAirlineLogo(flightAirlineDB.getLogo());
//                                                                                            flightDetailRouteViewModel.setAirlineName(flightAirlineDB.getName());
//                                                                                        }
//                                                                                        return flightDetailRouteViewModel;
//                                                                                    }
//                                                                                }
//                                                                        );
//                                                                    }
//                                                                }).toList(),
//                                                        new Func4<FlightOrderJourney,
//                                                                FlightAirportDB,
//                                                                FlightAirportDB,
//                                                                List<FlightDetailRouteViewModel>,
//                                                                FlightOrderJourney>() {
//                                                            @Override
//                                                            public FlightOrderJourney call(FlightOrderJourney flightOrderJourney,
//                                                                                           FlightAirportDB departureAirports,
//                                                                                           FlightAirportDB arrivalAirports,
//                                                                                           List<FlightDetailRouteViewModel> routeViewModels) {
//                                                                if (departureAirports != null) {
//                                                                    flightOrderJourney.setDepartureCityCode(departureAirports.getCityCode());
//                                                                    flightOrderJourney.setDepartureCity(departureAirports.getCityName());
//                                                                }
//                                                                if (arrivalAirports != null) {
//                                                                    flightOrderJourney.setArrivalCityCode(arrivalAirports.getCityCode());
//                                                                    flightOrderJourney.setArrivalCity(arrivalAirports.getCityName());
//                                                                }
//                                                                if (routeViewModels != null && routeViewModels.size() > 0) {
//                                                                    flightOrderJourney.setRouteViewModels(routeViewModels);
//                                                                }
//                                                                return flightOrderJourney;
//                                                            }
//                                                        });
//                                            }
//                                        })
//                                        .toList(),
//                                new Func2<FlightOrder, List<FlightOrderJourney>, FlightOrder>() {
//                                    @Override
//                                    public FlightOrder call(FlightOrder flightOrder, List<FlightOrderJourney> flightOrderJourneys) {
//                                        flightOrder.setJourneys(flightOrderJourneys);
//                                        return flightOrder;
//                                    }
//                                });
//                    }
//                });
    }

    public RequestParams createRequestParams(String id){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_ID, id);
        return requestParams;
    }
}
