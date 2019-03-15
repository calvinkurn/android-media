package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 08/10/18.
 */
public class GetExploreLoadMoreSubscriber extends Subscriber<ExploreViewModel> {

    private ExploreContract.View mainView;

    public GetExploreLoadMoreSubscriber(ExploreContract.View mainView) {
        this.mainView = mainView;
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
        mainView.onErrorGetMoreData(ErrorHandler.getErrorMessage(mainView.getContext(), e));
    }

    @Override
    public void onNext(ExploreViewModel exploreViewModel) {
        mainView.hideLoading();
        mainView.onSuccessGetMoreData(
                exploreViewModel.getExploreProducts(),
                exploreViewModel.getNextCursor()
        );
    }
}
