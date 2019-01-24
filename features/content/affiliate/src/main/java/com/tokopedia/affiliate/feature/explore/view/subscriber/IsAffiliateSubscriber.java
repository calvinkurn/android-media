package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;

import rx.Subscriber;

/**
 * @author by yfsx on 24/01/19.
 */
public class IsAffiliateSubscriber extends Subscriber<Boolean> {
    private ExploreContract.View view;

    public IsAffiliateSubscriber(ExploreContract.View mainView) {
        this.view = mainView;
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
        view.onErrorIsAffiliate(
                ErrorHandler.getErrorMessage(view.getContext(), e
        ));
    }

    @Override
    public void onNext(Boolean isAffiliate) {
        view.hideLoading();
        view.onSuccessIsAffiliate(isAffiliate);
    }
}
