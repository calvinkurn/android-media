package com.tokopedia.core.base.domain;

import rx.Observable;
import rx.Subscriber;

/**
 * @author erry on 23/02/17.
 */

public interface InteractorParams<P extends DefaultParams, T> {

    void execute(P requestParams, Subscriber<T> subscriber);

    Observable<T> execute(P requestParams);

}
