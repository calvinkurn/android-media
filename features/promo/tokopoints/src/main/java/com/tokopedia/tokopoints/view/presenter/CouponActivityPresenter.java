package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.contract.CouponActivityContract;
import com.tokopedia.tokopoints.view.contract.MyCouponListingContract;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponFilterBase;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
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

    @Override
    public void getFilter(String slug) {
        Map<String, Object> variablesFilter = new HashMap<>();
        variablesFilter.put(CommonConstant.GraphqlVariableKeys.SLUG, slug == null ? "" : slug.toLowerCase());
        GraphqlRequest filterRequest = new GraphqlRequest(getView().getStringRaw(R.raw.tp_gql_coupon_filter),
                CouponFilterBase.class,
                variablesFilter);
        mGetFilter.addRequest(filterRequest);

        mGetFilter.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onError(e.getLocalizedMessage());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                //handling the catalog listing and tabs
                getView().hideLoading();
                CouponFilterBase catalogListingOuter = graphqlResponse.getData(CouponFilterBase.class);
                getView().onSuccess(catalogListingOuter.getFilter().getCategories());
            }
        });
    }
}
