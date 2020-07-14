package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.FlightOrderRepository;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightRepository extends FlightOrderRepository {

    Observable<List<FlightClassEntity>> getFlightClasses();

    Observable<FlightClassEntity> getFlightClassById(int classId);

    Observable<OrderEntity> getOrderEntity(String id);

    Observable<Map<String, List<Passenger>>> getCancelablePassenger(String invoiceId);

    Observable<List<Reason>> getCancellationReasons();

    Observable<EstimateRefundResultEntity> estimateRefund(FlightEstimateRefundRequest object);

    Observable<CancellationRequestEntity> cancellationRequest(FlightCancellationRequestBody request);

}
