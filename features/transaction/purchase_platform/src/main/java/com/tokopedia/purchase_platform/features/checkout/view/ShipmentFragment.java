package com.tokopedia.purchase_platform.features.checkout.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.internal.ApplinkConstInternalPayment;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData;
import com.tokopedia.common.payment.PaymentConstant;
import com.tokopedia.common.payment.model.PaymentPassData;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipProd;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticdata.data.analytics.CodAnalytics;
import com.tokopedia.logisticdata.data.constant.LogisticConstant;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.merchantvoucher.common.gql.data.request.CartItemDataVoucher;
import com.tokopedia.merchantvoucher.voucherlistbottomsheet.MerchantVoucherListBottomSheetFragment;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutConstantKt;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.data.ConstantKt;
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.ProductDetail;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.TrackingDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment;
import com.tokopedia.purchase_platform.common.constant.CartConstant;
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.CheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.response.cod.Data;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartResponse;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShopItems;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData;
import com.tokopedia.purchase_platform.common.domain.model.PriceValidationData;
import com.tokopedia.purchase_platform.common.domain.model.TrackerData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData;
import com.tokopedia.purchase_platform.common.feature.promo_clashing.ClashBottomSheetFragment;
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.features.cart.view.InsuranceItemActionListener;
import com.tokopedia.purchase_platform.features.checkout.analytics.CheckoutAnalyticsMacroInsurance;
import com.tokopedia.purchase_platform.features.checkout.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.purchase_platform.features.checkout.analytics.CornerAnalytics;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.CartAddressChoiceActivity;
import com.tokopedia.purchase_platform.features.checkout.subfeature.cod_bottomsheet.CodBottomSheetFragment;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.MultipleAddressFormActivity;
import com.tokopedia.purchase_platform.features.checkout.subfeature.promo_benefit.TotalBenefitBottomSheetFragment;
import com.tokopedia.purchase_platform.features.checkout.subfeature.webview.CheckoutWebViewActivity;
import com.tokopedia.purchase_platform.features.checkout.view.adapter.ShipmentAdapter;
import com.tokopedia.purchase_platform.features.checkout.view.converter.RatesDataConverter;
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataConverter;
import com.tokopedia.purchase_platform.features.checkout.view.di.CheckoutModule;
import com.tokopedia.purchase_platform.features.checkout.view.di.DaggerCheckoutComponent;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.EgoldAttributeModel;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.NotEligiblePromoHolderdata;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.ShipmentButtonPaymentModel;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.ShipmentDonationModel;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.ShipmentNotifierModel;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.transaction.common.dialog.UnifyDialog;
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest;
import com.tokopedia.transaction.common.sharedata.ticket.SubmitTicketResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.logisticcart.cod.view.CodActivity.EXTRA_COD_DATA;
import static com.tokopedia.purchase_platform.common.constant.Constant.EXTRA_CHECKOUT_REQUEST;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_INSURANCE_RECOMMENDATION;

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View, PromoActionListener,
        ShipmentContract.AnalyticsActionListener, ShipmentAdapterActionListener,
        ShippingDurationBottomsheetListener, ShippingCourierBottomsheetListener,
        MerchantVoucherListBottomSheetFragment.ActionListener, ClashBottomSheetFragment.ActionListener,
        PromoNotEligibleActionListener, InsuranceItemActionListener {

    private static final int REQUEST_CODE_EDIT_ADDRESS = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    private static final int REQUEST_CODE_COURIER_PINPOINT = 13;
    private static final int REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS = 55;
    public static final int INDEX_PROMO_GLOBAL = -1;

    private static final int REQUEST_CODE_NORMAL_CHECKOUT = 0;
    private static final int REQUEST_CODE_COD = 1218;
    private static final String SHIPMENT_TRACE = "mp_shipment";

    public static final String ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO = "ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO";
    public static final String ARG_AUTO_APPLY_PROMO_CODE_APPLIED = "ARG_AUTO_APPLY_PROMO_CODE_APPLIED";
    public static final String ARG_IS_ONE_CLICK_SHIPMENT = "ARG_IS_ONE_CLICK_SHIPMENT";
    public static final String ARG_CHECKOUT_LEASING_ID = "ARG_CHECKOUT_LEASING_ID";
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
    TrackingPromoCheckoutUtil trackingPromoCheckoutUtil;
    @Inject
    CodAnalytics mTrackerCod;
    @Inject
    CheckoutAnalyticsPurchaseProtection mTrackerPurchaseProtection;
    @Inject
    CheckoutAnalyticsMacroInsurance mTrackerMacroInsurance;
    @Inject
    CornerAnalytics mTrackerCorner;
    @Inject
    ShipmentTrackingDataGenerator shipmentTrackingDataGenerator;

    SaveInstanceCacheManager saveInstanceCacheManager;
    TickerAnnouncementHolderData savedTickerAnnouncementModel;
    CartPromoSuggestionHolderData savedCartPromoSuggestionHolderData;
    List<ShipmentCartItemModel> savedShipmentCartItemModelList;
    ShipmentCostModel savedShipmentCostModel;
    EgoldAttributeModel savedEgoldAttributeModel;
    RecipientAddressModel savedRecipientAddressModel;
    PromoStackingData savedPromoStackingData;
    ShipmentDonationModel savedShipmentDonationModel;
    BenefitSummaryInfoUiModel benefitSummaryInfoUiModel;
    ShipmentButtonPaymentModel savedShipmentButtonPaymentModel;

    private HashSet<ShipmentSelectionStateData> shipmentSelectionStateDataHashSet = new HashSet<>();
    private boolean hasInsurance = false;
    private boolean isInsuranceEnabled = false;

    private Subscription delayScrollToFirstShopSubscription;

    public static ShipmentFragment newInstance(String defaultSelectedTabPromo,
                                               boolean isAutoApplyPromoCodeApplied,
                                               boolean isOneClickShipment,
                                               String leasingId,
                                               Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, defaultSelectedTabPromo);
        bundle.putBoolean(ARG_AUTO_APPLY_PROMO_CODE_APPLIED, isAutoApplyPromoCodeApplied);
        bundle.putString(ARG_CHECKOUT_LEASING_ID, leasingId);
        if (leasingId != null && !leasingId.isEmpty()) {
            bundle.putBoolean(ARG_IS_ONE_CLICK_SHIPMENT, true);
        } else {
            bundle.putBoolean(ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment);
        }
        ShipmentFragment shipmentFragment = new ShipmentFragment();
        shipmentFragment.setArguments(bundle);

        return shipmentFragment;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            BaseMainApplication baseMainApplication = (BaseMainApplication) getActivity().getApplication();
            DaggerCheckoutComponent.builder()
                    .baseAppComponent(baseMainApplication.getBaseAppComponent())
                    .checkoutModule(new CheckoutModule(this))
                    .build()
                    .inject(this);
        }
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
                savedTickerAnnouncementModel = saveInstanceCacheManager.get(TickerAnnouncementHolderData.class.getSimpleName(), TickerAnnouncementHolderData.class);
                savedCartPromoSuggestionHolderData = saveInstanceCacheManager.get(CartPromoSuggestionHolderData.class.getSimpleName(), CartPromoSuggestionHolderData.class);
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

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getContext());
        isInsuranceEnabled = remoteConfig.getBoolean(APP_ENABLE_INSURANCE_RECOMMENDATION, false);

        shipmentPresenter.attachView(this);
        shipmentTracePerformance = PerformanceMonitoring.start(SHIPMENT_TRACE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        delayScrollToFirstShopSubscription.unsubscribe();
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
        rvShipment.addItemDecoration(new ShipmentItemDecoration());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null || savedShipmentCartItemModelList == null) {
            shipmentPresenter.processInitialLoadCheckoutPage(
                    false, isOneClickShipment(), isTradeIn(), true,
                    false, null, getDeviceId(), getCheckoutLeasingId()
            );
        } else {
            shipmentPresenter.setTickerAnnouncementHolderData(savedTickerAnnouncementModel);
            shipmentPresenter.setShipmentCartItemModelList(savedShipmentCartItemModelList);
            shipmentPresenter.setCartPromoSuggestionHolderData(savedCartPromoSuggestionHolderData);
            shipmentPresenter.setRecipientAddressModel(savedRecipientAddressModel);
            shipmentPresenter.setShipmentCostModel(savedShipmentCostModel);
            shipmentPresenter.setShipmentDonationModel(savedShipmentDonationModel);
            shipmentPresenter.setShipmentButtonPaymentModel(savedShipmentButtonPaymentModel);
            shipmentPresenter.setEgoldAttributeModel(savedEgoldAttributeModel);
            shipmentAdapter.setLastChooseCourierItemPosition(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION));
            shipmentAdapter.setLastServiceId(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID));
            shipmentAdapter.addPromoStackingVoucherData(savedPromoStackingData);
            renderCheckoutPage(true, false, isOneClickShipment());
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
            saveInstanceCacheManager.put(TickerAnnouncementHolderData.class.getSimpleName(), shipmentPresenter.getTickerAnnouncementHolderData());
            saveInstanceCacheManager.put(ShipmentCartItemModel.class.getSimpleName(), new ArrayList<>(shipmentPresenter.getShipmentCartItemModelList()));
            saveInstanceCacheManager.put(CartPromoSuggestionHolderData.class.getSimpleName(), shipmentPresenter.getCartPromoSuggestionHolderData());
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

    private String getCheckoutLeasingId() {
        String leasingId = "0";
        if (getArguments() != null && getArguments().getString(ARG_CHECKOUT_LEASING_ID) != null &&
                !getArguments().getString(ARG_CHECKOUT_LEASING_ID).equalsIgnoreCase("null")) {
            leasingId = getArguments().getString(ARG_CHECKOUT_LEASING_ID);
        }
        return leasingId;
    }

    private boolean isTradeIn() {
        return getArguments() != null && getArguments().getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "") != null &&
                getArguments().getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "").length() > 0;
    }

    private void initRecyclerViewData(TickerAnnouncementHolderData tickerAnnouncementHolderData,
                                      PromoStackingData promoStackingData,
                                      CartPromoSuggestionHolderData cartPromoSuggestionHolderData,
                                      RecipientAddressModel recipientAddressModel,
                                      List<ShipmentCartItemModel> shipmentCartItemModelList,
                                      ShipmentDonationModel shipmentDonationModel,
                                      ShipmentCostModel shipmentCostModel,
                                      EgoldAttributeModel egoldAttributeModel,
                                      ShipmentButtonPaymentModel shipmentButtonPaymentModel,
                                      boolean isInitialRender,
                                      boolean isReloadAfterPriceChangeHigher) {
        shipmentAdapter.clearData();
        rvShipment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShipment.setAdapter(shipmentAdapter);

        if (tickerAnnouncementHolderData != null) {
            shipmentAdapter.addTickerAnnouncementdata(tickerAnnouncementHolderData);
        }

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
            if (promoStackingData.getState() != TickerPromoStackingCheckoutView.State.FAILED) {
                onPromoGlobalTrackingImpression(promoStackingData);
            }
            cartPromoSuggestionHolderData.setVisible(false);
            shipmentAdapter.addPromoSuggestionData(cartPromoSuggestionHolderData);
        } else {
            shipmentAdapter.addPromoSuggestionData(cartPromoSuggestionHolderData);
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

        if (isInitialRender) {
            sendEEStep2();
        }

        if (isReloadAfterPriceChangeHigher) {
            delayScrollToFirstShop();
        }
    }

    private void delayScrollToFirstShop() {
        delayScrollToFirstShopSubscription = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (!isUnsubscribed()) {
                            if (rvShipment.getLayoutManager() != null) {
                                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(rvShipment.getContext()) {
                                    @Override
                                    protected int getVerticalSnapPreference() {
                                        return SNAP_TO_START;
                                    }
                                };
                                linearSmoothScroller.setTargetPosition(shipmentAdapter.getFirstShopPosition());
                                rvShipment.getLayoutManager().startSmoothScroll(linearSmoothScroller);
                            }
                        }
                    }
                });
    }

    private void sendEEStep2() {
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentAdapter.getShipmentCartItemModelList()) {
            List<DataCheckoutRequest> dataCheckoutRequests = null;
            if (shipmentCartItemModel.isSaveStateFlag()) {
                dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                        shipmentCartItemModel.getCartString(), "", "", ""
                );
            } else {
                dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                        shipmentCartItemModel.getCartString(), "", "", String.valueOf(shipmentCartItemModel.getSpId())
                );
            }
            shipmentPresenter.setDataCheckoutRequestList(dataCheckoutRequests);
        }

        List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentAdapter.getPromoGlobalStackData(), shipmentAdapter.getShipmentCartItemModelList());
        shipmentPresenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
                dataCheckoutRequests,
                hasInsurance,
                EnhancedECommerceActionField.STEP_2,
                ConstantTransactionAnalytics.EventAction.VIEW_CHECKOUT_PAGE,
                ConstantTransactionAnalytics.EventLabel.SUCCESS,
                getCheckoutLeasingId());

        sendEESGTMv5(dataCheckoutRequests, 2, "checkout page loaded", "",
                ConstantTransactionAnalytics.EventAction.VIEW_CHECKOUT_PAGE, ConstantTransactionAnalytics.EventLabel.SUCCESS);
    }

    private void sendEESGTMv5(List<DataCheckoutRequest> dataCheckoutRequests, int step, String option,
                              String transactionId, String eventAction, String eventLabel) {
        CheckPromoParam checkPromoParam = new CheckPromoParam();
        checkPromoParam.setPromo(generateCheckPromoFirstStepParam());

        int isDonation = shipmentPresenter.getShipmentDonationModel() != null && shipmentPresenter.getShipmentDonationModel().isChecked() ? 1 : 0;
        CheckoutRequest checkoutRequest = shipmentPresenter.generateCheckoutRequest(
                dataCheckoutRequests, hasInsurance, checkPromoParam, isDonation, getCheckoutLeasingId()
        );

        Bundle eCommerceBundle = shipmentTrackingDataGenerator.generateBundleEnhancedEcommerce(checkoutRequest, shipmentPresenter.getShipmentCartItemModelList());
        checkoutAnalyticsCourierSelection.sendEnhancedECommerceCheckoutV5(eCommerceBundle, step, option,
                transactionId, isTradeIn(), eventAction, eventLabel);
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
                    TextView snackbarTextView = snackbarError.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    Button snackbarActionButton = snackbarError.getView().findViewById(com.google.android.material.R.id.snackbar_action);
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
                        shipmentPresenter.processInitialLoadCheckoutPage(
                                false, isOneClickShipment(), isTradeIn(), true,
                                false, null, getDeviceId(), getCheckoutLeasingId()
                        );
                    }
                });

    }

    @Override
    public void renderInsuranceCartData(InsuranceCartResponse insuranceCartResponse) {
        if (shipmentAdapter != null) {
            hasInsurance = false;
            if (insuranceCartResponse != null &&
                    !insuranceCartResponse.getCartShopsList().isEmpty()) {
                for (InsuranceCartShops insuranceCartShops : insuranceCartResponse.getCartShopsList()) {
                    for (InsuranceCartShopItems insuranceCartShopItems : insuranceCartShops.getShopItemsList()) {
                        for (InsuranceCartDigitalProduct insuranceCartDigitalProduct : insuranceCartShopItems.getDigitalProductList()) {
                            if (!insuranceCartDigitalProduct.isProductLevel()) {
                                hasInsurance = true;
                                shipmentAdapter.addInsuranceDataList(insuranceCartShops);
                            }
                        }
                    }
                }
                shipmentAdapter.notifyDataSetChanged();
                shipmentAdapter.updateShipmentCostModel();
            }
        }
    }

    @Override
    public void renderCheckoutPage(boolean isInitialRender, boolean isReloadAfterPriceChangeHigher, boolean isFromPdp) {
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        if (recipientAddressModel != null && isFromPdp) {
            recipientAddressModel.setDisableMultipleAddress(true);
        }
        shipmentAdapter.setShowOnboarding(shipmentPresenter.isShowOnboarding());
        initRecyclerViewData(
                shipmentPresenter.getTickerAnnouncementHolderData(),
                shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestionHolderData(),
                recipientAddressModel,
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getEgoldAttributeModel(),
                shipmentPresenter.getShipmentButtonPaymentModel(),
                isInitialRender,
                isReloadAfterPriceChangeHigher
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

        initRecyclerViewData(
                shipmentPresenter.getTickerAnnouncementHolderData(),
                shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestionHolderData(),
                shipmentPresenter.getRecipientAddressModel(),
                oldShipmentCartItemModelList,
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getEgoldAttributeModel(),
                shipmentPresenter.getShipmentButtonPaymentModel(),
                false,
                false
        );
    }

    @Override
    public void renderDataChanged() {
        initRecyclerViewData(
                shipmentPresenter.getTickerAnnouncementHolderData(),
                shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestionHolderData(),
                shipmentPresenter.getRecipientAddressModel(),
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getEgoldAttributeModel(),
                shipmentPresenter.getShipmentButtonPaymentModel(),
                false,
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

        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS);
    }

    @Deprecated
    @Override
    public void renderThanksTopPaySuccess(String message) {
        showToastNormal(getString(R.string.message_payment_succeded_transaction_module));
        RouteManager.route(getActivity(), ApplinkConst.PURCHASE_CONFIRMED);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void renderCheckoutCartSuccess(CheckoutData checkoutData) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setRedirectUrl(checkoutData.getRedirectUrl());
        paymentPassData.setTransactionId(checkoutData.getTransactionId());
        paymentPassData.setPaymentId(checkoutData.getPaymentId());
        paymentPassData.setCallbackSuccessUrl(checkoutData.getCallbackSuccessUrl());
        paymentPassData.setCallbackFailedUrl(checkoutData.getCallbackFailedUrl());
        paymentPassData.setQueryString(checkoutData.getQueryString());

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPayment.PAYMENT_CHECKOUT);
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE);
    }

    @Override
    public void renderCheckoutCartErrorReporter(CheckoutData checkoutData) {
        UnifyDialog createTicketDialog = new UnifyDialog(getActivity(), UnifyDialog.HORIZONTAL_ACTION, UnifyDialog.NO_HEADER);
        createTicketDialog.setTitle(checkoutData.getErrorReporter().getTexts().getSubmitTitle());
        createTicketDialog.setDescription(checkoutData.getErrorReporter().getTexts().getSubmitDescription());
        createTicketDialog.setSecondary(checkoutData.getErrorReporter().getTexts().getCancelButton());
        createTicketDialog.setSecondaryOnClickListener(v -> {
            checkoutAnalyticsCourierSelection.eventClickCloseOnHelpPopUpInCheckout();
            createTicketDialog.dismiss();
        });
        createTicketDialog.setOk(checkoutData.getErrorReporter().getTexts().getSubmitButton());
        createTicketDialog.setOkOnClickListener(v -> {
            checkoutAnalyticsCourierSelection.eventClickReportOnHelpPopUpInCheckout();
            createTicketDialog.dismiss();
            shipmentPresenter.processSubmitHelpTicket(checkoutData);
        });
        createTicketDialog.show();
        checkoutAnalyticsCourierSelection.eventViewHelpPopUpAfterErrorInCheckout();
    }

    @Override
    public void renderCheckoutPriceUpdated(PriceValidationData priceValidationData) {
        if (getActivity() != null) {
            com.tokopedia.purchase_platform.common.domain.model.MessageData messageData =
                    priceValidationData.getMessage();
            if (messageData != null) {
                DialogUnify priceValidationDialog = new DialogUnify(getActivity(), DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE);
                priceValidationDialog.setTitle(messageData.getTitle());
                priceValidationDialog.setDescription(messageData.getDesc());
                priceValidationDialog.setPrimaryCTAText(messageData.getAction());
                priceValidationDialog.setPrimaryCTAClickListener(() -> {
                    shipmentPresenter.processInitialLoadCheckoutPage(
                            true, isOneClickShipment(), isTradeIn(), true,
                            true, null, getDeviceId(), getCheckoutLeasingId()
                    );
                    priceValidationDialog.dismiss();
                    return Unit.INSTANCE;
                });

                priceValidationDialog.show();

                StringBuilder eventLabelBuilder = new StringBuilder();
                TrackerData trackerData = priceValidationData.getTrackerData();
                if (trackerData != null) {
                    eventLabelBuilder.append(trackerData.getProductChangesType());
                    eventLabelBuilder.append(" - ");
                    eventLabelBuilder.append(trackerData.getCampaignType());
                    eventLabelBuilder.append(" - ");
                    eventLabelBuilder.append(TextUtils.join(",", trackerData.getProductIds()));
                }

                checkoutAnalyticsCourierSelection.eventViewPopupPriceIncrease(eventLabelBuilder.toString());
            }
        }
    }

    @Override
    public void renderSubmitHelpTicketSuccess(SubmitTicketResult submitTicketResult) {
        UnifyDialog successTicketDialog = new UnifyDialog(getActivity(), UnifyDialog.SINGLE_ACTION, UnifyDialog.NO_HEADER);
        successTicketDialog.setTitle(submitTicketResult.getTexts().getSubmitTitle());
        successTicketDialog.setDescription(submitTicketResult.getTexts().getSubmitDescription());
        successTicketDialog.setOk(submitTicketResult.getTexts().getSuccessButton());
        successTicketDialog.setOkOnClickListener(v -> getActivity().finish());
        successTicketDialog.show();
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
    public void renderCheckPromoStackLogisticSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, String promoCode) {
        onSuccessCheckPromoFirstStep(responseGetPromoStackUiModel);
    }

    @Override
    public void setCourierPromoApplied(int itemPosition) {
        shipmentAdapter.setCourierPromoApplied(itemPosition);
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
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.COD);
        intent.putExtra(EXTRA_CHECKOUT_REQUEST, checkoutRequest);
        intent.putExtra(EXTRA_COD_DATA, data);
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

            if (autoApplyStackData.getVoucherOrdersItemDataList() != null) {
                if (autoApplyStackData.getVoucherOrdersItemDataList().size() > 0) {
                    for (int i = 0; i < autoApplyStackData.getVoucherOrdersItemDataList().size(); i++) {
                        VoucherOrdersItemData voucherOrdersItemData = autoApplyStackData.getVoucherOrdersItemDataList().get(i);
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
                    .trackingDetailUiModels(responseGetPromoStackUiModel.getData().getTrackingDetailUiModel())
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

        updatePromoTrackingData(dataUiModel.getTrackingDetailUiModel());
        sendEEStep3();
    }

    private boolean stillHasAppliedPromo() {
        PromoStackingData promoStackingGlobalData = shipmentAdapter.getPromoGlobalStackData();
        if (promoStackingGlobalData != null && promoStackingGlobalData.getState() != TickerPromoStackingCheckoutView.State.EMPTY) {
            return true;
        }
        List<ShipmentCartItemModel> shipmentCartItemModels = shipmentAdapter.getShipmentCartItemModelList();
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
            if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null ||
                    shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                return true;
            }
        }

        return false;
    }

    private void clearPromoTrackingData() {
        List<ShipmentCartItemModel> shipmentCartItemModels = shipmentAdapter.getShipmentCartItemModelList();
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                if (cartItemModel.getAnalyticsProductCheckoutData() != null) {
                    cartItemModel.getAnalyticsProductCheckoutData().setPromoDetails("");
                    cartItemModel.getAnalyticsProductCheckoutData().setPromoCode("");
                }
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
        if (courierItemData.getLogPromoCode() != null) {
            String cartString = shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition).getCartString();
            shipmentPresenter.processCheckPromoStackingLogisticPromo(itemPosition, cartString, courierItemData.getLogPromoCode());
        }
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
            navigateToPinpointActivity(locationPass);
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
    public void sendEnhancedEcommerceAnalyticsCheckout(Map<String, Object> stringObjectMap,
                                                       String transactionId,
                                                       String eventAction,
                                                       String eventLabel) {
        checkoutAnalyticsCourierSelection.sendEnhancedECommerceCheckout(
                stringObjectMap, transactionId, isTradeIn(), eventAction, eventLabel
        );
        checkoutAnalyticsCourierSelection.flushEnhancedECommerceCheckout();
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
        onDataDisableToCheckout(null);
        shipmentPresenter.processInitialLoadCheckoutPage(
                true, isOneClickShipment(), isTradeIn(), true,
                false, selectedAddress.getCornerId(), getDeviceId(), getCheckoutLeasingId()
        );
    }

    @Override
    public List<DataCheckoutRequest> generateNewCheckoutRequest
            (List<ShipmentCartItemModel> shipmentCartItemModelList, boolean isAnalyticsPurpose) {
        ShipmentAdapter.RequestData requestData = shipmentAdapter.getRequestData(null, shipmentCartItemModelList, isAnalyticsPurpose);
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
            shipmentPresenter.checkPromoFinalStackShipment(promo);
            shipmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PaymentConstant.REQUEST_CODE) {
            onResultFromPayment(resultCode);
        } else if ((requestCode == REQUEST_CHOOSE_PICKUP_POINT)
                && resultCode == Activity.RESULT_OK) {
            onResultFromRequestCodeCourierOptions(requestCode, data);
        } else if (requestCode == CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS) {
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
        shipmentPresenter.processInitialLoadCheckoutPage(
                true, isOneClickShipment(), isTradeIn(), true,
                false, null, getDeviceId(), getCheckoutLeasingId()
        );
    }

    private void onResultFromMultipleAddress(int resultCode, Intent data) {
        if (resultCode == MultipleAddressFormActivity.RESULT_CODE_RELOAD_CART_PAGE) {
            if (getActivity() != null) {
                getActivity().setResult(ShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                getActivity().finish();
            }
        } else if (data != null) {
            PromoStackingData promoStackingData = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_DATA);
            CartPromoSuggestionHolderData cartPromoSuggestionHolderData = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_SUGGESTION_DATA);
            RecipientAddressModel recipientAddressModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_RECIPIENT_ADDRESS_DATA);
            ArrayList<ShipmentCartItemModel> shipmentCartItemModels = data.getParcelableArrayListExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_CART_TEM_LIST_DATA);
            ShipmentCostModel shipmentCostModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_COST_SATA);
            ShipmentDonationModel shipmentDonationModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_DONATION_DATA);
            shipmentPresenter.processReloadCheckoutPageFromMultipleAddress(
                    promoStackingData, cartPromoSuggestionHolderData, recipientAddressModel, shipmentCartItemModels,
                    shipmentCostModel, shipmentDonationModel
            );
        } else {
            shipmentSelectionStateDataHashSet.clear();
            shipmentPresenter.processInitialLoadCheckoutPage(
                    true, isOneClickShipment(), isTradeIn(), true,
                    false, null, getDeviceId(), getCheckoutLeasingId()
            );
        }
    }

    private void onResultFromCourierPinpoint(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            LocationPass locationPass = data.getExtras().getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION);
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
            if (resultCode == PaymentConstant.PAYMENT_CANCELLED || resultCode == PaymentConstant.PAYMENT_FAILED) {
                shipmentPresenter.processInitialLoadCheckoutPage(
                        true, isOneClickShipment(), isTradeIn(), true,
                        false, null, getDeviceId(), getCheckoutLeasingId()
                );
            } else {
                getActivity().setResult(PaymentConstant.PAYMENT_SUCCESS);
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
                    updatePromoTrackingData(promoStackingData.getTrackingDetailUiModels());
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

    private void updatePromoTrackingData(List<TrackingDetailUiModel> trackingDetailUiModels) {
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentAdapter.getShipmentCartItemModelList()) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                if (trackingDetailUiModels.size() > 0) {
                    for (TrackingDetailUiModel trackingDetailUiModel : trackingDetailUiModels) {
                        if (trackingDetailUiModel.getProductId() == cartItemModel.getProductId() &&
                                cartItemModel.getAnalyticsProductCheckoutData() != null) {
                            cartItemModel.getAnalyticsProductCheckoutData().setPromoCode(trackingDetailUiModel.getPromoCodesTracking());
                            cartItemModel.getAnalyticsProductCheckoutData().setPromoDetails(trackingDetailUiModel.getPromoDetailsTracking());
                        }
                    }
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
            case CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS:
                RecipientAddressModel currentAddress = shipmentAdapter.getAddressShipmentData();
                RecipientAddressModel newAddress = data.getParcelableExtra(
                        CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA);

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
                        shipmentPresenter.setDataChangeAddressRequestList(shipmentAdapter.getRequestData(newAddress, null, false).getChangeAddressRequestData());
                        shipmentPresenter.changeShippingAddress(newAddress, isOneClickShipment());
                    }
                }
                break;

            case CartAddressChoiceActivity.RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS:
                shipmentPresenter.processInitialLoadCheckoutPage(
                        false, isOneClickShipment(), isTradeIn(), false,
                        false, null, getDeviceId(), getCheckoutLeasingId()
                );
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
        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS);
    }

    @Override
    public void onSendToMultipleAddress(RecipientAddressModel recipientAddressModel, String cartIds) {
        sendAnalyticsOnClickChooseToMultipleAddressShipment();
        Intent intent = MultipleAddressFormActivity.createInstance(getActivity(),
                shipmentAdapter.getPromoGlobalStackData(),
                shipmentPresenter.getCartPromoSuggestionHolderData(),
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
    public void sendAnalyticsOnClickChangeCourierShipmentRecommendation(ShipmentCartItemModel shipmentCartItemModel) {
        String label = "";
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getOntimeDelivery() != null) {
            OntimeDelivery otdg = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getOntimeDelivery();
            if (otdg.getAvailable()) {
                label = getString(R.string.otdg_gtm_label);
            }
        }
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickUbahKurir(label);
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
    public void sendAnalyticsPromoRedState() {
        checkoutAnalyticsCourierSelection.eventClickBuyPromoRedState();
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
    public void sendAnalyticsViewInformationAndWarningTickerInCheckout(String tickerId) {
        checkoutAnalyticsCourierSelection.eventViewInformationAndWarningTickerInCheckout(tickerId);
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
            shipmentPresenter.checkPromoFinalStackShipment(promo);
        } else {
            sendEEStep3();
        }

    }

    private void sendEEStep3() {
        List<ShipmentCartItemModel> shipmentCartItemModels = shipmentAdapter.getShipmentCartItemModelList();

        // if one of courier reseted because of apply promo logistic (PSL) and eventually not eligible after hit validate use, don't send EE
        boolean courierHasReseted = false;
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
            if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                        shipmentCartItemModel.getCartString(),
                        String.valueOf(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getServiceId()),
                        String.valueOf(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperPrice()),
                        String.valueOf(shipmentCartItemModel.getSpId())
                );
                shipmentPresenter.setDataCheckoutRequestList(dataCheckoutRequests);
            } else {
                courierHasReseted = true;
                break;
            }
        }

        if (!courierHasReseted) {
            List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentAdapter.getPromoGlobalStackData(), shipmentAdapter.getShipmentCartItemModelList());
            shipmentPresenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
                    dataCheckoutRequests,
                    hasInsurance,
                    EnhancedECommerceActionField.STEP_3,
                    ConstantTransactionAnalytics.EventAction.CLICK_ALL_COURIER_SELECTED,
                    "",
                    getCheckoutLeasingId());

            sendEESGTMv5(dataCheckoutRequests, 3, "data validation", "",
                    ConstantTransactionAnalytics.EventAction.CLICK_ALL_COURIER_SELECTED, ConstantTransactionAnalytics.EventLabel.SUCCESS);
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
        shipmentPresenter.cancelAutoApplyPromoStack(INDEX_PROMO_GLOBAL, promoCodes, false, "global");
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestionHolderData cartPromoSuggestionHolderData,
                                                        int position) {
        cartPromoSuggestionHolderData.setVisible(false);
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartPromoUseVoucherGlobalPromoClicked(PromoStackingData cartPromoGlobal, int position) {
        trackingPromoCheckoutUtil.checkoutClickUseTickerPromoOrCoupon();
        Promo promo = generateCheckPromoFirstStepParam();

        Intent intent = getIntentToPromoList(promo);
        startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    @NotNull
    private Intent getIntentToPromoList(Promo promo) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_MARKETPLACE);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true);
        bundle.putString(ConstantKt.getPROMO_CODE(), "");
        bundle.putBoolean(ConstantKt.getONE_CLICK_SHIPMENT(), isOneClickShipment());
        bundle.putInt(ConstantKt.getPAGE_TRACKING(), TrackingPromoCheckoutConstantKt.getFROM_CHECKOUT());
        bundle.putParcelable(ConstantKt.getCHECK_PROMO_FIRST_STEP_PARAM(), promo);
        intent.putExtras(bundle);
        return intent;
    }

    @NotNull
    private Intent getIntentToPromoDetail(Promo promo, PromoStackingData promoStackingData) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE);
        intent.putExtra(ConstantKt.getEXTRA_KUPON_CODE(), promoStackingData.getPromoCodeSafe());
        intent.putExtra(ConstantKt.getEXTRA_IS_USE(), true);
        intent.putExtra(ConstantKt.getONE_CLICK_SHIPMENT(), isOneClickShipment());
        intent.putExtra(ConstantKt.getPAGE_TRACKING(), TrackingPromoCheckoutConstantKt.getFROM_CHECKOUT());
        intent.putExtra(ConstantKt.getCHECK_PROMO_CODE_FIRST_STEP_PARAM(), promo);
        return intent;
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
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentAdapter.getShipmentCartItemModelList();
        ShipmentCartItemModel selectedShipmentCartItemModel = null;
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            if (shipmentCartItemModel.getCartString().equals(cartString)) {
                selectedShipmentCartItemModel = shipmentCartItemModel;
                break;
            }
        }

        ArrayList<CartItemDataVoucher> cartItemDataVoucherArrayList = new ArrayList<>();
        if (selectedShipmentCartItemModel != null) {
            for (CartItemModel cartItemModel : selectedShipmentCartItemModel.getCartItemModels()) {
                CartItemDataVoucher cartItemDataVoucher = new CartItemDataVoucher();
                cartItemDataVoucher.setProductId(cartItemModel.getProductId());
                cartItemDataVoucher.setProductName(cartItemModel.getName());
                cartItemDataVoucherArrayList.add(cartItemDataVoucher);
            }
        }

        MerchantVoucherListBottomSheetFragment merchantVoucherListBottomSheetFragment =
                MerchantVoucherListBottomSheetFragment.newInstance(shopId, cartString, promo, "shipment", cartItemDataVoucherArrayList);
        merchantVoucherListBottomSheetFragment.setActionListener(this);
        merchantVoucherListBottomSheetFragment.show(getFragmentManager(), "");
        checkoutAnalyticsCourierSelection.eventClickShowMerchantVoucherList();
    }

    @Override
    public void onCartPromoCancelVoucherPromoGlobalClicked(PromoStackingData cartPromoGlobal, int position) {
        ArrayList<String> promoCodes = new ArrayList<>();
        promoCodes.add(cartPromoGlobal.getPromoCode());
        shipmentPresenter.cancelAutoApplyPromoStack(INDEX_PROMO_GLOBAL, promoCodes, false, "global");
        if (isToogleYearEndPromoOn()) {
            shipmentAdapter.cancelAllCourierPromo();
        }
    }

    @Override
    public void onCancelVoucherMerchantClicked(String promoMerchantCode, int position, boolean ignoreAPIResponse) {
        checkoutAnalyticsCourierSelection.eventClickHapusPromoXOnTicker(promoMerchantCode);
        ArrayList<String> promoMerchantCodes = new ArrayList<>();
        promoMerchantCodes.add(promoMerchantCode);
        shipmentPresenter.cancelAutoApplyPromoStack(position, promoMerchantCodes, ignoreAPIResponse, "merchant");
        if (isToogleYearEndPromoOn()) {
            shipmentAdapter.cancelAllCourierPromo();
        }
    }

    @Override
    public void onCancelVoucherLogisticClicked(String pslCode, int position) {
        checkoutAnalyticsCourierSelection.eventCancelPromoStackingLogistic();
        ArrayList<String> codes = new ArrayList<>();
        codes.add(pslCode);
        shipmentPresenter.cancelAutoApplyPromoStack(position, codes, false, "logistic");
        if (isToogleYearEndPromoOn()) {
            shipmentAdapter.cancelAllCourierPromo();
        }
    }

    @Override
    public void onPromoGlobalTrackingImpression(PromoStackingData cartPromoGlobal) {
        trackingPromoCheckoutUtil.checkoutImpressionTicker(cartPromoGlobal.getDescription());
    }

    @Override
    public void onPromoGlobalTrackingCancelled(PromoStackingData cartPromoGlobal, int position) {
        sendAnalyticsOnClickCancelUsePromoCodeAndCouponBanner();
    }

    @Override
    public void onDataEnableToCheckout() {
        shipmentAdapter.updateCheckoutButtonData(null);
    }

    @Override
    public void onDataDisableToCheckout(String message) {
        shipmentAdapter.updateCheckoutButtonData(null);
    }

    @Override
    public void onCheckoutValidationResult(boolean result, Object shipmentData,
                                           int errorPosition, int requestCode) {
        if (shipmentData == null && result) {
            if (shipmentPresenter.isIneligbilePromoDialogEnabled()) {
                ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList = new ArrayList<>();
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
                    showPromoNotEligibleDialog(notEligiblePromoHolderdataList, requestCode);
                } else {
                    doCheckout(requestCode);
                }
            } else {
                boolean globalPromoRedState = shipmentAdapter.getPromoGlobalStackData() != null &&
                        shipmentAdapter.getPromoGlobalStackData().getState() == TickerPromoStackingCheckoutView.State.FAILED;

                boolean merchantOrLogisticPromoRedState = false;
                String merchantPromoRedStateDescription = "";
                int merchantIndex = 0;
                if (!globalPromoRedState) {
                    for (ShipmentCartItemModel shipmentCartItemModel : shipmentAdapter.getShipmentCartItemModelList()) {
                        if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null &&
                                shipmentCartItemModel.getVoucherOrdersItemUiModel().getMessage().getState().equalsIgnoreCase("red")) {
                            merchantOrLogisticPromoRedState = true;
                            merchantPromoRedStateDescription = shipmentCartItemModel.getVoucherOrdersItemUiModel().getMessage().getText();
                            merchantIndex = shipmentAdapter.getShipmentDataList().indexOf(shipmentCartItemModel);
                            break;
                        }
                        if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null &&
                                shipmentCartItemModel.getVoucherLogisticItemUiModel().getMessage().getState().equalsIgnoreCase("red")) {
                            merchantOrLogisticPromoRedState = true;
                            merchantPromoRedStateDescription = shipmentCartItemModel.getVoucherLogisticItemUiModel().getMessage().getText();
                            merchantIndex = shipmentAdapter.getShipmentDataList().indexOf(shipmentCartItemModel);
                            break;
                        }
                    }
                }

                if (globalPromoRedState) {
                    rvShipment.smoothScrollToPosition(0);
                    showToastError(shipmentAdapter.getPromoGlobalStackData().getDescription());
                    sendAnalyticsPromoRedState();
                } else if (merchantOrLogisticPromoRedState) {
                    if (merchantIndex > 0) {
                        rvShipment.smoothScrollToPosition(merchantIndex);
                    }
                    showToastError(merchantPromoRedStateDescription);
                    sendAnalyticsPromoRedState();
                } else {
                    doCheckout(requestCode);
                }
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
            onDataDisableToCheckout(null);
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

    private void doCheckout(int requestCode) {
        CheckPromoParam checkPromoParam = new CheckPromoParam();
        checkPromoParam.setPromo(generateCheckPromoFirstStepParam());

        if(hasInsurance) {
            mTrackerMacroInsurance.eventClickPaymentMethodWithInsurance(shipmentAdapter.getInsuranceProductId(),
                    shipmentAdapter.getInsuranceTitle());
        }

        switch (requestCode) {
            case REQUEST_CODE_NORMAL_CHECKOUT:
                shipmentPresenter.processSaveShipmentState();
                shipmentPresenter.processCheckout(checkPromoParam, hasInsurance, isOneClickShipment(), isTradeIn(), getDeviceId(), getCheckoutLeasingId());
                break;
            case REQUEST_CODE_COD:
                shipmentPresenter.proceedCodCheckout(checkPromoParam, hasInsurance, isOneClickShipment(), isTradeIn(), getDeviceId(), getCheckoutLeasingId());
        }
    }

    @Override
    public void onClickDetailPromoGlobal(PromoStackingData dataGlobal, int position) {
        trackingPromoCheckoutUtil.checkoutClickTicker(dataGlobal.getDescription());

        Promo promo = generateCheckPromoFirstStepParam();
        if (dataGlobal.getTypePromo() == PromoStackingData.CREATOR.getTYPE_COUPON()) {
            Intent intent = getIntentToPromoDetail(promo, dataGlobal);
            startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        } else {
            Intent intent = getIntentToPromoList(promo);
            startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onPriorityChecked(int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(() -> {
                shipmentAdapter.updateShipmentCostModel();
                shipmentAdapter.updateItemAndTotalCost(position);
            });
        } else {
            shipmentAdapter.updateShipmentCostModel();
            shipmentAdapter.updateItemAndTotalCost(position);
        }
    }

    @Override
    public void onInsuranceChecked(final int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(() -> {
                shipmentAdapter.updateShipmentCostModel();
                shipmentAdapter.updateItemAndTotalCost(position);
                shipmentAdapter.updateInsuranceTncVisibility();
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
            rvShipment.post(() -> shipmentAdapter.notifyItemChanged(position));
        } else {
            shipmentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onSubTotalItemClicked(int position) {
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
    public void onOntimeDeliveryClicked(String url) {
        Intent intent = CheckoutWebViewActivity.newInstance(getContext(), url,
                getString(R.string.title_activity_checkout_tnc_webview));
        startActivity(intent);
    }

    @Override
    public void onImpressionOntimeDelivery(String message) {
        checkoutAnalyticsCourierSelection.eventViewImpressionOntimeDeliveryGuarantee(message);
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
            rvShipment.post(() -> shipmentAdapter.updateDonation(checked));
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
    public void onLogisticPromoChosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                      CourierItemData courierData, RecipientAddressModel recipientAddressModel,
                                      int cartPosition, ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                      String promoCode, int selectedServiceId) {
        onShippingDurationChoosen(shippingCourierViewModels, courierData, recipientAddressModel,
                cartPosition, selectedServiceId, serviceData, flagNeedToSetPinpoint,
                false, false);
        String cartString = shipmentAdapter.getShipmentCartItemModelByIndex(cartPosition).getCartString();
        if (!flagNeedToSetPinpoint)
            shipmentPresenter.processCheckPromoStackingLogisticPromo(cartPosition, cartString, promoCode);
    }

    @Override
    public void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                          CourierItemData recommendedCourier,
                                          RecipientAddressModel recipientAddressModel,
                                          int cartItemPosition, int selectedServiceId,
                                          ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                          boolean isDurationClick, boolean isClearPromo) {
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
                    if (!stillHasAppliedPromo()) {
                        resetPromoBenefit();
                    }
                    shipmentAdapter.clearTotalPromoStackAmount();
                    shipmentAdapter.updateShipmentCostModel();
                    shipmentAdapter.updateCheckoutButtonData(null);
                }
                shipmentAdapter.setSelectedCourier(cartItemPosition, recommendedCourier);
                shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
                shipmentAdapter.setShippingCourierViewModels(shippingCourierViewModels, recommendedCourier, cartItemPosition);
                if (!TextUtils.isEmpty(recommendedCourier.getPromoCode()) && isDurationClick) {
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
            checkoutAnalyticsCourierSelection.eventViewCourierImpressionErrorCourierNoAvailable();

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

        navigateToPinpointActivity(locationPass);
    }

    private void navigateToPinpointActivity(LocationPass locationPass) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.GEOLOCATION);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass);
        bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true);
        intent.putExtras(bundle);
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
                String pslCode = RatesDataConverter.getLogisticPromoCode(shipmentCartItemModel);
                ArrayList<Product> products = getProductForRatesRequest(shipmentCartItemModel);
                shippingDurationBottomsheet = ShippingDurationBottomsheet.newInstance(
                        shipmentDetailData, shipmentAdapter.getLastServiceId(), shopShipmentList,
                        recipientAddressModel, cartPosition, codHistory,
                        shipmentCartItemModel.getIsLeasingProduct(), pslCode, products, shipmentCartItemModel.getCartString(), shipmentCartItemModel.isOrderPrioritasDisable());
                shippingDurationBottomsheet.setShippingDurationBottomsheetListener(this);

                if (getActivity() != null) {
                    shippingDurationBottomsheet.show(getActivity().getSupportFragmentManager(), null);
                }
            }
        }
    }

    private ArrayList<Product> getProductForRatesRequest(ShipmentCartItemModel shipmentCartItemModel) {
        ArrayList<Product> products = new ArrayList<>();
        if (shipmentCartItemModel != null && shipmentCartItemModel.getCartItemModels() != null) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                Product product = new Product();
                product.setProductId(cartItemModel.getProductId());
                product.setFreeShipping(cartItemModel.isFreeShipping());

                products.add(product);
            }
        }

        return products;
    }

    @Override
    public void onChangeShippingCourier(List<ShippingCourierViewModel> shippingCourierViewModels,
                                        RecipientAddressModel recipientAddressModel,
                                        ShipmentCartItemModel shipmentCartItemModel,
                                        List<ShopShipment> shopShipmentList,
                                        int cartPosition) {
        sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel);
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
                        shipmentCartItemModel, shopShipmentList, false,
                        getProductForRatesRequest(shipmentCartItemModel), shipmentCartItemModel.getCartString());
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
            shipmentPresenter.processGetCourierRecommendation(
                    shipperId, spId, itemPosition, shipmentDetailData,
                    shipmentCartItemModel, shopShipmentList, true,
                    getProductForRatesRequest(shipmentCartItemModel),
                    shipmentCartItemModel.getCartString());
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
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(() -> {
                shipmentAdapter.updateShipmentCostModel();
                shipmentAdapter.updateItemAndTotalCost(position);
                shipmentAdapter.updateInsuranceTncVisibility();
            });
        } else {
            shipmentAdapter.updateShipmentCostModel();
            shipmentAdapter.updateItemAndTotalCost(position);
            shipmentAdapter.updateInsuranceTncVisibility();
        }
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
            checkoutAnalyticsCourierSelection.eventClickGantiNomor(isTradeIn());
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
        shipmentAdapter.checkDropshipperValidation(REQUEST_CODE_COD);
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
    @Override
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
    public void onSuccessClearPromoStack(int shopIndex, String voucherType) {
        setBenefitSummaryInfoUiModel(null);

        if (shopIndex == INDEX_PROMO_GLOBAL) {
            PromoStackingData promoStackingData = shipmentAdapter.getPromoGlobalStackData();
            promoStackingData.setState(TickerPromoStackingCheckoutView.State.EMPTY);
            promoStackingData.setAmount(0);
            promoStackingData.setPromoCode("");
            promoStackingData.setDescription("");
            promoStackingData.setTitle(promoStackingData.getTitleDefault());
            promoStackingData.setCounterLabel(promoStackingData.getCounterLabelDefault());
            promoStackingData.setTrackingDetailUiModels(new ArrayList<>());
            shipmentAdapter.updateItemPromoStackVoucher(promoStackingData);
        } else {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(shopIndex);
            if (shipmentCartItemModel != null) {
                if (voucherType.equals("merchant") && shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                    shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
                    shipmentAdapter.notifyItemChanged(shopIndex);
                } else if (voucherType.equals("logistic") && shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                    shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                    shipmentAdapter.notifyItemChanged(shopIndex);
                }
            }
        }

        if (!stillHasAppliedPromo()) {
            clearPromoTrackingData();
            resetPromoBenefit();
        }

        shipmentAdapter.clearTotalPromoStackAmount();
        shipmentAdapter.updateShipmentCostModel();
        shipmentAdapter.updateCheckoutButtonData(null);
        shipmentAdapter.notifyItemChanged(shipmentAdapter.getShipmentCostPosition());
        shipmentAdapter.checkHasSelectAllCourier(false);
        shipmentPresenter.setCouponStateChanged(true);
    }

    @Override
    public void onSuccessClearPromoLogistic() {
        if (!stillHasAppliedPromo()) {
            clearPromoTrackingData();
            resetPromoBenefit();
        }
    }

    @Override
    public void triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(String transactionId) {
        List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentAdapter.getPromoGlobalStackData(), shipmentAdapter.getShipmentCartItemModelList());
        shipmentPresenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
                dataCheckoutRequests,
                hasInsurance,
                EnhancedECommerceActionField.STEP_4,
                isTradeIn() ? ConstantTransactionAnalytics.EventAction.CLICK_BAYAR : ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.EventLabel.SUCCESS,
                getCheckoutLeasingId());

        sendEESGTMv5(dataCheckoutRequests, 4, "click payment option button", transactionId,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN, ConstantTransactionAnalytics.EventLabel.SUCCESS);
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
    public void onSuccessCheckPromoMerchantFirstStep(@NotNull ResponseGetPromoStackUiModel promoData, @NotNull String promoCode) {
        onSuccessCheckPromoFirstStep(promoData);
    }

    @Override
    public void onSuccessCheckPromoFirstStepAfterClash(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, String promoCode) {
        onSuccessCheckPromoFirstStep(responseGetPromoStackUiModel);
    }

    @Override
    public void onSuccessCheckPromoFirstStep(@NotNull ResponseGetPromoStackUiModel promoData) {
        // Update global promo state
        PromoStackingData promoStackingGlobalData = shipmentAdapter.getPromoGlobalStackData();
        if (promoData.getData().getCodes().size() > 0) {
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
                            log.setCouponAmountRaw(voucherOrdersItemUiModel.getDiscountAmount());
                            log.setMessage(voucherOrdersItemUiModel.getMessage());
                            shipmentCartItemModel.setVoucherLogisticItemUiModel(log);
                        }
                        break;
                    }
                }
            }

            if (promoData.getData().getTrackingDetailUiModel().size() > 0) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                    for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                        for (TrackingDetailUiModel trackingDetailUiModel : promoData.getData().getTrackingDetailUiModel()) {
                            if (trackingDetailUiModel.getProductId() == cartItemModel.getProductId() &&
                                    cartItemModel.getAnalyticsProductCheckoutData() != null) {
                                cartItemModel.getAnalyticsProductCheckoutData().setPromoCode(trackingDetailUiModel.getPromoCodesTracking());
                                cartItemModel.getAnalyticsProductCheckoutData().setPromoDetails(trackingDetailUiModel.getPromoDetailsTracking());
                            }
                        }
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
                if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                    shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
                }
                if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                    shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                }
            }
        }

        shipmentAdapter.notifyDataSetChanged();
        if (!stillHasAppliedPromo()) {
            clearPromoTrackingData();
        }
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

    private void showPromoNotEligibleDialog(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList, int requestCode) {
        if (getActivity() != null && promoNotEligibleBottomsheet == null) {
            promoNotEligibleBottomsheet = PromoNotEligibleBottomsheet.Companion.createInstance();
            promoNotEligibleBottomsheet.setActionListener(this);
            promoNotEligibleBottomsheet.setDismissListener(() -> checkoutAnalyticsCourierSelection.eventClickBatalOnErrorPromoConfirmation());
        }
        promoNotEligibleBottomsheet.setNotEligiblePromoHolderDataList(notEligiblePromoHolderdataList);
        promoNotEligibleBottomsheet.setCheckoutType(requestCode);
        promoNotEligibleBottomsheet.show(getFragmentManager(), "");
        checkoutAnalyticsCourierSelection.eventViewPopupErrorPromoConfirmation();
    }

    @Override
    public void onButtonContinueClicked(int checkoutType) {
        if (promoNotEligibleBottomsheet != null) {
            checkoutAnalyticsCourierSelection.eventClickLanjutkanOnErrorPromoConfirmation();
            ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList = promoNotEligibleBottomsheet.getNotEligiblePromoHolderDataList();
            promoNotEligibleBottomsheet.dismiss();
            shipmentPresenter.cancelNotEligiblePromo(notEligiblePromoHolderdataArrayList, checkoutType);
        }
    }

    @Override
    public void onShow() {
        if (promoNotEligibleBottomsheet != null) {
            BottomSheetBehavior bottomSheetBehavior = promoNotEligibleBottomsheet.getBottomSheetBehavior();
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        }
    }

    @Override
    public boolean isInsuranceEnabled() {
        return isInsuranceEnabled;
    }

    @Override
    public void removeIneligiblePromo(int checkoutType, ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList) {
        for (NotEligiblePromoHolderdata notEligiblePromoHolderdata : notEligiblePromoHolderdataArrayList) {
            if (shipmentAdapter.getPromoGlobalStackData() != null &&
                    shipmentAdapter.getPromoGlobalStackData().getPromoCode().equals(notEligiblePromoHolderdata.getPromoCode())) {
                PromoStackingData promoStackingData = shipmentAdapter.getPromoGlobalStackData();
                promoStackingData.setState(TickerPromoStackingCheckoutView.State.EMPTY);
                promoStackingData.setAmount(0);
                promoStackingData.setPromoCode("");
                promoStackingData.setDescription("");
                promoStackingData.setTitle(promoStackingData.getTitleDefault());
                promoStackingData.setCounterLabel(promoStackingData.getCounterLabelDefault());
                break;
            }
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentAdapter.getShipmentCartItemModelList()) {
                if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null &&
                        shipmentCartItemModel.getVoucherOrdersItemUiModel().getCode().equals(notEligiblePromoHolderdata.getPromoCode())) {
                    shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
                    break;
                }
                if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null &&
                        shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode().equals(notEligiblePromoHolderdata.getPromoCode())) {
                    shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                    break;
                }
            }
        }
        doCheckout(checkoutType);
    }

    @Override
    public void deleteMacroInsurance(@NotNull ArrayList<InsuranceCartDigitalProduct> insuranceCartDigitalProductList, boolean showconfirmationDialog) {

    }

    @Override
    public void sendEventChangeInsuranceState(boolean isChecked, String title) {

    }

    @Override
    public void sendEventDeleteInsurance(String title) {

    }

    @Override
    public void updateInsuranceProductData(@NotNull InsuranceCartShops insuranceCartShops, @NotNull ArrayList<UpdateInsuranceProductApplicationDetails> updateInsuranceProductApplicationDetailsArrayList) {

    }

    @Override
    public void onInsuranceSelectStateChanges() {

    }

    @Override
    public void sendEventInsuranceImpression(String title) {

    }

    @Override
    public void sendEventInsuranceImpressionForShipment(String title) {
        mTrackerMacroInsurance.eventImpressionOfInsuranceProductOnCheckout(title);
    }

    @Override
    public void updateTickerAnnouncementMessage() {
        int index = shipmentAdapter.getTickerAnnouncementHolderDataIndex();
        if (index != RecyclerView.NO_POSITION) {
            onNeedUpdateViewItem(index);
        }
    }

    @Override
    public void setPromoBenefit(List<SummariesUiModel> summariesUiModels) {
        shipmentAdapter.setPromoBenefit(summariesUiModels);
        onNeedUpdateViewItem(shipmentAdapter.getShipmentCostPosition());
    }

    @Override
    public void resetPromoBenefit() {
        shipmentAdapter.resetPromoBenefit();
        onNeedUpdateViewItem(shipmentAdapter.getShipmentCostPosition());
    }
}
