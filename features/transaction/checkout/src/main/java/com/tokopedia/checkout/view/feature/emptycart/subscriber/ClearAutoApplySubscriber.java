package com.tokopedia.checkout.view.feature.emptycart.subscriber;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 26/03/19.
 */

public class ClearAutoApplySubscriber extends Subscriber<GraphqlResponse> {

    private EmptyCartContract.View view;

    public ClearAutoApplySubscriber(EmptyCartContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.hideLoadingDialog();
        view.showErrorToast(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoadingDialog();
        ClearCacheAutoApplyStackResponse responseData = graphqlResponse.getData(ClearCacheAutoApplyStackResponse.class);
        if (responseData != null && responseData.getSuccessData().getSuccess()) {
            view.renderCancelAutoApplyCouponSuccess();
        } else {
            view.showErrorToast("");
        }
    }

}
