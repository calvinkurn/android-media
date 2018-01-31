package com.tokopedia.core.base.domain;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Kulomady on 12/7/16.
 */

/**
 * Use Usecase from tkpd usecase
 */
@Deprecated
public interface Interactor<T> {

    void execute(RequestParams requestParams,Subscriber<T> subscriber);

    Observable<T> getExecuteObservable(RequestParams requestParams);
}
