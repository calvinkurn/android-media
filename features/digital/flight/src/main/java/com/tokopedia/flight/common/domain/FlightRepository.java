package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
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

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightRepository {
    Observable<List<FlightAirportDB>> getAirportList(String query);

    Observable<FlightAirportDB> getAirportById(String aiport);

    Observable<FlightAirportDB> getAirportWithParam(Map<String, String> params);

    Observable<List<FlightClassEntity>> getFlightClasses();

    Observable<FlightClassEntity> getFlightClassById(int classId);

    Observable<List<FlightAirlineDB>> getAirlineList(String airlineId);

    Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey);

    Observable<Boolean> getAirportListBackground(long versionAirport);

    Observable<AttributesVoucher> checkVoucherCode(HashMap<String, String> paramsAllValueInString);

    Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest);

    Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request);

    Observable<Boolean> checkVersionAirport(long current_version);

    Observable<List<FlightOrder>> getOrders(Map<String, Object> maps);

    Observable<FlightOrder> getOrder(String id);

    Observable<OrderEntity> getOrderEntity(String id);

    Observable<List<BannerDetail>> getBanners(Map<String, String> params);

    Observable<List<FlightAirportDB>> getPhoneCodeList(String string);

    Observable<FlightAirportDB> getAirportByCountryId(String id);

    Observable<FlightAirlineDB> getAirlineById(String airlineId);

    Observable<SendEmailEntity> sendEmail(Map<String, Object> params);

    Observable<List<FlightPassengerDB>> getPassengerList(String passengerId);

    Observable<FlightPassengerDB> getSinglePassengerById(String passengerId);

    Observable<Boolean> updateIsSelected(String passengerId, int isSelected);

    Observable<Boolean> deleteAllListPassenger();

    Observable<Boolean> deletePassenger(DeletePassengerRequest request, String idempotencyKey);

    Observable<Boolean> updatePassengerListData(UpdatePassengerRequest request, String idempotencyKey);

    Observable<List<Passenger>> getCancelablePassenger(String invoiceId);

    Observable<List<Reason>> getCancellationReasons();

    Observable<EstimateRefundResultEntity> estimateRefund(FlightEstimateRefundRequest object);

    Observable<CancellationRequestEntity> cancellationRequest(FlightCancellationRequestBody request);

    Observable<Boolean> cancelVoucherApply();
}
