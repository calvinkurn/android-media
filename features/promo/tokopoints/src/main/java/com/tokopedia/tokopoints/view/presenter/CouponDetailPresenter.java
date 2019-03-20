package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.contract.CouponDetailContract;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponDetailOuter;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter;
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class CouponDetailPresenter extends BaseDaggerPresenter<CouponDetailContract.View>
        implements CouponDetailContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private GraphqlUseCase mRedeemCouponUseCase;

    //new apis
    private GraphqlUseCase mGetCouponDetail;

    @Inject
    public CouponDetailPresenter(GraphqlUseCase redeemCouponUseCase,
                                 GraphqlUseCase getCouponDetail) {
        this.mRedeemCouponUseCase = redeemCouponUseCase;
        this.mGetCouponDetail = getCouponDetail;
    }

    @Override
    public void destroyView() {
        if (mRedeemCouponUseCase != null) {
            mRedeemCouponUseCase.unsubscribe();
        }

        if (mGetCouponDetail != null) {
            mGetCouponDetail.unsubscribe();
        }
    }

    @Override
    public void startValidateCoupon(final CatalogsValueEntity item) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_ID, item.getId());
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);   //Never be a gift
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_tokopoint_validate_redeem),
                ValidateCouponBaseEntity.class, variables, false);
        mRedeemCouponUseCase.clearRequest();
        mRedeemCouponUseCase.addRequest(graphqlRequest);
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
                int validateResponseCode = -1;
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

                    if (errorsMessage.length == 2) {
                        try {
                            validateResponseCode = Integer.parseInt(errorsMessage[2]);
                        } catch (NumberFormatException e) {
                            //Just to avoid NFE
                        }
                    }
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
                variables, false);
        mRedeemCouponUseCase.clearRequest();
        mRedeemCouponUseCase.addRequest(request);
        mRedeemCouponUseCase.execute(new Subscriber<GraphqlResponse>() {
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
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);     //Never be a gift

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_tokopoint_redeem_coupon),
                RedeemCouponBaseEntity.class,
                variables, false);
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
                } else {
                    String[] errorsMessage = response.getError(RedeemCouponBaseEntity.class).get(0).getMessage().split("\\|");
                    if (errorsMessage != null && errorsMessage.length > 0) {
                        String title = errorsMessage[0];
                        if (errorsMessage.length <= 2) {
                            getView().showRedeemFullError(item, null, title);
                        } else {
                            String desc = errorsMessage[1];
                            getView().showRedeemFullError(item, title, desc);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void getCouponDetail(String uniqueCouponCode) {
        getView().showLoader();
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CODE, uniqueCouponCode);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_coupon_detail),
                CouponDetailOuter.class,
                variables, false);
        mGetCouponDetail.clearRequest();
        mGetCouponDetail.addRequest(request);

        mGetCouponDetail.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
                getView().hideLoader();
            }

            @Override
            public void onNext(GraphqlResponse response) {
                getView().hideLoader();
                CouponDetailOuter data = response.getData(CouponDetailOuter.class);
                getView().populateDetail(data.getDetail());
            }
        });
    }

    @Override
    public void reFetchRealCode(String uniqueCouponCode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CODE, uniqueCouponCode);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_refetch_real_code),
                CouponDetailOuter.class,
                variables, false);
        mGetCouponDetail.clearRequest();
        mGetCouponDetail.addRequest(request);

        mGetCouponDetail.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onRealCodeReFreshError();
            }

            @Override
            public void onNext(GraphqlResponse response) {
                CouponDetailOuter data = response.getData(CouponDetailOuter.class);
                getView().onRealCodeReFresh(data.getDetail().getRealCode());
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

    public void swipeMyCoupon(String partnerCode, String pin) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CODE, partnerCode);
        variables.put(CommonConstant.GraphqlVariableKeys.PIN, pin);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_swipe_coupon),
                CouponSwipeUpdateOuter.class,
                variables, false);
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
                CouponSwipeUpdateOuter data = response.getData(CouponSwipeUpdateOuter.class);
                if (data != null && data.getSwipeCoupon() != null) {
                    if (data.getSwipeCoupon().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS) {
                        getView().onSwipeResponse(data.getSwipeCoupon(), null, null);
                    } else {
                        if (data.getSwipeCoupon().getResultStatus().getMessages().size() > 0) {
                            getView().onSwipeError(data.getSwipeCoupon().getResultStatus().getMessages().get(0));
                        }
                    }
                }
            }
        });
    }
}
