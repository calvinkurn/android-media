package com.tokopedia.promocheckout.detail.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.promocheckout.R;
import com.tokopedia.promocheckout.detail.model.ResponseDetailCoupon;
import com.tokopedia.promocheckout.detail.model.couponprevalidate.PromoCouponPrevalidateResponse;
import com.tokopedia.promocheckout.detail.model.couponredeem.PromoRedeemCouponResponse;
import com.tokopedia.promocheckout.detail.model.detailmodel.CouponDetailsResponse;
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class CheckoutCatalogDetailPresenter extends BaseDaggerPresenter<CheckoutCatalogDetailContract.View>
        implements CheckoutCatalogDetailContract.Presenter {
    //new apis
    private GraphqlUseCase mGetCouponDetail;
    GraphqlUseCase mValidateCouponUseCase;
    GraphqlUseCase mRedeemCouponUseCase;


    @Inject
    public CheckoutCatalogDetailPresenter(
            GraphqlUseCase getCouponDetail,
            GraphqlUseCase getValidateCoupon,
            GraphqlUseCase getRedeemCoupon
    ) {
        this.mGetCouponDetail = getCouponDetail;
        this.mValidateCouponUseCase=getValidateCoupon;
        this.mRedeemCouponUseCase=getRedeemCoupon;

    }

    @Override
    public void destroyView() {
        if (mGetCouponDetail != null) {
            mGetCouponDetail.unsubscribe();
        }}

    //    @Override
    public void startValidateCoupon(final HachikoCatalogDetail item) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("catalog_id", item.getId());
        variables.put("is_gift", 0);   //Never be a gift
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.promo_checkout_prevalidate_coupon),
                PromoCouponPrevalidateResponse.class, variables, false);
        mValidateCouponUseCase.clearRequest();
        mValidateCouponUseCase.addRequest(graphqlRequest);
        mValidateCouponUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("kamalio","error");
                //NA

                getView().showValidationMessageDialog(item, "kamal", "kumar",200);


            }

            @Override
            public void onNext(GraphqlResponse response) {
                int validateResponseCode;
                String message;
                String title;
                PromoCouponPrevalidateResponse validateCoupon = response.getData(PromoCouponPrevalidateResponse.class);

                if (validateCoupon != null && validateCoupon.getHachikoValidateRedeem() != null) {
                    validateResponseCode =200;
                    message = validateCoupon.getHachikoValidateRedeem().getMessageSuccess();
                    title = validateCoupon.getHachikoValidateRedeem().getMessageTitle();
                } else {
                    String[] errorsMessage = response.getError(PromoCouponPrevalidateResponse.class).get(0).getMessage().split("\\|");
                    title = errorsMessage[0];
                    message = errorsMessage[1];
                    validateResponseCode = Integer.parseInt(errorsMessage[2]);
                }

                getView().showValidationMessageDialog(item, title, message, validateResponseCode);
            }
      });
    }

    //    @Override
    public void redeemCoupon(String promoCode, String cta) {
//        Map<String, Object> variables = new HashMap<>();
//        variables.put(CommonConstant.GraphqlVariableKeys.PROMO_CODE, promoCode);
//
//        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
//                R.raw.tp_gql_tokopoint_apply_coupon),
//                com.tokopedia.tokopoints.view.model.ApplyCouponBaseEntity.class,
//                variables, false);
//        mSaveCouponUseCase.clearRequest();
//        mSaveCouponUseCase.addRequest(request);
//        mSaveCouponUseCase.execute(new Subscriber<GraphqlResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                RouteManager.route(getView().getActivityContext(), cta);
//            }
//
//            @Override
//            public void onNext(GraphqlResponse saveCoupon) {
//                RouteManager.route(getView().getActivityContext(), cta);
//            }
//        });
    }

    //    @Override
    public void startSaveCoupon(HachikoCatalogDetail item) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("catalog_id", item.getId());
        variables.put("is_gift", 0);     //Never be a gift

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.promo_checkout_redeem_coupon),
                PromoRedeemCouponResponse.class,
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
                PromoRedeemCouponResponse redeemCouponBaseEntity = response.getData(PromoRedeemCouponResponse.class);
                if (redeemCouponBaseEntity != null && redeemCouponBaseEntity.getHachikoRedeem() != null) {
                    getView().showConfirmRedeemDialog(redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCta(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getCode(),
                            redeemCouponBaseEntity.getHachikoRedeem().getCoupons().get(0).getTitle());
                } else {
                    String[] errorsMessage = response.getError(PromoRedeemCouponResponse.class).get(0).getMessage().split("\\|");
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
    public void getCatalogDetail(String uniqueCatalogCode,int catalog_id) {
        getView().showLoader();
        Map<String, Object> variables = new HashMap<>();
        variables.put("slug", uniqueCatalogCode);
        variables.put("catalog_id",catalog_id);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.promo_checkout_catalog_detail),
                CouponDetailsResponse.class,
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
                if (getView() != null) {
                    getView().hideLoader();
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (getView() != null) {
                    getView().hideLoader();
                    CouponDetailsResponse data = response.getData(CouponDetailsResponse.class);
                    getView().populateDetail(data.getHachikoCatalogDetail());
                }
            }
        });
    }

    @Override
    public void fetchLatestStatus(List<Integer> catalogsIds) {
//        Map<String, Object> variables = new HashMap<>();
//        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_IDS, catalogsIds);
//
//        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
//                R.raw.tp_gql_catalog_status),
//                CatalogStatusOuter.class,
//                variables, false);
//        mRefreshCatalogStatus.clearRequest();
//        mRefreshCatalogStatus.addRequest(request);
//        mRefreshCatalogStatus.execute(new Subscriber<GraphqlResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                //NA No handling require.
//            }
//
//            @Override
//            public void onNext(GraphqlResponse response) {
//                CatalogStatusOuter data = response.getData(CatalogStatusOuter.class);
//                if (data != null && data.getCatalogStatus() != null) {
//                    //For detail page we only interested in one item
//                    getView().refreshCatalog(data.getCatalogStatus().getCatalogs().get(0));
//                }
//            }
//        });
    }

    //    @Override
    public void navigateToWebView(String url) {
        getView().openWebView(url);
    }

    //    @Override
    public void showRedeemCouponDialog(String cta, String code, String title) {
        getView().showRedeemCouponDialog(cta, code, title);
    }

//    private void handlePointQuery(TokoPointDetailEntity pointDetailEntity) {
    //Handling the point
//        if (pointDetailEntity == null
//                || pointDetailEntity.getTokoPoints() == null
//                || pointDetailEntity.getTokoPoints().getResultStatus() == null
//                || pointDetailEntity.getTokoPoints().getStatus() == null
//                || pointDetailEntity.getTokoPoints().getStatus().getPoints() == null) {
//            getView().onErrorPoint(null);
//        } else {
//            if (pointDetailEntity.getTokoPoints().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS) {
//                getView().onSuccessPoints(pointDetailEntity.getTokoPoints().getStatus().getPoints().getRewardStr());
//            }
//        }
}
