package com.tokopedia.gm.cashback.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.manage.constant.CashbackOption;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class SetCashbackUseCase extends UseCase<Boolean> {
    public static final String PRODUCT_ID = "product_id";
    public static final String CASHBACK = "cashback";
    private final GMCashbackRepository gmCashbackRepository;

    @Inject
    public SetCashbackUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                              GMCashbackRepository gmCashbackRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmCashbackRepository = gmCashbackRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gmCashbackRepository.setCashback(requestParams.getString(PRODUCT_ID, ""), requestParams.getInt(CASHBACK, CashbackOption.CASHBACK_OPTION_NONE));
    }

    public static RequestParams createRequestParams(String productId, int cashback) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        requestParams.putInt(CASHBACK, cashback);
        return requestParams;
    }
}
