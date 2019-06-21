package com.tokopedia.checkout.view.feature.shipment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.checkout.CartConstant;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.promostacking.AutoApplyStackData;
import com.tokopedia.checkout.domain.datamodel.promostacking.MessageData;
import com.tokopedia.checkout.domain.datamodel.promostacking.VoucherOrdersItemData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.checkout.view.feature.bottomsheetcod.CodBottomSheetFragment;
import com.tokopedia.checkout.view.feature.bottomsheetpromostacking.ClashBottomSheetFragment;
import com.tokopedia.checkout.view.feature.bottomsheetpromostacking.TotalBenefitBottomSheetFragment;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressFormActivity;
import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.feature.shipment.di.DaggerShipmentComponent;
import com.tokopedia.checkout.view.feature.shipment.di.ShipmentComponent;
import com.tokopedia.checkout.view.feature.shipment.di.ShipmentModule;
import com.tokopedia.checkout.view.feature.shipment.util.Utils;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.NotEligiblePromoHolderdata;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentNotifierModel;
import com.tokopedia.checkout.view.feature.shippingoptions.CourierBottomsheet;
import com.tokopedia.checkout.view.feature.webview.CheckoutWebViewActivity;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.logisticanalytics.CodAnalytics;
import com.tokopedia.logisticcommon.LogisticCommonConstant;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.merchantvoucher.voucherlistbottomsheet.MerchantVoucherListBottomSheetFragment;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutConstantKt;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.ProductDetail;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CodModel;
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
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.CornerAnalytics;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;
import com.tokopedia.transactiondata.entity.response.cod.Data;
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.transactiondata.constant.Constant.EXTRA_CHECKOUT_REQUEST;

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View,
        ShipmentContract.AnalyticsActionListener, ShipmentAdapterActionListener, CourierBottomsheet.ActionListener,
        ShippingDurationBottomsheetListener, ShippingCourierBottomsheetListener,
        MerchantVoucherListBottomSheetFragment.ActionListener, ClashBottomSheetFragment.ActionListener {

    private static final int REQUEST_CODE_EDIT_ADDRESS = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    private static final int REQUEST_CODE_COURIER_PINPOINT = 13;
    private static final int REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS = 55;
    public static final int SHOP_INDEX_PROMO_GLOBAL = -1;

    private static final int REQUEST_CODE_NORMAL_CHECKOUT = 0;
    private static final int REQUEST_CODE_COD = 1218;
    private static final String SHIPMENT_TRACE = "mp_shipment";

    public static final String ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO = "ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO";
    public static final String ARG_AUTO_APPLY_PROMO_CODE_APPLIED = "ARG_AUTO_APPLY_PROMO_CODE_APPLIED";
    public static final String ARG_IS_ONE_CLICK_SHIPMENT = "ARG_IS_ONE_CLICK_SHIPMENT";
    private static final String NO_PINPOINT_ETD = "Belum Pinpoint";
    private static final String EXTRA_STATE_SHIPMENT_SELECTION = "EXTRA_STATE_SHIPMENT_SELECTION";
    private static final String DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION = "LAST_CHOOSE_COURIER_ITEM_POSITION";
    private static final String DATA_STATE_LAST_CHOOSEN_SERVICE_ID = "DATA_STATE_LAST_CHOOSEN_SERVICE_ID";
    private static final String EXTRA_EXISTING_LOCATION = "EXTRA_EXISTING_LOCATION";
    public static final String BOTTOM_SHEET_TAG = "BOTTOM_SHEET_TAG";
    private static final String GLOBAL_COUPON_ATTR_DESC = "GLOBAL_COUPON_ATTR_DESC";
    private static final String GLOBAL_COUPON_ATTR_QTY = "GLOBAL_COUPON_ATTR_QTY";

    private RecyclerView rvShipment;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayout llNetworkErrorView;
    private ProgressDialog progressDialogNormal;
    // For regular shipment
    private CourierBottomsheet courierBottomsheet;
    // For shipment recommendation
    private ShippingDurationBottomsheet shippingDurationBottomsheet;
    private ShippingCourierBottomsheet shippingCourierBottomsheet;
    private Snackbar snackbarError;
    private PerformanceMonitoring shipmentTracePerformance;
    private boolean isShipmentTraceStopped;
    private String cornerId;
    private PromoNotEligibleBottomsheet promoNotEligibleBottomsheet;

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
    CodAnalytics mTrackerCod;
    @Inject
    CheckoutAnalyticsPurchaseProtection mTrackerPurchaseProtection;
    @Inject
    CornerAnalytics mTrackerCorner;

    SaveInstanceCacheManager saveInstanceCacheManager;
    CartPromoSuggestion savedCartPromoSuggestion;
    List<ShipmentCartItemModel> savedShipmentCartItemModelList;
    ShipmentCostModel savedShipmentCostModel;
    EgoldAttributeModel savedEgoldAttributeModel;
    RecipientAddressModel savedRecipientAddressModel;
    PromoStackingData savedPromoStackingData;
    ShipmentDonationModel savedShipmentDonationModel;
    BenefitSummaryInfoUiModel benefitSummaryInfoUiModel;
    ShipmentButtonPaymentModel savedShipmentButtonPaymentModel;

    private HashSet<ShipmentSelectionStateData> shipmentSelectionStateDataHashSet = new HashSet<>();

    public static ShipmentFragment newInstance(String defaultSelectedTabPromo,
                                               boolean isAutoApplyPromoCodeApplied,
                                               boolean isOneClickShipment,
                                               Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, defaultSelectedTabPromo);
        bundle.putBoolean(ARG_AUTO_APPLY_PROMO_CODE_APPLIED, isAutoApplyPromoCodeApplied);
        bundle.putBoolean(ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment);
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
        if (getContext() != null) {
            saveInstanceCacheManager = new SaveInstanceCacheManager(getContext(), savedInstanceState);
        }
        if (savedInstanceState != null) {
            savedShipmentCartItemModelList = saveInstanceCacheManager.get(ShipmentCartItemModel.class.getSimpleName(),
                    (new TypeToken<ArrayList<ShipmentCartItemModel>>() {
                    }).getType());
            if (savedShipmentCartItemModelList != null) {
                savedCartPromoSuggestion = saveInstanceCacheManager.get(CartPromoSuggestion.class.getSimpleName(), CartPromoSuggestion.class);
                savedRecipientAddressModel = saveInstanceCacheManager.get(RecipientAddressModel.class.getSimpleName(), RecipientAddressModel.class);
                savedShipmentCostModel = saveInstanceCacheManager.get(ShipmentCostModel.class.getSimpleName(), ShipmentCostModel.class);
                savedEgoldAttributeModel = saveInstanceCacheManager.get(EgoldAttributeModel.class.getSimpleName(), EgoldAttributeModel.class);
                savedShipmentDonationModel = saveInstanceCacheManager.get(ShipmentDonationModel.class.getSimpleName(), ShipmentDonationModel.class);
                savedShipmentButtonPaymentModel = saveInstanceCacheManager.get(ShipmentButtonPaymentModel.class.getSimpleName(), ShipmentButtonPaymentModel.class);
                savedPromoStackingData = saveInstanceCacheManager.get(PromoStackingData.class.getSimpleName(), PromoStackingData.class);
            }
            ArrayList<ShipmentSelectionStateData> shipmentSelectionStateData =
                    saveInstanceCacheManager.get(EXTRA_STATE_SHIPMENT_SELECTION,
                            (new TypeToken<ArrayList<ShipmentSelectionStateData>>() {
                            }).getType(), null);
            if (shipmentSelectionStateData != null) {
                shipmentSelectionStateDataHashSet = new HashSet<>(shipmentSelectionStateData);
            }
        }
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
    public void onRestoreState(Bundle savedState) {
        // no op, already in onCreate()
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

        snackbarError = Snackbar.make(view, "", BaseToaster.LENGTH_SHORT);

        progressDialogNormal = new ProgressDialog(getActivity());
        progressDialogNormal.setMessage(getString(R.string.title_loading));
        progressDialogNormal.setCancelable(false);

        ((SimpleItemAnimator) rvShipment.getItemAnimator()).setSupportsChangeAnimations(false);
        rvShipment.addItemDecoration(new CartItemDecoration());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null || savedShipmentCartItemModelList == null) {
            shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment(), isTradeIn(), true, null, getDeviceId());
        } else {
            shipmentPresenter.setShipmentCartItemModelList(savedShipmentCartItemModelList);
            shipmentPresenter.setCartPromoSuggestion(savedCartPromoSuggestion);
            shipmentPresenter.setRecipientAddressModel(savedRecipientAddressModel);
            shipmentPresenter.setShipmentCostModel(savedShipmentCostModel);
            shipmentPresenter.setShipmentDonationModel(savedShipmentDonationModel);
            shipmentPresenter.setShipmentButtonPaymentModel(savedShipmentButtonPaymentModel);
            shipmentPresenter.setEgoldAttributeModel(savedEgoldAttributeModel);
            shipmentAdapter.setLastChooseCourierItemPosition(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION));
            shipmentAdapter.setLastServiceId(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID));
            shipmentAdapter.addPromoStackingVoucherData(savedPromoStackingData);
            renderCheckoutPage(true, isOneClickShipment());
            swipeToRefresh.setEnabled(false);
        }
    }

    private String getDeviceId() {
        if (getArguments() != null && getArguments().getString(ShipmentFormRequest.EXTRA_DEVICE_ID) != null) {
            return getArguments().getString(ShipmentFormRequest.EXTRA_DEVICE_ID);
        }
        return "";
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInstanceCacheManager.onSave(outState);
        if (shipmentPresenter.getShipmentCartItemModelList() != null) {
            saveInstanceCacheManager.put(ShipmentCartItemModel.class.getSimpleName(), new ArrayList<>(shipmentPresenter.getShipmentCartItemModelList()));
            saveInstanceCacheManager.put(CartPromoSuggestion.class.getSimpleName(), shipmentPresenter.getCartPromoSuggestion());
            saveInstanceCacheManager.put(RecipientAddressModel.class.getSimpleName(), shipmentPresenter.getRecipientAddressModel());
            saveInstanceCacheManager.put(ShipmentCostModel.class.getSimpleName(), shipmentPresenter.getShipmentCostModel());
            saveInstanceCacheManager.put(ShipmentDonationModel.class.getSimpleName(), shipmentPresenter.getShipmentDonationModel());
            saveInstanceCacheManager.put(PromoStackingData.class.getSimpleName(), shipmentAdapter.getPromoGlobalStackData());
            saveInstanceCacheManager.put(ShipmentButtonPaymentModel.class.getSimpleName(), shipmentPresenter.getShipmentButtonPaymentModel());
            outState.putInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION, shipmentAdapter.getLastChooseCourierItemPosition());
            outState.putInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID, shipmentAdapter.getLastServiceId());
        } else {
            saveInstanceCacheManager.put(ShipmentCartItemModel.class.getSimpleName(), null);
        }
        if (!shipmentSelectionStateDataHashSet.isEmpty()) {
            saveInstanceCacheManager.put(EXTRA_STATE_SHIPMENT_SELECTION, new ArrayList<>(shipmentSelectionStateDataHashSet));
        }
    }

    @Override
    public void onSaveState(Bundle state) {
        // no op, already in onSaveInstanceState
    }

    @Override
    protected void setViewListener() {

    }

    private boolean isOneClickShipment() {
        return getArguments() != null && getArguments().getBoolean(ARG_IS_ONE_CLICK_SHIPMENT);
    }

    private boolean isTradeIn() {
        return getArguments() != null && getArguments().getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "") != null &&
                getArguments().getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "").length() > 0;
    }

    private void initRecyclerViewData(PromoStackingData promoStackingData,
                                      CartPromoSuggestion cartPromoSuggestion,
                                      RecipientAddressModel recipientAddressModel,
                                      List<ShipmentCartItemModel> shipmentCartItemModelList,
                                      ShipmentDonationModel shipmentDonationModel,
                                      ShipmentCostModel shipmentCostModel,
                                      EgoldAttributeModel egoldAttributeModel,
                                      ShipmentButtonPaymentModel shipmentButtonPaymentModel,
                                      boolean isInitialRender) {
        shipmentAdapter.clearData();
        rvShipment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShipment.setAdapter(shipmentAdapter);

        CodModel tempCod = shipmentPresenter.getCodData();
        if (tempCod != null && tempCod.isCod()) {
            shipmentAdapter.addNotifierData(new ShipmentNotifierModel(
                    tempCod.getMessageInfo(),
                    tempCod.getMessageLink()
            ));
            mTrackerCod.eventViewBayarDiTempat();
            mTrackerCod.eventImpressionEligibleCod();
            shipmentPresenter.getShipmentButtonPaymentModel().setCod(true);
            onNeedUpdateViewItem(shipmentAdapter.getItemCount() - 1);
        }

        shipmentAdapter.addPromoStackingVoucherData(promoStackingData);
        if (promoStackingData != null) {
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
        if (egoldAttributeModel != null && egoldAttributeModel.isEligible()) {
            shipmentAdapter.updateEgold(false);
            shipmentAdapter.addEgoldAttributeData(egoldAttributeModel);
        }

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
        if (isTradeIn()) {
            checkoutAnalyticsCourierSelection.eventViewCheckoutPageTradeIn();
        }

        shipmentAdapter.addShipmentButtonPaymentModel(shipmentButtonPaymentModel);
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
        if (progressDialogNormal != null && !progressDialogNormal.isShowing())
            progressDialogNormal.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialogNormal != null && progressDialogNormal.isShowing())
            progressDialogNormal.dismiss();
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
                if (snackbarError == null) {
                    snackbarError = Snackbar.make(getView(), "", BaseToaster.LENGTH_SHORT);
                }
                if (!snackbarError.isShownOrQueued()) {
                    snackbarError.setText(message);
                    TextView snackbarTextView = snackbarError.getView().findViewById(android.support.design.R.id.snackbar_text);
                    Button snackbarActionButton = snackbarError.getView().findViewById(android.support.design.R.id.snackbar_action);
                    snackbarError.getView().setBackground(ContextCompat.getDrawable(getView().getContext(), com.tokopedia.design.R.drawable.bg_snackbar_error));
                    snackbarTextView.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.font_black_secondary_54));
                    snackbarActionButton.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.font_black_primary_70));
                    snackbarTextView.setMaxLines(5);
                    snackbarError.setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
                }
            }
        }
    }

    @Override
    public void renderErrorPage(String message) {
        rvShipment.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        llNetworkErrorView.setVisibility(View.GONE);
                        rvShipment.setVisibility(View.VISIBLE);
                        shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment(), isTradeIn(), true, null, getDeviceId());
                    }
                });

    }

    @Override
    public void renderCheckoutPage(boolean isInitialRender, boolean isFromPdp) {
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        if (recipientAddressModel != null && isFromPdp) {
            recipientAddressModel.setDisableMultipleAddress(true);
        }
        shipmentAdapter.setShowOnboarding(shipmentPresenter.isShowOnboarding());
        initRecyclerViewData(
                shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestion(),
                recipientAddressModel,
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getEgoldAttributeModel(),
                shipmentPresenter.getShipmentButtonPaymentModel(),
                isInitialRender
        );
    }

    @Override
    public void stopTrace() {
        if (!isShipmentTraceStopped) {
            shipmentTracePerformance.stopTrace();
            isShipmentTraceStopped = true;
        }
    }

    @Deprecated
    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        CheckPromoParam checkPromoParam = new CheckPromoParam();
        checkPromoParam.setPromo(generateCheckPromoFirstStepParam());
        shipmentPresenter.processCheckout(checkPromoParam, isOneClickShipment(), isTradeIn(), getDeviceId());
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

        initRecyclerViewData(shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                oldShipmentCartItemModelList,
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getEgoldAttributeModel(),
                shipmentPresenter.getShipmentButtonPaymentModel(),
                false
        );
    }

    @Override
    public void renderDataChanged() {
        initRecyclerViewData(
                shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getEgoldAttributeModel(),
                shipmentPresenter.getShipmentButtonPaymentModel(),
                false
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
        if (message.contains("Pre Order") && message.contains("Corner"))
            mTrackerCorner.sendViewCornerPoError();
        if (message.equalsIgnoreCase("")) {
            NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(R.string.default_request_error_unknown));
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
        }
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodSuccess() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranSuccess();
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodFailed(String errorMessage) {
        if (isTradeIn()) {
            checkoutAnalyticsCourierSelection.eventClickBayarTradeInFailed();
        }
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(errorMessage);
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodCourierNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranCourierNotComplete();
    }

    @Deprecated
    @Override
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData) {
        setAppliedPromoCodeData(promoCodeCartListData);
    }

    @Deprecated
    private void setAppliedPromoCodeData(PromoCodeCartListData promoCodeCartListData) {
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
    public void renderCheckPromoStackCodeFromCourierSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel,
                                                            int itemPosition, boolean noToast) {
        if (!noToast && !shipmentAdapter.isCourierPromoStillExist()) {
            showToastNormal(responseGetPromoStackUiModel.getData().getMessage().getText());
        }
        setCourierPromoApplied(itemPosition);
        onSuccessCheckPromoFirstStep(responseGetPromoStackUiModel);
    }

    @Override
    public void setCourierPromoApplied(int itemPosition) {
        shipmentAdapter.setCourierPromoApplied(itemPosition);
    }

    @Override
    public void proceedCod() {
        shipmentAdapter.checkDropshipperValidation(REQUEST_CODE_COD);
    }

    @Override
    public void showBottomSheetError(String htmlMessage) {
        if (getFragmentManager() != null) {
            CodBottomSheetFragment bottomSheet = CodBottomSheetFragment.newInstance(htmlMessage);
            bottomSheet.show(getFragmentManager(), BOTTOM_SHEET_TAG);
        }
    }

    @Override
    public void navigateToCodConfirmationPage(Data data, CheckoutRequest checkoutRequest) {
        Intent intent = checkoutModuleRouter.getCodPageIntent(getContext(), data);
        intent.putExtra(EXTRA_CHECKOUT_REQUEST, checkoutRequest);
        startActivity(intent);
    }

    @Override
    public void setPromoStackingData(CartShipmentAddressFormData cartShipmentAddressFormData) {
        boolean flagAutoApplyStack = false;
        PromoStackingData.Builder builder = new PromoStackingData.Builder();
        if (cartShipmentAddressFormData.getAutoApplyStackData() != null) {
            AutoApplyStackData autoApplyStackData = cartShipmentAddressFormData.getAutoApplyStackData();
            if (!TextUtils.isEmpty(autoApplyStackData.getCode())) {
                flagAutoApplyStack = true;
                builder.typePromo(autoApplyStackData.getIsCoupon() == PromoStackingData.CREATOR.getVALUE_COUPON() ?
                        PromoStackingData.CREATOR.getTYPE_COUPON() : PromoStackingData.CREATOR.getTYPE_VOUCHER())
                        .description(autoApplyStackData.getMessageSuccess())
                        .amount(autoApplyStackData.getDiscountAmount())
                        .promoCode(autoApplyStackData.getCode())
                        .state(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(autoApplyStackData.getState()))
                        .title(autoApplyStackData.getTitleDescription())
                        .build();
                sendAnalyticsOnViewPromoAutoApply();
            } else {
                builder.state(TickerPromoStackingCheckoutView.State.EMPTY);
            }

            if (autoApplyStackData.getVoucherOrders() != null) {
                if (autoApplyStackData.getVoucherOrders().size() > 0) {
                    for (int i = 0; i < autoApplyStackData.getVoucherOrders().size(); i++) {
                        VoucherOrdersItemData voucherOrdersItemData = autoApplyStackData.getVoucherOrders().get(i);
                        if (shipmentAdapter.getShipmentCartItemModelList() != null) {
                            for (ShipmentCartItemModel shipmentCartItemModel : shipmentAdapter.getShipmentCartItemModelList()) {
                                if (shipmentCartItemModel.getCartString().equalsIgnoreCase(voucherOrdersItemData.getUniqueId())) {
                                    if (voucherOrdersItemData.getType().equals(TickerCheckoutUtilKt.getMERCHANT())) {
                                        shipmentCartItemModel.setVoucherOrdersItemUiModel(setVouchersItemUiModel(voucherOrdersItemData));
                                    } else if (voucherOrdersItemData.getType().equals(TickerCheckoutUtilKt.getLOGISTIC())) {
                                        VoucherLogisticItemUiModel model = new VoucherLogisticItemUiModel();
                                        model.setCode(voucherOrdersItemData.getCode());
                                        model.setCouponDesc(voucherOrdersItemData.getTitleDescription());
                                        model.setCouponAmount(Utils.getFormattedCurrency(voucherOrdersItemData.getDiscountAmount()));
                                        shipmentCartItemModel.setVoucherLogisticItemUiModel(model);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            builder.state(TickerPromoStackingCheckoutView.State.EMPTY);
        }

        if (!flagAutoApplyStack) {
            if (cartShipmentAddressFormData.getGlobalCouponAttrData() != null) {
                if (cartShipmentAddressFormData.getGlobalCouponAttrData().getDescription() != null) {
                    if (!cartShipmentAddressFormData.getGlobalCouponAttrData().getDescription().isEmpty()) {
                        builder.title(cartShipmentAddressFormData.getGlobalCouponAttrData().getDescription());
                        builder.titleDefault(cartShipmentAddressFormData.getGlobalCouponAttrData().getDescription());
                    }
                }

                if (cartShipmentAddressFormData.getGlobalCouponAttrData().getQuantityLabel() != null) {
                    if (!cartShipmentAddressFormData.getGlobalCouponAttrData().getQuantityLabel().isEmpty()) {
                        builder.counterLabel(cartShipmentAddressFormData.getGlobalCouponAttrData().getQuantityLabel());
                        builder.counterLabelDefault(cartShipmentAddressFormData.getGlobalCouponAttrData().getQuantityLabel());
                    }
                }
            }
        }

        shipmentAdapter.addPromoStackingVoucherData(builder.build());
        shipmentAdapter.notifyDataSetChanged();
    }

    public VoucherOrdersItemUiModel setVouchersItemUiModel(VoucherOrdersItemData voucherOrdersItemData) {
        VoucherOrdersItemUiModel voucherOrdersItemUiModel = new VoucherOrdersItemUiModel();
        voucherOrdersItemUiModel.setSuccess(voucherOrdersItemData.isSuccess());
        voucherOrdersItemUiModel.setCode(voucherOrdersItemData.getCode());
        voucherOrdersItemUiModel.setUniqueId(voucherOrdersItemData.getUniqueId());
        voucherOrdersItemUiModel.setCartId(voucherOrdersItemData.getCartId());
        voucherOrdersItemUiModel.setType(voucherOrdersItemData.getType());
        voucherOrdersItemUiModel.setCashbackWalletAmount(voucherOrdersItemData.getCashbackWalletAmount());
        voucherOrdersItemUiModel.setDiscountAmount(voucherOrdersItemData.getDiscountAmount());
        voucherOrdersItemUiModel.setInvoiceDescription(voucherOrdersItemData.getInvoiceDescription());
        voucherOrdersItemUiModel.setMessage(setMessageItemUiModel(voucherOrdersItemData.getMessageData()));
        return voucherOrdersItemUiModel;
    }

    public MessageUiModel setMessageItemUiModel(MessageData messageData) {
        MessageUiModel messageUiModel = new MessageUiModel();
        messageUiModel.setColor(messageData.getColor());
        messageUiModel.setState(messageData.getState());
        messageUiModel.setText(messageData.getText());
        return messageUiModel;
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
        else Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderErrorCheckPromoShipmentData(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
        shipmentAdapter.resetCourierPromoState();
    }

    @Override
    public void renderCheckPromoStackingShipmentDataSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel) {
        DataUiModel dataUiModel = responseGetPromoStackUiModel.getData();
        // update benefit
        setBenefitSummaryInfoUiModel(dataUiModel.getBenefit());

        // update global
        if (dataUiModel.getCodes().size() > 0) {
            PromoStackingData promoData = new PromoStackingData.Builder()
                    .typePromo(dataUiModel.isCoupon() == PromoStackingData.CREATOR.getVALUE_COUPON()
                            ? PromoStackingData.CREATOR.getTYPE_COUPON() : PromoStackingData.CREATOR.getTYPE_VOUCHER())
                    .promoCode(dataUiModel.getCodes().get(0))
                    .description(dataUiModel.getMessage().getText())
                    .amount(dataUiModel.getCashbackWalletAmount())
                    .state(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(dataUiModel.getMessage().getState()))
                    .title(dataUiModel.getTitleDescription())
                    .build();

            if (responseGetPromoStackUiModel.getData().getMessage().getState().equals("red")) {
                rvShipment.scrollToPosition(0);
            }

            shipmentAdapter.updateItemPromoGlobalStack(promoData);
        }

        // update merchant
        shipmentAdapter.updatePromoStack(dataUiModel);
        if (getArguments() != null && getArguments().getBoolean(ARG_AUTO_APPLY_PROMO_CODE_APPLIED)) {
            sendAnalyticsOnViewPromoAutoApply();
        } else {
            if (dataUiModel.isCoupon() == 1) {
                sendAnalyticsOnViewPromoManualApply("coupon");
            } else {
                sendAnalyticsOnViewPromoManualApply("voucher");
            }
        }
    }

    public BenefitSummaryInfoUiModel getBenefitSummaryInfoUiModel() {
        return benefitSummaryInfoUiModel;
    }

    public void setBenefitSummaryInfoUiModel(BenefitSummaryInfoUiModel benefitSummaryInfoUiModel) {
        this.benefitSummaryInfoUiModel = benefitSummaryInfoUiModel;
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
    public void renderCancelAutoApplyCouponSuccess(String variant) {
        shipmentAdapter.updatePromoStack(null);
        shipmentPresenter.setHasDeletePromoAfterChecKPromoCodeFinal(true);
        shipmentAdapter.resetCourierPromoState();
        shipmentAdapter.cancelAutoApplyCoupon(variant);
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
        checkoutAnalyticsCourierSelection.enhancedECommerceGoToCheckoutStep2(stringObjectMap, transactionId, isTradeIn());
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

    public String getCornerId() {
        return cornerId;
    }

    public void setCornerId(String cornerId) {
        this.cornerId = cornerId;
    }

    @Override
    public void renderChangeAddressSuccess(RecipientAddressModel selectedAddress) {
        if (shipmentAdapter.hasAppliedPromoStackCode()) {
            setCornerId(selectedAddress.getCornerId());
        }
        if (!TextUtils.isEmpty(selectedAddress.getCornerId()) && shipmentPresenter.getCodData() != null) {
            shipmentAdapter.removeNotifierData();
            shipmentPresenter.getShipmentButtonPaymentModel().setCod(false);
            onNeedUpdateViewItem(shipmentAdapter.getItemCount() - 1);
        }
        shipmentPresenter.setRecipientAddressModel(selectedAddress);
        shipmentAdapter.updateSelectedAddress(selectedAddress);
        courierBottomsheet = null;
        onCartDataDisableToCheckout(null);
        shipmentPresenter.processInitialLoadCheckoutPage(true, isOneClickShipment(), isTradeIn(), true, selectedAddress.getCornerId(), getDeviceId());
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

    private void updateAppliedPromoStack(PromoStackingData cartPromoStacking) {
        shipmentPresenter.setCouponStateChanged(true);
        shipmentAdapter.updateItemPromoGlobalStack(cartPromoStacking);
        if (shipmentAdapter.hasSetAllCourier()) {
            Promo promo = generateCheckPromoFirstStepParam();
            shipmentPresenter.checkPromoStackShipment(promo);

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
        } else if (requestCode == REQUEST_CODE_EDIT_ADDRESS) {
            onResultFromEditAddress();
        }
    }

    public void onResultFromEditAddress() {
        shipmentPresenter.processInitialLoadCheckoutPage(true, isOneClickShipment(), isTradeIn(), true, null, getDeviceId());
    }

    private void onResultFromMultipleAddress(int resultCode, Intent data) {
        if (resultCode == MultipleAddressFormActivity.RESULT_CODE_RELOAD_CART_PAGE) {
            if (getActivity() != null) {
                getActivity().setResult(ShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                getActivity().finish();
            }
        } else if (data != null) {
            PromoStackingData promoStackingData = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_DATA);
            CartPromoSuggestion cartPromoSuggestion = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_SUGGESTION_DATA);
            RecipientAddressModel recipientAddressModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_RECIPIENT_ADDRESS_DATA);
            ArrayList<ShipmentCartItemModel> shipmentCartItemModels = data.getParcelableArrayListExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_CART_TEM_LIST_DATA);
            ShipmentCostModel shipmentCostModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_COST_SATA);
            ShipmentDonationModel shipmentDonationModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_DONATION_DATA);
            shipmentPresenter.processReloadCheckoutPageFromMultipleAddress(
                    promoStackingData, cartPromoSuggestion, recipientAddressModel, shipmentCartItemModels,
                    shipmentCostModel, shipmentDonationModel
            );
        } else {
            shipmentSelectionStateDataHashSet.clear();
            shipmentPresenter.processInitialLoadCheckoutPage(true, isOneClickShipment(), isTradeIn(), true, null, getDeviceId());
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
                shipmentPresenter.processInitialLoadCheckoutPage(true, isOneClickShipment(), isTradeIn(), true, null, getDeviceId());
            } else {
                getActivity().setResult(TopPayActivity.PAYMENT_SUCCESS);
                getActivity().finish();
            }
        }
    }

    private void onResultFromRequestCodeLoyalty(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                PromoStackingData promoStackingData = bundle.getParcelable(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA());
                if (promoStackingData != null) {
                    updateAppliedPromoStack(promoStackingData);
                }
            }
        } else if (resultCode == TickerCheckoutUtilKt.getRESULT_CLASHING()) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                ClashingInfoDetailUiModel clashingInfoDetailUiModel = bundle.getParcelable(TickerCheckoutUtilKt.getEXTRA_CLASHING_DATA());
                if (clashingInfoDetailUiModel != null) {
                    String type = bundle.getString(TickerCheckoutUtilKt.getEXTRA_TYPE());
                    if (type == null) type = "";
                    onClashCheckPromo(clashingInfoDetailUiModel, type);
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
                        if (isOneClickShipment() || (shipmentPresenter.getCodData() != null && shipmentPresenter.getCodData().isCod())) {
                            newAddress.setDisableMultipleAddress(true);
                        }
                        shipmentPresenter.setDataChangeAddressRequestList(shipmentAdapter.getRequestData(newAddress, null).getChangeAddressRequestData());
                        shipmentPresenter.changeShippingAddress(newAddress, isOneClickShipment());
                    }
                }
                break;

            case CartAddressChoiceActivity.RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS:
                shipmentPresenter.processInitialLoadCheckoutPage(false, isOneClickShipment(), isTradeIn(), false, null, getDeviceId());
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
                shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                new ArrayList<>(),
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

    @Override
    public void showBottomSheetTotalBenefit() {
        if (getFragmentManager() != null && getBenefitSummaryInfoUiModel() != null) {
            TotalBenefitBottomSheetFragment bottomSheet = TotalBenefitBottomSheetFragment.newInstance();
            bottomSheet.setBenefit(getBenefitSummaryInfoUiModel());
            bottomSheet.show(getFragmentManager(), null);
        }
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
        shipmentDetailData.setTradein(isTradeIn());

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
        String isBlackbox = "0";
        if (shipmentCartItemModel.isHidingCourier()) isBlackbox = "1";
        sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation(isBlackbox);
        showShippingDurationBottomsheet(shipmentCartItemModel, recipientAddressModel, shopShipmentList, cartPosition);
        if (isTradeIn()) {
            checkoutAnalyticsCourierSelection.eventClickButtonPilihDurasi();
        }
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
    public void sendAnalyticsOnClickDurationThatContainPromo(boolean isCourierPromo, String duration, boolean isCod, String shippingPriceMin, String shippingPriceHigh) {
        checkoutAnalyticsCourierSelection.eventClickChecklistPilihDurasiPengiriman(isCourierPromo, duration, isCod, shippingPriceMin, shippingPriceHigh);
    }

    @Override
    public void sendAnalyticsOnClickLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId, boolean isCod) {
        checkoutAnalyticsCourierSelection.eventClickChangeCourierOption(isCourierPromo, shippingProductId, isCod);
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
    public void sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation(String isBlackbox) {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickButtonDurasiPengiriman(isBlackbox);
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
        shipmentPresenter.getShipmentButtonPaymentModel().setTotalPrice(totalPayment);
        onNeedUpdateViewItem(shipmentAdapter.getItemCount() - 1);
    }

    @Override
    public void onFinishChoosingShipment(List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData) {
        shipmentPresenter.setPromoCodeCartShipmentRequestData(promoRequestData);

        if (shipmentAdapter.hasAppliedPromoStackCode()) {
            Promo promo = generateCheckPromoFirstStepParam();
            shipmentPresenter.checkPromoStackShipment(promo);
        }
    }

    @Override
    public void updateCheckoutRequest(List<DataCheckoutRequest> checkoutRequestData) {
        shipmentPresenter.setDataCheckoutRequestList(checkoutRequestData);
    }

    @Override
    public void onRemovePromoCode(String promoCode) {
        ArrayList<String> promoCodes = new ArrayList<>();
        promoCodes.add(promoCode);
        shipmentPresenter.cancelAutoApplyPromoStack(SHOP_INDEX_PROMO_GLOBAL, promoCodes, false);
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion,
                                                        int position) {
        cartPromoSuggestion.setVisible(false);
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartPromoUseVoucherGlobalPromoClicked(PromoStackingData cartPromoGlobal, int position) {
        trackingPromoCheckoutUtil.checkoutClickUseTickerPromoOrCoupon();
        Promo promo = generateCheckPromoFirstStepParam();
        startActivityForResult(
                checkoutModuleRouter
                        .checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
                                true, "", isOneClickShipment(),
                                TrackingPromoCheckoutConstantKt.getFROM_CHECKOUT(), promo
                        ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
        );
    }

    @Override
    public void onVoucherMerchantPromoClicked(Object object) {
        Promo promo = generateCheckPromoFirstStepParam();
        if (object instanceof ShipmentCartItemModel) {
            if (getFragmentManager() != null) {
                showMerchantVoucherListBottomsheet(
                        ((ShipmentCartItemModel) object).getShopId(),
                        ((ShipmentCartItemModel) object).getCartString(),
                        promo
                );
            }
        }
    }

    private void showMerchantVoucherListBottomsheet(int shopId, String cartString, Promo promo) {
        MerchantVoucherListBottomSheetFragment merchantVoucherListBottomSheetFragment =
                MerchantVoucherListBottomSheetFragment.newInstance(shopId, cartString, promo, "shipment");
        merchantVoucherListBottomSheetFragment.setActionListener(this);
        merchantVoucherListBottomSheetFragment.show(getFragmentManager(), "");
        checkoutAnalyticsCourierSelection.eventClickShowMerchantVoucherList();
    }

    @Override
    public void onCartPromoCancelVoucherPromoGlobalClicked(PromoStackingData cartPromoGlobal, int position) {
        ArrayList<String> promoCodes = new ArrayList<>();
        promoCodes.add(cartPromoGlobal.getPromoCode());
        shipmentPresenter.cancelAutoApplyPromoStack(-1, promoCodes, false);
        if (isToogleYearEndPromoOn()) {
            shipmentAdapter.cancelAllCourierPromo();
        }
    }

    @Override
    public void onCancelVoucherMerchantClicked(String promoMerchantCode, int position, boolean ignoreAPIResponse) {
        checkoutAnalyticsCourierSelection.eventClickHapusPromoXOnTicker(promoMerchantCode);
        ArrayList<String> promoMerchantCodes = new ArrayList<>();
        promoMerchantCodes.add(promoMerchantCode);
        shipmentPresenter.cancelAutoApplyPromoStack(position, promoMerchantCodes, ignoreAPIResponse);
        if (isToogleYearEndPromoOn()) {
            shipmentAdapter.cancelAllCourierPromo();
        }
    }

    @Override
    public void onCartPromoGlobalTrackingImpression(PromoStackingData cartPromoGlobal, int position) {
        trackingPromoCheckoutUtil.checkoutImpressionTicker(cartPromoGlobal.getDescription());
    }

    @Override
    public void onCartPromoGlobalTrackingCancelled(PromoStackingData cartPromoGlobal, int position) {
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
    public void onCheckoutValidationResult(boolean result, Object shipmentData,
                                           int errorPosition, int requestCode) {
        if (shipmentData == null && result) {
            CheckPromoParam checkPromoParam = new CheckPromoParam();
            checkPromoParam.setPromo(generateCheckPromoFirstStepParam());
            switch (requestCode) {
                case REQUEST_CODE_NORMAL_CHECKOUT:
                    List<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList = new ArrayList<>();
                    if (shipmentAdapter.getPromoGlobalStackData() != null &&
                            shipmentAdapter.getPromoGlobalStackData().getState() == TickerPromoStackingCheckoutView.State.FAILED) {
                        NotEligiblePromoHolderdata notEligiblePromoHolderdata = new NotEligiblePromoHolderdata();
                        notEligiblePromoHolderdata.setPromoTitle(shipmentAdapter.getPromoGlobalStackData().getTitle());
                        notEligiblePromoHolderdata.setPromoCode(shipmentAdapter.getPromoGlobalStackData().getPromoCode());
                        notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata);
                    }

                    for (ShipmentCartItemModel shipmentCartItemModel : shipmentAdapter.getShipmentCartItemModelList()) {
                        if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null &&
                                shipmentCartItemModel.getVoucherOrdersItemUiModel().getMessage().getState().equalsIgnoreCase("red")) {
                            NotEligiblePromoHolderdata notEligiblePromoHolderdata = new NotEligiblePromoHolderdata();
                            notEligiblePromoHolderdata.setPromoTitle(shipmentCartItemModel.getVoucherOrdersItemUiModel().getTitleDescription());
                            notEligiblePromoHolderdata.setPromoCode(shipmentCartItemModel.getVoucherOrdersItemUiModel().getCode());
                            notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata);
                        }
                        if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null &&
                                shipmentCartItemModel.getVoucherLogisticItemUiModel().getMessage().getState().equalsIgnoreCase("red")) {
                            NotEligiblePromoHolderdata notEligiblePromoHolderdata = new NotEligiblePromoHolderdata();
                            notEligiblePromoHolderdata.setPromoTitle(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCouponDesc());
                            notEligiblePromoHolderdata.setPromoCode(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                            notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata);
                        }
                    }

                    if (notEligiblePromoHolderdataList.size() > 0) {
                        showPromoNotEligibleDialog();
                    }

//                    if (shipmentAdapter.getPromoGlobalStackData() != null &&
//                            shipmentAdapter.getPromoGlobalStackData().getState() == TickerPromoStackingCheckoutView.State.FAILED) {
//                        rvShipment.smoothScrollToPosition(0);
//                        showToastError(shipmentAdapter.getPromoGlobalStackData().getDescription());
//                    } else {
//                        shipmentPresenter.processSaveShipmentState();
//                        shipmentPresenter.processCheckout(checkPromoParam, isOneClickShipment(), isTradeIn(), getDeviceId());
//                    }
                    break;
                case REQUEST_CODE_COD:
                    shipmentPresenter.proceedCodCheckout(checkPromoParam, isOneClickShipment(), isTradeIn(), getDeviceId());
            }

        } else if (shipmentData != null && !result) {
            sendAnalyticsDropshipperNotComplete();
            if (requestCode == REQUEST_CODE_COD) {
                mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIncomplete();
            }
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment.smoothScrollToPosition(errorPosition);
            }
            showToastError(getActivity().getString(R.string.message_error_courier_not_selected_or_dropshipper_empty));
            onCartDataDisableToCheckout(null);
            ((ShipmentCartItemModel) shipmentData).setStateDropshipperHasError(true);
            shipmentAdapter.notifyItemChanged(errorPosition);
        } else if (shipmentData == null) {
            if (isTradeIn()) {
                checkoutAnalyticsCourierSelection.eventClickBayarCourierNotComplete();
            }
            sendAnalyticsCourierNotComplete();
            if (requestCode == REQUEST_CODE_COD) {
                mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIncomplete();
            }
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment.smoothScrollToPosition(errorPosition);
            }
            showToastError(getActivity().getString(R.string.message_error_courier_not_selected_or_dropshipper_empty));
        }
    }

    @Override
    public void onClickDetailPromoGlobal(PromoStackingData dataGlobal, int position) {
        trackingPromoCheckoutUtil.checkoutClickTicker(dataGlobal.getDescription());

        Promo promo = generateCheckPromoFirstStepParam();
        if (dataGlobal.getTypePromo() == PromoStackingData.CREATOR.getTYPE_COUPON()) {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutDetailIntentWithCode(dataGlobal.getPromoCodeSafe(),
                    true, isOneClickShipment(), TrackingPromoCheckoutConstantKt.getFROM_CART(), promo), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        } else {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutListIntentWithCode(dataGlobal.getPromoCodeSafe(),
                    true, isOneClickShipment(), TrackingPromoCheckoutConstantKt.getFROM_CART(), promo), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
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
    public void onPriorityChecked(int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(new Runnable() {
                @Override
                public void run() {
                    shipmentAdapter.updateShipmentCostModel();
                    shipmentAdapter.updateItemAndTotalCost(position);
                }
            });
        } else {
            shipmentAdapter.updateShipmentCostModel();
            shipmentAdapter.updateItemAndTotalCost(position);
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
    public void onPriorityTncClicker() {
        Intent intent = CheckoutWebViewActivity.newInstance(getContext(),
                CartConstant.PRIORITY_TNC_URL,
                getString(R.string.title_activity_checkout_tnc_webview));
        startActivity(intent);
    }

    @Override
    public void onNeedUpdateRequestData() {
        shipmentAdapter.checkHasSelectAllCourier(true);
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
        shipmentAdapter.checkDropshipperValidation(REQUEST_CODE_NORMAL_CHECKOUT);
    }

    @Override
    public void onDonationChecked(boolean checked) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(new Runnable() {
                @Override
                public void run() {
                    shipmentAdapter.updateDonation(checked);
                }
            });
        } else {
            shipmentAdapter.updateDonation(checked);
        }
        if (checked) sendAnalyticsOnClickTopDonation();
    }

    @Override
    public void onEgoldChecked(boolean checked) {
        shipmentAdapter.updateEgold(checked);
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
    public void onLogisticPromoChosen(List<ShippingCourierViewModel> shippingCourierViewModels, CourierItemData courierData, RecipientAddressModel recipientAddressModel, int cartPosition, int selectedServiceId, ServiceData serviceData, boolean flagNeedToSetPinpoint, String promoCode) {
        onShippingDurationChoosen(shippingCourierViewModels, courierData, recipientAddressModel, cartPosition, selectedServiceId, serviceData, flagNeedToSetPinpoint, false);
        String cartString = shipmentAdapter.getShipmentCartItemModelByIndex(cartPosition).getCartString();
        if (!flagNeedToSetPinpoint)
            shipmentPresenter.processCheckPromoStackingLogisticPromo(cartPosition, cartString, promoCode);
    }

    @Override
    public void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                          CourierItemData recommendedCourier,
                                          RecipientAddressModel recipientAddressModel,
                                          int cartItemPosition, int selectedServiceId, ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                          boolean isClearPromo) {
        if (isTradeIn()) {
            checkoutAnalyticsCourierSelection.eventClickKurirTradeIn(serviceData.getServiceName());
        }
        sendAnalyticsOnClickChecklistShipmentRecommendationDuration(serviceData.getServiceName());
        // Has courier promo means that one of duration has promo, not always current selected duration.
        // It's for analytics purpose
        if (shippingCourierViewModels.size() > 0) {
            ServiceData serviceDataTracker = shippingCourierViewModels.get(0).getServiceData();
            sendAnalyticsOnClickDurationThatContainPromo(
                    (serviceDataTracker.getIsPromo() == 1),
                    serviceDataTracker.getServiceName(),
                    (serviceDataTracker.getCodData().getIsCod() == 1),
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(serviceDataTracker.getRangePrice().getMinPrice(), false),
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(serviceDataTracker.getRangePrice().getMaxPrice(), false)
            );
        }
        if (flagNeedToSetPinpoint) {
            // If instant courier and has not set pinpoint
            shipmentAdapter.setLastServiceId(selectedServiceId);
            setPinpoint(cartItemPosition);
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
                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition);

                // Clear logistic voucher data when any duration is selected and voucher is not null
                if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null &&
                        !TextUtils.isEmpty(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode()) && isClearPromo) {
                    shipmentPresenter.cancelAutoApplyPromoStackLogistic(
                            shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                    shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                    setBenefitSummaryInfoUiModel(null);
                    shipmentAdapter.clearTotalPromoStackAmount();
                    shipmentAdapter.updateShipmentCostModel();
                    shipmentAdapter.updateCheckoutButtonData(null);
                }
                shipmentAdapter.setSelectedCourier(cartItemPosition, recommendedCourier);
                shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
                shipmentAdapter.setShippingCourierViewModels(shippingCourierViewModels, recommendedCourier, cartItemPosition);
                if (!TextUtils.isEmpty(recommendedCourier.getPromoCode())) {
                    checkCourierPromo(recommendedCourier, cartItemPosition);
                }
            }
        }
    }

    private void checkCourierPromo(CourierItemData courierItemData, int itemPosition) {
        if (isToogleYearEndPromoOn() && !TextUtils.isEmpty(courierItemData.getPromoCode())) {
            String promoCode = courierItemData.getPromoCode();
            if (!shipmentAdapter.isCourierPromoStillExist() && shipmentAdapter.getPromoGlobalStackData() != null &&
                    !TextUtils.isEmpty(shipmentAdapter.getPromoGlobalStackData().getPromoCodeSafe())) {
                promoCode = shipmentAdapter.getPromoGlobalStackData().getPromoCodeSafe();
                shipmentPresenter.processCheckPromoStackingCodeFromSelectedCourier(promoCode, itemPosition, true);
            } else {
                shipmentPresenter.processCheckPromoStackingCodeFromSelectedCourier(promoCode, itemPosition, false);
            }
        }
    }

    @Override
    public boolean checkCourierPromoStillExist() {
        return shipmentAdapter.isCourierPromoStillExist();
    }

    @Override
    public void onNoCourierAvailable(String message) {
        if (message.contains(getString(R.string.corner_error_stub)))
            mTrackerCorner.sendViewCornerError();
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
    public void onShowDurationListWithCourierPromo(boolean isCourierPromo, String duration) {
        if (isToogleYearEndPromoOn()) {
            sendAnalyticsOnDisplayDurationThatContainPromo(isCourierPromo, duration);
        }
    }

    @Override
    public void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
                                 int cartItemPosition, boolean isCod, boolean isPromoCourier, boolean isNeedPinpoint) {
        sendAnalyticsOnClickLogisticThatContainPromo(isPromoCourier, courierItemData.getShipperProductId(), isCod);
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
        if (shopShipmentList == null || shopShipmentList.size() == 0) {
            onNoCourierAvailable(getString(R.string.label_no_courier_bottomsheet_message));
        } else {
            ShipmentDetailData shipmentDetailData = getShipmentDetailData(shipmentCartItemModel,
                    recipientAddressModel);
            int codHistory = -1;
            if (shipmentPresenter.getCodData() != null && shipmentPresenter.getCodData().isCod()) {
                codHistory = shipmentPresenter.getCodData().getCounterCod();
            }
            if (shipmentDetailData != null) {
                shippingDurationBottomsheet = ShippingDurationBottomsheet.newInstance(
                        shipmentDetailData, shipmentAdapter.getLastServiceId(), shopShipmentList,
                        recipientAddressModel, cartPosition, codHistory);
                shippingDurationBottomsheet.setShippingDurationBottomsheetListener(this);

                if (getActivity() != null) {
                    shippingDurationBottomsheet.show(getActivity().getSupportFragmentManager(), null);
                }
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
        if (shipmentCartItemModel != null && shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
            if (shipmentCartItemModel.getSelectedShipmentDetailData().getShopId() == null) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setShopId(String.valueOf(shipmentCartItemModel.getShopId()));
            }
            shipmentCartItemModel.getSelectedShipmentDetailData().setTradein(isTradeIn());
            if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                shipmentPresenter.processGetCourierRecommendation(
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId(),
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId(),
                        cartPosition,
                        shipmentCartItemModel.getSelectedShipmentDetailData(),
                        shipmentCartItemModel, shopShipmentList, false);
            }
        }
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
        if (shopShipmentList != null && shopShipmentList.size() > 0) {
            shipmentDetailData.setTradein(isTradeIn());
            if (useCourierRecommendation) {
                shipmentPresenter.processGetCourierRecommendation(shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel, shopShipmentList, true);
            } else {
                shipmentPresenter.processGetRates(shipperId, spId, itemPosition, shipmentDetailData, shopShipmentList);
            }
        }
    }

    @Override
    public void onCourierPromoCanceled(String shipperName, String promoCode) {
        if (shipmentAdapter.isCourierPromoStillExist()) {
            shipmentAdapter.cancelAutoApplyCoupon("");
            shipmentAdapter.updatePromoStack(null);
            onRemovePromoCode(promoCode);
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

    @Override
    public void onNotifierClicked(String url) {
        mTrackerCod.eventClickPelajariSelengkapnya();
        Intent intent = CheckoutWebViewActivity.newInstance(getContext(), url,
                getString(R.string.title_activity_checkout_tnc_webview),
                CheckoutWebViewActivity.CALLER_CODE_COD);
        startActivity(intent);
    }

    @Override
    public void onClickChangePhoneNumber(RecipientAddressModel recipientAddressModel) {
        if (getActivity() != null) {
            checkoutAnalyticsCourierSelection.eventClickGantiNomor();
            Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                    shipmentPresenter.getRecipientAddressModel(), shipmentPresenter.getKeroToken(),
                    CartAddressChoiceActivity.TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN);
            startActivityForResult(intent, REQUEST_CODE_EDIT_ADDRESS);
        }
    }

    @Override
    public void onProcessToPayment() {
        shipmentAdapter.checkDropshipperValidation(REQUEST_CODE_NORMAL_CHECKOUT);
    }

    @Override
    public void onProcessToPaymentCod() {
        proceedCod();
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

    @NonNull
    public Promo generateCheckPromoFirstStepParam() {
        Promo promo = new Promo();
        ArrayList<Order> orders = new ArrayList<>();
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentAdapter.getShipmentCartItemModelList();
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                Order order = new Order();
                ArrayList<ProductDetail> productDetails = new ArrayList<>();
                for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                    if (!cartItemModel.isError()) {
                        ProductDetail productDetail = new ProductDetail();
                        productDetail.setProductId(cartItemModel.getProductId());
                        productDetail.setQuantity(cartItemModel.getQuantity());
                        productDetails.add(productDetail);
                    }
                }
                order.setProductDetails(productDetails);

                ArrayList<String> merchantPromoCodes = new ArrayList<>();
                VoucherOrdersItemUiModel voucherOrdersItemUiModel = shipmentCartItemModel.getVoucherOrdersItemUiModel();
                if (voucherOrdersItemUiModel != null) {
                    if (!merchantPromoCodes.contains(voucherOrdersItemUiModel.getCode())) {
                        merchantPromoCodes.add(voucherOrdersItemUiModel.getCode());
                    }
                }
                VoucherLogisticItemUiModel voucherLogisticItemUiModel = shipmentCartItemModel.getVoucherLogisticItemUiModel();
                if (voucherLogisticItemUiModel != null) {
                    if (!merchantPromoCodes.contains(voucherLogisticItemUiModel.getCode())) {
                        merchantPromoCodes.add(voucherLogisticItemUiModel.getCode());
                    }
                }
                order.setCodes(merchantPromoCodes);

                order.setUniqueId(shipmentCartItemModel.getCartString());
                order.setShopId(shipmentCartItemModel.getShopId());
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                    order.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId());
                    order.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId());
                }
                order.setInsurancePrice(shipmentCartItemModel.isInsurance() ? 1 : 0);
                orders.add(order);
            }
        }
        promo.setOrders(orders);

        String cartType = Promo.CREATOR.getCART_TYPE_DEFAULT();
        if (isOneClickShipment()) {
            cartType = Promo.CREATOR.getCART_TYPE_OCS();
        }
        promo.setState(Promo.CREATOR.getSTATE_CHECKOUT());
        promo.setCartType(cartType);

        PromoStackingData promoStackingGlobalData = shipmentAdapter.getPromoGlobalStackData();
        if (promoStackingGlobalData != null) {
            ArrayList<String> globalPromoCodes = new ArrayList<>();
            if (!TextUtils.isEmpty(promoStackingGlobalData.getPromoCode())) {
                globalPromoCodes.add(promoStackingGlobalData.getPromoCode());
            }
            promo.setCodes(globalPromoCodes);
        }

        promo.setSkipApply(0);
        promo.setSuggested(0);
        return promo;
    }

    @Override
    public void onSuccessClearPromoStack(int shopIndex) {
        setBenefitSummaryInfoUiModel(null);

        if (shopIndex == SHOP_INDEX_PROMO_GLOBAL) {
            PromoStackingData promoStackingData = shipmentAdapter.getPromoGlobalStackData();
            promoStackingData.setState(TickerPromoStackingCheckoutView.State.EMPTY);
            promoStackingData.setAmount(0);
            promoStackingData.setPromoCode("");
            promoStackingData.setDescription("");
            promoStackingData.setTitle(promoStackingData.getTitleDefault());
            promoStackingData.setCounterLabel(promoStackingData.getCounterLabelDefault());
            shipmentAdapter.updateItemPromoStackVoucher(promoStackingData);
        } else {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(shopIndex);
            if (shipmentCartItemModel != null) {
                shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
                shipmentAdapter.notifyItemChanged(shopIndex);
            }
        }
        shipmentAdapter.clearTotalPromoStackAmount();
        shipmentAdapter.updateShipmentCostModel();
        shipmentAdapter.updateCheckoutButtonData(null);
        shipmentAdapter.notifyItemChanged(shipmentAdapter.getShipmentCostPosition());
        shipmentAdapter.checkHasSelectAllCourier(false);
        shipmentPresenter.setCouponStateChanged(true);
    }

    @Override
    public void resetCourier(int position) {
        shipmentAdapter.resetCourier(position);
    }

    @Override
    public void onFailedClearPromoStack(boolean ignoreAPIResponse) {
        shipmentPresenter.setCouponStateChanged(true);
        if (!ignoreAPIResponse) {
            ToasterError.make(getView(), "Terjadi kesalahan. Ulangi beberapa saat lagi", ToasterError.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClashCheckPromo(@NotNull ClashingInfoDetailUiModel clashingInfoDetailUiModel, @NotNull String type) {
        ClashBottomSheetFragment clashBottomSheetFragment = ClashBottomSheetFragment.newInstance();
        clashBottomSheetFragment.setData(clashingInfoDetailUiModel);
        clashBottomSheetFragment.setActionListener(this);
        clashBottomSheetFragment.setAnalyticsShipment(checkoutAnalyticsCourierSelection);
        clashBottomSheetFragment.setSource("shipment");
        clashBottomSheetFragment.setType(type);
        clashBottomSheetFragment.show(getFragmentManager(), "");
    }

    @Override
    public void onSuccessCheckPromoFirstStep(@NotNull ResponseGetPromoStackUiModel promoData) {
        // Update global promo state
        if (promoData.getData().getCodes().size() > 0) {
            PromoStackingData promoStackingGlobalData = shipmentAdapter.getPromoGlobalStackData();
            int typePromo;
            if (promoData.getData().isCoupon() == PromoStackingData.CREATOR.getVALUE_COUPON()) {
                typePromo = PromoStackingData.CREATOR.getTYPE_COUPON();
            } else {
                typePromo = PromoStackingData.CREATOR.getTYPE_VOUCHER();
            }
            promoStackingGlobalData.setTypePromo(typePromo);
            promoStackingGlobalData.setPromoCode(promoData.getData().getCodes().get(0));
            promoStackingGlobalData.setDescription(promoData.getData().getMessage().getText());
            promoStackingGlobalData.setTitle(promoData.getData().getTitleDescription());
            promoStackingGlobalData.setAmount(promoData.getData().getCashbackWalletAmount());
            promoStackingGlobalData.setState(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(promoData.getData().getMessage().getState()));
            promoStackingGlobalData.setVariant(TickerPromoStackingCheckoutView.Variant.GLOBAL);
        }

        // Update merchant voucher state
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentAdapter.getShipmentCartItemModelList();
        if (shipmentCartItemModelList != null) {
            for (VoucherOrdersItemUiModel voucherOrdersItemUiModel : promoData.getData().getVoucherOrders()) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                    if (voucherOrdersItemUiModel.getUniqueId().equals(shipmentCartItemModel.getCartString())) {
                        if (voucherOrdersItemUiModel.getType().equals(TickerCheckoutUtilKt.getMERCHANT())) {
                            shipmentCartItemModel.setVoucherOrdersItemUiModel(voucherOrdersItemUiModel);
                        } else if (voucherOrdersItemUiModel.getType().equals(TickerCheckoutUtilKt.getLOGISTIC())) {
                            VoucherLogisticItemUiModel log = new VoucherLogisticItemUiModel();
                            log.setCode(voucherOrdersItemUiModel.getCode());
                            log.setCouponDesc(voucherOrdersItemUiModel.getTitleDescription());
                            log.setCouponAmount(Utils.getFormattedCurrency(voucherOrdersItemUiModel.getDiscountAmount()));
                            log.setMessage(voucherOrdersItemUiModel.getMessage());
                            shipmentCartItemModel.setVoucherLogisticItemUiModel(log);
                        }
                        break;
                    }
                }
            }
        }
        shipmentAdapter.notifyDataSetChanged();
        shipmentAdapter.checkHasSelectAllCourier(false);
    }

    @Override
    public void onSuccessClearPromoStackAfterClash() {
        // Reset global promo
        PromoStackingData promoStackingData = shipmentAdapter.getPromoGlobalStackData();
        promoStackingData.setState(TickerPromoStackingCheckoutView.State.EMPTY);
        promoStackingData.setAmount(0);
        promoStackingData.setPromoCode("");
        promoStackingData.setDescription("");
        promoStackingData.setTitle(promoStackingData.getTitleDefault());
        promoStackingData.setCounterLabel(promoStackingData.getCounterLabelDefault());

        // Reset merchant promo
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentAdapter.getShipmentCartItemModelList();
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
                shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
            }
        }

        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSubmitNewPromoAfterClash(@NotNull ArrayList<String> oldPromoList,
                                           @NotNull ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                           @NotNull String type
    ) {
        shipmentPresenter.cancelAutoApplyPromoStackAfterClash(oldPromoList, newPromoList,
                true, isOneClickShipment(), isTradeIn(), getCornerId(), getDeviceId(), type);
    }

    @Override
    public void clearTotalBenefitPromoStacking() {
        shipmentAdapter.clearTotalPromoStackAmount();
        shipmentAdapter.updateShipmentCostModel();
    }

    private void showPromoNotEligibleDialog() {
        if (getActivity() != null && promoNotEligibleBottomsheet == null) {
            promoNotEligibleBottomsheet = new PromoNotEligibleBottomsheet(getActivity());
            promoNotEligibleBottomsheet.show();
        }
    }
}
