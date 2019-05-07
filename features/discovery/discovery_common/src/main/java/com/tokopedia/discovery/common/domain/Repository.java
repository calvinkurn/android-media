package com.tokopedia.discovery.common.domain;

import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public interface Repository<T> {

    Observable<T> query(RequestParams requestParams);
}