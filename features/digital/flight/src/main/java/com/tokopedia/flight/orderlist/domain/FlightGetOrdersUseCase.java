package com.tokopedia.flight.orderlist.domain;

import android.text.TextUtils;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func4;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightGetOrdersUseCase extends UseCase<List<FlightOrder>> {
    private static final String PARAM_STATUS = "status_bulk";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PER_PAGE = "per_page";
    private static final int DEFAULT_PER_PAGE_VALUE = 10;

    private FlightRepository flightRepository;

    public FlightGetOrdersUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightOrder>> createObservable(RequestParams requestParams) {
        return flightRepository.getOrders(requestParams.getParameters())
                .flatMap(new Func1<List<FlightOrder>, Observable<List<FlightOrder>>>() {
                    @Override
                    public Observable<List<FlightOrder>> call(List<FlightOrder> flightOrders) {
                        return Observable.from(flightOrders)
                                .flatMap(new Func1<FlightOrder, Observable<FlightOrder>>() {
                                    @Override
                                    public Observable<FlightOrder> call(FlightOrder flightOrder) {
                                        return Observable.zip(
                                                Observable.just(flightOrder),
                                                Observable.from(flightOrder.getJourneys())
                                                        .flatMap(new Func1<FlightOrderJourney, Observable<FlightOrderJourney>>() {
                                                            @Override
                                                            public Observable<FlightOrderJourney> call(FlightOrderJourney flightOrderJourney) {
                                                                return Observable.zip(Observable.just(flightOrderJourney),
                                                                        flightRepository.getAirportById(flightOrderJourney.getDepartureAiportId()),
                                                                        flightRepository.getAirportById(flightOrderJourney.getArrivalAirportId()),
                                                                        Observable
                                                                                .from(flightOrderJourney.getRouteViewModels())
                                                                                .flatMap(new Func1<FlightDetailRouteViewModel, Observable<FlightDetailRouteViewModel>>() {
                                                                                    @Override
                                                                                    public Observable<FlightDetailRouteViewModel> call(FlightDetailRouteViewModel flightDetailRouteViewModel) {
                                                                                        return Observable.zip(Observable.just(flightDetailRouteViewModel),
                                                                                                flightRepository.getAirportById(flightDetailRouteViewModel.getDepartureAirportCode()),
                                                                                                flightRepository.getAirportById(flightDetailRouteViewModel.getArrivalAirportCode()),
                                                                                                flightRepository.getAirlineList(flightDetailRouteViewModel.getAirlineCode()),
                                                                                                new Func4<FlightDetailRouteViewModel,
                                                                                                        FlightAirportDB,
                                                                                                        FlightAirportDB,
                                                                                                        List<FlightAirlineDB>,
                                                                                                        FlightDetailRouteViewModel>() {
                                                                                                    @Override
                                                                                                    public FlightDetailRouteViewModel call(FlightDetailRouteViewModel flightDetailRouteViewModel,
                                                                                                                                           FlightAirportDB departureAirports,
                                                                                                                                           FlightAirportDB arrivalAirports,
                                                                                                                                           List<FlightAirlineDB> flightAirlineDBS) {
                                                                                                        if (departureAirports != null) {
                                                                                                            flightDetailRouteViewModel.setDepartureAirportCity(departureAirports.getCityName());
                                                                                                            flightDetailRouteViewModel.setDepartureAirportName(departureAirports.getAirportName());
                                                                                                        }
                                                                                                        if (arrivalAirports != null) {
                                                                                                            flightDetailRouteViewModel.setArrivalAirportCity(arrivalAirports.getCityName());
                                                                                                            flightDetailRouteViewModel.setArrivalAirportName(arrivalAirports.getAirportName());
                                                                                                        }

                                                                                                        if (flightAirlineDBS != null && flightAirlineDBS.size() > 0) {
                                                                                                            FlightAirlineDB flightAirlineDB = flightAirlineDBS.get(0);
                                                                                                            flightDetailRouteViewModel.setAirlineLogo(flightAirlineDB.getLogo());
                                                                                                            flightDetailRouteViewModel.setAirlineName(flightAirlineDB.getName());
                                                                                                        }
                                                                                                        return flightDetailRouteViewModel;
                                                                                                    }
                                                                                                }
                                                                                        );
                                                                                    }
                                                                                }).toList(),
                                                                        new Func4<FlightOrderJourney,
                                                                                FlightAirportDB,
                                                                                FlightAirportDB,
                                                                                List<FlightDetailRouteViewModel>,
                                                                                FlightOrderJourney>() {
                                                                            @Override
                                                                            public FlightOrderJourney call(FlightOrderJourney flightOrderJourney,
                                                                                                           FlightAirportDB departureAirports,
                                                                                                           FlightAirportDB arrivalAirports,
                                                                                                           List<FlightDetailRouteViewModel> routeViewModels) {
                                                                                if (departureAirports != null) {
                                                                                    flightOrderJourney.setDepartureCityCode(departureAirports.getCityCode());
                                                                                    flightOrderJourney.setDepartureCity(departureAirports.getCityName());
                                                                                }
                                                                                if (arrivalAirports != null) {
                                                                                    flightOrderJourney.setArrivalCityCode(arrivalAirports.getCityCode());
                                                                                    flightOrderJourney.setArrivalCity(arrivalAirports.getCityName());
                                                                                }
                                                                                if (routeViewModels != null && routeViewModels.size() > 0) {
                                                                                    flightOrderJourney.setRouteViewModels(routeViewModels);
                                                                                }
                                                                                return flightOrderJourney;
                                                                            }
                                                                        });
                                                            }
                                                        })
                                                        .toList(),
                                                new Func2<FlightOrder, List<FlightOrderJourney>, FlightOrder>() {
                                                    @Override
                                                    public FlightOrder call(FlightOrder flightOrder, List<FlightOrderJourney> flightOrderJourneys) {
                                                        flightOrder.setJourneys(flightOrderJourneys);
                                                        return flightOrder;
                                                    }
                                                });
                                    }
                                })
                                .toList();

                    }
                });
    }

    public RequestParams createRequestParam(int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_PAGE, page);
        requestParams.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE_VALUE);
        return requestParams;
    }

    public RequestParams createRequestParam(int page, String status) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_PAGE, page);
        if (!TextUtils.isEmpty(status)) {
            requestParams.putString(PARAM_STATUS, status);
        }
        requestParams.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE_VALUE);
        return requestParams;
    }

    public RequestParams createRequestParam(int page, String status, int perPage) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_PAGE, page);
        if (!TextUtils.isEmpty(status)) {
            requestParams.putString(PARAM_STATUS, status);
        }
        requestParams.putInt(PARAM_PER_PAGE, perPage == 0 ? DEFAULT_PER_PAGE_VALUE : perPage);
        return requestParams;
    }
}
