package com.tokopedia.flight.common.data.repository;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.airline.data.FlightAirlineDataListSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListBackgroundSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.FlightAirportVersionDBSource;
import com.tokopedia.flight.banner.data.source.BannerDataSource;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.data.cloud.FlightCartDataSource;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.cancellation.data.cloud.FlightCancellationCloudDataSource;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.FlightClassesDataSource;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderMapper;
import com.tokopedia.flight.passenger.data.FlightPassengerFactorySource;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.flight.review.data.FlightBookingDataSource;
import com.tokopedia.flight.review.data.FlightCancelVoucherDataSource;
import com.tokopedia.flight.review.data.FlightCheckVoucheCodeDataSource;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight_dbflow.FlightAirlineDB;
import com.tokopedia.flight_dbflow.FlightAirportDB;
import com.tokopedia.flight_dbflow.FlightPassengerDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightRepositoryImpl implements FlightRepository {
    private BannerDataSource bannerDataSource;
    private FlightAirportDataListSource flightAirportDataListSource;
    private FlightAirlineDataListSource flightAirlineDataListSource;
    private FlightClassesDataSource flightClassesDataSource;
    private FlightCartDataSource flightCartDataSource;
    private FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource;
    private FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource;
    private FlightBookingDataSource flightBookingDataSource;
    private FlightAirportVersionDBSource flightAirportVersionDBSource;
    private FlightOrderDataSource flightOrderDataSource;
    private FlightOrderMapper flightOrderMapper;
    private FlightPassengerFactorySource flightPassengerFactorySource;
    private FlightCancellationCloudDataSource flightCancellationCloudDataSource;
    private FlightCancelVoucherDataSource flightCancelVoucherDataSource;

    public FlightRepositoryImpl(BannerDataSource bannerDataSource,
                                FlightAirportDataListSource flightAirportDataListSource,
                                FlightAirlineDataListSource flightAirlineDataListSource,
                                FlightClassesDataSource flightClassesDataSource,
                                FlightCartDataSource flightCartDataSource,
                                FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource,
                                FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource,
                                FlightBookingDataSource flightBookingDataSource,
                                FlightAirportVersionDBSource flightAirportVersionDBSource,
                                FlightOrderDataSource flightOrderDataSource,
                                FlightOrderMapper flightOrderMapper,
                                FlightPassengerFactorySource flightPassengerFactorySource,
                                FlightCancellationCloudDataSource flightCancellationCloudDataSource,
                                FlightCancelVoucherDataSource flightCancelVoucherDataSource) {
        this.bannerDataSource = bannerDataSource;
        this.flightAirportDataListSource = flightAirportDataListSource;
        this.flightAirlineDataListSource = flightAirlineDataListSource;
        this.flightClassesDataSource = flightClassesDataSource;
        this.flightCartDataSource = flightCartDataSource;
        this.flightAirportDataListBackgroundSource = flightAirportDataListBackgroundSource;
        this.flightCheckVoucheCodeDataSource = flightCheckVoucheCodeDataSource;
        this.flightBookingDataSource = flightBookingDataSource;
        this.flightAirportVersionDBSource = flightAirportVersionDBSource;
        this.flightOrderDataSource = flightOrderDataSource;
        this.flightOrderMapper = flightOrderMapper;
        this.flightPassengerFactorySource = flightPassengerFactorySource;
        this.flightCancellationCloudDataSource = flightCancellationCloudDataSource;
        this.flightCancelVoucherDataSource = flightCancelVoucherDataSource;
    }

    @Override
    public Observable<List<FlightAirportDB>> getAirportList(String query) {
        return flightAirportDataListSource.getAirportList(query);
    }

    @Override
    public Observable<FlightAirportDB> getAirportById(final String aiport) {
        return flightAirportDataListSource.getAirport(aiport);
    }

    @Override
    public Observable<FlightAirportDB> getAirportWithParam(Map<String, String> params) {
        return flightAirportDataListSource.getAirport(params);
    }

    @Override
    public Observable<Boolean> checkPreloadAirport() {
        return flightAirportDataListSource.checkPreloadAirport();
    }

    @Override
    public Observable<List<FlightAirportDB>> getPhoneCodeList(String query) {
        return flightAirportDataListSource.getPhoneCodeList(query);
    }

    @Override
    public Observable<FlightAirportDB> getAirportByCountryId(String id) {
        return flightAirportDataListSource.getAirportByCountryId(id);
    }

    @Override
    public Observable<FlightAirlineDB> getAirlineById(final String airlineId) {
        return flightAirlineDataListSource.getAirline(airlineId);
    }

    @Override
    public Observable<SendEmailEntity> sendEmail(Map<String, Object> params) {
        return flightOrderDataSource.sendEmail(params);
    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList(String airlineId) {
        return flightAirlineDataListSource.getAirlineList(airlineId);
    }

    @Override
    public Observable<List<FlightClassEntity>> getFlightClasses() {
        return flightClassesDataSource.getClasses();
    }

    @Override
    public Observable<FlightClassEntity> getFlightClassById(final int classId) {
        return flightClassesDataSource.getClasses()
                .flatMap(new Func1<List<FlightClassEntity>, Observable<FlightClassEntity>>() {
                    @Override
                    public Observable<FlightClassEntity> call(final List<FlightClassEntity> flightClassEntities) {
                        return Observable.from(flightClassEntities)
                                .filter(new Func1<FlightClassEntity, Boolean>() {
                                    @Override
                                    public Boolean call(FlightClassEntity flightClassEntity) {
                                        return flightClassEntity.getId() == classId;
                                    }
                                });
                    }
                });
    }

    @Override
    public Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey) {
        return flightCartDataSource.addCart(request, idEmpotencyKey);
    }

    @Override
    public Observable<Boolean> getAirportListBackground(long versionAirport) {
        return flightAirportDataListBackgroundSource.getAirportList(versionAirport);
    }

    @Override
    public Observable<AttributesVoucher> checkVoucherCode(HashMap<String, String> paramsAllValueInString) {
        return flightCheckVoucheCodeDataSource.checkVoucherCode(paramsAllValueInString);
    }

    @Override
    public Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest) {
        return flightBookingDataSource.verifyBooking(verifyRequest);
    }

    @Override
    public Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request) {
        return flightBookingDataSource.checkout(request);
    }

    @Override
    public Observable<Boolean> checkVersionAirport(long versionOnCloud) {
        if (flightAirportVersionDBSource.getVersion() < versionOnCloud) {
            return Observable.just(true);
        } else {
            return Observable.just(false);
        }
    }

    @Override
    public Observable<List<FlightOrder>> getOrders(Map<String, Object> maps) {
        return flightOrderDataSource.getOrders(maps)
                .map(new Func1<List<OrderEntity>, List<FlightOrder>>() {
                    @Override
                    public List<FlightOrder> call(List<OrderEntity> orderEntities) {
                        return flightOrderMapper.transform(orderEntities);
                    }
                });

    }

    @Override
    public Observable<FlightOrder> getOrder(String id) {
        return flightOrderDataSource.getOrder(id)
                .map(new Func1<OrderEntity, FlightOrder>() {
                    @Override
                    public FlightOrder call(OrderEntity orderEntity) {
                        return flightOrderMapper.transform(orderEntity);
                    }
                });
    }

    @Override
    public Observable<OrderEntity> getOrderEntity(String id) {
        return flightOrderDataSource.getOrder(id);
    }

    @Override
    public Observable<List<BannerDetail>> getBanners(Map<String, String> params) {
        return bannerDataSource.getBannerData(params);
    }

    @Override
    public Observable<List<FlightPassengerDB>> getPassengerList(String passengerId) {
        return flightPassengerFactorySource.getPassengerList(passengerId);
    }

    @Override
    public Observable<FlightPassengerDB> getSinglePassengerById(String passengerId) {
        return flightPassengerFactorySource.getSinglePassenger(passengerId);
    }

    @Override
    public Observable<Boolean> updateIsSelected(String passengerId, int isSelected) {
        return flightPassengerFactorySource.updateIsSelected(passengerId, isSelected);
    }

    @Override
    public Observable<Boolean> deleteAllListPassenger() {
        return flightPassengerFactorySource.deleteAllListPassenger();
    }

    @Override
    public Observable<Boolean> deletePassenger(DeletePassengerRequest request, String idempotencyKey) {
        return flightPassengerFactorySource.deletePassenger(request, idempotencyKey);
    }

    @Override
    public Observable<Boolean> updatePassengerListData(UpdatePassengerRequest request, String idempotencyKey) {
        return flightPassengerFactorySource.updatePassenger(request, idempotencyKey);
    }

    @Override
    public Observable<List<Passenger>> getCancelablePassenger(String invoiceId) {
        return flightCancellationCloudDataSource.getCancelablePassenger(invoiceId);
    }

    @Override
    public Observable<List<Reason>> getCancellationReasons() {
        return flightCancellationCloudDataSource.getCancellationReasons();
    }

    @Override
    public Observable<EstimateRefundResultEntity> estimateRefund(FlightEstimateRefundRequest request) {
        return flightCancellationCloudDataSource.getEstimateRefund(request);
    }

    @Override
    public Observable<CancellationRequestEntity> cancellationRequest(FlightCancellationRequestBody request) {
        DataRequest<FlightCancellationRequestBody> requestBody = new DataRequest<>(request);
        return flightCancellationCloudDataSource.requestCancellation(requestBody);
    }

    @Override
    public Observable<Boolean> cancelVoucherApply() {
        return flightCancelVoucherDataSource.cancelVoucher();
    }
}
