package com.tokopedia.flight.review.data;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 26/06/18.
 */

public class FlightCancelVoucherDataSource {

    private final FlightApi flightApi;
    private UserSession userSession;

    @Inject
    public FlightCancelVoucherDataSource(FlightApi flightApi, UserSession userSession) {
        this.flightApi = flightApi;
        this.userSession = userSession;
    }

    public Observable<Boolean> cancelVoucher() {
        return this.flightApi.cancelCoupounVoucher(userSession.getUserId())
                .flatMap(new Func1<Response<Object>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<Object> objectResponse) {
                        return Observable.just(true);
                    }
                });
    }
}
