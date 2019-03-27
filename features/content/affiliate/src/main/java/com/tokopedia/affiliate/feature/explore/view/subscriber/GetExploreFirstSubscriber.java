package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreFirstPageViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;

import rx.Subscriber;

/**
 * @author by yfsx on 08/10/18.
 */
public class GetExploreFirstSubscriber extends Subscriber<ExploreFirstPageViewModel> {

    protected ExploreContract.View mainView;
    protected boolean isPullToRefresh;
    private ExploreParams exploreParams;
    private boolean isSearch;

    public GetExploreFirstSubscriber(ExploreContract.View mainView,
                                     boolean isSearch,
                                     boolean isPullToRefresh,
                                     ExploreParams exploreParams) {
        this.mainView = mainView;
        this.isSearch = isSearch;
        this.isPullToRefresh = isPullToRefresh;
        this.exploreParams = exploreParams;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }

        if (mainView == null) {
            return;
        }
        mainView.hideLoading();
        mainView.unsubscribeAutoComplete();
        mainView.onErrorGetFirstData(ErrorHandler.getErrorMessage(mainView.getContext(), e));
        mainView.stopTrace();
    }

    @Override
    public void onNext(ExploreFirstPageViewModel firstPageViewModel) {
        mainView.hideLoading();
        mainView.unsubscribeAutoComplete();
        mainView.onSuccessGetFirstData(
                firstPageViewModel.getSection(),
                firstPageViewModel.getProducts(),
                firstPageViewModel.getNextCursor(),
                isSearch,
                isPullToRefresh,
                firstPageViewModel.getSortList()
        );

        mainView.stopTrace();
    }
}
