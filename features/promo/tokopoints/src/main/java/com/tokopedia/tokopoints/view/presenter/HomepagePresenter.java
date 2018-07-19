package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.contract.HomepageContract;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.TokenDetailOuter;
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class HomepagePresenter extends BaseDaggerPresenter<HomepageContract.View>
        implements HomepageContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private GraphqlUseCase mGetTokoPointDetailUseCase;
    private GraphqlUseCase mGetTokoPointPromoUseCase;
    private GraphqlUseCase mSaveCouponUseCase;
    private GraphqlUseCase mValidateCouponUseCase;
    private GraphqlUseCase mRedeemCouponUseCase;

    @Inject
    public HomepagePresenter(GraphqlUseCase getTokoPointDetailUseCase,
                             GraphqlUseCase getTokoPointPromoUseCase,
                             GraphqlUseCase saveCouponUseCase,
                             GraphqlUseCase redeemCouponUseCase,
                             GraphqlUseCase validateCouponUseCase
    ) {

        this.mGetTokoPointDetailUseCase = getTokoPointDetailUseCase;
        this.mGetTokoPointPromoUseCase = getTokoPointPromoUseCase;
        this.mSaveCouponUseCase = saveCouponUseCase;
        this.mValidateCouponUseCase = validateCouponUseCase;
        this.mRedeemCouponUseCase = redeemCouponUseCase;
    }


    @Override
    public void destroyView() {
        if (mGetTokoPointDetailUseCase != null) {
            mGetTokoPointDetailUseCase.unsubscribe();
        }

        if (mGetTokoPointPromoUseCase != null) {
            mGetTokoPointPromoUseCase.unsubscribe();
        }

        if (mSaveCouponUseCase != null) {
            mSaveCouponUseCase.unsubscribe();
        }

        if (mValidateCouponUseCase != null) {
            mValidateCouponUseCase.unsubscribe();
        }

        if (mRedeemCouponUseCase != null) {
            mRedeemCouponUseCase.unsubscribe();
        }
    }

    @Override
    public void getTokoPointDetail() {
        getView().showLoading();
        mGetTokoPointDetailUseCase.clearRequest();
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_tokopoint_detail),
                TokoPointDetailEntity.class);
        mGetTokoPointDetailUseCase.addRequest(graphqlRequest);

        GraphqlRequest graphqlRequestEgg = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_lucky_egg_details),
                TokenDetailOuter.class);
        mGetTokoPointDetailUseCase.addRequest(graphqlRequestEgg);

        mGetTokoPointDetailUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onError(e.getLocalizedMessage());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                //Handling for main data
                TokoPointDetailEntity data = graphqlResponse.getData(TokoPointDetailEntity.class);
                getView().onSuccess(data.getTokoPoints().getStatus().getTier(), data.getTokoPoints().getStatus().getPoints());

                //handling for lucky egg data
                TokenDetailOuter tokenDetail = graphqlResponse.getData(TokenDetailOuter.class);
                if (tokenDetail != null
                        && tokenDetail.getTokenDetail() != null
                        && tokenDetail.getTokenDetail().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS) {
                    getView().onSuccessTokenDetail(tokenDetail.getTokenDetail());
                }

                if (data.getTokoPoints() == null
                        || data.getTokoPoints().getTicker() == null
                        || data.getTokoPoints().getTicker().getTickers() == null) {
                    getView().onErrorTicker(null);
                } else {
                    getView().onSuccessTicker(data.getTokoPoints().getTicker().getTickers());
                }
            }
        });
    }

    @Override
    public void getPromos() {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.PAGE, 1);
        variables.put(CommonConstant.GraphqlVariableKeys.PAGE_SIZE, CommonConstant.HOMEPAGE_PAGE_SIZE);  //For home page max page will be 1
        variables.put(CommonConstant.GraphqlVariableKeys.SORT_ID, CommonConstant.DEFAULT_SORT_TYPE); // 1 for all catalog
        variables.put(CommonConstant.GraphqlVariableKeys.CATEGORY_ID, CommonConstant.DEFAULT_CATEGORY_TYPE); // zero for no filter
        variables.put(CommonConstant.GraphqlVariableKeys.POINTS_RANGE, 0); //zero for all catalog
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_tokopoint_promos),
                TokoPointPromosEntity.class,
                variables);
        mGetTokoPointPromoUseCase.clearRequest();
        mGetTokoPointPromoUseCase.addRequest(graphqlRequest);
        mGetTokoPointPromoUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorPromos(e.getLocalizedMessage());
            }

            @Override
            public void onNext(GraphqlResponse response) {
                getView().onSuccessPromos(response.getData(TokoPointPromosEntity.class));
            }
        });
    }

    @Override
    public void startValidateCoupon(final CatalogsValueEntity item) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_ID, item.getId());
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_tokopoint_validate_redeem),
                ValidateCouponBaseEntity.class, variables);
        mValidateCouponUseCase.clearRequest();
        mValidateCouponUseCase.addRequest(graphqlRequest);
        mValidateCouponUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
            }

            @Override
            public void onNext(GraphqlResponse response) {
                int validateResponseCode;
                String message;
                String title;
                ValidateCouponBaseEntity validateCoupon = response.getData(ValidateCouponBaseEntity.class);

                if (validateCoupon.getValidateCoupon() != null) {
                    validateResponseCode = CommonConstant.CouponRedemptionCode.SUCCESS;
                    message = validateCoupon.getValidateCoupon().getMessageSuccess();
                    title = validateCoupon.getValidateCoupon().getMessageTitle();
                } else {
                    String[] errorsMessage = response.getError(ValidateCouponBaseEntity.class).get(0).getMessage().split("\\|");
                    title = errorsMessage[0];
                    message = errorsMessage[1];
                    validateResponseCode = Integer.parseInt(errorsMessage[2]);
                }

                getView().showValidationMessageDialog(item, title, message, validateResponseCode);
            }
        });
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
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_ID, item.getId());
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_tokopoint_redeem_coupon),
                com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity.class,
                variables);
        mRedeemCouponUseCase.clearRequest();
        mRedeemCouponUseCase.addRequest(request);
        mRedeemCouponUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
            }

            @Override
            public void onNext(GraphqlResponse response) {
                RedeemCouponBaseEntity redeemCouponBaseEntity = response.getData(RedeemCouponBaseEntity.class);
                if (redeemCouponBaseEntity != null) {
                    getView().showConfirmRedeemDialog(redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCta(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCode(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getTitle());
                }
            }
        });
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
