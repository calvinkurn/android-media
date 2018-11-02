package com.tokopedia.affiliate.feature.dashboard.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;

import rx.Subscriber;

/**
 * @author by milhamj on 10/17/18.
 */
public class CheckAffiliateSubscriber extends Subscriber<Boolean> {
    private final DashboardContract.View view;

    public CheckAffiliateSubscriber(DashboardContract.View view) {
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
                ErrorHandler.getErrorMessage(view.getContext(), e)
        );
    }

    @Override
    public void onNext(Boolean isAffiliate) {
        view.onSuccessCheckAffiliate(isAffiliate);
    }
}
