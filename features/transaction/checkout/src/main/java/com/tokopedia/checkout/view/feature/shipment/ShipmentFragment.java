package com.tokopedia.checkout.view.feature.shipment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.checkout.CartConstant;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressFormActivity;
import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.feature.shipment.di.DaggerShipmentComponent;
import com.tokopedia.checkout.view.feature.shipment.di.ShipmentComponent;
import com.tokopedia.checkout.view.feature.shipment.di.ShipmentModule;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.feature.shippingoptions.CourierBottomsheet;
import com.tokopedia.checkout.view.feature.webview.CheckoutWebViewActivity;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.logisticcommon.LogisticCommonConstant;
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticinsurance.view.InsuranceTnCActivity;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutConstantKt;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipProd;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheet;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheetListener;
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheet;
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheetListener;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View,
        ShipmentContract.AnalyticsActionListener, ShipmentAdapterActionListener, CourierBottomsheet.ActionListener,
        ShippingDurationBottomsheetListener, ShippingCourierBottomsheetListener {

    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    private static final int REQUEST_CODE_COURIER_PINPOINT = 13;
    private static final int REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS = 55;
    private static final String SHIPMENT_TRACE = "mp_shipment";

    public static final String ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO = "ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO";
    public static final String ARG_AUTO_APPLY_PROMO_CODE_APPLIED = "ARG_AUTO_APPLY_PROMO_CODE_APPLIED";
    public static final String ARG_IS_FROM_PDP = "ARG_IS_FROM_PDP";
    private static final String NO_PINPOINT_ETD = "Belum Pinpoint";
    private static final String EXTRA_STATE_SHIPMENT_SELECTION = "EXTRA_STATE_SHIPMENT_SELECTION";
    private static final String DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION = "LAST_CHOOSE_COURIER_ITEM_POSITION";
    private static final String DATA_STATE_LAST_CHOOSEN_SERVICE_ID = "DATA_STATE_LAST_CHOOSEN_SERVICE_ID";
    private static final String EXTRA_EXISTING_LOCATION = "EXTRA_EXISTING_LOCATION";

    private RecyclerView rvShipment;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayout llNetworkErrorView;
    private CardView cardFooter;
    private TextView tvTotalPayment;
    private TextView tvSelectPaymentMethod;
    private TkpdProgressDialog progressDialogNormal;
    // For regular shipment
    private CourierBottomsheet courierBottomsheet;
    // For shipment recommendation
    private ShippingDurationBottomsheet shippingDurationBottomsheet;
    private ShippingCourierBottomsheet shippingCourierBottomsheet;

    private PerformanceMonitoring shipmentTracePerformance;
    private boolean isShipmentTraceStopped;

    @Inject
    ShipmentAdapter shipmentAdapter;
    @Inject
    ShipmentContract.Presenter shipmentPresenter;
    @Inject
    ShipmentDataConverter shipmentDataConverter;
    @Inject
    RatesDataConverter ratesDataConverter;
    @Inject
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    @Inject
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    @Inject
    ICheckoutModuleRouter checkoutModuleRouter;
    @Inject
    TrackingPromoCheckoutUtil trackingPromoCheckoutUtil;
    @Inject
    CheckoutAnalyticsPurchaseProtection mTrackerPurchaseProtection;

    private HashSet<ShipmentSelectionStateData> shipmentSelectionStateDataHashSet = new HashSet<>();

    public static ShipmentFragment newInstance(String defaultSelectedTabPromo,
                                               boolean isAutoApplyPromoCodeApplied,
                                               boolean isFromPdp) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, defaultSelectedTabPromo);
        bundle.putBoolean(ARG_AUTO_APPLY_PROMO_CODE_APPLIED, isAutoApplyPromoCodeApplied);
        bundle.putBoolean(ARG_IS_FROM_PDP, isFromPdp);
        ShipmentFragment shipmentFragment = new ShipmentFragment();
        shipmentFragment.setArguments(bundle);

        return shipmentFragment;
    }

    @Override
    protected void initInjector() {
        ShipmentComponent component = DaggerShipmentComponent.builder()
                .cartComponent(getComponent(CartComponent.class))
                .shipmentModule(new ShipmentModule(this))
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .promoCheckoutModule(new PromoCheckoutModule())
                .build();
        component.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shipmentPresenter.attachView(this);
        shipmentTracePerformance = PerformanceMonitoring.start(SHIPMENT_TRACE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shipmentPresenter.detachView();
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        if (!shipmentSelectionStateDataHashSet.isEmpty()) {
            state.putParcelableArrayList(
                    EXTRA_STATE_SHIPMENT_SELECTION, new ArrayList<>(shipmentSelectionStateDataHashSet
                    ));
        }
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        ArrayList<ShipmentSelectionStateData> shipmentSelectionStateData =
                savedState.getParcelableArrayList(EXTRA_STATE_SHIPMENT_SELECTION);
        if (shipmentSelectionStateData != null) {
            shipmentSelectionStateDataHashSet = new HashSet<>(shipmentSelectionStateData);
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment;
    }

    @Override
    protected void initView(View view) {
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        rvShipment = view.findViewById(R.id.rv_shipment);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        cardFooter = view.findViewById(R.id.card_footer);
        tvTotalPayment = view.findViewById(R.id.tv_total_payment);
        tvSelectPaymentMethod = view.findViewById(R.id.tv_select_payment_method);
        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        ((SimpleItemAnimator) rvShipment.getItemAnimator()).setSupportsChangeAnimations(false);
        rvShipment.addItemDecoration(new CartItemDecoration());

        tvSelectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipmentAdapter.checkDropshipperValidation();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment());
        } else {
            swipeToRefresh.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (shipmentPresenter.getShipmentCartItemModelList() != null) {
            outState.putParcelable(CartPromoSuggestion.class.getSimpleName(), shipmentPresenter.getCartPromoSuggestion());
            outState.putParcelable(RecipientAddressModel.class.getSimpleName(), shipmentPresenter.getRecipientAddressModel());
            outState.putParcelableArrayList(ShipmentCartItemModel.class.getSimpleName(), new ArrayList<>(shipmentPresenter.getShipmentCartItemModelList()));
            outState.putParcelable(ShipmentDonationModel.class.getSimpleName(), shipmentPresenter.getShipmentDonationModel());
            outState.putParcelable(ShipmentCostModel.class.getSimpleName(), shipmentPresenter.getShipmentCostModel());
            outState.putInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION, shipmentAdapter.getLastChooseCourierItemPosition());
            outState.putInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID, shipmentAdapter.getLastServiceId());
            outState.putParcelable(PromoData.class.getSimpleName(), shipmentAdapter.getPromoData());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelableArrayList(ShipmentCartItemModel.class.getSimpleName()) != null) {
                shipmentPresenter.setCartPromoSuggestion(savedInstanceState.getParcelable(CartPromoSuggestion.class.getSimpleName()));
                shipmentPresenter.setRecipientAddressModel(savedInstanceState.getParcelable(RecipientAddressModel.class.getSimpleName()));
                shipmentPresenter.setShipmentCartItemModelList(savedInstanceState.getParcelableArrayList(ShipmentCartItemModel.class.getSimpleName()));
                shipmentPresenter.setShipmentDonationModel(savedInstanceState.getParcelable(ShipmentDonationModel.class.getSimpleName()));
                shipmentPresenter.setShipmentCostModel(savedInstanceState.getParcelable(ShipmentCostModel.class.getSimpleName()));
                shipmentAdapter.setLastChooseCourierItemPosition(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION));
                shipmentAdapter.setLastServiceId(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID));
                shipmentAdapter.addPromoVoucherData(savedInstanceState.getParcelable(PromoData.class.getSimpleName()));
                renderCheckoutPage(true, isOneClickShipment());
            } else {
                shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment());
            }
        }
    }

    @Override
    protected void setViewListener() {

    }

    private boolean isOneClickShipment() {
        return getArguments() != null && getArguments().getBoolean(ARG_IS_FROM_PDP);
    }

    private void initRecyclerViewData(PromoData promoData,
                                      CartPromoSuggestion cartPromoSuggestion,
                                      RecipientAddressModel recipientAddressModel,
                                      List<ShipmentCartItemModel> shipmentCartItemModelList,
                                      ShipmentDonationModel shipmentDonationModel,
                                      ShipmentCostModel shipmentCostModel,
                                      boolean isInitialRender) {
        shipmentAdapter.clearData();
        rvShipment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShipment.setAdapter(shipmentAdapter);
        shipmentAdapter.addPromoVoucherData(promoData);
        if (promoData != null) {
            cartPromoSuggestion.setVisible(false);
            shipmentAdapter.addPromoSuggestionData(cartPromoSuggestion);
        } else {
            shipmentAdapter.addPromoSuggestionData(cartPromoSuggestion);
        }
        if (recipientAddressModel != null) {
            shipmentAdapter.addAddressShipmentData(recipientAddressModel);
        }
        shipmentAdapter.addCartItemDataList(shipmentCartItemModelList);
        StringBuilder cartIdsStringBuilder = new StringBuilder();
        for (int i = 0; i < shipmentCartItemModelList.size(); i++) {
            if (shipmentCartItemModelList.get(i).getCartItemModels() != null &&
                    shipmentCartItemModelList.get(i).getCartItemModels().size() > 0) {
                for (CartItemModel cartItemModel : shipmentCartItemModelList.get(i).getCartItemModels()) {
                    cartIdsStringBuilder.append(cartItemModel.getCartId());
                    cartIdsStringBuilder.append(",");
                }
            }
        }
        cartIdsStringBuilder.replace(cartIdsStringBuilder.lastIndexOf(","), cartIdsStringBuilder.lastIndexOf(",") + 1, "");
        shipmentAdapter.setCartIds(cartIdsStringBuilder.toString());

        shipmentAdapter.addShipmentDonationModel(shipmentDonationModel);
        shipmentAdapter.addShipmentCostData(shipmentCostModel);
        shipmentAdapter.updateShipmentSellerCashbackVisibility();
        if (isInitialRender) {
            if (!shipmentSelectionStateDataHashSet.isEmpty()) {
                for (ShipmentSelectionStateData shipmentSelectionStateData : shipmentSelectionStateDataHashSet) {
                    if (!shipmentSelectionStateData.getCourierItemData().getEstimatedTimeDelivery().equalsIgnoreCase(NO_PINPOINT_ETD)
                            && shipmentSelectionStateData.getPosition() < shipmentAdapter.getItemCount()) {
                        shipmentAdapter.setSelectedCourier(shipmentSelectionStateData.getPosition(),
                                shipmentSelectionStateData.getCourierItemData());
                    }
                }
            }
        }
        cardFooter.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showInitialLoading() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void hideInitialLoading() {
        swipeToRefresh.setRefreshing(false);
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void showLoading() {
        if (!progressDialogNormal.isProgress()) progressDialogNormal.showDialog();
    }

    @Override
    public void hideLoading() {
        if (progressDialogNormal.isProgress()) progressDialogNormal.dismiss();
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void showToastNormal(String message) {
        if (getView() != null && getActivity() != null) {
            ToasterNormal.make(getView(), message, BaseToaster.LENGTH_SHORT)
                    .setAction(getActivity().getString(R.string.label_action_snackbar_close), view -> {

                    }).show();
        }
    }

    @Override
    public void showToastError(String message) {
        if (getView() != null && getActivity() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getActivity().getString(R.string.default_request_error_unknown);
            }
            if (shipmentAdapter == null || shipmentAdapter.getItemCount() == 0) {
                renderErrorPage(message);
            } else {
                Snackbar snackbar = Snackbar.make(getView(), message, BaseToaster.LENGTH_SHORT);
                TextView snackbarTextView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                Button snackbarActionButton = snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
                snackbar.getView().setBackground(ContextCompat.getDrawable(getView().getContext(), com.tokopedia.design.R.drawable.bg_snackbar_error));
                snackbarTextView.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.font_black_secondary_54));
                snackbarActionButton.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.font_black_primary_70));
                snackbarTextView.setMaxLines(5);
                snackbar.setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        }
    }

    @Override
    public void renderErrorPage(String message) {
        rvShipment.setVisibility(View.GONE);
        cardFooter.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        llNetworkErrorView.setVisibility(View.GONE);
                        rvShipment.setVisibility(View.VISIBLE);
                        shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment());
                    }
                });

    }

    @Override
    public void renderCheckoutPage(boolean isInitialRender, boolean isFromPdp) {
        PromoData promoData = shipmentAdapter.getPromoData();
        CartPromoSuggestion cartPromoSuggestion = shipmentPresenter.getCartPromoSuggestion();
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        if (recipientAddressModel != null) {
            recipientAddressModel.setFromPdp(isFromPdp);
        }
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentPresenter.getShipmentCartItemModelList();
        ShipmentDonationModel shipmentDonationModel = shipmentPresenter.getShipmentDonationModel();
        ShipmentCostModel shipmentCostModel = shipmentPresenter.getShipmentCostModel();

        initRecyclerViewData(
                promoData, cartPromoSuggestion, recipientAddressModel,
                shipmentCartItemModelList, shipmentDonationModel, shipmentCostModel, isInitialRender
        );
    }

    @Override
    public void stopTrace() {
        if (!isShipmentTraceStopped) {
            shipmentTracePerformance.stopTrace();
            isShipmentTraceStopped = true;
        }
    }

    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        shipmentPresenter.processCheckout(shipmentAdapter.getPromoData().getPromoCodeSafe(), isOneClickShipment());
    }

    @Override
    public void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
            CartShipmentAddressFormData cartShipmentAddressFormData, boolean needToRefreshItemList
    ) {
        if (getActivity() != null && needToRefreshItemList) {
            showToastError(getActivity().getString(R.string.error_message_checkout_failed));
        }
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderErrorDataHasChangedAfterCheckout(List<ShipmentCartItemModel> oldShipmentCartItemModelList) {
        if (getActivity() != null) {
            showToastError(getActivity().getString(R.string.error_message_checkout_failed));
        }
        initRecyclerViewData(shipmentAdapter.getPromoData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                oldShipmentCartItemModelList,
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                false
        );
    }

    @Override
    public void renderDataChanged() {
        PromoData promoData = shipmentAdapter.getPromoData();
        CartPromoSuggestion cartPromoSuggestion = shipmentPresenter.getCartPromoSuggestion();
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentPresenter.getShipmentCartItemModelList();
        ShipmentDonationModel shipmentDonationModel = shipmentPresenter.getShipmentDonationModel();
        ShipmentCostModel shipmentCostModel = shipmentPresenter.getShipmentCostModel();

        initRecyclerViewData(
                promoData, cartPromoSuggestion, recipientAddressModel,
                shipmentCartItemModelList, shipmentDonationModel, shipmentCostModel, false
        );
    }

    @Override
    public void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData shipmentAddressFormData) {
        Intent intent;
        if (shipmentAddressFormData.getKeroDiscomToken() != null &&
                shipmentAddressFormData.getKeroUnixTime() != 0) {
            Token token = new Token();
            token.setUt(shipmentAddressFormData.getKeroUnixTime());
            token.setDistrictRecommendation(shipmentAddressFormData.getKeroDiscomToken());

            intent = CartAddressChoiceActivity.createInstance(getActivity(),
                    CartAddressChoiceActivity.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS, token);
        } else {
            intent = CartAddressChoiceActivity.createInstance(getActivity(),
                    CartAddressChoiceActivity.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS);
        }

        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Deprecated
    @Override
    public void renderThanksTopPaySuccess(String message) {
        showToastNormal(getString(R.string.message_payment_succeded_transaction_module));
        startActivity(checkoutModuleRouter.checkoutModuleRouterGetTransactionSummaryIntent());
        if (getActivity() != null) {
            checkoutModuleRouter.checkoutModuleRouterResetBadgeCart();
            getActivity().finish();
        }
    }

    @Override
    public void renderCheckoutCartSuccess(CheckoutData checkoutData) {
        shipmentPresenter.setCheckoutData(checkoutData);
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setRedirectUrl(checkoutData.getRedirectUrl());
        paymentPassData.setTransactionId(checkoutData.getTransactionId());
        paymentPassData.setPaymentId(checkoutData.getPaymentId());
        paymentPassData.setCallbackSuccessUrl(checkoutData.getCallbackSuccessUrl());
        paymentPassData.setCallbackFailedUrl(checkoutData.getCallbackFailedUrl());
        paymentPassData.setQueryString(checkoutData.getQueryString());
        startActivityForResult(
                TopPayActivity.createInstance(getActivity(), paymentPassData),
                TopPayActivity.REQUEST_CODE);
    }

    @Override
    public void renderCheckoutCartError(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodSuccess() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranSuccess();
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodFailed() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess();
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodCourierNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranCourierNotComplete();
    }

    @Override
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData) {
        setAppliedPromoCodeData(promoCodeCartListData);
    }

    private void setAppliedPromoCodeData(PromoCodeCartListData promoCodeCartListData) {
        PromoData promoData = new PromoData.Builder()
                .typePromo(PromoData.CREATOR.getTYPE_VOUCHER())
                .promoCode(promoCodeCartListData.getDataVoucher().getCode())
                .description(promoCodeCartListData.getDataVoucher().getMessageSuccess())
                .amount(promoCodeCartListData.getDataVoucher().getCashbackAmount())
                .state(TickerCheckoutUtilKt.mapToStatePromoCheckout(promoCodeCartListData.getDataVoucher().getState()))
                .title(promoCodeCartListData.getDataVoucher().getTitleDescription())
                .build();
        updateAppliedPromo(promoData);
    }

    @Override
    public void renderCheckPromoCodeFromCourierSuccess(PromoCodeCartListData promoCodeCartListData,
                                                       int itemPosition, boolean noToast) {
        if (!noToast && !shipmentAdapter.isCourierPromoStillExist()) {
            showToastNormal(promoCodeCartListData.getDataVoucher().getMessageSuccess());
        }
        setAppliedPromoCodeData(promoCodeCartListData);
        setCourierPromoApplied(itemPosition);
    }

    @Override
    public void setCourierPromoApplied(int itemPosition) {
        shipmentAdapter.setCourierPromoApplied(itemPosition);
    }

    @Override
    public void setPromoData(CartShipmentAddressFormData cartShipmentAddressFormData) {
        PromoData.Builder builder = new PromoData.Builder();
        if (cartShipmentAddressFormData.getAutoApplyData() != null && cartShipmentAddressFormData.getAutoApplyData().isSuccess()) {
            AutoApplyData autoApplyData = cartShipmentAddressFormData.getAutoApplyData();
            builder.typePromo(autoApplyData.getIsCoupon() == PromoData.CREATOR.getVALUE_COUPON() ?
                    PromoData.CREATOR.getTYPE_COUPON() : PromoData.CREATOR.getTYPE_VOUCHER())
                    .description(autoApplyData.getMessageSuccess())
                    .amount(autoApplyData.getDiscountAmount())
                    .promoCode(autoApplyData.getCode())
                    .state(TickerCheckoutUtilKt.mapToStatePromoCheckout(autoApplyData.getState()))
                    .title(autoApplyData.getTitleDescription())
                    .build();
            sendAnalyticsOnViewPromoAutoApply();
        } else {
            builder.state(TickerCheckoutView.State.EMPTY);
        }
        shipmentAdapter.addPromoVoucherData(builder.build());
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showToastFailedTickerPromo(String text) {
        showToastError(text);
        //scroll to ticker section
        rvShipment.scrollToPosition(0);
    }

    @Override
    public void renderErrorCheckPromoCodeFromSuggestedPromo(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showRedCloseSnackbar(view, message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderErrorCheckPromoShipmentData(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
        shipmentAdapter.updatePromo(null);
        shipmentAdapter.resetCourierPromoState();
    }

    @Override
    public void renderCheckPromoShipmentDataSuccess(PromoCodeCartShipmentData checkPromoCodeCartShipmentResult) {
        PromoData promoData = new PromoData.Builder()
                .typePromo(checkPromoCodeCartShipmentResult.getDataVoucher().getIsCoupon() == PromoData.CREATOR.getVALUE_COUPON()
                        ? PromoData.CREATOR.getTYPE_COUPON() : PromoData.CREATOR.getTYPE_VOUCHER())
                .promoCode(checkPromoCodeCartShipmentResult.getDataVoucher().getPromoCode())
                .description(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherPromoDesc())
                .amount(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherAmount())
                .state(TickerCheckoutUtilKt.mapToStatePromoCheckout(checkPromoCodeCartShipmentResult.getDataVoucher().getState()))
                .title(checkPromoCodeCartShipmentResult.getDataVoucher().getTitleDescription())
                .build();
        shipmentAdapter.updateItemPromoVoucher(promoData);
        shipmentAdapter.updatePromo(checkPromoCodeCartShipmentResult.getDataVoucher());
        if (getArguments() != null && getArguments().getBoolean(ARG_AUTO_APPLY_PROMO_CODE_APPLIED)) {
            sendAnalyticsOnViewPromoAutoApply();
        } else {
            if (checkPromoCodeCartShipmentResult.getDataVoucher().getIsCoupon() == 1) {
                sendAnalyticsOnViewPromoManualApply("coupon");
            } else {
                sendAnalyticsOnViewPromoManualApply("voucher");
            }
        }
    }

    @Override
    public void renderEditAddressSuccess(String latitude, String longitude) {
        shipmentAdapter.updateShipmentDestinationPinpoint(latitude, longitude);
        int position = shipmentAdapter.getLastChooseCourierItemPosition();

        ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position);
        if (shipmentCartItemModel != null) {
            if (!shipmentCartItemModel.isUseCourierRecommendation()) {
                courierBottomsheet = null;
                onChooseShipment(position, shipmentCartItemModel, shipmentPresenter.getRecipientAddressModel());
            } else {
                shippingCourierBottomsheet = null;
                shippingDurationBottomsheet = null;
                RecipientAddressModel recipientAddressModel;
                if (shipmentPresenter.getRecipientAddressModel() != null) {
                    recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
                } else {
                    recipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                }
                onChangeShippingDuration(shipmentCartItemModel, recipientAddressModel,
                        shipmentCartItemModel.getShopShipmentList(), position);
            }
        }
    }

    @Override
    public void renderCancelAutoApplyCouponSuccess() {
        shipmentAdapter.updatePromo(null);
        shipmentPresenter.setHasDeletePromoAfterChecKPromoCodeFinal(true);
        shipmentAdapter.resetCourierPromoState();
        shipmentAdapter.cancelAutoApplyCoupon();
        shipmentAdapter.notifyItemChanged(shipmentAdapter.getShipmentCostPosition());
    }

    @Override
    public void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition) {
        checkCourierPromo(courierItemData, itemPosition);
        shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition).setStateLoadingCourierState(false);
        shipmentAdapter.setSelectedCourier(itemPosition, courierItemData);
        onNeedUpdateViewItem(itemPosition);
    }

    @Override
    public void renderCourierStateFailed(int itemPosition) {
        shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition).setStateLoadingCourierState(false);
        onNeedUpdateViewItem(itemPosition);
    }

    @Override
    public void cancelAllCourierPromo() {
        shipmentAdapter.cancelAllCourierPromo();
    }

    @Override
    public void navigateToSetPinpoint(String message, LocationPass locationPass) {
        sendAnalyticsOnClickEditPinPointErrorValidation(message);
        if (getView() != null) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_rectangle, getView().findViewById(R.id.toast_layout));

            TextView tvMessage = layout.findViewById(R.id.tv_message);
            tvMessage.setText(message);

            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
        if (getActivity() != null) {
            Intent intent = checkoutModuleRouter.getGeolocationIntent(getActivity(), locationPass);
            startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT);
        }
    }

    public void sendAnalyticsOnClickEditPinPointErrorValidation(String message) {
        checkoutAnalyticsChangeAddress.eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(message);
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public void sendAnalyticsCheckoutStep2(Map<String, Object> stringObjectMap, String transactionId) {
        checkoutAnalyticsCourierSelection.enhancedECommerceGoToCheckoutStep2(stringObjectMap, transactionId);
        checkoutAnalyticsCourierSelection.flushEnhancedECommerceGoToCheckoutStep2();
    }

    @Override
    public void sendAnalyticsOnClickChooseOtherAddressShipment() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickPilihAlamatLain();
    }

    @Override
    public void sendAnalyticsOnClickChooseToMultipleAddressShipment() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickKirimKeBanyakAlamat();
    }

    @Override
    public void sendAnalyticsOnClickTopDonation() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickTopDonasi();
    }

    @Override
    public void sendAnalyticsOnClickChangeAddress() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat();
    }

    @Override
    public void sendAnalyticsOnClickChooseCourierSelection() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickSelectCourier();
    }

    @Override
    public void renderChangeAddressSuccess(RecipientAddressModel selectedAddress) {
        if (shipmentAdapter.hasAppliedPromoCode()) {
            shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment());
        }
        shipmentPresenter.setRecipientAddressModel(selectedAddress);
        shipmentAdapter.updateSelectedAddress(selectedAddress);
        courierBottomsheet = null;
        onCartDataDisableToCheckout(null);
    }

    @Override
    public List<DataCheckoutRequest> generateNewCheckoutRequest
            (List<ShipmentCartItemModel> shipmentCartItemModelList) {
        ShipmentAdapter.RequestData requestData = shipmentAdapter.getRequestData(null, shipmentCartItemModelList);
        return requestData.getCheckoutRequestData();
    }

    @Override
    public ShipmentDataConverter getShipmentDataConverter() {
        return shipmentDataConverter;
    }

    private void updateAppliedPromo(PromoData cartPromo) {
        shipmentPresenter.setCouponStateChanged(true);
        shipmentAdapter.updateItemPromoVoucher(cartPromo);
        if (shipmentAdapter.hasSetAllCourier()) {
            ShipmentAdapter.RequestData requestData =
                    shipmentAdapter.getRequestData(null, null);
            shipmentPresenter.setPromoCodeCartShipmentRequestData(requestData.getPromoRequestData());
            shipmentPresenter.checkPromoShipment(cartPromo.getPromoCodeSafe(), isOneClickShipment());

            shipmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TopPayActivity.REQUEST_CODE) {
            onResultFromPayment(resultCode);
        } else if ((requestCode == REQUEST_CHOOSE_PICKUP_POINT)
                && resultCode == Activity.RESULT_OK) {
            onResultFromRequestCodeCourierOptions(requestCode, data);
        } else if (requestCode == CartAddressChoiceActivity.REQUEST_CODE) {
            onResultFromRequestCodeAddressOptions(resultCode, data);
        } else if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            onResultFromRequestCodeLoyalty(resultCode, data);
        } else if (requestCode == REQUEST_CODE_COURIER_PINPOINT) {
            onResultFromCourierPinpoint(resultCode, data);
        } else if (requestCode == REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS) {
            onResultFromMultipleAddress(resultCode, data);
        }
    }

    private void onResultFromMultipleAddress(int resultCode, Intent data) {
        if (resultCode == MultipleAddressFormActivity.RESULT_CODE_RELOAD_CART_PAGE) {
            if (getActivity() != null) {
                getActivity().setResult(ShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                getActivity().finish();
            }
        } else if (data != null) {
            PromoData promoData = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_DATA);
            CartPromoSuggestion cartPromoSuggestion = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_SUGGESTION_DATA);
            RecipientAddressModel recipientAddressModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_RECIPIENT_ADDRESS_DATA);
            ArrayList<ShipmentCartItemModel> shipmentCartItemModels = data.getParcelableArrayListExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_CART_TEM_LIST_DATA);
            ShipmentCostModel shipmentCostModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_COST_SATA);
            ShipmentDonationModel shipmentDonationModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_DONATION_DATA);
            shipmentPresenter.processReloadCheckoutPageFromMultipleAddress(
                    promoData, cartPromoSuggestion, recipientAddressModel, shipmentCartItemModels,
                    shipmentCostModel, shipmentDonationModel, isOneClickShipment()
            );
        } else {
            shipmentSelectionStateDataHashSet.clear();
            shipmentPresenter.processInitialLoadCheckoutPage(true, isOneClickShipment());
        }
    }

    private void onResultFromCourierPinpoint(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            LocationPass locationPass = data.getExtras().getParcelable(LogisticCommonConstant.EXTRA_EXISTING_LOCATION);
            if (locationPass != null) {
                int index = shipmentAdapter.getLastChooseCourierItemPosition();
                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(index);
                shipmentPresenter.editAddressPinpoint(locationPass.getLatitude(), locationPass.getLongitude(),
                        shipmentCartItemModel, locationPass);
            }
        } else {
            shipmentAdapter.setLastServiceId(0);
        }
    }

    private void onResultFromPayment(int resultCode) {
        if (getActivity() != null) {
            if (resultCode == TopPayActivity.PAYMENT_CANCELLED || resultCode == TopPayActivity.PAYMENT_FAILED) {
                shipmentPresenter.processInitialLoadCheckoutPage(true, isOneClickShipment());
            } else {
                getActivity().setResult(resultCode);
                getActivity().finish();
            }
        }
    }

    private void onResultFromRequestCodeLoyalty(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                PromoData promoData = bundle.getParcelable(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA());
                if (promoData != null) {
                    updateAppliedPromo(promoData);
                }
            }
        }
    }

    private void onResultFromRequestCodeCourierOptions(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSE_PICKUP_POINT:
//                Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
//                shipmentAdapter.setPickupPoint(pickupBooth);
                break;
            default:
                break;
        }
    }

    private void onResultFromRequestCodeAddressOptions(int resultCode, Intent data) {
        switch (resultCode) {
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS:
                RecipientAddressModel currentAddress = shipmentAdapter.getAddressShipmentData();
                RecipientAddressModel newAddress = data.getParcelableExtra(
                        CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);

                if (currentAddress != null && newAddress != null) {
                    if (!currentAddress.getId().equals(newAddress.getId()) ||
                            !currentAddress.getAddressName().equals(newAddress.getAddressName()) ||
                            !currentAddress.getStreet().equals(newAddress.getStreet()) ||
                            !currentAddress.getRecipientName().equals(newAddress.getRecipientName()) ||
                            !currentAddress.getRecipientPhoneNumber().equals(newAddress.getRecipientPhoneNumber()) ||
                            !String.valueOf(currentAddress.getLatitude()).equals(String.valueOf(newAddress.getLatitude())) ||
                            !String.valueOf(currentAddress.getLongitude()).equals(String.valueOf(newAddress.getLongitude())) ||
                            !currentAddress.getPostalCode().equals(newAddress.getPostalCode()) ||
                            !currentAddress.getDestinationDistrictId().equals(newAddress.getDestinationDistrictId()) ||
                            !currentAddress.getCityId().equals(newAddress.getCityId()) ||
                            !currentAddress.getProvinceId().equals(newAddress.getProvinceId())) {
                        newAddress.setFromPdp(isOneClickShipment());
                        shipmentPresenter.setDataChangeAddressRequestList(shipmentAdapter.getRequestData(newAddress, null).getChangeAddressRequestData());
                        shipmentPresenter.changeShippingAddress(newAddress, isOneClickShipment());
                    }
                }
                break;

            case CartAddressChoiceActivity.RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS:
                shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment());
                break;

            case Activity.RESULT_CANCELED:
                if (getActivity() != null && data == null && shipmentPresenter.getShipmentCartItemModelList() == null) {
                    getActivity().finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onChangeAddress() {
        sendAnalyticsOnClickChangeAddress();
        sendAnalyticsOnClickChooseOtherAddressShipment();
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                shipmentPresenter.getRecipientAddressModel(),
                CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST);
        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void onSendToMultipleAddress(RecipientAddressModel recipientAddressModel, String cartIds) {
        sendAnalyticsOnClickChooseToMultipleAddressShipment();
        Intent intent = MultipleAddressFormActivity.createInstance(getActivity(),
                shipmentAdapter.getPromoData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getShipmentDonationModel(),
                cartIds
        );
        startActivityForResult(intent, REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS);
    }

    @Override
    public void resetTotalPrice() {
        shipmentAdapter.updateCheckoutButtonData("-");
    }

    private ShipmentDetailData getShipmentDetailData(ShipmentCartItemModel shipmentCartItemModel,
                                                     RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData shipmentDetailData;
        ShipmentDetailData oldShipmentDetailData = null;
        if (recipientAddressModel == null) {
            recipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
        }
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            oldShipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        }
        shipmentDetailData = ratesDataConverter.getShipmentDetailData(shipmentCartItemModel,
                recipientAddressModel);
        if (oldShipmentDetailData != null && oldShipmentDetailData.getSelectedCourier() != null) {
            shipmentDetailData.setSelectedCourier(oldShipmentDetailData.getSelectedCourier());
        }

        return shipmentDetailData;
    }

    @Override
    public void onChooseShipment(int position, ShipmentCartItemModel shipmentCartItemModel,
                                 RecipientAddressModel recipientAddressModel) {
        sendAnalyticsOnClickChooseCourierSelection();

        RecipientAddressModel currentAddress;
        if (recipientAddressModel != null) {
            currentAddress = recipientAddressModel;
        } else {
            currentAddress = shipmentCartItemModel.getRecipientAddressModel();
        }
        ShipmentDetailData shipmentDetailData = getShipmentDetailData(shipmentCartItemModel,
                currentAddress);
        if (shipmentDetailData != null) {
            showCourierChoiceBottomSheet(shipmentDetailData, shipmentCartItemModel.getShopShipmentList(),
                    currentAddress, position);
        }
    }

    @Override
    public void onChooseShipmentDuration(ShipmentCartItemModel shipmentCartItemModel,
                                         RecipientAddressModel recipientAddressModel,
                                         List<ShopShipment> shopShipmentList,
                                         int cartPosition) {
        sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation();
        showShippingDurationBottomsheet(shipmentCartItemModel, recipientAddressModel, shopShipmentList, cartPosition);
    }

    private void showCourierChoiceBottomSheet(ShipmentDetailData shipmentDetailData,
                                              List<ShopShipment> shopShipmentList,
                                              RecipientAddressModel recipientAddressModel,
                                              int position) {
        if (courierBottomsheet == null || position != courierBottomsheet.getLastCartItemPosition() ||
                shipmentDetailData.getSelectedCourier() == null) {
            courierBottomsheet = new CourierBottomsheet(getActivity(), shipmentDetailData,
                    shopShipmentList, recipientAddressModel, position);
        }
        if (shipmentDetailData.getSelectedCourier() != null) {
            courierBottomsheet.setSelectedCourier(shipmentDetailData.getSelectedCourier());
        }
        courierBottomsheet.setListener(this);
        courierBottomsheet.setOnShowListener(dialogInterface -> {
            sendAnalyticsOnBottomShetCourierSelectionShow(shopShipmentList);
            sendAnalyticsScreenName(ConstantTransactionAnalytics.ScreenName.SELECT_COURIER);
            sendAnalyticsOnImpressionCourierSelectionShow();
            courierBottomsheet.updateHeight();

        });
        courierBottomsheet.show();
    }

    public void sendAnalyticsOnBottomShetCourierSelectionShow(List<ShopShipment> shopShipmentList) {
        for (ShopShipment shopShipment : shopShipmentList) {
            for (ShipProd shipProd : shopShipment.getShipProds()) {
                checkoutAnalyticsCourierSelection.eventViewCourierSelectionImpressionCourierOption(
                        shopShipment.getShipName(), shipProd.getShipProdName()
                );
            }
        }
    }

    @Override
    public void sendAnalyticsOnImpressionCourierSelectionShow() {
        checkoutAnalyticsCourierSelection.eventViewAtcCourierSelectionImpressionCourierSelection();
    }

    @Override
    public void sendAnalyticsOnClickUsePromoCodeAndCoupon() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickGunakanKodePromoAtauKupon();
    }

    @Override
    public void sendAnalyticsOnClickCancelUsePromoCodeAndCouponBanner() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickXOnBannerPromoCodeCode();
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickXFromGunakanKodePromoAtauKupon();
    }

    @Override
    public void sendAnalyticsOnClickShipmentCourierItem(String agent, String service) {
        checkoutAnalyticsCourierSelection.eventViewCourierSelectionClickCourierOption(agent, service);
    }

    @Override
    public void sendAnalyticsOnClickSubtotal() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickSubtotal();
    }

    @Override
    public void sendAnalyticsOnClickChecklistShipmentRecommendationDuration(String duration) {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickChecklistPilihDurasiPengiriman(duration);
    }

    @Override
    public void sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(String courier) {
        checkoutAnalyticsCourierSelection.eventViewCourierCourierSelectionViewPreselectedCourierOption(courier);
    }

    @Override
    public void sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(int shippingProductId) {
        checkoutAnalyticsCourierSelection.eventViewPreselectedCourierOption(shippingProductId);
    }

    @Override
    public void sendAnalyticsOnDisplayDurationThatContainPromo(boolean isCourierPromo, String duration) {
        checkoutAnalyticsCourierSelection.eventViewDuration(isCourierPromo, duration);
    }

    @Override
    public void sendAnalyticsOnDisplayLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId) {
        checkoutAnalyticsCourierSelection.eventViewCourierOption(isCourierPromo, shippingProductId);
    }

    @Override
    public void sendAnalyticsOnClickDurationThatContainPromo(boolean isCourierPromo, String duration) {
        checkoutAnalyticsCourierSelection.eventClickChecklistPilihDurasiPengiriman(isCourierPromo, duration);
    }

    @Override
    public void sendAnalyticsOnClickLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId) {
        checkoutAnalyticsCourierSelection.eventClickChangeCourierOption(isCourierPromo, shippingProductId);
    }

    @Override
    public void sendAnalyticsOnClickChangeCourierShipmentRecommendation() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickUbahKurir();
    }

    @Override
    public void sendAnalyticsOnClickSelectedCourierShipmentRecommendation(String courierName) {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickChangeCourierOption(courierName);
    }

    public void sendAnalyticsOnClickChangeDurationShipmentRecommendation() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickUbahDurasi();
    }

    @Override
    public void sendAnalyticsOnClickButtonDoneShowCaseDurationShipmentRecommendation() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickCtaButton();
    }

    @Override
    public void sendAnalyticsOnClickCheckBoxDropShipperOption() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickDropship();
    }

    @Override
    public void sendAnalyticsOnClickCheckBoxInsuranceOption() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickAsuransiPengiriman();
    }

    @Override
    public void sendAnalyticsScreenName(String screenName) {
        checkoutAnalyticsCourierSelection.sendScreenName(getActivity(), screenName);
    }

    @Override
    public void sendAnalyticsCourierNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickBuyCourierSelectionClickPilihMetodePembayaranCourierNotComplete();
    }

    @Override
    public void sendAnalyticsDropshipperNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickBuyCourierSelectionClickBayarFailedDropshipper();
    }

    @Override
    public void sendAnalyticsOnCourierChanged(String agent, String service) {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickUbahKurirAgentService(agent, service);
    }

    @Override
    public void sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickButtonDurasiPengiriman();
    }

    @Override
    public void sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickXPadaDurasiPengiriman();
    }

    @Override
    public void sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickXPadaKurirPengiriman();
    }

    @Override
    public void sendAnalyticsOnViewPromoAutoApply() {
        checkoutAnalyticsCourierSelection.eventViewPromoAutoApply();
    }

    @Override
    public void sendAnalyticsOnViewPromoManualApply(String type) {
        checkoutAnalyticsCourierSelection.eventViewPromoManualApply(type);
    }

    @Override
    public void onChoosePickupPoint(RecipientAddressModel addressAdapterData) {

    }

    @Override
    public void onClearPickupPoint(RecipientAddressModel addressAdapterData) {

    }

    @Override
    public void onEditPickupPoint(RecipientAddressModel addressAdapterData) {

    }

    @Override
    public void onTotalPaymentChange(String totalPayment) {
        tvTotalPayment.setText(totalPayment);
    }

    @Override
    public void onFinishChoosingShipment(List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData) {
        shipmentPresenter.setPromoCodeCartShipmentRequestData(promoRequestData);
        if (shipmentAdapter.getPromoData() != null &&
                shipmentAdapter.hasAppliedPromoCode()) {
            shipmentPresenter.checkPromoShipment(shipmentAdapter.getPromoData().getPromoCodeSafe(), isOneClickShipment());
        }
    }

    @Override
    public void updateCheckoutRequest(List<DataCheckoutRequest> checkoutRequestData) {
        shipmentPresenter.setDataCheckoutRequestList(checkoutRequestData);
    }

    @Override
    public void onRemovePromoCode() {
        shipmentPresenter.cancelAutoApplyCoupon();
    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion,
                                                   int position) {
        shipmentPresenter.processCheckPromoCodeFromSuggestedPromo(cartPromoSuggestion.getPromoCode(), isOneClickShipment());
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion,
                                                        int position) {
        cartPromoSuggestion.setVisible(false);
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(PromoData cartPromo,
                                                  int position) {
        trackingPromoCheckoutUtil.checkoutClickUseTickerPromoOrCoupon();
        startActivityForResult(
                checkoutModuleRouter
                        .checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
                                true, "", isOneClickShipment(), TrackingPromoCheckoutConstantKt.getFROM_CHECKOUT()
                        ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
        );

    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(PromoData cartPromo,
                                                     int position) {
        shipmentPresenter.cancelAutoApplyCoupon();
        if (isToogleYearEndPromoOn()) {
            shipmentAdapter.cancelAllCourierPromo();
        }
    }

    @Override
    public void onCartPromoTrackingImpression(PromoData cartPromo, int position) {
        trackingPromoCheckoutUtil.checkoutImpressionTicker(cartPromo.getDescription());
    }

    @Override
    public void onCartPromoTrackingCancelled(PromoData cartPromo, int position) {
        sendAnalyticsOnClickCancelUsePromoCodeAndCouponBanner();

    }

    @Override
    public void onCartDataEnableToCheckout() {
        shipmentAdapter.updateCheckoutButtonData(null);
    }

    @Override
    public void onCartDataDisableToCheckout(String message) {
        shipmentAdapter.updateCheckoutButtonData(null);
    }

    @Override
    public void onDropshipperValidationResult(boolean result, Object shipmentData,
                                              int errorPosition) {
        if (shipmentData == null && result) {
            shipmentPresenter.processCheckShipmentPrepareCheckout(shipmentAdapter.getPromoData().getPromoCodeSafe(), isOneClickShipment());
            shipmentPresenter.processSaveShipmentState();
        } else if (shipmentData != null && !result) {
            sendAnalyticsDropshipperNotComplete();
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment.smoothScrollToPosition(errorPosition);
            }
            showToastError(getActivity().getString(R.string.message_error_courier_not_selected_or_dropshipper_empty));
            onCartDataDisableToCheckout(null);
            ((ShipmentCartItemModel) shipmentData).setStateDropshipperHasError(true);
            shipmentAdapter.notifyItemChanged(errorPosition);
        } else if (shipmentData == null) {
            sendAnalyticsCourierNotComplete();
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment.smoothScrollToPosition(errorPosition);
            }
            showToastError(getActivity().getString(R.string.message_error_courier_not_selected_or_dropshipper_empty));
        }
    }

    @Override
    public void onClickDetailPromo(PromoData data, int position) {
        trackingPromoCheckoutUtil.checkoutClickTicker(data.getDescription());
        if (data.getTypePromo() == PromoData.CREATOR.getTYPE_COUPON()) {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutDetailIntentWithCode(data.getPromoCodeSafe(),
                    true, isOneClickShipment(), TrackingPromoCheckoutConstantKt.getFROM_CHECKOUT()), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        } else {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutListIntentWithCode(data.getPromoCodeSafe(),
                    true, isOneClickShipment(), TrackingPromoCheckoutConstantKt.getFROM_CHECKOUT()), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data,
                                                   int position) {

    }

    @Override
    public void onShipmentItemClick(CourierItemData courierItemData,
                                    RecipientAddressModel recipientAddressModel,
                                    int cartItemPosition, boolean isChangeCourier) {
        if (isChangeCourier) {
            sendAnalyticsOnCourierChanged(courierItemData.getName(),
                    courierItemData.getShipmentItemDataType());
        } else {
            sendAnalyticsOnClickShipmentCourierItem(courierItemData.getName(),
                    courierItemData.getShipmentItemDataType());
        }
        ShipmentSelectionStateData shipmentSelectionStateData = new ShipmentSelectionStateData();
        shipmentSelectionStateData.setPosition(cartItemPosition);
        shipmentSelectionStateData.setCourierItemData(courierItemData);
        shipmentSelectionStateDataHashSet.add(shipmentSelectionStateData);
        if (courierItemData.isUsePinPoint()
                && (recipientAddressModel.getLatitude() == null ||
                recipientAddressModel.getLatitude().equalsIgnoreCase("0")
                || recipientAddressModel.getLongitude() == null ||
                recipientAddressModel.getLongitude().equalsIgnoreCase("0"))) {
            setPinpoint(cartItemPosition);
        } else {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.setSelectedCourier(cartItemPosition, courierItemData);
            shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
        }
    }

    @Override
    public void onInsuranceChecked(final int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(new Runnable() {
                @Override
                public void run() {
                    shipmentAdapter.updateShipmentCostModel();
                    shipmentAdapter.updateItemAndTotalCost(position);
                    shipmentAdapter.updateInsuranceTncVisibility();
                }
            });
        } else {
            shipmentAdapter.updateShipmentCostModel();
            shipmentAdapter.updateItemAndTotalCost(position);
            shipmentAdapter.updateInsuranceTncVisibility();
        }
    }

    @Override
    public void onNeedUpdateViewItem(final int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(new Runnable() {
                @Override
                public void run() {
                    shipmentAdapter.notifyItemChanged(position);
                }
            });
        } else {
            shipmentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onSubTotalCartItemClicked(int position) {
        sendAnalyticsOnClickSubtotal();
    }

    @Override
    public void onInsuranceTncClicked() {
        Intent intent = CheckoutWebViewActivity.newInstance(getContext(),
                CartConstant.TERM_AND_CONDITION_URL,
                getString(R.string.title_activity_checkout_tnc_webview));
        startActivity(intent);
    }

    @Override
    public void onNeedUpdateRequestData() {
        shipmentAdapter.checkHasSelectAllCourier(false);
    }

    @Override
    public void onDropshipCheckedForTrackingAnalytics() {
        sendAnalyticsOnClickCheckBoxDropShipperOption();
    }

    @Override
    public void onInsuranceCheckedForTrackingAnalytics() {
        sendAnalyticsOnClickCheckBoxInsuranceOption();

    }

    @Override
    public void onChoosePaymentMethodButtonClicked() {
        shipmentAdapter.checkDropshipperValidation();
    }

    @Override
    public void onDonationChecked(boolean checked) {
        shipmentAdapter.updateDonation(checked);
        if (checked) sendAnalyticsOnClickTopDonation();
    }

    @Override
    public void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel) {
        shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
    }

    @Override
    protected String getScreenName() {
        if (isOneClickShipment()) {
            return ConstantTransactionAnalytics.ScreenName.ONE_CLICK_SHIPMENT;
        } else {
            return ConstantTransactionAnalytics.ScreenName.CHECKOUT;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        sendAnalyticsScreenName(getScreenName());
    }

    @Override
    public void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                          CourierItemData recommendedCourier,
                                          RecipientAddressModel recipientAddressModel,
                                          int cartItemPosition, int selectedServiceId,
                                          String selectedServiceName, boolean flagNeedToSetPinpoint,
                                          boolean hasCourierPromo) {
        sendAnalyticsOnClickChecklistShipmentRecommendationDuration(selectedServiceName);
        // Has courier promo means that one of duration has promo, not always current selected duration.
        // It's for analytics purpose
        if (shippingCourierViewModels.size() > 0) {
            sendAnalyticsOnClickDurationThatContainPromo(
                    shippingCourierViewModels.get(0).getServiceData().getIsPromo() == 1,
                    shippingCourierViewModels.get(0).getServiceData().getServiceName()
            );
        }
        if (flagNeedToSetPinpoint) {
            // If instant courier and has not set pinpoint
            if (getActivity() != null) {
                Tooltip tooltip = new Tooltip(getActivity());
                tooltip.setTitle(getActivity().getString(R.string.label_no_courier_bottomsheet_title));
                tooltip.setDesc(getActivity().getString(R.string.label_hardcoded_shipping_duration_info));
                tooltip.setTextButton(getActivity().getString(R.string.label_no_courier_bottomsheet_button));
                tooltip.setIcon(R.drawable.ic_dropshipper);
                tooltip.getBtnAction().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tooltip.dismiss();
                        shipmentAdapter.setLastServiceId(selectedServiceId);
                        setPinpoint(cartItemPosition);
                    }
                });
                tooltip.show();
            }
        } else if (recommendedCourier == null) {
            // If there's no recommendation, user choose courier manually
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition);
            List<ShopShipment> shopShipments = shipmentCartItemModel.getShopShipmentList();
            onChangeShippingCourier(shippingCourierViewModels, recipientAddressModel, shipmentCartItemModel, shopShipments, cartItemPosition);
        } else {
            if (recommendedCourier.isUsePinPoint()
                    && (recipientAddressModel.getLatitude() == null ||
                    recipientAddressModel.getLatitude().equalsIgnoreCase("0") ||
                    recipientAddressModel.getLongitude() == null ||
                    recipientAddressModel.getLongitude().equalsIgnoreCase("0"))) {
                setPinpoint(cartItemPosition);
            } else {
                sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(recommendedCourier.getName());
                if (isToogleYearEndPromoOn()) {
                    sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(recommendedCourier.getShipperProductId());
                }
                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.setSelectedCourier(cartItemPosition, recommendedCourier);
                shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
                shipmentAdapter.setShippingCourierViewModels(shippingCourierViewModels, recommendedCourier, cartItemPosition);
                checkCourierPromo(recommendedCourier, cartItemPosition);
            }
        }
    }

    private void checkCourierPromo(CourierItemData courierItemData, int itemPosition) {
        if (isToogleYearEndPromoOn() && !TextUtils.isEmpty(courierItemData.getPromoCode())) {
            String promoCode = courierItemData.getPromoCode();
            if (!shipmentAdapter.isCourierPromoStillExist() && shipmentAdapter.getPromoData() != null &&
                    !TextUtils.isEmpty(shipmentAdapter.getPromoData().getPromoCodeSafe())) {
                promoCode = shipmentAdapter.getPromoData().getPromoCodeSafe();
                shipmentPresenter.processCheckPromoCodeFromSelectedCourier(promoCode, itemPosition, true, isOneClickShipment());
            } else {
                shipmentPresenter.processCheckPromoCodeFromSelectedCourier(
                        promoCode, itemPosition, false, isOneClickShipment()
                );
            }
        }
    }

    @Override
    public boolean checkCourierPromoStillExist() {
        return shipmentAdapter.isCourierPromoStillExist();
    }

    @Override
    public void onNoCourierAvailable(String message) {
        if (getActivity() != null) {
            Tooltip tooltip = new Tooltip(getActivity());
            tooltip.setTitle(getActivity().getString(R.string.label_no_courier_bottomsheet_title));
            tooltip.setDesc(message);
            tooltip.setTextButton(getActivity().getString(R.string.label_no_courier_bottomsheet_button));
            tooltip.setIcon(R.drawable.ic_dropshipper);
            tooltip.getBtnAction().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tooltip.dismiss();
                }
            });
            tooltip.show();
        }
    }

    @Override
    public void onShippingDurationButtonCloseClicked() {
        sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration();
    }

    @Override
    public void onShippingDurationButtonShowCaseDoneClicked() {
        sendAnalyticsOnClickButtonDoneShowCaseDurationShipmentRecommendation();
    }

    @Override
    public void onShowDurationListWithCourierPromo(boolean isCourierPromo, String duration) {
        if (isToogleYearEndPromoOn()) {
            sendAnalyticsOnDisplayDurationThatContainPromo(isCourierPromo, duration);
        }
    }

    @Override
    public void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
                                 int cartItemPosition, boolean hasCourierPromo, boolean isPromoCourier, boolean isNeedPinpoint) {
        sendAnalyticsOnClickLogisticThatContainPromo(isPromoCourier, courierItemData.getShipperProductId());
        if (isNeedPinpoint || (courierItemData.isUsePinPoint() && (recipientAddressModel.getLatitude() == null ||
                recipientAddressModel.getLatitude().equalsIgnoreCase("0") ||
                recipientAddressModel.getLongitude() == null ||
                recipientAddressModel.getLongitude().equalsIgnoreCase("0")))) {
            setPinpoint(cartItemPosition);
        } else {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.setSelectedCourier(cartItemPosition, courierItemData);
            shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
            checkCourierPromo(courierItemData, cartItemPosition);
        }
    }

    @Override
    public void onCourierShipmentRecpmmendationCloseClicked() {
        sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier();
    }

    private void setPinpoint(int cartItemPosition) {
        shipmentAdapter.setLastChooseCourierItemPosition(cartItemPosition);
        LocationPass locationPass = new LocationPass();
        if (shipmentAdapter.getAddressShipmentData() != null) {
            locationPass.setCityName(shipmentAdapter.getAddressShipmentData().getCityName());
            locationPass.setDistrictName(shipmentAdapter.getAddressShipmentData().getDestinationDistrictName());
        } else {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition);
            if (shipmentCartItemModel != null) {
                RecipientAddressModel updatedRecipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                if (updatedRecipientAddressModel != null) {
                    locationPass.setCityName(updatedRecipientAddressModel.getCityName());
                    locationPass.setDistrictName(updatedRecipientAddressModel.getDestinationDistrictName());
                }
            }
        }
        Intent intent = checkoutModuleRouter.getGeolocationIntent(getActivity(), locationPass);
        startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT);
    }

    @Override
    public void onChangeShippingDuration(ShipmentCartItemModel shipmentCartItemModel,
                                         RecipientAddressModel recipientAddressModel,
                                         List<ShopShipment> shopShipmentList,
                                         int cartPosition) {
        sendAnalyticsOnClickChangeDurationShipmentRecommendation();
        showShippingDurationBottomsheet(shipmentCartItemModel, recipientAddressModel, shopShipmentList, cartPosition);
    }

    private void showShippingDurationBottomsheet(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, List<ShopShipment> shopShipmentList, int cartPosition) {
        ShipmentDetailData shipmentDetailData = getShipmentDetailData(shipmentCartItemModel,
                recipientAddressModel);
        if (shipmentDetailData != null) {
            shippingDurationBottomsheet = ShippingDurationBottomsheet.newInstance(
                    shipmentDetailData, shipmentAdapter.getLastServiceId(), shopShipmentList,
                    recipientAddressModel, cartPosition);
            shippingDurationBottomsheet.setShippingDurationBottomsheetListener(this);

            if (getActivity() != null) {
                shippingDurationBottomsheet.show(getActivity().getSupportFragmentManager(), null);
            }
        }
    }

    @Override
    public void onChangeShippingCourier(List<ShippingCourierViewModel> shippingCourierViewModels,
                                        RecipientAddressModel recipientAddressModel,
                                        ShipmentCartItemModel shipmentCartItemModel,
                                        List<ShopShipment> shopShipmentList,
                                        int cartPosition) {
        sendAnalyticsOnClickChangeCourierShipmentRecommendation();
        if (shippingCourierViewModels == null || shippingCourierViewModels.size() == 0 &&
                shipmentPresenter.getShippingCourierViewModelsState(cartPosition) != null) {
            shippingCourierViewModels = shipmentPresenter.getShippingCourierViewModelsState(cartPosition);
        }
        if (shipmentPresenter.getHasDeletePromoAfterChecKPromoCodeFinal()) {
            shippingCourierBottomsheet = ShippingCourierBottomsheet.newInstance(
                    null, recipientAddressModel, cartPosition);
            shippingCourierBottomsheet.setShippingCourierBottomsheetListener(this);
            reloadCourier(shipmentCartItemModel, cartPosition, shopShipmentList);
        } else {
            shippingCourierBottomsheet = ShippingCourierBottomsheet.newInstance(
                    shippingCourierViewModels, recipientAddressModel, cartPosition);
            shippingCourierBottomsheet.setShippingCourierBottomsheetListener(this);
            if (isToogleYearEndPromoOn() && shippingCourierViewModels != null) {
                checkHasCourierPromo(shippingCourierViewModels);
            }
        }

        if (getActivity() != null) {
            shippingCourierBottomsheet.show(getActivity().getSupportFragmentManager(), null);
        }
    }

    private void reloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList) {
        if (shipmentCartItemModel.getSelectedShipmentDetailData().getShopId() == null) {
            shipmentCartItemModel.getSelectedShipmentDetailData().setShopId(String.valueOf(shipmentCartItemModel.getShopId()));
        }
        shipmentPresenter.processGetCourierRecommendation(
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId(),
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId(),
                cartPosition,
                shipmentCartItemModel.getSelectedShipmentDetailData(),
                shipmentCartItemModel, shopShipmentList, false);
    }

    @Override
    public void onRetryReloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList) {
        reloadCourier(shipmentCartItemModel, cartPosition, shopShipmentList);
    }

    private void checkHasCourierPromo(List<ShippingCourierViewModel> shippingCourierViewModels) {
        boolean hasCourierPromo = false;
        for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
            if (!TextUtils.isEmpty(shippingCourierViewModel.getProductData().getPromoCode())) {
                hasCourierPromo = true;
                break;
            }
        }
        if (hasCourierPromo) {
            for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
                sendAnalyticsOnDisplayLogisticThatContainPromo(
                        !TextUtils.isEmpty(shippingCourierViewModel.getProductData().getPromoCode()),
                        shippingCourierViewModel.getProductData().getShipperProductId()
                );
            }
        }
    }

    @Override
    public void hideSoftKeyboard() {
        if (getActivity() != null) {
            KeyboardHandler.hideSoftKeyboard(getActivity());
        }
    }

    @Override
    public void onLoadShippingState(int shipperId, int spId, int itemPosition,
                                    ShipmentDetailData shipmentDetailData,
                                    ShipmentCartItemModel shipmentCartItemModel,
                                    List<ShopShipment> shopShipmentList,
                                    boolean useCourierRecommendation) {
        if (useCourierRecommendation) {
            shipmentPresenter.processGetCourierRecommendation(shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel, shopShipmentList, true);
        } else {
            shipmentPresenter.processGetRates(shipperId, spId, itemPosition, shipmentDetailData, shopShipmentList);
        }
    }

    @Override
    public void onCourierPromoCanceled(String shipperName) {
        if (shipmentAdapter.isCourierPromoStillExist()) {
            shipmentAdapter.cancelAutoApplyCoupon();
            shipmentAdapter.updatePromo(null);
            onRemovePromoCode();
            showToastError(String.format(getString(R.string.message_cannot_apply_courier_promo), shipperName));
        }
    }

    @Override
    public boolean isToogleYearEndPromoOn() {
        if (getActivity() != null) {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
            return remoteConfig.getBoolean("mainapp_enable_year_end_promotion");
        }
        return false;
    }

    @Override
    public void onPurchaseProtectionLogicError() {
        String message = getString(R.string.error_dropshipper);
        showToastError(message);
    }

    @Override
    public void onPurchaseProtectionChangeListener(int position) {
        shipmentAdapter.updateShipmentCostModel();
        shipmentAdapter.updateItemAndTotalCost(position);
        shipmentAdapter.updateInsuranceTncVisibility();
    }

    @Override
    public void navigateToProtectionMore(String url) {
        mTrackerPurchaseProtection.eventClickOnPelajari(url);
        Intent intent = CheckoutWebViewActivity.newInstance(getContext(), url,
                getString(R.string.title_activity_checkout_webview));
        startActivity(intent);
    }

    public int getResultCode() {
        if (shipmentPresenter.getCouponStateChanged()) {
            return ShipmentActivity.RESULT_CODE_COUPON_STATE_CHANGED;
        } else {
            return Activity.RESULT_CANCELED;
        }
    }

    @Override
    public void updateCourierBottomsheetHasNoData(int cartPosition, ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList) {
        if (shippingCourierBottomsheet != null) {
            shippingCourierBottomsheet.setShippingCourierViewModels(null, cartPosition, shipmentCartItemModel, shopShipmentList);
        }
    }

    @Override
    public void updateCourierBottomssheetHasData(List<ShippingCourierViewModel> shippingCourierViewModels, int cartPosition, ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList) {
        if (shippingCourierBottomsheet != null) {
            shippingCourierBottomsheet.setShippingCourierViewModels(shippingCourierViewModels, cartPosition, shipmentCartItemModel, shopShipmentList);
        }
    }
}
