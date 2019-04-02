package com.tokopedia.affiliate.feature.explore.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.common.domain.usecase.CheckQuotaUseCase;
import com.tokopedia.affiliate.feature.explore.domain.usecase.AutoCompleteUseCase;
import com.tokopedia.affiliate.feature.explore.domain.usecase.ExploreFirstPageUseCase;
import com.tokopedia.affiliate.feature.explore.domain.usecase.ExploreUseCase;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.subscriber.AutoCompleteSubscriber;
import com.tokopedia.affiliate.feature.explore.view.subscriber.CheckQuotaSubscriber;
import com.tokopedia.affiliate.feature.explore.view.subscriber.GetExploreDataSubscriber;
import com.tokopedia.affiliate.feature.explore.view.subscriber.GetExploreFirstSubscriber;
import com.tokopedia.affiliate.feature.explore.view.subscriber.GetExploreLoadMoreSubscriber;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;

import javax.inject.Inject;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExplorePresenter extends BaseDaggerPresenter<ExploreContract.View> implements ExploreContract.Presenter {

    private ExploreUseCase exploreUseCase;
    private CheckQuotaUseCase checkQuotaUseCase;
    private AutoCompleteUseCase autoCompleteUseCase;
    private ExploreFirstPageUseCase exploreFirstPageUseCase;

    @Inject
    ExplorePresenter(ExploreUseCase exploreUseCase,
                     CheckQuotaUseCase checkQuotaUseCase,
                     AutoCompleteUseCase autoCompleteUseCase,
                     ExploreFirstPageUseCase exploreFirstPageUseCase) {
        this.exploreUseCase = exploreUseCase;
        this.checkQuotaUseCase = checkQuotaUseCase;
        this.autoCompleteUseCase = autoCompleteUseCase;
        this.exploreFirstPageUseCase = exploreFirstPageUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        exploreUseCase.unsubscribe();
        checkQuotaUseCase.unsubscribe();
        autoCompleteUseCase.unsubscribe();
        exploreFirstPageUseCase.unsubscribe();
    }

    @Override
    public void getFirstData(ExploreParams exploreParams, boolean isPullToRefresh) {
        unsubscribeAutoComplete();

        if (!isPullToRefresh) {
            getView().showLoading();
        }

        exploreFirstPageUseCase.setExploreParams(exploreParams);
        exploreFirstPageUseCase.execute(new GetExploreFirstSubscriber(
                getView(),
                !TextUtils.isEmpty(exploreParams.getKeyword()),
                isPullToRefresh,
                exploreParams
        ));
    }

    @Override
    public void getData(ExploreParams exploreParams) {
        unsubscribeAutoComplete();

        getView().showLoadingScreen();

        exploreUseCase.setExploreParams(exploreParams);
        exploreUseCase.execute(new GetExploreDataSubscriber(
                getView(),
                !TextUtils.isEmpty(exploreParams.getKeyword()),
                exploreParams
        ));
    }

    @Override
    public void loadMoreData(ExploreParams exploreParams) {
        unsubscribeAutoComplete();
        exploreUseCase.setExploreParams(exploreParams);
        exploreUseCase.execute(new GetExploreLoadMoreSubscriber(getView()));
    }

    @Override
    public void getAutoComplete(String keyword) {
        autoCompleteUseCase.clearRequest();
        autoCompleteUseCase.addRequest(autoCompleteUseCase.getRequest(keyword));
        autoCompleteUseCase.execute(new AutoCompleteSubscriber(getView()));
    }

    @Override
    public void checkAffiliateQuota(String productId, String adId) {
        checkQuotaUseCase.clearRequest();
        checkQuotaUseCase.addRequest(checkQuotaUseCase.getRequest());
        checkQuotaUseCase.execute(new CheckQuotaSubscriber(getView(), productId, adId));
    }

    @Override
    public void unsubscribeAutoComplete() {
        autoCompleteUseCase.unsubscribe();
    }

}
