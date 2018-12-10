package com.tokopedia.common.travel.data;

import com.tokopedia.common.travel.data.specification.Specification;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 08/11/18.
 */
public interface TravelPassengerDataDbSource<T, U> {
    Observable<Boolean> isDataAvailable();

    Observable<Boolean> deleteAll();

    Observable<Boolean> insert(T data);

    Observable<Boolean> insertAll(List<T> datas);

    Observable<List<U>> getDatas();

    Observable<Integer> getCount(Specification specification);

    Observable<U> getData(Specification specification);

}
