package com.tokopedia.reputation.common.domain.interactor;

import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.domain.repository.ReputationCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetReputationSpeedUseCase extends UseCase<ReputationSpeed> {

    private static final String SHOP_ID = "SHOP_ID";

    private ReputationCommonRepository reputationCommonRepository;

    public GetReputationSpeedUseCase(ReputationCommonRepository reputationCommonRepository) {
        this.reputationCommonRepository = reputationCommonRepository;
    }

    @Override
    public Observable<ReputationSpeed> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, null);
        return reputationCommonRepository.getStatisticSpeed(shopId);
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
