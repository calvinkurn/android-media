package com.tokopedia.flight.dashboard.domain;

import android.content.Context;

import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.Subscriber;

/**
 * @author by alvarisi on 3/13/18.
 */

public class FlightDeleteDashboardCacheUseCase extends UseCase<Boolean> {
    private FlightDashboardCache cache;

    public FlightDeleteDashboardCacheUseCase(Context context) {
        this.cache = new FlightDashboardCache(context);
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(cache.clearCache());
            }
        });
    }
}
