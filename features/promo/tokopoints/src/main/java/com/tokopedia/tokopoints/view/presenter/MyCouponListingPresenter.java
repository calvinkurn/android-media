package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.contract.MyCouponListingContract;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponFilterBase;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class MyCouponListingPresenter extends BaseDaggerPresenter<MyCouponListingContract.View>
        implements MyCouponListingContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private GraphqlUseCase mGetCoupon;
    private GraphqlUseCase mSaveCouponUseCase;
    private int categoryId=0;

    @Inject
    public MyCouponListingPresenter(GraphqlUseCase getCoupon, GraphqlUseCase saveCouponUseCase) {
        this.mGetCoupon = getCoupon;
        this.mSaveCouponUseCase = saveCouponUseCase;
    }

    @Override
    public void destroyView() {
        if (mGetCoupon != null) {
            mGetCoupon.unsubscribe();
        }
    }

    @Override
    public void getCoupons(int categoryId) {
        getView().populateCoupons(categoryId);
    }

    @Override
    public void setCategoryId(int categoryId) {
        this.categoryId=categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public void startValidateCoupon(final CatalogsValueEntity item) {
    }

    @Override
    public void redeemCoupon(String promoCode, String cta) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.PROMO_CODE, promoCode);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_tokopoint_apply_coupon),
                com.tokopedia.tokopoints.view.model.ApplyCouponBaseEntity.class,
                variables);
        mSaveCouponUseCase.clearRequest();
        mSaveCouponUseCase.addRequest(request);
        mSaveCouponUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                RouteManager.route(getView().getActivityContext(), cta);
            }

            @Override
            public void onNext(GraphqlResponse saveCoupon) {
                RouteManager.route(getView().getActivityContext(), cta);
            }
        });
    }

    @Override
    public void startSaveCoupon(CatalogsValueEntity item) {
    }

    @Override
    public void navigateToWebView(String url) {
        getView().openWebView(url);
    }

    @Override
    public void showRedeemCouponDialog(String cta, String code, String title) {
        getView().showRedeemCouponDialog(cta, code, title);
    }
}
