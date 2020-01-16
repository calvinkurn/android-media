package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CouponActivityContract;
import com.tokopedia.tokopoints.view.model.CouponFilterBase;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class CouponActivityPresenter extends BaseDaggerPresenter<CouponActivityContract.View>
        implements CouponActivityContract.Presenter {
    private GraphqlUseCase mGetFilter;

    @Inject
    public CouponActivityPresenter(GraphqlUseCase getFilter) {
        this.mGetFilter = getFilter;
    }

    @Override
    public void destroyView() {
        if (mGetFilter != null) {
            mGetFilter.unsubscribe();
        }
    }
}
