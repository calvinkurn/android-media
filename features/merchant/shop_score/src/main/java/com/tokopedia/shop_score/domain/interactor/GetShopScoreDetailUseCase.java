package com.tokopedia.shop_score.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.shop_score.domain.ShopScoreRepository;
import com.tokopedia.shop_score.domain.model.ShopScoreDetailDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class GetShopScoreDetailUseCase extends UseCase<ShopScoreDetailDomainModel> {
    private final ShopScoreRepository shopScoreRepository;

    public GetShopScoreDetailUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ShopScoreRepository shopScoreRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.shopScoreRepository = shopScoreRepository;
    }

    @Override
    public Observable<ShopScoreDetailDomainModel> createObservable(RequestParams requestParams) {
        return shopScoreRepository.getShopScoreDetail();
    }
}
