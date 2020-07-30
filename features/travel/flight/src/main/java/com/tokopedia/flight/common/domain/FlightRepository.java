package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationAttachmentUploadEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.FlightOrderRepository;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightRepository extends FlightOrderRepository {

    Observable<OrderEntity> getOrderEntity(String id);

    Observable<Map<String, List<Passenger>>> getCancelablePassenger(String invoiceId);

    Observable<List<Reason>> getCancellationReasons();

    Observable<EstimateRefundResultEntity> estimateRefund(FlightEstimateRefundRequest object);

    Observable<CancellationRequestEntity> cancellationRequest(FlightCancellationRequestBody request);

    Observable<CancellationAttachmentUploadEntity> uploadCancellationAttachment(Map<String, RequestBody> params, MultipartBody.Part file);

}
