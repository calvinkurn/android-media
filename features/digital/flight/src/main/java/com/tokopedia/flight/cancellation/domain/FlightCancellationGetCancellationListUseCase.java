package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight_dbflow.FlightAirlineDB;
import com.tokopedia.flight_dbflow.FlightAirportDB;
import com.tokopedia.flight.cancellation.domain.mapper.FlightOrderEntityToCancellationListMapper;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.orderlist.data.cache.FlightOrderDataCacheSource;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
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
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationGetCancellationListUseCase extends UseCase<List<FlightCancellationListViewModel>> {

    private static final String INVOICE_ID_PARAM = "INVOICE_ID_PARAM";
    private static final String DEFAULT_INVOICE_ID = "";

    private FlightOrderDataCacheSource flightOrderDataCacheSource;
    private FlightOrderEntityToCancellationListMapper flightOrderEntityToCancellationListMapper;
    private FlightRepository flightRepository;

    @Inject
    public FlightCancellationGetCancellationListUseCase(FlightOrderDataCacheSource flightOrderDataCacheSource,
                                                        FlightOrderEntityToCancellationListMapper flightOrderEntityToCancellationListMapper,
                                                        FlightRepository flightRepository) {
        this.flightOrderDataCacheSource = flightOrderDataCacheSource;
        this.flightOrderEntityToCancellationListMapper = flightOrderEntityToCancellationListMapper;
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightCancellationListViewModel>> createObservable(final RequestParams requestParams) {
        return flightOrderDataCacheSource.isExpired()
                .flatMap(new Func1<Boolean, Observable<OrderEntity>>() {
                    @Override
                    public Observable<OrderEntity> call(Boolean aBoolean) {
                        if (aBoolean) {
                            return flightRepository.getOrderEntity(requestParams.getString(INVOICE_ID_PARAM, DEFAULT_INVOICE_ID));
                        } else {
                            return flightOrderDataCacheSource.getCache();
                        }
                    }
                })
                .flatMap(new Func1<OrderEntity, Observable<List<FlightCancellationListViewModel>>>() {
                    @Override
                    public Observable<List<FlightCancellationListViewModel>> call(OrderEntity orderEntity) {
                        return Observable.just(flightOrderEntityToCancellationListMapper.transform(orderEntity));
                    }
                });
//                .flatMap(new Func1<List<FlightCancellationListViewModel>, Observable<List<FlightCancellationListViewModel>>>() {
//                    @Override
//                    public Observable<List<FlightCancellationListViewModel>> call(List<FlightCancellationListViewModel> flightCancellationListViewModels) {
//                        return Observable.from(flightCancellationListViewModels)
//                                .flatMap(new Func1<FlightCancellationListViewModel, Observable<FlightCancellationListViewModel>>() {
//                                    @Override
//                                    public Observable<FlightCancellationListViewModel> call(FlightCancellationListViewModel flightCancellationListViewModel) {
//                                        return getJourneyObservable(flightCancellationListViewModel);
//                                    }
//                                })
//                                .toList();
//                    }
//                });
    }
//
//    private Observable<FlightCancellationListViewModel> getJourneyObservable(FlightCancellationListViewModel flightCancellationListViewModel) {
//        return Observable.zip(
//                Observable.just(flightCancellationListViewModel),
//                Observable.from(flightCancellationListViewModel.getCancellations().getJourneys())
//                        .flatMap(new Func1<FlightOrderJourney, Observable<FlightOrderJourney>>() {
//                            @Override
//                            public Observable<FlightOrderJourney> call(FlightOrderJourney flightOrderJourney) {
//                                return Observable.zip(
//                                        Observable.just(flightOrderJourney),
//                                        flightRepository.getAirportById(flightOrderJourney.getDepartureAiportId()),
//                                        flightRepository.getAirportById(flightOrderJourney.getArrivalAirportId()),
//                                        Observable.from(flightOrderJourney.getRouteViewModels())
//                                                .flatMap(new Func1<FlightDetailRouteViewModel, Observable<FlightDetailRouteViewModel>>() {
//                                                    @Override
//                                                    public Observable<FlightDetailRouteViewModel> call(FlightDetailRouteViewModel flightDetailRouteViewModel) {
//                                                        return getDetailRouteObservable(flightDetailRouteViewModel);
//                                                    }
//                                                }).toList(),
//                                        new Func4<FlightOrderJourney, FlightAirportDB, FlightAirportDB, List<FlightDetailRouteViewModel>, FlightOrderJourney>() {
//                                            @Override
//                                            public FlightOrderJourney call(FlightOrderJourney flightOrderJourney, FlightAirportDB departureAirport, FlightAirportDB arrivalAirport, List<FlightDetailRouteViewModel> flightDetailRouteViewModels) {
//                                                if (departureAirport != null) {
//                                                    flightOrderJourney.setDepartureCity(departureAirport.getCityName());
//                                                    flightOrderJourney.setDepartureCityCode(departureAirport.getCityCode());
//                                                }
//
//                                                if (arrivalAirport != null) {
//                                                    flightOrderJourney.setArrivalCity(arrivalAirport.getCityName());
//                                                    flightOrderJourney.setArrivalCityCode(arrivalAirport.getCityCode());
//                                                }
//
//                                                return flightOrderJourney;
//                                            }
//                                        }
//                                );
//                            }
//                        })
//                        .toList(),
//                new Func2<FlightCancellationListViewModel, List<FlightOrderJourney>, FlightCancellationListViewModel>() {
//                    @Override
//                    public FlightCancellationListViewModel call(FlightCancellationListViewModel flightCancellationListViewModel, List<FlightOrderJourney> flightOrderJourneyList) {
//                        flightCancellationListViewModel.getCancellations().setJourneys(flightOrderJourneyList);
//                        return flightCancellationListViewModel;
//                    }
//                }
//        );
//    }
//
//    private Observable<FlightDetailRouteViewModel> getDetailRouteObservable(FlightDetailRouteViewModel flightDetailRouteViewModel) {
//        return Observable.zip(Observable.just(flightDetailRouteViewModel),
//                flightRepository.getAirportById(flightDetailRouteViewModel.getDepartureAirportCode()),
//                flightRepository.getAirportById(flightDetailRouteViewModel.getArrivalAirportCode()),
//                flightRepository.getAirlineList(flightDetailRouteViewModel.getAirlineCode()),
//                new Func4<FlightDetailRouteViewModel,
//                        FlightAirportDB,
//                        FlightAirportDB,
//                        List<FlightAirlineDB>,
//                        FlightDetailRouteViewModel>() {
//                    @Override
//                    public FlightDetailRouteViewModel call(FlightDetailRouteViewModel flightDetailRouteViewModel,
//                                                           FlightAirportDB departureAirports,
//                                                           FlightAirportDB arrivalAirports,
//                                                           List<FlightAirlineDB> flightAirlineDBS) {
//                        if (departureAirports != null) {
//                            flightDetailRouteViewModel.setDepartureAirportCity(departureAirports.getCityName());
//                            flightDetailRouteViewModel.setDepartureAirportName(departureAirports.getAirportName());
//                        }
//                        if (arrivalAirports != null) {
//                            flightDetailRouteViewModel.setArrivalAirportCity(arrivalAirports.getCityName());
//                            flightDetailRouteViewModel.setArrivalAirportName(arrivalAirports.getAirportName());
//                        }
//
//                        if (flightAirlineDBS != null && flightAirlineDBS.size() > 0) {
//                            FlightAirlineDB flightAirlineDB = flightAirlineDBS.get(0);
//                            flightDetailRouteViewModel.setAirlineLogo(flightAirlineDB.getLogo());
//                            flightDetailRouteViewModel.setAirlineName(flightAirlineDB.getName());
//                        }
//                        return flightDetailRouteViewModel;
//                    }
//                }
//        );
//    }

    public RequestParams createRequestParams(String invoiceId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(INVOICE_ID_PARAM, invoiceId);
        return requestParams;
    }

}
