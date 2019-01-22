package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by yfsx on 12/10/18.
 */
public class CheckQuotaSubscriber extends Subscriber<GraphqlResponse> {
    private ExploreContract.View mainView;
    private String productId;
    private String adId;

    public CheckQuotaSubscriber(ExploreContract.View mainView, String productId, String adId) {
        this.mainView = mainView;
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
        if (mainView == null)
            return;
        mainView.hideLoading();
        mainView.onErrorCheckQuota(
                ErrorHandler.getErrorMessage(mainView.getContext(), e), productId, adId
        );
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        CheckQuotaQuery query = response.getData(CheckQuotaQuery.class);
        if (query != null) {
            if (query.getData().getNumber() == 0) mainView.onSuccessCheckQuotaButEmpty();
            else mainView.onSuccessCheckQuota(productId, adId);
        } else {
            mainView.onErrorCheckQuota(
                    ErrorHandler.getErrorMessage(mainView.getContext(), new Throwable()),
                    productId,
                    adId
            );
        }
    }
}
