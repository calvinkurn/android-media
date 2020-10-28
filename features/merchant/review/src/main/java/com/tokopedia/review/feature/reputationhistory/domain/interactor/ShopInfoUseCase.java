package com.tokopedia.review.feature.reputationhistory.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.review.feature.reputationhistory.util.ShopNetworkController;
import com.tokopedia.review.feature.reputationhistory.domain.ReputationReviewRepository;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 3/17/17.
 */

@Deprecated
public class ShopInfoUseCase extends UseCase<ShopModel> {
    private ReputationReviewRepository reputationReviewRepository;

    @Inject
    public ShopInfoUseCase(
            ReputationReviewRepository reputationReviewRepository) {
        this.reputationReviewRepository = reputationReviewRepository;
    }

    public void execute(String userid, String deviceId,
                        ShopNetworkController.ShopInfoParam shopInfoParam,
                        Subscriber<ShopModel> subscriber) {
        this.subscription = createObservable(userid, deviceId, shopInfoParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Observable<ShopModel> createObservable(String userid, String deviceId, ShopNetworkController.ShopInfoParam shopInfoParam) {
        return reputationReviewRepository.getShopInfo(userid, deviceId, shopInfoParam);
    }

    @Override
    public Observable<ShopModel> createObservable(com.tokopedia.usecase.RequestParams requestParams) {
        return reputationReviewRepository.getShopInfo(requestParams);
    }
}
