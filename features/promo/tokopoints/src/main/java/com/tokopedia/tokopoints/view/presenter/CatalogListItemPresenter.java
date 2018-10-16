package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogListItemContract;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.model.CatalogListingOuter;
import com.tokopedia.tokopoints.view.model.CatalogStatusOuter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.PreValidateRedeemBase;
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class CatalogListItemPresenter extends BaseDaggerPresenter<CatalogListItemContract.View>
        implements CatalogListItemContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private GraphqlUseCase mGetHomePageData;
    private GraphqlUseCase mSaveCouponUseCase;
    private GraphqlUseCase mValidateCouponUseCase;
    private GraphqlUseCase mRedeemCouponUseCase;
    private GraphqlUseCase mFetchCatalogStatusUseCase;
    private GraphqlUseCase mStartSendGift;

    @Inject
    public CatalogListItemPresenter(GraphqlUseCase getHomePageData,
                                    GraphqlUseCase saveCouponUseCase,
                                    GraphqlUseCase validateCouponUseCase,
                                    GraphqlUseCase fetchCatalogStatusUseCase,
                                    GraphqlUseCase startSendGift,
                                    GraphqlUseCase redeemCouponUseCase) {
        this.mGetHomePageData = getHomePageData;
        this.mSaveCouponUseCase = saveCouponUseCase;
        this.mValidateCouponUseCase = validateCouponUseCase;
        this.mRedeemCouponUseCase = redeemCouponUseCase;
        this.mFetchCatalogStatusUseCase = fetchCatalogStatusUseCase;
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

        if (mFetchCatalogStatusUseCase != null) {
            mFetchCatalogStatusUseCase.unsubscribe();
        }

        if (mStartSendGift != null) {
            mStartSendGift.unsubscribe();
        }
    }

    @Override
    public void getCatalog(int categoryId, int sortId) {
        mGetHomePageData.clearRequest();
        getView().showLoader();

        //Adding request for main query
        Map<String, Object> variablesMain = new HashMap<>();
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE, 1);  //Default page size always will be 1
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE_SIZE, CommonConstant.PAGE_SIZE);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.SORT_ID, sortId);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.CATEGORY_ID, categoryId);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.POINTS_RANGE, 0); //Point range will be zero for all catalog

        GraphqlRequest graphqlRequestMain = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.tp_gql_catalog_listing),
                CatalogListingOuter.class,
                variablesMain);
        mGetHomePageData.addRequest(graphqlRequestMain);


        mGetHomePageData.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                //handling the catalog listing and tabs
                CatalogListingOuter catalogListingOuter = graphqlResponse.getData(CatalogListingOuter.class);
                if (catalogListingOuter != null) {
                    getView().populateCatalog(catalogListingOuter.getCatalog().getCatalogs());
                } else {
                    getView().showError();
                }
            }
        });
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
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);     //Never be a gift

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
    public void fetchLatestStatus(List<Integer> catalogsIds) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_IDS, catalogsIds);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_catalog_status),
                CatalogStatusOuter.class,
                variables);
        mFetchCatalogStatusUseCase.clearRequest();
        mFetchCatalogStatusUseCase.addRequest(request);
        mFetchCatalogStatusUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
            }

            @Override
            public void onNext(GraphqlResponse response) {
                CatalogStatusOuter data = response.getData(CatalogStatusOuter.class);
                if (data != null && data.getCatalogStatus() != null) {
                    getView().refreshCatalog(data.getCatalogStatus().getCatalogs());
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
}
