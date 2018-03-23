package com.tokopedia.posapp.react.datasource.cloud;

import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.posapp.react.datasource.ReactDataSource;

import rx.Observable;

/**
 * @author okasurya on 3/22/18.
 */

public class ReactProductCloudSource implements ReactDataSource {
    private ProductRepository productRepository;
    ReactProductCloudSource() {

    }
    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return null;
    }

    @Override
    public Observable<String> getDataAll() {
        return null;
    }

    @Override
    public Observable<String> deleteAll() {
        return null;
    }

    @Override
    public Observable<String> deleteItem(String id) {
        return null;
    }

    @Override
    public Observable<String> update(String data) {
        return null;
    }

    @Override
    public Observable<String> insert(String data) {
        return null;
    }
}
