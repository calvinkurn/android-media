package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.country.database.FlightAirportCountryTable;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.bookingV2.data.entity.AddToCartEntity;
import com.tokopedia.flight.bookingV2.data.entity.GetCartEntity;
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
import com.tokopedia.flight.passenger.data.db.FlightPassengerTable;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightRepository {

    Observable<FlightAirportCountryTable> getAirportByCountryId(String id);

    Observable<List<FlightClassEntity>> getFlightClasses();

    Observable<FlightClassEntity> getFlightClassById(int classId);

    Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey);

    Observable<AddToCartEntity> addCartV11(FlightCartRequest request, String idEmpotencyKey);

    Observable<GetCartEntity> getCart(String cartId);

    Observable<AttributesVoucher> checkVoucherCode(HashMap<String, String> paramsAllValueInString);

    Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest);

    Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request);

    Observable<List<FlightOrder>> getOrders(Map<String, Object> maps);

    Observable<FlightOrder> getOrder(String id);

    Observable<OrderEntity> getOrderEntity(String id);

    Observable<List<BannerDetail>> getBanners(Map<String, String> params);

    Observable<List<FlightAirportCountryTable>> getPhoneCodeList(String string);

    Observable<SendEmailEntity> sendEmail(Map<String, Object> params);

    Observable<List<FlightPassengerTable>> getPassengerList(String passengerId);

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
