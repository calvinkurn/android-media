package com.tokopedia.sellerapp.dashboard.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult;
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel;
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusNoKycUseCase;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;

import javax.inject.Inject;

import kotlin.Triple;
import rx.Observable;
import rx.schedulers.Schedulers;

public class GetShopInfoWithScoreUseCase extends UseCase<Triple<ShopModel, ShopStatusModel, ShopScoreResult>> {
    public static final String SHOP_ID = "shop_id";

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetPowerMerchantStatusNoKycUseCase getPowerMerchantStatusNoKycUseCase;

    @Inject
    public GetShopInfoWithScoreUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       GetShopInfoUseCase getShopInfoUseCase,
                                       GetPowerMerchantStatusNoKycUseCase getPowerMerchantStatusNoKycUseCase){
        super(threadExecutor, postExecutionThread);
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getPowerMerchantStatusNoKycUseCase = getPowerMerchantStatusNoKycUseCase;
    }

    @Override
    public Observable<Triple<ShopModel, ShopStatusModel, ShopScoreResult>> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getShopInfoUseCase.createObservable(RequestParams.EMPTY).subscribeOn(Schedulers.io()),
                getPowerMerchantStatusNoKycUseCase.createObservable(
                        GetPowerMerchantStatusNoKycUseCase.createRequestParams(requestParams.getString(SHOP_ID, "")))
                        .subscribeOn(Schedulers.io()),
                (shopModel, pair) -> new Triple<>(shopModel, pair.getFirst(), pair.getSecond())
        ).take(1);
    }

    public static RequestParams createRequestParams(String shopId){
        RequestParams requestParams= RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
