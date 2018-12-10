package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;

import rx.Subscriber;

/**
 * @author by milhamj on 10/17/18.
 */
public class CheckAffiliateSubscriber extends Subscriber<Boolean> {
    private final ExploreContract.View view;
    private String productId;
    private String adId;

    public CheckAffiliateSubscriber(ExploreContract.View view, String productId, String adId) {
        this.view = view;
        this.productId = productId;
        this.adId = adId;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        if (view == null)
            return;
        view.hideLoading();
        view.onErrorCheckAffiliate(
                ErrorHandler.getErrorMessage(view.getContext(), e), productId, adId
        );
    }

    @Override
    public void onNext(Boolean isAffiliate) {
        view.hideLoading();
        view.onSuccessCheckAffiliate(isAffiliate, productId, adId);
    }
}
