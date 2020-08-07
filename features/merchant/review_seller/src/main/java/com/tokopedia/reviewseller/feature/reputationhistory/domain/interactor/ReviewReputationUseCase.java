package com.tokopedia.reviewseller.feature.reputationhistory.domain.interactor;

import com.tokopedia.reviewseller.feature.reputationhistory.domain.model.SellerReputationDomain;
import com.tokopedia.reviewseller.feature.reputationhistory.util.ShopNetworkController;
import com.tokopedia.reviewseller.feature.reputationhistory.domain.ReputationReviewRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author normansyahputa on 3/16/17.
 */
public class ReviewReputationUseCase extends UseCase<SellerReputationDomain> {

    private ReputationReviewRepository reputationReviewRepository;

    @Inject
    public ReviewReputationUseCase(
            ReputationReviewRepository reputationReviewRepository) {
        this.reputationReviewRepository = reputationReviewRepository;
    }

    public Observable<SellerReputationDomain> createObservable(String shopId, Map<String, String> param) {
        return reputationReviewRepository.getReputationHistory(shopId, param);
    }

    public void execute(String shopId, Map<String, String> param, Subscriber<SellerReputationDomain> subscriber) {
        this.subscription = createObservable(shopId, param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    @Override
    public Observable<SellerReputationDomain> createObservable(com.tokopedia.usecase.RequestParams requestParams) {
        return reputationReviewRepository.getReputationHistory(requestParams);
    }

    public static class RequestParamFactory {
        public static final String KEY_REVIEW_REPUTATION_PARAM = "review_reputation_param";

        public static RequestParams generateRequestParam(String shopId,
                                                         Map<String, String> param) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(ShopNetworkController.RequestParamFactory.KEY_SHOP_ID, shopId);
            requestParams.putObject(KEY_REVIEW_REPUTATION_PARAM, param);
            return requestParams;
        }
    }
}
