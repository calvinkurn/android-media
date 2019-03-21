package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoCouponComponent;
import com.tokopedia.loyalty.di.component.PromoCouponComponent;
import com.tokopedia.loyalty.di.module.PromoCouponViewModule;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.adapter.CouponListAdapter;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.presenter.IPromoCouponPresenter;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_LIST;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.TRAIN_STRING;


/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponFragment extends BaseDaggerFragment
        implements IPromoCouponView, CouponListAdapter.CouponListAdapterListener, RefreshHandler.OnRefreshHandlerListener {

    @Inject
    IPromoCouponPresenter dPresenter;
    @Inject
    LoyaltyModuleRouter loyaltyModuleRouter;

    private ProgressDialog progressDialog;

    private RecyclerView couponListRecyclerView;

    private RefreshHandler refreshHandler;

    private ViewGroup mainView;

    private CouponListAdapter adapter;

    private ChooseCouponListener listener;

    private static final String PLATFORM_KEY = "PLATFORM_KEY";

    private static final String CATEGORY_KEY = "CATEGORY_KEY";

    private static final String PLATFORM_PAGE_KEY = "PLATFORM_PAGE_KEY";

    private static final String ADDITIONAL_DATA_KEY = "ADDITIONAL_DATA_KEY";

    private static final String DIGITAL_CATEGORY_ID = "DIGI_CATEGORY_ID";

    private static final String DIGITAL_PRODUCT_ID = "DIGI_PRODUCT_ID";

    private static final String TRAIN_RESERVATION_ID = "TRAIN_RESERVATION_ID";

    private static final String TRAIN_RESERVATION_CODE = "TRAIN_RESERVATION_CODE";

    private static final String CART_ID_KEY = "CART_ID_KEY";

    private static final String CHECKOUT = "checkoutdata";

    @Override
    protected String getScreenName() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_coupon, container, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        refreshHandler.startRefresh();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        mainView = view.findViewById(R.id.main_view_coupon);
        couponListRecyclerView = view.findViewById(R.id.coupon_recycler_view);
        couponListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initInjector() {
        PromoCouponComponent promoCouponComponent = DaggerPromoCouponComponent.builder()
                .baseAppComponent((BaseAppComponent) getComponent(BaseAppComponent.class))
                .promoCouponViewModule(new PromoCouponViewModule(this))
                .build();
        promoCouponComponent.inject(this);
    }

    @Override
    public void renderCouponListDataResult(List<CouponData> couponData) {
        refreshHandler.finishRefresh();
        adapter = new CouponListAdapter(couponData, this);
        couponListRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderErrorGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }


    @Override
    public void renderErrorHttpGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }

    @Override
    public void sendTrackingOnCheckTrainVoucherError(String errorMessage) {
        if (getActivity() instanceof LoyaltyModuleRouter) {
            ((LoyaltyModuleRouter) getActivity())
                    .trainSendTrackingOnCheckVoucherCodeError(errorMessage);
        }
    }

    @Override
    public void sendEventDigitalEventTracking(Context context, String text, String failmsg) {
        loyaltyModuleRouter.sendEventDigitalEventTracking(context, text, failmsg);
    }


    @Override
    public void renderErrorNoConnectionGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }

    @Override
    public void renderErrorTimeoutConnectionGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }

    @Override
    public void couponDataNoResult() {
        couponDataNoResult(getString(R.string.loyalty_default_empty_coupons_title),
                getString(R.string.loyalty_default_empty_coupons_subtitle));
    }

    @Override
    public void couponDataNoResult(String title, String subTitle) {
        if (getArguments().getString(PLATFORM_KEY, "")
                .equalsIgnoreCase(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING)) {
            switch (getArguments().getString(PLATFORM_PAGE_KEY, "")) {
                case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                    listener.sendAnalyticsImpressionCouponEmptyCartListPage();
                    break;
                case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                    listener.sendAnalyticsImpressionCouponEmptyShipmentPage();
                    break;
            }
        }
        NetworkErrorHelper.showEmptyState(getActivity(), mainView,
                title,
                subTitle,
                null,
                R.drawable.ic_coupon_image_big, null);
    }

    @Override
    public void receiveResult(CouponViewModel couponViewModel) {
        if (getArguments().getString(PLATFORM_KEY, "")
                .equalsIgnoreCase(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING)) {
            switch (getArguments().getString(PLATFORM_PAGE_KEY, "")) {
                case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                    listener.sendAnalyticsOnCouponItemClickedCartListPageSuccess();
                    break;
                case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                    listener.sendAnalyticsOnCouponItemClickedCartShipmentPageSuccess();
                    break;
            }
        }
        listener.onCouponSuccess(couponViewModel.getCode(),
                couponViewModel.getMessage(),
                couponViewModel.getAmount(),
                couponViewModel.getTitle());
    }

    @Override
    public void receiveDigitalResult(CouponViewModel couponViewModel) {
        listener.onDigitalCouponSuccess(couponViewModel.getCode(),
                couponViewModel.getMessage(),
                couponViewModel.getTitle(),
                couponViewModel.getRawDiscount(),
                couponViewModel.getRawCashback());
    }

    @Override
    public void onErrorFetchCouponList(String errorMessage) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(getActivity(),
                mainView,
                errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                });
    }

    @Override
    public void couponError() {
        if (getArguments().getString(PLATFORM_KEY, "")
                .equalsIgnoreCase(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING)) {
            switch (getArguments().getString(PLATFORM_PAGE_KEY, "")) {
                case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                    listener.sendAnalyticsOnCouponItemClickedCartListPageFailed();
                    break;
                case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                    listener.sendAnalyticsOnCouponItemClickedCartShipmentPageFailed();
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSnackbarError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {
        progressDialog.show();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }

    @Override
    public String getCategoryId() {
        return getArguments().getString(CATEGORY_KEY);
    }

    public static PromoCouponFragment newInstance(
            String platformString, String platformPageString, String additionalDataString,
            String categoryKey, String cartIdString,
            int categoryId, int productId) {
        PromoCouponFragment fragment = new PromoCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platformString);
        bundle.putString(PLATFORM_PAGE_KEY, platformPageString);
        bundle.putString(CATEGORY_KEY, categoryKey);
        bundle.putString(CART_ID_KEY, cartIdString);
        bundle.putInt(DIGITAL_CATEGORY_ID, categoryId);
        bundle.putInt(DIGITAL_PRODUCT_ID, productId);
        bundle.putString(ADDITIONAL_DATA_KEY, additionalDataString);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Deprecated
    public static PromoCouponFragment newInstanceEvent(
            String platform, String categoryKey, int categoryId, int productId
    ) {
        PromoCouponFragment fragment = new PromoCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platform);
        bundle.putString(CATEGORY_KEY, categoryKey);
        bundle.putInt(DIGITAL_CATEGORY_ID, categoryId);
        bundle.putInt(DIGITAL_PRODUCT_ID, productId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PromoCouponFragment newInstanceTrain(
            String platform, String categoryKey, String reservationId, String reservtionCode
    ) {
        PromoCouponFragment fragment = new PromoCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platform);
        bundle.putString(CATEGORY_KEY, categoryKey);
        bundle.putString(TRAIN_RESERVATION_ID, reservationId);
        bundle.putString(TRAIN_RESERVATION_CODE, reservtionCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onVoucherChosen(CouponData data) {
        switch (getArguments().getString(PLATFORM_PAGE_KEY, "")) {
            case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                listener.sendAnalyticsOnCouponItemClickedCartListPage();
                listener.sendAnalyticsOnCouponItemClicked(data.getTitle());
                break;
            case IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                listener.sendAnalyticsOnCouponItemClickedCartShipmentPage();
                listener.sendAnalyticsOnCouponItemClicked(data.getTitle());
                break;
        }

        adapter.clearError();
        loyaltyModuleRouter.sendEventCouponChosen(getActivity(),data.getTitle());
        String platformString = getArguments().getString(PLATFORM_KEY, "");
        if (platformString.equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING)) {
            dPresenter.submitDigitalVoucher(data, getArguments().getString(CATEGORY_KEY, ""));
        } else if (platformString.equalsIgnoreCase(TRAIN_STRING)) {
            dPresenter.submitTrainVoucher(data,
                    getArguments().getString(TRAIN_RESERVATION_ID),
                    getArguments().getString(TRAIN_RESERVATION_CODE));
        } else if (platformString.equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EVENT_STRING)
                || platformString.equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DEALS_STRING)) {
            String jsonbody = getActivity().getIntent().getStringExtra(CHECKOUT);
            dPresenter.parseAndSubmitEventVoucher(jsonbody, data, getArguments().getString(PLATFORM_KEY));
        } else if (platformString.equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.FLIGHT_STRING)) {
            dPresenter.submitFlightVoucher(data, getArguments().getString(CART_ID_KEY));
        } else {
            dPresenter.processCheckMarketPlaceCartListPromoCode(
                    getActivity(), data, getArguments().getString(ADDITIONAL_DATA_KEY, "")
            );
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ChooseCouponListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ChooseCouponListener) context;
    }

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryGetCouponListErrorHandlerListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshHandler.startRefresh();
            }
        };
    }

    @Override
    public void onRefresh(View view) {
        if (refreshHandler.isRefreshing())
            if (getArguments().getString(PLATFORM_KEY, "").equals(
                    IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EVENT_STRING)
                    || getArguments().getString(PLATFORM_KEY, "").equals(
                    IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DEALS_STRING)) {
                dPresenter.processGetEventCouponList(getArguments().getInt(DIGITAL_CATEGORY_ID), getArguments().getInt(DIGITAL_PRODUCT_ID));
            } else {
                dPresenter.processGetCouponList(getArguments().getString(PLATFORM_KEY));
            }
    }

    @Override
    public void onDestroy() {
        dPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            listener.sendAnalyticsScreenNameCoupon();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) listener.sendAnalyticsScreenNameCoupon();
    }

    public interface ChooseCouponListener {

        void onCouponSuccess(
                String promoCode,
                String promoMessage,
                String amount,
                String couponTitle);

        void onDigitalCouponSuccess(
                String promoCode,
                String promoMessage,
                String CouponTitle,
                long discountAmount,
                long cashbackAmount);

        void sendAnalyticsOnCouponItemClickedCartListPage();

        void sendAnalyticsOnCouponItemClickedCartListPageSuccess();

        void sendAnalyticsOnCouponItemClickedCartListPageFailed();

        void sendAnalyticsOnCouponItemClicked(String couponName);

        void sendAnalyticsOnCouponItemClickedCartShipmentPage();

        void sendAnalyticsOnCouponItemClickedCartShipmentPageSuccess();

        void sendAnalyticsOnCouponItemClickedCartShipmentPageFailed();

        void sendAnalyticsImpressionCouponEmptyCartListPage();

        void sendAnalyticsImpressionCouponEmptyShipmentPage();

        void sendAnalyticsScreenNameCoupon();

    }


}

