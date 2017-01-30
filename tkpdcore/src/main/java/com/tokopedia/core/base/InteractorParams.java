package com.tokopedia.core.base;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Kulomady on 12/9/16.
 */

public interface InteractorParams<P extends DefaultParams, T> {

    void execute(P requestParams, Subscriber<T> subscriber);

    Observable<T> execute(P requestParams);

}
