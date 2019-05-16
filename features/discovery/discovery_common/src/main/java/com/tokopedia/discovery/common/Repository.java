package com.tokopedia.discovery.common;

import java.util.Map;

import rx.Observable;

public interface Repository<T> {

    Observable<T> query(Map<String, Object> parameters);
}
