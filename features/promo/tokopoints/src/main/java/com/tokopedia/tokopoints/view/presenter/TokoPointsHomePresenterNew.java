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
import com.tokopedia.tokopoints.view.contract.TokoPointsHomeContract;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.DynamicLinkResponse;
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.TokenDetailOuter;
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.model.TokoPointSumCouponOuter;
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.section.TokopointsSection;
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class TokoPointsHomePresenterNew extends BaseDaggerPresenter<TokoPointsHomeContract.View>
        implements TokoPointsHomeContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private GraphqlUseCase mGetTokoPointDetailUseCase;
    private GraphqlUseCase mRedeemCouponUseCase;
    private int selectedItem = 0;

    @Inject
    public TokoPointsHomePresenterNew(GraphqlUseCase getTokoPointDetailUseCase, GraphqlUseCase redeemCouponUseCase) {

        this.mGetTokoPointDetailUseCase = getTokoPointDetailUseCase;
        this.mRedeemCouponUseCase = redeemCouponUseCase;
    }


    @Override
    public void destroyView() {
        if (mGetTokoPointDetailUseCase != null) {
            mGetTokoPointDetailUseCase.unsubscribe();
        }

        if (mRedeemCouponUseCase != null) {
            mRedeemCouponUseCase.unsubscribe();
        }
    }

    @Override
    public void getTokoPointDetail() {
        getView().showLoading();
        mGetTokoPointDetailUseCase.clearRequest();

        //Main details
        GraphqlRequest request1 = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_tokopoint_detail_new),
                TokoPointDetailEntity.class, false);
        mGetTokoPointDetailUseCase.addRequest(request1);

        //Lucky egg
        GraphqlRequest request2 = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_lucky_egg_details),
                TokenDetailOuter.class, false);
        mGetTokoPointDetailUseCase.addRequest(request2);

        //Section
        GraphqlRequest request4 = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_homepage_section),
                TokopointsSectionOuter.class, false);
        mGetTokoPointDetailUseCase.addRequest(request4);

        mGetTokoPointDetailUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onError(e.getLocalizedMessage());

                if (getView() != null) getView().onFinishRendering();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                //Handling for main data
                if(getView() == null){
                    return;
                }

                getView().hideLoading();
                TokoPointDetailEntity data = graphqlResponse.getData(TokoPointDetailEntity.class);
                TokopointsSectionOuter dataSection = graphqlResponse.getData(TokopointsSectionOuter.class);
                if (data != null
                        && dataSection != null
                        && dataSection.getSectionContent() != null) {
                    getView().onSuccessResponse(data.getTokoPoints(), dataSection.getSectionContent().getSectionContent());
                }

                //handling for lucky egg data
                TokenDetailOuter tokenDetail = graphqlResponse.getData(TokenDetailOuter.class);
                if (tokenDetail != null
                        && tokenDetail.getTokenDetail() != null
                        && tokenDetail.getTokenDetail().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS) {
                    getView().onSuccessTokenDetail(tokenDetail.getTokenDetail());
                }

                if (getView() != null) getView().onFinishRendering();
            }
        });
    }

    @Override
    public void startValidateCoupon(final CatalogsValueEntity item) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_ID, item.getId());
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);
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
                int validateResponseCode;
                String message;
                String title;
                ValidateCouponBaseEntity validateCoupon = response.getData(ValidateCouponBaseEntity.class);

                if (validateCoupon != null && validateCoupon.getValidateCoupon() != null) {
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
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 0);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_tokopoint_redeem_coupon),
                com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity.class,
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
                if (redeemCouponBaseEntity != null && redeemCouponBaseEntity.getHachikoRedeem() != null) {
                    getView().showConfirmRedeemDialog(redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCta(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCode(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getTitle());
                } else {
                    String[] errorsMessage = response.getError(RedeemCouponBaseEntity.class).get(0).getMessage().split("\\|");
                    if (errorsMessage != null && errorsMessage.length > 0) {
                        String title = errorsMessage[0];
                        String desc = null;
                        int validateResponseCode = 0;

                        if (errorsMessage.length >= 2) {
                            desc = errorsMessage[1];
                        }
                        if (errorsMessage.length >= 3)
                            validateResponseCode = Integer.parseInt(errorsMessage[2]);
                        getView().showValidationMessageDialog(item, title, desc, validateResponseCode);

                    }
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

    public int getPagerSelectedItem() {
        return selectedItem;
    }

    public void setPagerSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }
}
