package com.tokopedia.affiliate.feature.onboarding.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.onboarding.view.contract.RecommendProductContract;

/**
 * @author by milhamj on 10/4/18.
 */
public class RecommendProductPresenter extends BaseDaggerPresenter<RecommendProductContract.View>
        implements RecommendProductContract.Presenter {

    @Override
    public void detachView() {
        super.detachView();

    }
}
