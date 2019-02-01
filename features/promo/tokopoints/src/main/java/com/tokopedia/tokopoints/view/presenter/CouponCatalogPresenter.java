package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.contract.CouponCatalogContract;
import com.tokopedia.tokopoints.view.model.CatalogDetailOuter;
import com.tokopedia.tokopoints.view.model.CatalogStatusOuter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponDetailOuter;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter;
import com.tokopedia.tokopoints.view.model.PreValidateRedeemBase;
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity;
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class CouponCatalogPresenter extends BaseDaggerPresenter<CouponCatalogContract.View>
        implements CouponCatalogContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private GraphqlUseCase mGetHomePageData;
    private GraphqlUseCase mSaveCouponUseCase;
    private GraphqlUseCase mValidateCouponUseCase;
    private GraphqlUseCase mRedeemCouponUseCase;
    private GraphqlUseCase mRefreshCatalogStatus;
    private GraphqlUseCase mStartSendGift;

    //new apis
    private GraphqlUseCase mGetCouponDetail;

    @Inject
    public CouponCatalogPresenter(GraphqlUseCase getHomePageData,
                                  GraphqlUseCase saveCouponUseCase,
                                  GraphqlUseCase validateCouponUseCase,
                                  GraphqlUseCase redeemCouponUseCase,
                                  GraphqlUseCase getCouponDetail,
                                  GraphqlUseCase startSendGift,
                                  GraphqlUseCase refreshCatalogStatus) {
        this.mGetHomePageData = getHomePageData;
        this.mSaveCouponUseCase = saveCouponUseCase;
        this.mValidateCouponUseCase = validateCouponUseCase;
        this.mRedeemCouponUseCase = redeemCouponUseCase;
        this.mGetCouponDetail = getCouponDetail;
        this.mRefreshCatalogStatus = refreshCatalogStatus;
        this.mStartSendGift = startSendGift;
    }

    @Override
    public void destroyView() {
        if (mGetHomePageData != null) {
            mGetHomePageData.unsubscribe();
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

                if (validateCoupon!=null && validateCoupon.getValidateCoupon() != null) {
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
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);     //Never be a gift

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_tokopoint_redeem_coupon),
                RedeemCouponBaseEntity.class,
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
                if (redeemCouponBaseEntity != null && redeemCouponBaseEntity.getHachikoRedeem()!=null) {
                    getView().showConfirmRedeemDialog(redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCta(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCode(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getTitle());
                } else {
                    String[] errorsMessage = response.getError(RedeemCouponBaseEntity.class).get(0).getMessage().split("\\|");
                    if (errorsMessage != null && errorsMessage.length > 0) {
                        String title = errorsMessage[0];
                        String desc = null;
                        int validateResponseCode = 0;

                        if (errorsMessage.length == 2) {
                            desc = errorsMessage[1];
                        }
                        if (errorsMessage.length == 3)
                            validateResponseCode = Integer.parseInt(errorsMessage[2]);
                        getView().showValidationMessageDialog(item, title, desc, validateResponseCode);

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
                variables);
        mGetCouponDetail.clearRequest();
        mGetCouponDetail.addRequest(request);

        GraphqlRequest graphqlRequestPoints = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.tp_gql_current_points),
                TokoPointDetailEntity.class);
        mGetCouponDetail.addRequest(graphqlRequestPoints);
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
                handlePointQuery(response.getData(TokoPointDetailEntity.class));
            }
        });
    }

    @Override
    public void getCatalogDetail(String uniqueCatalogCode) {
        getView().showLoader();
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.SLUG, uniqueCatalogCode);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_catalog_detail),
                CatalogDetailOuter.class,
                variables);
        mGetCouponDetail.clearRequest();
        mGetCouponDetail.addRequest(request);

        GraphqlRequest graphqlRequestPoints = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.tp_gql_current_points),
                TokoPointDetailEntity.class);
        mGetCouponDetail.addRequest(graphqlRequestPoints);

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
                CatalogDetailOuter data = response.getData(CatalogDetailOuter.class);
                getView().populateDetail(data.getDetail());
                handlePointQuery(response.getData(TokoPointDetailEntity.class));
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
                variables);
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
    public void fetchLatestStatus(List<Integer> catalogsIds) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_IDS, catalogsIds);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_catalog_status),
                CatalogStatusOuter.class,
                variables);
        mRefreshCatalogStatus.clearRequest();
        mRefreshCatalogStatus.addRequest(request);
        mRefreshCatalogStatus.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA No handling require.
            }

            @Override
            public void onNext(GraphqlResponse response) {
                CatalogStatusOuter data = response.getData(CatalogStatusOuter.class);
                if (data != null && data.getCatalogStatus() != null) {
                    //For detail page we only interested in one item
                    getView().refreshCatalog(data.getCatalogStatus().getCatalogs().get(0));
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

    private void handlePointQuery(TokoPointDetailEntity pointDetailEntity) {
        //Handling the point
        if (pointDetailEntity == null
                || pointDetailEntity.getTokoPoints() == null
                || pointDetailEntity.getTokoPoints().getResultStatus() == null
                || pointDetailEntity.getTokoPoints().getStatus() == null
                || pointDetailEntity.getTokoPoints().getStatus().getPoints() == null) {
            getView().onErrorPoint(null);
        } else {
            if (pointDetailEntity.getTokoPoints().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS) {
                getView().onSuccessPoints(pointDetailEntity.getTokoPoints().getStatus().getPoints().getRewardStr());
            }
        }
    }

    @Override
    public void startSendGift(int id, String title, String pointStr) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_ID, id);
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 1);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_pre_validate_redeem),
                PreValidateRedeemBase.class,
                variables);
        mStartSendGift.clearRequest();
        mStartSendGift.addRequest(request);
        mStartSendGift.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
            }

            @Override
            public void onNext(GraphqlResponse response) {
                PreValidateRedeemBase data = response.getData(PreValidateRedeemBase.class);
                if (data != null
                        && data.getPreValidateRedeem() != null
                        && data.getPreValidateRedeem().getIsValid() == 1) {
                    getView().gotoSendGiftPage(id, title, pointStr);
                } else {
                    //show error
                    List<GraphqlError> errors = response.getError(PreValidateRedeemBase.class);

                    String errorTitle = getView().getAppContext().getString(R.string.tp_send_gift_failed_title);
                    String errorMessage = getView().getAppContext().getString(R.string.tp_send_gift_failed_message);

                    if (errors != null && errors.size() > 0) {
                        String[] mesList = errors.get(0).getMessage().split("|");
                        if (mesList.length == 3) {
                            errorTitle = mesList[0];
                            errorMessage = mesList[1];
                        } else if (mesList.length == 2) {
                            errorMessage = mesList[0];
                        }
                    }

                    getView().onPreValidateError(errorTitle, errorMessage);
                }
            }
        });
    }

    public void swipeMyCoupon(String partnerCode, String pin) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CODE, partnerCode);
        variables.put(CommonConstant.GraphqlVariableKeys.PIN, pin);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_swipe_coupon),
                CouponSwipeUpdateOuter.class,
                variables);
        mStartSendGift.clearRequest();
        mStartSendGift.addRequest(request);
        mStartSendGift.execute(new Subscriber<GraphqlResponse>() {
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
