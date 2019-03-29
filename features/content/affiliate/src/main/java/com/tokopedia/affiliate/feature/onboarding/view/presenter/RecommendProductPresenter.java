package com.tokopedia.affiliate.feature.onboarding.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.onboarding.view.listener.RecommendProductContract;
import com.tokopedia.affiliate.feature.onboarding.view.subscriber.GetProductInfoSubscriber;
import com.tokopedia.affiliatecommon.domain.GetProductAffiliateGqlUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 10/4/18.
 */
public class RecommendProductPresenter extends BaseDaggerPresenter<RecommendProductContract.View>
        implements RecommendProductContract.Presenter {

    private final GetProductAffiliateGqlUseCase getProductAffiliateGqlUseCase;

    @Inject
    RecommendProductPresenter(GetProductAffiliateGqlUseCase getProductAffiliateGqlUseCase) {
        this.getProductAffiliateGqlUseCase = getProductAffiliateGqlUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductAffiliateGqlUseCase.unsubscribe();
    }

    @Override
    public void getProductInfo(String productId) {
        getView().showLoading();
        List<Integer> productList = new ArrayList<>();
        productList.add(Integer.valueOf(productId));
        getProductAffiliateGqlUseCase.execute(
                GetProductAffiliateGqlUseCase.Companion.createRequestParams(productList),
                new GetProductInfoSubscriber(getView())
        );
    }
}
