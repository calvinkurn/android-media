package com.tokopedia.posapp.product.management.domain;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class EditProductLocalPriceUseCase extends UseCase<DataStatus> {
    @Override
    public Observable<DataStatus> createObservable(RequestParams requestParams) {
        return null;
    }
}
