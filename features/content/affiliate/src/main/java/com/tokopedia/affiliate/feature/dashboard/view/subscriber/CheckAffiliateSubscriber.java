package com.tokopedia.affiliate.feature.dashboard.view.subscriber;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract;

import rx.Subscriber;

/**
 * @author by milhamj on 10/17/18.
 */
public class CheckAffiliateSubscriber extends Subscriber<Boolean> {
    private final AffiliateDashboardContract.View view;

    public CheckAffiliateSubscriber(AffiliateDashboardContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        view.onErrorCheckAffiliate(
                ErrorHandler.getErrorMessage(view.getCtx(), e)
        );
        view.hideLoading();
    }

    @Override
    public void onNext(Boolean isAffiliate) {
        view.onSuccessCheckAffiliate(isAffiliate);
    }
}
