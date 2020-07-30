package com.tokopedia.flight.common.data.repository;

import com.tokopedia.flight.cancellation.data.cloud.FlightCancellationCloudDataSource;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.common.data.model.request.DataRequest;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.FlightClassesDataSource;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.FlightOrderRepositoryImpl;
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderMapper;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightRepositoryImpl extends FlightOrderRepositoryImpl implements FlightRepository {
    private FlightClassesDataSource flightClassesDataSource;
    private FlightCancellationCloudDataSource flightCancellationCloudDataSource;

    public FlightRepositoryImpl(FlightClassesDataSource flightClassesDataSource,
                                FlightOrderDataSource flightOrderDataSource,
                                FlightOrderMapper flightOrderMapper,
                                FlightCancellationCloudDataSource flightCancellationCloudDataSource) {
        super(flightOrderDataSource, flightOrderMapper);

        this.flightClassesDataSource = flightClassesDataSource;
        this.flightCancellationCloudDataSource = flightCancellationCloudDataSource;
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
    public Observable<OrderEntity> getOrderEntity(String id) {
        return getFlightOrderDataSource().getOrder(id);
    }

    @Override
    public Observable<Map<String, List<Passenger>>> getCancelablePassenger(String invoiceId) {
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
}
