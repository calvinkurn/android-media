package com.tokopedia.gm.common.domain.interactor;

import com.tokopedia.gm.common.data.source.cloud.model.GMGetCashbackModel;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/4/17.
 */

public class GetCashbackUseCase extends UseCase<List<GMGetCashbackModel>> {
    public static final String PRODUCT_ID_LIST = "product_ids";
    public static final String SHOP_ID = "shop_id";

    private final GMCommonRepository cashbackRepository;

    @Inject
    public GetCashbackUseCase(GMCommonRepository cashbackRepository) {
        super();
        this.cashbackRepository = cashbackRepository;
    }

    @Override
    public Observable<List<GMGetCashbackModel>> createObservable(RequestParams requestParams) {
        List<String> productIdList = ((List<String>) requestParams.getObject(PRODUCT_ID_LIST));
        String shopId = requestParams.getString(SHOP_ID, "");
        return cashbackRepository.getCashbackList(productIdList, shopId);
    }

    public static RequestParams createRequestParams(List<String> productIdList, String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PRODUCT_ID_LIST, productIdList);
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
