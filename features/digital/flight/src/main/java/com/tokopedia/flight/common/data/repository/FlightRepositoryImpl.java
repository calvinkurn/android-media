package com.tokopedia.flight.common.data.repository;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.airport.data.source.FlightAirportListDbSource;
import com.tokopedia.flight.airport.data.source.database.FlightAirportCountryTable;
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
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderMapper;
import com.tokopedia.flight.passenger.data.FlightPassengerFactorySource;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.flight.passenger.data.db.FlightPassengerTable;
import com.tokopedia.flight.review.data.FlightBookingDataSource;
import com.tokopedia.flight.review.data.FlightCancelVoucherDataSource;
import com.tokopedia.flight.review.data.FlightCheckVoucheCodeDataSource;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;

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
    private FlightAirportListDbSource flightAirportListDbSource;
    private FlightClassesDataSource flightClassesDataSource;
    private FlightCartDataSource flightCartDataSource;
    private FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource;
    private FlightBookingDataSource flightBookingDataSource;
    private FlightOrderDataSource flightOrderDataSource;
    private FlightOrderMapper flightOrderMapper;
    private FlightPassengerFactorySource flightPassengerFactorySource;
    private FlightCancellationCloudDataSource flightCancellationCloudDataSource;
    private FlightCancelVoucherDataSource flightCancelVoucherDataSource;

    public FlightRepositoryImpl(BannerDataSource bannerDataSource,
                                FlightAirportListDbSource flightAirportListDbSource,
                                FlightClassesDataSource flightClassesDataSource,
                                FlightCartDataSource flightCartDataSource,
                                FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource,
                                FlightBookingDataSource flightBookingDataSource,
                                FlightOrderDataSource flightOrderDataSource,
                                FlightOrderMapper flightOrderMapper,
                                FlightPassengerFactorySource flightPassengerFactorySource,
                                FlightCancellationCloudDataSource flightCancellationCloudDataSource,
                                FlightCancelVoucherDataSource flightCancelVoucherDataSource) {
        this.bannerDataSource = bannerDataSource;
        this.flightAirportListDbSource = flightAirportListDbSource;
        this.flightClassesDataSource = flightClassesDataSource;
        this.flightCartDataSource = flightCartDataSource;
        this.flightCheckVoucheCodeDataSource = flightCheckVoucheCodeDataSource;
        this.flightBookingDataSource = flightBookingDataSource;
        this.flightOrderDataSource = flightOrderDataSource;
        this.flightOrderMapper = flightOrderMapper;
        this.flightPassengerFactorySource = flightPassengerFactorySource;
        this.flightCancellationCloudDataSource = flightCancellationCloudDataSource;
        this.flightCancelVoucherDataSource = flightCancelVoucherDataSource;
    }

    @Override
    public Observable<List<FlightAirportCountryTable>> getPhoneCodeList(String query) {
        return flightAirportListDbSource.getPhoneCodeList(query);
    }

    @Override
    public Observable<FlightAirportCountryTable> getAirportByCountryId(String id) {
        return flightAirportListDbSource.getAirportByCountryId(id);
    }

    @Override
    public Observable<SendEmailEntity> sendEmail(Map<String, Object> params) {
        return flightOrderDataSource.sendEmail(params);
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
    public Observable<List<FlightOrder>> getOrders(Map<String, Object> maps) {
        return flightOrderDataSource.getOrders(maps)
                .map(it -> flightOrderMapper.transform(it));
    }

    @Override
    public Observable<FlightOrder> getOrder(String id) {
        return flightOrderDataSource.getOrder(id)
                .map(it -> flightOrderMapper.transform(it));
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
    public Observable<List<FlightPassengerTable>> getPassengerList(String passengerId) {
        return flightPassengerFactorySource.getPassengerList(passengerId);
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
