package com.tokopedia.reviewseller.feature.reputationhistory.domain.interactor;

import com.tokopedia.reviewseller.feature.reputationhistory.util.ShopNetworkController;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.reviewseller.feature.reputationhistory.domain.interactor.ReviewReputationMergeUseCase.RequestParamFactory.KEY_REVIEW_REPUTATION_CONTAINER_PARAM;
import static com.tokopedia.reviewseller.feature.reputationhistory.domain.interactor.ReviewReputationMergeUseCase.RequestParamFactory.KEY_SHOP_INFO_CONTAINER_PARAM;

/**
 * @author normansyahputa on 3/17/17.
 */

public class ReviewReputationMergeUseCase extends UseCase<List<Object>> {
    private final ReviewReputationUseCase reviewReputationUseCase;
    private final ShopInfoUseCase shopInfoUseCase;


    @Inject
    public ReviewReputationMergeUseCase(
            ReviewReputationUseCase reviewReputationUseCase,
            ShopInfoUseCase shopInfoUseCase) {
        this.reviewReputationUseCase = reviewReputationUseCase;
        this.shopInfoUseCase = shopInfoUseCase;
    }


    public Observable<List<Object>> createObservable(
            String userid, String deviceId,
            ShopNetworkController.ShopInfoParam shopInfoParam,
            String shopId, Map<String, String> param) {
        return Observable.concat(
                shopInfoUseCase.createObservable(userid, deviceId, shopInfoParam),
                reviewReputationUseCase.createObservable(shopId, param)
        ).toList();
    }

    public void execute(String userid, String deviceId,
                        ShopNetworkController.ShopInfoParam shopInfoParam,
                        String shopId, Map<String, String> param, Subscriber<List<Object>> subscriber) {
        createObservable(userid, deviceId, shopInfoParam, shopId, param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    @Override
    public Observable<List<Object>> createObservable(com.tokopedia.usecase.RequestParams requestParams) {
        return Observable.concat(
                shopInfoUseCase.createObservable((RequestParams) requestParams.getObject(KEY_SHOP_INFO_CONTAINER_PARAM)),
                reviewReputationUseCase.createObservable((RequestParams) requestParams.getObject(KEY_REVIEW_REPUTATION_CONTAINER_PARAM))
        ).toList();
    }

    public static class RequestParamFactory {
        public static final String KEY_REVIEW_REPUTATION_CONTAINER_PARAM = "KEY_REVIEW_REPUTATION_CONTAINER_PARAM";
        public static final String KEY_SHOP_INFO_CONTAINER_PARAM = "KEY_SHOP_INFO_CONTAINER_PARAM";

        public static RequestParams generateRequestParam(RequestParams reviewParam, RequestParams shopInfoParam) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(KEY_REVIEW_REPUTATION_CONTAINER_PARAM, reviewParam);
            requestParams.putObject(KEY_SHOP_INFO_CONTAINER_PARAM, shopInfoParam);
            return requestParams;
        }
    }
}
