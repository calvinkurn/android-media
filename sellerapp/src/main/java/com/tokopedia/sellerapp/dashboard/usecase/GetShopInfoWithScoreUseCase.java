package com.tokopedia.sellerapp.dashboard.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreMainDataUseCase;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.sellerapp.dashboard.model.ShopModelWithScore;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by User on 9/12/2017.
 */

public class GetShopInfoWithScoreUseCase extends UseCase<ShopModelWithScore> {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetShopScoreMainDataUseCase getShopScoreMainDataUseCase;

    @Inject
    public GetShopInfoWithScoreUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       GetShopInfoUseCase getShopInfoUseCase,
                                       GetShopScoreMainDataUseCase getShopScoreMainDataUseCase){
        super(threadExecutor, postExecutionThread);
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getShopScoreMainDataUseCase = getShopScoreMainDataUseCase;
    }

    @Override
    public Observable<ShopModelWithScore> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getShopInfoUseCase.createObservable(RequestParams.EMPTY),
                getShopScoreMainDataUseCase.createObservable(RequestParams.EMPTY),
                new Func2<ShopModel, ShopScoreMainDomainModel, ShopModelWithScore>() {
                    @Override
                    public ShopModelWithScore call(ShopModel shopModel, ShopScoreMainDomainModel shopScoreMainDomainModel) {
                        return new ShopModelWithScore(shopModel, shopScoreMainDomainModel);
                    }
                }
        ).take(1);
    }
}
