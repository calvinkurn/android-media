package com.tokopedia.affiliate.feature.explore.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.common.domain.usecase.CheckAffiliateUseCase;
import com.tokopedia.affiliate.common.domain.usecase.CheckQuotaUseCase;
import com.tokopedia.affiliate.feature.explore.domain.usecase.AutoCompleteUseCase;
import com.tokopedia.affiliate.feature.explore.domain.usecase.ExploreUseCase;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.subscriber.AutoCompleteSubscriber;
import com.tokopedia.affiliate.feature.explore.view.subscriber.CheckAffiliateSubscriber;
import com.tokopedia.affiliate.feature.explore.view.subscriber.CheckQuotaSubscriber;
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
    private CheckAffiliateUseCase checkAffiliateUseCase;
    private AutoCompleteUseCase autoCompleteUseCase;

    @Inject
    public ExplorePresenter(ExploreUseCase exploreUseCase,
                            CheckQuotaUseCase checkQuotaUseCase,
                            CheckAffiliateUseCase checkAffiliateUseCase,
                            AutoCompleteUseCase autoCompleteUseCase) {
        this.exploreUseCase = exploreUseCase;
        this.checkQuotaUseCase = checkQuotaUseCase;
        this.checkAffiliateUseCase = checkAffiliateUseCase;
        this.autoCompleteUseCase = autoCompleteUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        exploreUseCase.unsubscribe();
        checkQuotaUseCase.unsubscribe();
        checkAffiliateUseCase.unsubscribe();
        autoCompleteUseCase.unsubscribe();
    }

    @Override
    public void getFirstData(ExploreParams exploreParams, boolean isPullToRefresh) {
        if (!isPullToRefresh) getView().showLoading();
        exploreUseCase.clearRequest();
        exploreUseCase.addRequest(exploreUseCase.getRequest(exploreParams));
        exploreUseCase.execute(
                new GetExploreFirstSubscriber(
                        getView(),
                        !TextUtils.isEmpty(exploreParams.getKeyword()))
        );
    }

    @Override
    public void loadMoreData(ExploreParams exploreParams) {
        exploreUseCase.clearRequest();
        exploreUseCase.addRequest(exploreUseCase.getRequestLoadMore(exploreParams));
        exploreUseCase.execute(new GetExploreLoadMoreSubscriber(getView()));
    }

    @Override
    public void checkIsAffiliate(String productId, String adId) {
        checkAffiliateUseCase.execute(new CheckAffiliateSubscriber(getView(), productId, adId));
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
}
