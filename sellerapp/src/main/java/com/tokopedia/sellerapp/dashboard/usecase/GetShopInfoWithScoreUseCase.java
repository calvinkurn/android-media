package com.tokopedia.sellerapp.dashboard.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus;
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult;
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusNoKycUseCase;
import com.tokopedia.sellerapp.dashboard.model.ShopInfoDashboardModel;

import javax.inject.Inject;

import kotlin.Triple;
import rx.Observable;
import rx.schedulers.Schedulers;

public class GetShopInfoWithScoreUseCase extends UseCase<Triple<ShopInfoDashboardModel, GoldGetPmOsStatus, ShopScoreResult>> {
    public static final String SHOP_ID = "shop_id";

    private final GetShopInfoDashboardDataUseCase getShopInfoDashboardDataUseCase;
    private final GetPowerMerchantStatusNoKycUseCase getPowerMerchantStatusNoKycUseCase;

    @Inject
    public GetShopInfoWithScoreUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       GetShopInfoDashboardDataUseCase getShopInfoDashboardDataUseCase,
                                       GetPowerMerchantStatusNoKycUseCase getPowerMerchantStatusNoKycUseCase){
        super(threadExecutor, postExecutionThread);
        this.getShopInfoDashboardDataUseCase = getShopInfoDashboardDataUseCase;
        this.getPowerMerchantStatusNoKycUseCase = getPowerMerchantStatusNoKycUseCase;
    }

    @Override
    public Observable<Triple<ShopInfoDashboardModel, GoldGetPmOsStatus, ShopScoreResult>> createObservable(RequestParams requestParams) {
        int shopId = Integer.parseInt(requestParams.getString(SHOP_ID, ""));
        getShopInfoDashboardDataUseCase.setParams(GetShopInfoDashboardDataUseCase.createParams(shopId));
        return Observable.zip(
                getShopInfoDashboardDataUseCase.createObservable(getShopInfoDashboardDataUseCase.getParams()).subscribeOn(Schedulers.io()),
                getPowerMerchantStatusNoKycUseCase.createObservable(
                        GetPowerMerchantStatusNoKycUseCase.createRequestParams(requestParams.getString(SHOP_ID, "")))
                        .subscribeOn(Schedulers.io()),
                (shopInfo, pair) -> new Triple<>(shopInfo, pair.getFirst(), pair.getSecond())
        ).take(1);
    }

    public static RequestParams createRequestParams(String shopId){
        RequestParams requestParams= RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
