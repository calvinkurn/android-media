package com.tokopedia.gm.common.domain.interactor;

import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class SetCashbackUseCase extends UseCase<Boolean> {
    public static final String PRODUCT_ID = "product_id";
    public static final String CASHBACK = "cashback";
    private final GMCommonRepository gmCommonRepository;

    @Inject
    public SetCashbackUseCase(GMCommonRepository gmCommonRepository) {
        this.gmCommonRepository = gmCommonRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gmCommonRepository.setCashback(requestParams.getString(PRODUCT_ID, ""), requestParams.getInt(CASHBACK, 0));
    }

    public static RequestParams createRequestParams(String productId, int cashback) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        requestParams.putInt(CASHBACK, cashback);
        return requestParams;
    }
}
