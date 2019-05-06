package com.tokopedia.discovery.common.domain;

import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public interface Repository<T> {

    Observable<T> query(Specification specification, RequestParams requestParams);
}