package com.tokopedia.checkout.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.internal.ApplinkConstInternalPayment;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.analytics.CheckoutAnalyticsMacroInsurance;
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.checkout.analytics.CornerAnalytics;
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData;
import com.tokopedia.checkout.domain.model.checkout.TrackerData;
import com.tokopedia.checkout.subfeature.cod_bottomsheet.CodBottomSheetFragment;
import com.tokopedia.checkout.subfeature.multiple_address.view.MultipleAddressFormActivity;
import com.tokopedia.checkout.subfeature.webview.CheckoutWebViewActivity;
import com.tokopedia.checkout.view.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.checkout.view.di.CheckoutModule;
import com.tokopedia.checkout.view.di.DaggerCheckoutComponent;
import com.tokopedia.checkout.view.dialog.ExpiredTimeDialog;
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.uimodel.ShipmentNotifierModel;
import com.tokopedia.common.payment.PaymentConstant;
import com.tokopedia.common.payment.model.PaymentPassData;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticdata.data.analytics.CodAnalytics;
import com.tokopedia.logisticdata.data.constant.LogisticConstant;
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel;
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment;
import com.tokopedia.purchase_platform.common.constant.CartConstant;
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant;
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.CheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.cod.Data;
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult;
import com.tokopedia.purchase_platform.common.feature.insurance.InsuranceItemActionListener;
import com.tokopedia.purchase_platform.common.feature.insurance.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartDigitalProduct;
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartResponse;
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartShopItems;
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartShops;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.TrackingDetailsItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel;
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata;
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleActionListener;
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleBottomsheet;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.time.TimeHelper;

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
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.PARAM_CHECKOUT;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.PARAM_DEFAULT;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.RESULT_CODE_COUPON_STATE_CHANGED;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT;
import static com.tokopedia.purchase_platform.common.constant.Constant.EXTRA_CHECKOUT_REQUEST;
import static com.tokopedia.purchase_platform.common.constant.PromoConstantKt.PAGE_CHECKOUT;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_INSURANCE_RECOMMENDATION;

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View,
        ShipmentContract.AnalyticsActionListener, ShipmentAdapterActionListener,
        ShippingDurationBottomsheetListener, ShippingCourierBottomsheetListener,
        PromoNotEligibleActionListener, InsuranceItemActionListener {

    private static final int REQUEST_CODE_EDIT_ADDRESS = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    private static final int REQUEST_CODE_COURIER_PINPOINT = 13;
    private static final int REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS = 55;
    private static final int REQUEST_CODE_PROMO = 954;

    public static final int INDEX_PROMO_GLOBAL = -1;

    private static final int REQUEST_CODE_NORMAL_CHECKOUT = 0;
    private static final int REQUEST_CODE_COD = 1218;
    private static final String SHIPMENT_TRACE = "mp_shipment";

    public static final String ARG_IS_ONE_CLICK_SHIPMENT = "ARG_IS_ONE_CLICK_SHIPMENT";
    public static final String ARG_CHECKOUT_LEASING_ID = "ARG_CHECKOUT_LEASING_ID";
    private static final String NO_PINPOINT_ETD = "Belum Pinpoint";
    private static final String EXTRA_STATE_SHIPMENT_SELECTION = "EXTRA_STATE_SHIPMENT_SELECTION";
    private static final String DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION = "LAST_CHOOSE_COURIER_ITEM_POSITION";
    private static final String DATA_STATE_LAST_CHOOSEN_SERVICE_ID = "DATA_STATE_LAST_CHOOSEN_SERVICE_ID";
    public static final String BOTTOM_SHEET_TAG = "BOTTOM_SHEET_TAG";

    private RecyclerView rvShipment;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayout llNetworkErrorView;
    private AlertDialog progressDialogNormal;
    private ShippingDurationBottomsheet shippingDurationBottomsheet;
    private ShippingCourierBottomsheet shippingCourierBottomsheet;
    private PerformanceMonitoring shipmentTracePerformance;
    private boolean isShipmentTraceStopped;
    private String cornerId;
    private PromoNotEligibleBottomsheet promoNotEligibleBottomsheet;

    @Inject
    ShipmentAdapter shipmentAdapter;
    @Inject
    ShipmentContract.Presenter shipmentPresenter;
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
    UserSessionInterface userSessionInterface;

    SaveInstanceCacheManager saveInstanceCacheManager;
    TickerAnnouncementHolderData savedTickerAnnouncementModel;
    List<ShipmentCartItemModel> savedShipmentCartItemModelList;
    ShipmentCostModel savedShipmentCostModel;
    EgoldAttributeModel savedEgoldAttributeModel;
    RecipientAddressModel savedRecipientAddressModel;
    ShipmentDonationModel savedShipmentDonationModel;
    BenefitSummaryInfoUiModel benefitSummaryInfoUiModel;
    ShipmentButtonPaymentModel savedShipmentButtonPaymentModel;
    LastApplyUiModel savedLastApplyData;

    private HashSet<ShipmentSelectionStateData> shipmentSelectionStateDataHashSet = new HashSet<>();
    private boolean hasInsurance = false;
    private boolean isInsuranceEnabled = false;
    private boolean hasRunningApiCall = false;
    private ArrayList<String> bboPromoCodes = new ArrayList<>();

    private Subscription delayScrollToFirstShopSubscription;

    // count down component
    private View cdLayout;
    private CountDownView cdView;
    private Typography cdText;

    public static ShipmentFragment newInstance(boolean isOneClickShipment,
                                               String leasingId,
                                               Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
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
                savedRecipientAddressModel = saveInstanceCacheManager.get(RecipientAddressModel.class.getSimpleName(), RecipientAddressModel.class);
                savedShipmentCostModel = saveInstanceCacheManager.get(ShipmentCostModel.class.getSimpleName(), ShipmentCostModel.class);
                savedEgoldAttributeModel = saveInstanceCacheManager.get(EgoldAttributeModel.class.getSimpleName(), EgoldAttributeModel.class);
                savedShipmentDonationModel = saveInstanceCacheManager.get(ShipmentDonationModel.class.getSimpleName(), ShipmentDonationModel.class);
                savedShipmentButtonPaymentModel = saveInstanceCacheManager.get(ShipmentButtonPaymentModel.class.getSimpleName(), ShipmentButtonPaymentModel.class);
                savedLastApplyData = saveInstanceCacheManager.get(LastApplyUiModel.class.getSimpleName(), LastApplyUiModel.class);
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
    public void onStop() {
        super.onStop();
        hideLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (delayScrollToFirstShopSubscription != null) {
            delayScrollToFirstShopSubscription.unsubscribe();
        }
        shipmentPresenter.detachView();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
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
        cdLayout = view.findViewById(R.id.partial_countdown);
        cdView = view.findViewById(R.id.count_down);
        cdText = view.findViewById(R.id.tv_count_down);

        progressDialogNormal = new AlertDialog.Builder(getActivity())
                .setView(com.tokopedia.purchase_platform.common.R.layout.purchase_platform_progress_dialog_view)
                .setCancelable(false)
                .create();

        ((SimpleItemAnimator) rvShipment.getItemAnimator()).setSupportsChangeAnimations(false);
        rvShipment.addItemDecoration(new ShipmentItemDecoration());
    }

    @Override
    public void onResume() {
        super.onResume();
        checkCampaignTimer();
        restoreProgressLoading();
    }

    private void restoreProgressLoading() {
        if (hasRunningApiCall) {
            showLoading();
        }
    }

    @Override
    public void setHasRunningApiCall(boolean hasRunningApiCall) {
        this.hasRunningApiCall = hasRunningApiCall;
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
            shipmentPresenter.setRecipientAddressModel(savedRecipientAddressModel);
            shipmentPresenter.setShipmentCostModel(savedShipmentCostModel);
            shipmentPresenter.setShipmentDonationModel(savedShipmentDonationModel);
            shipmentPresenter.setShipmentButtonPaymentModel(savedShipmentButtonPaymentModel);
            shipmentPresenter.setEgoldAttributeModel(savedEgoldAttributeModel);
            shipmentAdapter.setLastChooseCourierItemPosition(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION));
            shipmentAdapter.setLastServiceId(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID));
            shipmentPresenter.setLastApplyData(savedLastApplyData);
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
            saveInstanceCacheManager.put(RecipientAddressModel.class.getSimpleName(), shipmentPresenter.getRecipientAddressModel());
            saveInstanceCacheManager.put(ShipmentCostModel.class.getSimpleName(), shipmentPresenter.getShipmentCostModel());
            saveInstanceCacheManager.put(ShipmentDonationModel.class.getSimpleName(), shipmentPresenter.getShipmentDonationModel());
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
                                      RecipientAddressModel recipientAddressModel,
                                      List<ShipmentCartItemModel> shipmentCartItemModelList,
                                      ShipmentDonationModel shipmentDonationModel,
                                      LastApplyUiModel lastApplyUiModel,
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

        if (shipmentDonationModel != null) {
            shipmentAdapter.addShipmentDonationModel(shipmentDonationModel);
            if (shipmentDonationModel.isChecked()) {
                checkoutAnalyticsCourierSelection.eventViewAutoCheckDonation(userSessionInterface.getUserId());
            }
        }

        if (egoldAttributeModel != null && egoldAttributeModel.isEligible()) {
            shipmentAdapter.updateEgold(false);
            shipmentAdapter.addEgoldAttributeData(egoldAttributeModel);
        }

        if (shipmentCartItemModelList.size() > 0) {
            // Don't show promo section if all shop is error
            int errorShopCount = 0;
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel.isError()) {
                    errorShopCount++;
                }
            }
            if (errorShopCount == 0 || errorShopCount < shipmentCartItemModelList.size()) {
                if (lastApplyUiModel != null && !lastApplyUiModel.getAdditionalInfo().getErrorDetail().getMessage().isEmpty()) {
                    PromoRevampAnalytics.INSTANCE.eventCartViewPromoMessage(lastApplyUiModel.getAdditionalInfo().getErrorDetail().getMessage());
                }
                shipmentAdapter.addLastApplyUiDataModel(lastApplyUiModel);
            }
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
        if (shipmentCartItemModelList.size() > 0) {
            addShippingCompletionTicker(shipmentCartItemModelList.get(0).isEligibleNewShippingExperience());
        }

        if (isInitialRender) {
            sendEEStep2();
        }

        if (isReloadAfterPriceChangeHigher) {
            delayScrollToFirstShop();
        }
    }

    private void addShippingCompletionTicker(boolean isEligibleNewShippingExperience) {
        if (isEligibleNewShippingExperience) {
            shipmentAdapter.updateShippingCompletionTickerVisibility();
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

        List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentAdapter.getShipmentCartItemModelList());
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
        int isDonation = shipmentPresenter.getShipmentDonationModel() != null && shipmentPresenter.getShipmentDonationModel().isChecked() ? 1 : 0;
        CheckoutRequest checkoutRequest = shipmentPresenter.generateCheckoutRequest(
                dataCheckoutRequests, hasInsurance, isDonation, getCheckoutLeasingId()
        );
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
        if (progressDialogNormal != null && !progressDialogNormal.isShowing()) {
            progressDialogNormal.show();
        }
    }

    @Override
    public void hideLoading() {
        if (progressDialogNormal != null && progressDialogNormal.isShowing()) {
            progressDialogNormal.dismiss();
        }
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void showToastNormal(String message) {
        if (getView() != null && getActivity() != null) {
            Toaster.make(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getActivity().getString(R.string.label_action_snackbar_close), view -> {
            });
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void showToastError(String message) {
        if (getView() != null && getActivity() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getActivity().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown);
            }
            if (shipmentAdapter == null || shipmentAdapter.getItemCount() == 0) {
                renderErrorPage(message);
            } else {
                Toaster.make(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getActivity().getString(R.string.label_action_snackbar_close), view -> {
                });
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
    public void onCacheExpired(String message) {
        if (getActivity() != null && getView() != null) {
            Intent intent = new Intent();
            intent.putExtra(CheckoutConstant.EXTRA_CACHE_EXPIRED_ERROR_MESSAGE, message);
            getActivity().setResult(CheckoutConstant.RESULT_CHECKOUT_CACHE_EXPIRED, intent);
            getActivity().finish();
        }
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
    public void renderCheckoutPage(boolean isInitialRender, boolean isReloadAfterPriceChangeHigher, boolean isOneClickShipment) {
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        if (recipientAddressModel != null && isOneClickShipment) {
            recipientAddressModel.setDisableMultipleAddress(true);
        }
        shipmentAdapter.setShowOnboarding(shipmentPresenter.isShowOnboarding());
        setCampaignTimer();
        initRecyclerViewData(
                shipmentPresenter.getTickerAnnouncementHolderData(),
                recipientAddressModel,
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getLastApplyData(),
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
    public void renderDataChanged() {
        initRecyclerViewData(
                shipmentPresenter.getTickerAnnouncementHolderData(),
                shipmentPresenter.getRecipientAddressModel(),
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getLastApplyData(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getEgoldAttributeModel(),
                shipmentPresenter.getShipmentButtonPaymentModel(),
                false,
                false
        );
    }

    @Override
    public void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData shipmentAddressFormData) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.CHECKOUT_ADDRESS_SELECTION);
        ;
        if (shipmentAddressFormData.getKeroDiscomToken() != null &&
                shipmentAddressFormData.getKeroUnixTime() != 0) {
            Token token = new Token();
            token.setUt(shipmentAddressFormData.getKeroUnixTime());
            token.setDistrictRecommendation(shipmentAddressFormData.getKeroDiscomToken());

            intent.putExtra(CheckoutConstant.EXTRA_DISTRICT_RECOMMENDATION_TOKEN, token);
            intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, CheckoutConstant.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS);
        } else {
            intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, CheckoutConstant.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS);
        }

        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS);
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
        DialogUnify createTicketDialog = new DialogUnify(getActivityContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE);
        createTicketDialog.setTitle(checkoutData.getErrorReporter().getTexts().getSubmitTitle());
        createTicketDialog.setDescription(checkoutData.getErrorReporter().getTexts().getSubmitDescription());
        createTicketDialog.setSecondaryCTAText(checkoutData.getErrorReporter().getTexts().getCancelButton());
        createTicketDialog.setSecondaryCTAClickListener(() -> {
            checkoutAnalyticsCourierSelection.eventClickCloseOnHelpPopUpInCheckout();
            createTicketDialog.dismiss();
            return Unit.INSTANCE;
        });
        createTicketDialog.setPrimaryCTAText(checkoutData.getErrorReporter().getTexts().getSubmitButton());
        createTicketDialog.setPrimaryCTAClickListener(() -> {
            checkoutAnalyticsCourierSelection.eventClickReportOnHelpPopUpInCheckout();
            createTicketDialog.dismiss();
            shipmentPresenter.processSubmitHelpTicket(checkoutData);
            return Unit.INSTANCE;
        });
        createTicketDialog.show();
        checkoutAnalyticsCourierSelection.eventViewHelpPopUpAfterErrorInCheckout();
    }

    @Override
    public void renderCheckoutPriceUpdated(PriceValidationData priceValidationData) {
        if (getActivity() != null) {
            com.tokopedia.checkout.domain.model.checkout.MessageData messageData =
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
        DialogUnify successTicketDialog = new DialogUnify(getActivity(), DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE);
        successTicketDialog.setTitle(submitTicketResult.getTexts().getSubmitTitle());
        successTicketDialog.setDescription(submitTicketResult.getTexts().getSubmitDescription());
        successTicketDialog.setPrimaryCTAText(submitTicketResult.getTexts().getSuccessButton());
        successTicketDialog.setPrimaryCTAClickListener(() -> {
            getActivity().finish();
            return Unit.INSTANCE;
        });
        successTicketDialog.show();
    }

    @Override
    public void renderCheckoutCartError(String message) {
        if (message.contains("Pre Order") && message.contains("Corner"))
            mTrackerCorner.sendViewCornerPoError();
        showToastError(message);
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

    public void renderPromoCheckoutFromCourierSuccess(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel, int itemPosition, boolean noToast) {
        if (!noToast && !shipmentAdapter.isCourierPromoStillExist()) {
            if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                showToastNormal(validateUsePromoRevampUiModel.getMessage().get(0));
            }
        }
        setCourierPromoApplied(itemPosition);
        updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel());
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
    public void renderErrorCheckPromoShipmentData(String message) {
        showToastError(message);
        shipmentAdapter.resetCourierPromoState();
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
            onChangeShippingDuration(shipmentCartItemModel, recipientAddressModel, position);
        }
    }

    @Override
    public void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition, boolean isTradeInDropOff) {
        shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition).setStateLoadingCourierState(false);
        if (isTradeInDropOff) {
            shipmentAdapter.setSelectedCourierTradeInPickup(courierItemData);
        } else {
            if (courierItemData.getLogPromoCode() != null) {
                String cartString = shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition).getCartString();

                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition);
                ValidateUsePromoRequest validateUsePromoRequest = generateValidateUsePromoRequest();
                if (courierItemData.getLogPromoCode() != null && courierItemData.getLogPromoCode().length() > 0) {
                    for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                        if (ordersItem.getUniqueId().equals(shipmentCartItemModel.getCartString()) &&
                                !ordersItem.getCodes().contains(courierItemData.getLogPromoCode())) {
                            ordersItem.getCodes().add(courierItemData.getLogPromoCode());
                            break;
                        }
                    }
                }

                shipmentPresenter.doValidateuseLogisticPromo(itemPosition, cartString, validateUsePromoRequest);
            }
            checkCourierPromo(courierItemData, itemPosition);
            shipmentAdapter.setSelectedCourier(itemPosition, courierItemData);
        }
        onNeedUpdateViewItem(itemPosition);
    }

    @Override
    public void renderCourierStateFailed(int itemPosition, boolean isTradeInDropOff) {
        ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition);
        if (shipmentCartItemModel != null) {
            shipmentCartItemModel.setStateLoadingCourierState(false);
            if (isTradeInDropOff) {
                shipmentCartItemModel.setStateHasLoadCourierTradeInDropOffState(true);
            } else {
                shipmentCartItemModel.setStateHasLoadCourierState(true);
            }
            onNeedUpdateViewItem(itemPosition);
        }
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
        String sessionId = "";
        Context context = getContext();
        if (context != null) {
            IrisSession irisSession = new IrisSession(context);
            sessionId = irisSession.getSessionId();

        }
        checkoutAnalyticsCourierSelection.sendEnhancedECommerceCheckout(
                stringObjectMap, sessionId, transactionId, isTradeIn(), eventAction, eventLabel
        );
        checkoutAnalyticsCourierSelection.flushEnhancedECommerceCheckout();
    }

    @Override
    public void sendAnalyticsOnClickChooseOtherAddressShipment() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickPilihAlamatLain();
    }

    @Override
    public void sendAnalyticsOnClickTopDonation() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickTopDonasi();
    }

    @Override
    public void sendAnalyticsOnClickChangeAddress() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat();
    }

    public String getCornerId() {
        return cornerId;
    }

    public void setCornerId(String cornerId) {
        this.cornerId = cornerId;
    }

    @Override
    public void renderChangeAddressSuccess() {
        shipmentPresenter.processInitialLoadCheckoutPage(
                true, isOneClickShipment(), isTradeIn(), true,
                false, shipmentAdapter.getAddressShipmentData().getCornerId(), getDeviceId(), getCheckoutLeasingId()
        );
    }

    @Override
    public void renderChangeAddressFailed() {
        RecipientAddressModel recipientAddressModel = shipmentAdapter.getAddressShipmentData();
        if (recipientAddressModel.getSelectedTabIndex() == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
            recipientAddressModel.setSelectedTabIndex(RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN);
        } else if (recipientAddressModel.getSelectedTabIndex() == RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN) {
            recipientAddressModel.setLocationDataModel(null);
            recipientAddressModel.setDropOffAddressDetail("");
            recipientAddressModel.setDropOffAddressName("");
        }
        onNeedUpdateViewItem(shipmentAdapter.getRecipientAddressModelPosition());
    }

    @Override
    public List<DataCheckoutRequest> generateNewCheckoutRequest(List<ShipmentCartItemModel> shipmentCartItemModelList,
                                                                boolean isAnalyticsPurpose) {
        ShipmentAdapter.RequestData requestData = shipmentAdapter.getRequestData(null, shipmentCartItemModelList, isAnalyticsPurpose);
        return requestData.getCheckoutRequestData();
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
        } else if (requestCode == REQUEST_CODE_COURIER_PINPOINT) {
            onResultFromCourierPinpoint(resultCode, data);
        } else if (requestCode == REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS) {
            onResultFromMultipleAddress(resultCode, data);
        } else if (requestCode == REQUEST_CODE_EDIT_ADDRESS) {
            onResultFromEditAddress();
        } else if (requestCode == LogisticConstant.REQUEST_CODE_PICK_DROP_OFF_TRADE_IN) {
            onResultFromSetTradeInPinpoint(data);
        } else if (requestCode == REQUEST_CODE_PROMO) {
            onResultFromPromo(resultCode, data);
        }
    }

    private void onResultFromPromo(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            shipmentPresenter.setCouponStateChanged(true);
            ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel = data.getParcelableExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_VALIDATE_USE_DATA_RESULT);
            if (validateUsePromoRevampUiModel != null) {
                String messageInfo = validateUsePromoRevampUiModel.getPromoUiModel().getAdditionalInfoUiModel().getErrorDetailUiModel().getMessage();
                if (messageInfo.length() > 0) {
                    showToastNormal(messageInfo);
                }
                shipmentPresenter.setValidateUsePromoRevampUiModel(validateUsePromoRevampUiModel);
                updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel());
            }

            ValidateUsePromoRequest validateUsePromoRequest = data.getParcelableExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_LAST_VALIDATE_USE_REQUEST);
            if (validateUsePromoRequest != null) {
                boolean stillHasPromo = false;
                for (String promoGlobalCode : validateUsePromoRequest.getCodes()) {
                    if (promoGlobalCode.length() > 0) {
                        stillHasPromo = true;
                        break;
                    }
                }

                if (!stillHasPromo) {
                    for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                        for (String promoMerchantCode : ordersItem.getCodes()) {
                            if (promoMerchantCode.length() > 0) {
                                stillHasPromo = true;
                                break;
                            }
                        }
                    }
                }

                shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest);
                if (!stillHasPromo) {
                    doResetButtonPromoCheckout();
                }
            }

            ClearPromoUiModel clearPromoUiModel = data.getParcelableExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_CLEAR_PROMO_RESULT);
            if (clearPromoUiModel != null) {
                PromoUiModel promoUiModel = new PromoUiModel();
                promoUiModel.setTitleDescription(clearPromoUiModel.getSuccessDataModel().getDefaultEmptyPromoMessage());

                TickerAnnouncementHolderData tickerAnnouncementHolderData = shipmentPresenter.getTickerAnnouncementHolderData();
                if (tickerAnnouncementHolderData != null && !TextUtils.isEmpty(clearPromoUiModel.getSuccessDataModel().getTickerMessage())) {
                    tickerAnnouncementHolderData.setMessage(clearPromoUiModel.getSuccessDataModel().getTickerMessage());
                    updateTickerAnnouncementMessage();
                }

                doUpdateButtonPromoCheckout(promoUiModel);
                shipmentPresenter.setValidateUsePromoRevampUiModel(null);
                shipmentAdapter.checkHasSelectAllCourier(false);
            }
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
                getActivity().setResult(RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                getActivity().finish();
            }
        } else if (data != null) {
            RecipientAddressModel recipientAddressModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_RECIPIENT_ADDRESS_DATA);
            ArrayList<ShipmentCartItemModel> shipmentCartItemModels = data.getParcelableArrayListExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_CART_TEM_LIST_DATA);
            shipmentPresenter.processReloadCheckoutPageFromMultipleAddress(recipientAddressModel, shipmentCartItemModels);
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
            if (resultCode != PaymentConstant.PAYMENT_CANCELLED && resultCode != PaymentConstant.PAYMENT_FAILED) {
                getActivity().setResult(PaymentConstant.PAYMENT_SUCCESS);
                getActivity().finish();
            }
        }
    }

    private void updatePromoTrackingData(List<TrackingDetailsItemUiModel> trackingDetailsItemUiModels) {
        List<ShipmentCartItemModel> dataList = shipmentAdapter.getShipmentCartItemModelList();
        if (dataList == null) return;
        for (ShipmentCartItemModel shipmentCartItemModel : dataList) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                if (trackingDetailsItemUiModels.size() > 0) {
                    for (TrackingDetailsItemUiModel trackingDetailsItemUiModel : trackingDetailsItemUiModels) {
                        if (trackingDetailsItemUiModel.getProductId() != null && trackingDetailsItemUiModel.getProductId() == cartItemModel.getProductId() &&
                                cartItemModel.getAnalyticsProductCheckoutData() != null) {
                            cartItemModel.getAnalyticsProductCheckoutData().setPromoCode(trackingDetailsItemUiModel.getPromoCodesTracking());
                            cartItemModel.getAnalyticsProductCheckoutData().setPromoDetails(trackingDetailsItemUiModel.getPromoDetailsTracking());
                        }
                    }
                }
            }
        }
    }

    private void clearPromoTrackingData() {
        List<ShipmentCartItemModel> dataList = shipmentAdapter.getShipmentCartItemModelList();
        if (dataList == null) return;
        for (ShipmentCartItemModel shipmentCartItemModel : dataList) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                if (cartItemModel.getAnalyticsProductCheckoutData() != null) {
                    cartItemModel.getAnalyticsProductCheckoutData().setPromoCode("");
                    cartItemModel.getAnalyticsProductCheckoutData().setPromoDetails("");
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
//                        shipmentPresenter.setDataChangeAddressRequestList(shipmentAdapter.getRequestData(newAddress, null, false).getChangeAddressRequestData());
                        shipmentPresenter.changeShippingAddress(newAddress, isOneClickShipment(), false, false);
                    }
                }
                break;

            case CheckoutConstant.RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS:
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
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.CHECKOUT_ADDRESS_SELECTION);
        intent.putExtra(CheckoutConstant.EXTRA_CURRENT_ADDRESS, shipmentPresenter.getRecipientAddressModel());
        intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST);
        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS);
    }

    @Override
    public void onSendToMultipleAddress(RecipientAddressModel recipientAddressModel, String cartIds) {
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
        shipmentDetailData.setTradein(isTradeIn());

        return shipmentDetailData;
    }

    @Override
    public void onChooseShipmentDuration(ShipmentCartItemModel shipmentCartItemModel,
                                         RecipientAddressModel recipientAddressModel,
                                         int cartPosition) {
        String isBlackbox = "0";
        if (shipmentCartItemModel.isHidingCourier()) isBlackbox = "1";
        sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation(isBlackbox);
        showShippingDurationBottomsheet(shipmentCartItemModel, recipientAddressModel, cartPosition);
        if (isTradeIn()) {
            checkoutAnalyticsCourierSelection.eventClickButtonPilihDurasi();
        }
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
    public void sendAnalyticsViewInformationAndWarningTickerInCheckout(String tickerId) {
        checkoutAnalyticsCourierSelection.eventViewInformationAndWarningTickerInCheckout(tickerId);
    }

    @Override
    public void sendAnalyticsViewPromoAfterAdjustItem(String msg) {
        checkoutAnalyticsCourierSelection.eventViewPromoAfterAdjustItem(msg);
    }

    @Override
    public void onTotalPaymentChange(String totalPayment) {
        shipmentPresenter.getShipmentButtonPaymentModel().setTotalPrice(totalPayment);
        onNeedUpdateViewItem(shipmentAdapter.getItemCount() - 1);
    }

    @Override
    public void onFinishChoosingShipment() {
        ValidateUsePromoRequest validateUsePromoRequest = shipmentPresenter.getLastValidateUseRequest();
        boolean stillHasPromo = false;
        if (validateUsePromoRequest != null) {
            if (validateUsePromoRequest.getCodes().size() > 0) stillHasPromo = true;
            for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                if (ordersItem.getCodes().size() > 0) {
                    stillHasPromo = true;
                    break;
                }
            }
        } else {
            LastApplyUiModel lastApplyUiModel = shipmentPresenter.getLastApplyData();
            if (lastApplyUiModel != null) {
                if (lastApplyUiModel.getCodes().size() > 0) {
                    stillHasPromo = true;
                } else {
                    if (lastApplyUiModel.getVoucherOrders().size() > 0) {
                        for (LastApplyVoucherOrdersItemUiModel voucherOrder : lastApplyUiModel.getVoucherOrders()) {
                            if (voucherOrder.getCode().length() > 0) {
                                stillHasPromo = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (stillHasPromo) {
            shipmentPresenter.checkPromoCheckoutFinalShipment(generateValidateUsePromoRequest());
        } else {
            clearPromoTrackingData();
            sendEEStep3();
        }
    }

    private void sendEEStep3() {
        List<ShipmentCartItemModel> shipmentCartItemModels = shipmentAdapter.getShipmentCartItemModelList();

        // if one of courier reseted because of apply promo logistic (PSL) and eventually not eligible after hit validate use, don't send EE
        boolean courierHasReseted = false;
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
            ShipmentDetailData selectedShipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
            if (selectedShipmentDetailData == null) {
                courierHasReseted = true;
                break;
            }
            CourierItemData selectedCourier = selectedShipmentDetailData.getSelectedCourier();
            if (selectedCourier == null) {
                courierHasReseted = true;
                break;
            }
            List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                    shipmentCartItemModel.getCartString(),
                    String.valueOf(selectedCourier.getServiceId()),
                    String.valueOf(selectedCourier.getShipperPrice()),
                    String.valueOf(shipmentCartItemModel.getSpId())
            );
            shipmentPresenter.setDataCheckoutRequestList(dataCheckoutRequests);
        }

        if (!courierHasReseted) {
            List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentAdapter.getShipmentCartItemModelList());
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

    // Project ARMY, clear from promo BBO ticker
    @Override
    public void onCancelVoucherLogisticClicked(String pslCode, int position) {
        checkoutAnalyticsCourierSelection.eventCancelPromoStackingLogistic();
        shipmentPresenter.cancelAutoApplyPromoStackLogistic(position, pslCode);
        shipmentAdapter.cancelAllCourierPromo();
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
            if (shipmentPresenter.isIneligiblePromoDialogEnabled()) {
                ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList = new ArrayList<>();
                ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel = shipmentPresenter.getValidateUsePromoRevampUiModel();
                if (validateUsePromoRevampUiModel != null) {
                    if (validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getState().equals("red")) {
                        NotEligiblePromoHolderdata notEligiblePromoHolderdata = new NotEligiblePromoHolderdata();
                        notEligiblePromoHolderdata.setPromoTitle(validateUsePromoRevampUiModel.getPromoUiModel().getTitleDescription());
                        if (validateUsePromoRevampUiModel.getPromoUiModel().getCodes().size() > 0) {
                            notEligiblePromoHolderdata.setPromoCode(validateUsePromoRevampUiModel.getPromoUiModel().getCodes().get(0));
                        }
                        notEligiblePromoHolderdata.setShopName("Kode promo");
                        notEligiblePromoHolderdata.setIconType(NotEligiblePromoHolderdata.getTYPE_ICON_GLOBAL());
                        notEligiblePromoHolderdata.setShowShopSection(true);
                        notEligiblePromoHolderdata.setErrorMessage(validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getText());
                        notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata);
                    }

                    List<PromoCheckoutVoucherOrdersItemUiModel> voucherOrdersItemUiModels = validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels();
                    if (voucherOrdersItemUiModels.size() > 0) {
                        for (int i = 0; i < voucherOrdersItemUiModels.size(); i++) {
                            PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel = voucherOrdersItemUiModels.get(i);
                            if (voucherOrdersItemUiModel != null && voucherOrdersItemUiModel.getMessageUiModel().getState().equals("red")) {
                                NotEligiblePromoHolderdata notEligiblePromoHolderdata = new NotEligiblePromoHolderdata();
                                notEligiblePromoHolderdata.setPromoTitle(voucherOrdersItemUiModel.getTitleDescription());
                                notEligiblePromoHolderdata.setPromoCode(voucherOrdersItemUiModel.getCode());
                                for (ShipmentCartItemModel shipmentCartItemModel : shipmentAdapter.getShipmentCartItemModelList()) {
                                    if (shipmentCartItemModel.getCartString().equals(voucherOrdersItemUiModel.getUniqueId())) {
                                        notEligiblePromoHolderdata.setShopName(shipmentCartItemModel.getShopName());
                                        if (shipmentCartItemModel.isOfficialStore()) {
                                            notEligiblePromoHolderdata.setIconType(NotEligiblePromoHolderdata.getTYPE_ICON_OFFICIAL_STORE());
                                        } else if (shipmentCartItemModel.isGoldMerchant()) {
                                            notEligiblePromoHolderdata.setIconType(NotEligiblePromoHolderdata.getTYPE_ICON_POWER_MERCHANT());
                                        }
                                        break;
                                    }
                                }

                                if (i <= 0) {
                                    notEligiblePromoHolderdata.setShowShopSection(true);
                                } else if (voucherOrdersItemUiModels.get(i - 1).getUniqueId() != null && voucherOrdersItemUiModel.getUniqueId() != null && voucherOrdersItemUiModels.get(i - 1).getUniqueId().equals(voucherOrdersItemUiModel.getUniqueId())) {
                                    notEligiblePromoHolderdata.setShowShopSection(false);
                                } else {
                                    notEligiblePromoHolderdata.setShowShopSection(true);
                                }
                                notEligiblePromoHolderdata.setErrorMessage(validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getText());
                                notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata);
                            }
                        }
                    }
                }

                if (notEligiblePromoHolderdataList.size() > 0) {
                    shipmentPresenter.cancelNotEligiblePromo(notEligiblePromoHolderdataList, requestCode);
                } else {
                    doCheckout(requestCode);
                }
            } else {
                boolean hasRedStatePromo = false;
                String errorMessage = "";
                ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel = shipmentPresenter.getValidateUsePromoRevampUiModel();
                if (validateUsePromoRevampUiModel != null) {
                    if (validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getState().equals("red")) {
                        hasRedStatePromo = true;
                        errorMessage = validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getText();
                    } else {
                        List<PromoCheckoutVoucherOrdersItemUiModel> voucherOrdersItemUiModels = validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels();
                        if (validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels().size() > 0) {
                            for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : voucherOrdersItemUiModels) {
                                if (voucherOrdersItemUiModel.getMessageUiModel().getState().equals("red")) {
                                    hasRedStatePromo = true;
                                    errorMessage = voucherOrdersItemUiModel.getMessageUiModel().getText();
                                    break;
                                }
                            }
                        }
                    }
                }

                if (hasRedStatePromo) {
                    hasRunningApiCall = false;
                    hideLoading();
                    showToastError(errorMessage);
                    sendAnalyticsPromoRedState();
                } else {
                    doCheckout(requestCode);
                }
            }
        } else if (shipmentData != null && !result) {
            hasRunningApiCall = false;
            hideLoading();
            sendAnalyticsDropshipperNotComplete();
            if (requestCode == REQUEST_CODE_COD) {
                mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIncomplete();
            }
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment.smoothScrollToPosition(errorPosition);
                onDataDisableToCheckout(null);
                String message = getActivity().getString(R.string.message_error_dropshipper_empty);
                showToastNormal(message);
                ((ShipmentCartItemModel) shipmentData).setStateDropshipperHasError(true);
                ((ShipmentCartItemModel) shipmentData).setStateAllItemViewExpanded(false);
                onNeedUpdateViewItem(errorPosition);
            }
        } else if (shipmentData == null) {
            hasRunningApiCall = false;
            hideLoading();
            if (isTradeIn()) {
                checkoutAnalyticsCourierSelection.eventClickBayarCourierNotComplete();
            }
            sendAnalyticsCourierNotComplete();
            if (requestCode == REQUEST_CODE_COD) {
                mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIncomplete();
            }
            checkShippingCompletion(true);
        }
    }

    private void doCheckout(int requestCode) {
        if (hasInsurance) {
            mTrackerMacroInsurance.eventClickPaymentMethodWithInsurance(shipmentAdapter.getInsuranceProductId(),
                    shipmentAdapter.getInsuranceTitle());
        }

        switch (requestCode) {
            case REQUEST_CODE_NORMAL_CHECKOUT:
                shipmentPresenter.processSaveShipmentState();
                shipmentPresenter.processCheckout(hasInsurance, isOneClickShipment(),
                        isTradeIn(), isTradeInByDropOff(), getDeviceId(), getCornerId(), getCheckoutLeasingId());
                break;
            case REQUEST_CODE_COD:
                shipmentPresenter.proceedCodCheckout(hasInsurance, isOneClickShipment(),
                        isTradeIn(), getDeviceId(), getCheckoutLeasingId());
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
        checkoutAnalyticsCourierSelection.eventClickCheckboxDonation(checked);
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
    public void onLogisticPromoChosen(List<ShippingCourierUiModel> shippingCourierUiModels,
                                      CourierItemData courierData, RecipientAddressModel recipientAddressModel,
                                      int cartPosition, ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                      String promoCode, int selectedServiceId) {
        onShippingDurationChoosen(shippingCourierUiModels, courierData, recipientAddressModel,
                cartPosition, selectedServiceId, serviceData, flagNeedToSetPinpoint,
                false, false);
        String cartString = shipmentAdapter.getShipmentCartItemModelByIndex(cartPosition).getCartString();
        if (!flagNeedToSetPinpoint) {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(cartPosition);
            ValidateUsePromoRequest validateUsePromoRequest = generateValidateUsePromoRequest();
            if (promoCode != null && promoCode.length() > 0) {
                for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                    if (ordersItem.getUniqueId().equals(shipmentCartItemModel.getCartString()) && !ordersItem.getCodes().contains(promoCode)) {
                        ordersItem.getCodes().add(promoCode);
                        break;
                    }
                }
            }
            shipmentPresenter.doValidateuseLogisticPromo(cartPosition, cartString, validateUsePromoRequest);
        }
    }

    @Override
    public void onShippingDurationChoosen(List<ShippingCourierUiModel> shippingCourierUiModels,
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
        if (shippingCourierUiModels.size() > 0) {
            ServiceData serviceDataTracker = shippingCourierUiModels.get(0).getServiceData();
            sendAnalyticsOnClickDurationThatContainPromo(
                    (serviceDataTracker.getIsPromo() == 1),
                    serviceDataTracker.getServiceName(),
                    (serviceDataTracker.getCodData().getIsCod() == 1),
                    Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(serviceDataTracker.getRangePrice().getMinPrice(), false)),
                    Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(serviceDataTracker.getRangePrice().getMaxPrice(), false))
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
            onChangeShippingCourier(recipientAddressModel, shipmentCartItemModel, cartItemPosition);
        } else {
            if (recommendedCourier.isUsePinPoint()
                    && (recipientAddressModel.getLatitude() == null ||
                    recipientAddressModel.getLatitude().equalsIgnoreCase("0") ||
                    recipientAddressModel.getLongitude() == null ||
                    recipientAddressModel.getLongitude().equalsIgnoreCase("0"))) {
                setPinpoint(cartItemPosition);
            } else {
                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition);

                if (isTradeInByDropOff()) {
                    shipmentAdapter.setSelectedCourierTradeInPickup(recommendedCourier);
                    shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
                } else {
                    sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(recommendedCourier.getName());
                    sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(recommendedCourier.getShipperProductId());

                    // Clear logistic voucher data when any duration is selected and voucher is not null
                    if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null &&
                            !TextUtils.isEmpty(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode()) && isClearPromo) {
                        String promoLogisticCode = shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode();
                        shipmentPresenter.cancelAutoApplyPromoStackLogistic(0, promoLogisticCode);
                        ValidateUsePromoRequest validateUsePromoRequest = shipmentPresenter.getLastValidateUseRequest();
                        if (validateUsePromoRequest != null) {
                            for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                                if (ordersItem != null && ordersItem.getCodes().size() > 0) {
                                    ordersItem.getCodes().remove(promoLogisticCode);
                                }
                            }
                        }
                        shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                        setBenefitSummaryInfoUiModel(null);
                        shipmentAdapter.clearTotalPromoStackAmount();
                        shipmentAdapter.updateShipmentCostModel();
                        shipmentAdapter.updateCheckoutButtonData(null);
                    }
                    shipmentAdapter.setSelectedCourier(cartItemPosition, recommendedCourier);
                    shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
                    shipmentAdapter.setShippingCourierViewModels(shippingCourierUiModels, recommendedCourier, cartItemPosition);
                    if (!TextUtils.isEmpty(recommendedCourier.getPromoCode()) && isDurationClick) {
                        checkCourierPromo(recommendedCourier, cartItemPosition);
                    }
                }
            }
        }
    }

    private void checkCourierPromo(CourierItemData courierItemData, int itemPosition) {
        if (!TextUtils.isEmpty(courierItemData.getPromoCode())) {
            String promoCode = courierItemData.getPromoCode();
            shipmentPresenter.processCheckPromoCheckoutCodeFromSelectedCourier(promoCode, itemPosition, false);
        }
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
            tooltip.setIcon(R.drawable.checkout_module_ic_dropshipper);
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
        sendAnalyticsOnDisplayDurationThatContainPromo(isCourierPromo, duration);
    }

    @Override
    public void onCourierChoosen(ShippingCourierUiModel shippingCourierUiModel, CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
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
                                         int cartPosition) {
        sendAnalyticsOnClickChangeDurationShipmentRecommendation();
        showShippingDurationBottomsheet(shipmentCartItemModel, recipientAddressModel, cartPosition);
    }

    private void showShippingDurationBottomsheet(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, int cartPosition) {
        if (shipmentCartItemModel.getShopShipmentList() == null || shipmentCartItemModel.getShopShipmentList().size() == 0) {
            onNoCourierAvailable(getString(com.tokopedia.logisticcart.R.string.label_no_courier_bottomsheet_message));
        } else {
            ShipmentDetailData shipmentDetailData = getShipmentDetailData(shipmentCartItemModel,
                    recipientAddressModel);
            int codHistory = -1;
            if (shipmentPresenter.getCodData() != null) {
                codHistory = shipmentPresenter.getCodData().getCounterCod();
            }
            if (shipmentDetailData != null) {
                String pslCode = RatesDataConverter.getLogisticPromoCode(shipmentCartItemModel);
                ArrayList<Product> products = getProductForRatesRequest(shipmentCartItemModel);
                shippingDurationBottomsheet = ShippingDurationBottomsheet.newInstance(
                        shipmentDetailData, shipmentAdapter.getLastServiceId(), shipmentCartItemModel.getShopShipmentList(),
                        recipientAddressModel, cartPosition, codHistory,
                        shipmentCartItemModel.getIsLeasingProduct(), pslCode, products,
                        shipmentCartItemModel.getCartString(), shipmentCartItemModel.isOrderPrioritasDisable(),
                        isTradeInByDropOff());
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
    public void onChangeShippingCourier(RecipientAddressModel recipientAddressModel,
                                        ShipmentCartItemModel shipmentCartItemModel,
                                        int cartPosition) {
        List<ShippingCourierUiModel> shippingCourierUiModels = shipmentCartItemModel.getSelectedShipmentDetailData().getShippingCourierViewModels();
        sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel);
        if (shippingCourierUiModels == null || shippingCourierUiModels.size() == 0 &&
                shipmentPresenter.getShippingCourierViewModelsState(cartPosition) != null) {
            shippingCourierUiModels = shipmentPresenter.getShippingCourierViewModelsState(cartPosition);
        }
        shippingCourierBottomsheet = ShippingCourierBottomsheet.newInstance(
                shippingCourierUiModels, recipientAddressModel, cartPosition);
        shippingCourierBottomsheet.setShippingCourierBottomsheetListener(this);
        if (shippingCourierUiModels != null) {
            checkHasCourierPromo(shippingCourierUiModels);
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
                        getProductForRatesRequest(shipmentCartItemModel), shipmentCartItemModel.getCartString(),
                        isTradeInByDropOff(), shipmentAdapter.getAddressShipmentData());
            }
        }
    }

    @Override
    public void onRetryReloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList) {
        reloadCourier(shipmentCartItemModel, cartPosition, shopShipmentList);
    }

    private void checkHasCourierPromo(List<ShippingCourierUiModel> shippingCourierUiModels) {
        boolean hasCourierPromo = false;
        for (ShippingCourierUiModel shippingCourierUiModel : shippingCourierUiModels) {
            if (!TextUtils.isEmpty(shippingCourierUiModel.getProductData().getPromoCode())) {
                hasCourierPromo = true;
                break;
            }
        }
        if (hasCourierPromo) {
            for (ShippingCourierUiModel shippingCourierUiModel : shippingCourierUiModels) {
                sendAnalyticsOnDisplayLogisticThatContainPromo(
                        !TextUtils.isEmpty(shippingCourierUiModel.getProductData().getPromoCode()),
                        shippingCourierUiModel.getProductData().getShipperProductId()
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
                                    boolean isTradeInDropOff) {
        if (shopShipmentList != null && shopShipmentList.size() > 0) {
            shipmentDetailData.setTradein(isTradeIn());
            shipmentPresenter.processGetCourierRecommendation(
                    shipperId, spId, itemPosition, shipmentDetailData,
                    shipmentCartItemModel, shopShipmentList, true,
                    getProductForRatesRequest(shipmentCartItemModel),
                    shipmentCartItemModel.getCartString(), isTradeInDropOff,
                    shipmentAdapter.getAddressShipmentData());
        }
    }

    @Override
    public void onCourierPromoCanceled(String shipperName, String promoCode) {
        if (shipmentAdapter.isCourierPromoStillExist()) {
            showToastError(String.format(getString(com.tokopedia.logisticcart.R.string.message_cannot_apply_courier_promo), shipperName));
        }
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
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.CHECKOUT);
            intent.putExtra(CheckoutConstant.EXTRA_CURRENT_ADDRESS, shipmentPresenter.getRecipientAddressModel());
            intent.putExtra(CheckoutConstant.EXTRA_DISTRICT_RECOMMENDATION_TOKEN, shipmentPresenter.getKeroToken());
            intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, CheckoutConstant.TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN);
            startActivityForResult(intent, REQUEST_CODE_EDIT_ADDRESS);
        }
    }

    @Override
    public void onProcessToPayment() {
        showLoading();
        shipmentAdapter.checkDropshipperValidation(REQUEST_CODE_NORMAL_CHECKOUT);
    }

    @Override
    public void onProcessToPaymentCod() {
        shipmentAdapter.checkDropshipperValidation(REQUEST_CODE_COD);
    }

    public int getResultCode() {
        if (shipmentPresenter.getCouponStateChanged()) {
            return RESULT_CODE_COUPON_STATE_CHANGED;
        } else {
            return Activity.RESULT_CANCELED;
        }
    }

    void releaseBookingIfAny() {
        if (cdLayout.getVisibility() == View.VISIBLE) {
            shipmentPresenter.releaseBooking();
        }
    }

    @Override
    public void updateCourierBottomsheetHasNoData(int cartPosition, ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList) {
        if (shippingCourierBottomsheet != null) {
            shippingCourierBottomsheet.setShippingCourierViewModels(null, cartPosition, shipmentCartItemModel, shopShipmentList);
        }
    }

    @Override
    public void updateCourierBottomssheetHasData(List<ShippingCourierUiModel> shippingCourierUiModels, int cartPosition, ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList) {
        if (shippingCourierBottomsheet != null) {
            shippingCourierBottomsheet.setShippingCourierViewModels(shippingCourierUiModels, cartPosition, shipmentCartItemModel, shopShipmentList);
        }
    }

    @Override
    public ValidateUsePromoRequest generateValidateUsePromoRequest() {
        ArrayList<String> bboPromoCodes = new ArrayList<>();
        if (shipmentPresenter.getLastValidateUseRequest() != null) {
            // Update param if have done param data generation before
            ValidateUsePromoRequest validateUsePromoRequest = shipmentPresenter.getLastValidateUseRequest();
            List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentAdapter.getShipmentCartItemModelList();
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                    if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                            shipmentCartItemModel.getCartString().equals(ordersItem.getUniqueId())) {
                        if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                            if (!ordersItem.getCodes().contains(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode())) {
                                ordersItem.getCodes().add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                            }
                            if (!bboPromoCodes.contains(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode())) {
                                bboPromoCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                            }
                        }

                        if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                            ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId());
                            ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId());
                        } else if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                            ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperId());
                            ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperProductId());
                        }
                    }
                }
            }
            if (isTradeIn()) {
                validateUsePromoRequest.setTradeIn(1);
                validateUsePromoRequest.setTradeInDropOff(isTradeInByDropOff() ? 1 : 0);
            }
            shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest);
            this.bboPromoCodes = bboPromoCodes;
            if (isOneClickShipment()) {
                validateUsePromoRequest.setCartType("ocs");
            } else {
                validateUsePromoRequest.setCartType("default");
            }
            return validateUsePromoRequest;
        } else {
            // First param data generation / initialization
            ValidateUsePromoRequest validateUsePromoRequest = new ValidateUsePromoRequest();
            ArrayList<OrdersItem> listOrderItem = new ArrayList<>();

            List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentAdapter.getShipmentCartItemModelList();
            LastApplyUiModel lastApplyUiModel = shipmentPresenter.getLastApplyData();
            if (shipmentCartItemModelList != null) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                    OrdersItem ordersItem = new OrdersItem();
                    ArrayList<ProductDetailsItem> productDetailsItems = new ArrayList<>();
                    for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                        if (!cartItemModel.isError()) {
                            ProductDetailsItem productDetail = new ProductDetailsItem();
                            productDetail.setProductId(cartItemModel.getProductId());
                            productDetail.setQuantity(cartItemModel.getQuantity());
                            productDetailsItems.add(productDetail);
                        }
                    }
                    ordersItem.setProductDetails(productDetailsItems);

                    ArrayList<String> listOrderCodes = new ArrayList<>();
                    if (lastApplyUiModel != null) {
                        for (LastApplyVoucherOrdersItemUiModel lastApplyVoucherOrdersItemUiModel : lastApplyUiModel.getVoucherOrders()) {
                            if (shipmentCartItemModel.getCartString().equalsIgnoreCase(lastApplyVoucherOrdersItemUiModel.getUniqueId())) {
                                if (!listOrderCodes.contains(lastApplyVoucherOrdersItemUiModel.getCode())) {
                                    listOrderCodes.add(lastApplyVoucherOrdersItemUiModel.getCode());
                                }
                                break;
                            }
                        }
                    }
                    // Add data BBO
                    if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                        if (!listOrderCodes.contains(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode())) {
                            listOrderCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                        }
                        if (!bboPromoCodes.contains(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode())) {
                            bboPromoCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                        }
                    }
                    ordersItem.setCodes(listOrderCodes);
                    ordersItem.setUniqueId(shipmentCartItemModel.getCartString());
                    ordersItem.setShopId(shipmentCartItemModel.getShopId());
                    if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                        if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                            ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId());
                            ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId());
                        } else if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                            ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperId());
                            ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperProductId());
                        }
                    }
                    listOrderItem.add(ordersItem);
                }
            }
            validateUsePromoRequest.setOrders(listOrderItem);
            validateUsePromoRequest.setState(PARAM_CHECKOUT);
            validateUsePromoRequest.setCartType(PARAM_DEFAULT);
            validateUsePromoRequest.setSkipApply(0);
            if (isTradeIn()) {
                validateUsePromoRequest.setTradeIn(1);
                validateUsePromoRequest.setTradeInDropOff(isTradeInByDropOff() ? 1 : 0);
            }

            if (lastApplyUiModel != null) {
                ArrayList<String> globalPromoCodes = new ArrayList<>();
                if (lastApplyUiModel.getCodes().size() > 0) {
                    for (String code : lastApplyUiModel.getCodes()) {
                        if (code.length() > 0 && !globalPromoCodes.contains(code)) {
                            globalPromoCodes.add(code);
                        }
                    }
                }
                validateUsePromoRequest.setCodes(globalPromoCodes);
            }
            shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest);
            this.bboPromoCodes = bboPromoCodes;
            if (isOneClickShipment()) {
                validateUsePromoRequest.setCartType("ocs");
            } else {
                validateUsePromoRequest.setCartType("default");
            }
            return validateUsePromoRequest;
        }
    }

    @Override
    public PromoRequest generateCouponListRecommendationRequest() {
        PromoRequest promoRequest = new PromoRequest();
        ArrayList<Order> listOrderItem = new ArrayList<>();

        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentAdapter.getShipmentCartItemModelList();
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                Order ordersItem = new Order();
                ArrayList<ProductDetail> productDetailsItems = new ArrayList<>();
                for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                    if (!cartItemModel.isError()) {
                        ProductDetail productDetail = new ProductDetail();
                        productDetail.setProductId(cartItemModel.getProductId());
                        productDetail.setQuantity(cartItemModel.getQuantity());
                        productDetailsItems.add(productDetail);
                    }
                }
                ordersItem.setProduct_details(productDetailsItems);
                ordersItem.setChecked(true);

                ArrayList<String> listCodes = new ArrayList<>();
                if (shipmentCartItemModel.getListPromoCodes() != null) {
                    for (String code : shipmentCartItemModel.getListPromoCodes()) {
                        listCodes.add(code);
                    }
                }
                ordersItem.setCodes(listCodes);
                ordersItem.setUniqueId(shipmentCartItemModel.getCartString());
                ordersItem.setShopId(shipmentCartItemModel.getShopId());
                ordersItem.setInsurancePrice(shipmentCartItemModel.isInsurance() ? 1 : 0);
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                    if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                        ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId());
                        ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId());
                    } else if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                        ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperId());
                        ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperProductId());
                    }
                }
                listOrderItem.add(ordersItem);
            }
        }
        promoRequest.setOrders(listOrderItem);
        promoRequest.setState(PARAM_CHECKOUT);
        if (isOneClickShipment()) {
            promoRequest.setCartType("ocs");
        } else {
            promoRequest.setCartType(PARAM_DEFAULT);
        }

        if (isTradeIn()) {
            promoRequest.setTradeIn(1);
            promoRequest.setTradeInDropOff(isTradeInByDropOff() ? 1 : 0);
        }

        LastApplyUiModel lastApplyUiModel = shipmentPresenter.getLastApplyData();
        if (lastApplyUiModel != null) {
            ArrayList<String> globalPromoCodes = new ArrayList<>();
            if (lastApplyUiModel.getCodes().size() > 0) {
                globalPromoCodes.addAll(lastApplyUiModel.getCodes());
            }
            promoRequest.setCodes(globalPromoCodes);
        }

        return promoRequest;
    }

    @Override
    public void onSuccessClearPromoLogistic(int position, boolean isLastAppliedPromo) {
        if (position != 0) {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position);
            shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
            onNeedUpdateViewItem(position);
        }

        if (isLastAppliedPromo) {
            doResetButtonPromoCheckout();
        } else {
            shipmentAdapter.checkHasSelectAllCourier(false);
        }
    }

    @Override
    public void triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(String transactionId) {
        List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentAdapter.getShipmentCartItemModelList());
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
        ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position);
        if (shipmentCartItemModel != null) {
            addShippingCompletionTicker(shipmentCartItemModel.isEligibleNewShippingExperience());
        }
    }

    @Override
    public void clearTotalBenefitPromoStacking() {
        shipmentAdapter.clearTotalPromoStackAmount();
        shipmentAdapter.updateShipmentCostModel();
    }

    // Keep this method
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
        PromoRevampAnalytics.INSTANCE.eventCheckoutViewBottomsheetPromoError();
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
    public void onButtonChooseOtherPromo() {
        if (promoNotEligibleBottomsheet != null) {
            promoNotEligibleBottomsheet.dismiss();
            PromoRevampAnalytics.INSTANCE.eventCheckoutClickPilihPromoLainOnBottomsheetPromoError();
            ValidateUsePromoRequest validateUseRequestParam = generateValidateUsePromoRequest();
            PromoRequest promoRequestParam = generateCouponListRecommendationRequest();
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE);
            intent.putExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_PAGE_SOURCE, PAGE_CHECKOUT);
            intent.putExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_PROMO_REQUEST, promoRequestParam);
            intent.putExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_VALIDATE_USE_REQUEST, validateUseRequestParam);
            intent.putStringArrayListExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_BBO_PROMO_CODES, bboPromoCodes);

            startActivityForResult(intent, REQUEST_CODE_PROMO);
        }
    }

    @Override
    public void onShow() {
        if (promoNotEligibleBottomsheet != null) {
            BottomSheetBehavior bottomSheetBehavior = promoNotEligibleBottomsheet.getBottomSheetBehavior();
            if (bottomSheetBehavior != null) {
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
    }

    @Override
    public boolean isInsuranceEnabled() {
        return isInsuranceEnabled;
    }

    @Override
    public void removeIneligiblePromo(int checkoutType, ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList) {
        ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel = shipmentPresenter.getValidateUsePromoRevampUiModel();
        if (validateUsePromoRevampUiModel != null) {
            if (validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getState().equals("red")) {
                validateUsePromoRevampUiModel.getPromoUiModel().getCodes().clear();
            }

            ArrayList<PromoCheckoutVoucherOrdersItemUiModel> deletedVoucherOrder = new ArrayList<>();
            for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel :
                    validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
                if (voucherOrdersItemUiModel.getMessageUiModel().getState().equals("red")) {
                    deletedVoucherOrder.add(voucherOrdersItemUiModel);
                }
            }

            if (deletedVoucherOrder.size() > 0) {
                for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : deletedVoucherOrder) {
                    validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels().remove(voucherOrdersItemUiModel);
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
            // If ticker exist, update the view
            onNeedUpdateViewItem(index);
        } else {
            // If ticker not exist, add ticker to list, then update the list
            TickerAnnouncementHolderData tickerAnnouncementHolderData = shipmentPresenter.getTickerAnnouncementHolderData();
            shipmentAdapter.addTickerAnnouncementdata(tickerAnnouncementHolderData);
            shipmentAdapter.notifyItemInserted(ShipmentAdapter.HEADER_POSITION);
        }
    }

    @Override
    public void setPromoBenefit(List<SummariesItemUiModel> summariesUiModels) {
        shipmentAdapter.setPromoBenefit(summariesUiModels);
        onNeedUpdateViewItem(shipmentAdapter.getShipmentCostPosition());
    }

    @Override
    public void resetPromoBenefit() {
        shipmentAdapter.resetPromoBenefit();
        onNeedUpdateViewItem(shipmentAdapter.getShipmentCostPosition());
        shipmentAdapter.updateShipmentCostModel();
    }

    @Override
    public void onChangeTradeInDropOffClicked() {
        checkoutAnalyticsCourierSelection.eventClickUbahTitikDropoffButton();
        startActivityForResult(RouteManager.getIntent(getActivity(), ApplinkConstInternalLogistic.DROPOFF_PICKER),
                LogisticConstant.REQUEST_CODE_PICK_DROP_OFF_TRADE_IN);
    }

    /*
     * This method is to solve expired dialog not shown up after time expired in background
     * Little caveat: what if device's time is tempered and not synchronized with server?
     * Later: consider serverTimeOffset, need more time
     * */
    private void checkCampaignTimer() {
        if (shipmentPresenter.getCampaignTimer() != null && shipmentPresenter.getCampaignTimer().getShowTimer()) {
            CampaignTimerUi timer = shipmentPresenter.getCampaignTimer();

            long diff = TimeHelper.timeSinceNow(timer.getTimerExpired());

            if (diff <= 0 && getFragmentManager() != null) {
                ExpiredTimeDialog dialog = ExpiredTimeDialog.newInstance(timer, checkoutAnalyticsCourierSelection);
                dialog.show(getFragmentManager(), "expired dialog");
            }
        }
    }

    private void setCampaignTimer() {
        if (shipmentPresenter.getCampaignTimer() != null && shipmentPresenter.getCampaignTimer().getShowTimer()) {
            CampaignTimerUi timer = shipmentPresenter.getCampaignTimer();

            long diff = TimeHelper.timeBetweenRFC3339(timer.getTimerServer(), timer.getTimerExpired());

            cdLayout.setVisibility(View.VISIBLE);
            cdText.setText(timer.getTimerDescription());
            cdView.setupTimerFromRemianingMillis(diff, () -> {
                // need to check with resumed to avoid crash when time is expired in background
                // time needs to be running in background
                if (getFragmentManager() != null && isResumed()) {
                    ExpiredTimeDialog dialog = ExpiredTimeDialog.newInstance(timer, checkoutAnalyticsCourierSelection);
                    dialog.show(getFragmentManager(), "expired dialog");
                }
            });
        }
    }

    private void onResultFromSetTradeInPinpoint(Intent data) {
        if (data != null) {
            LocationDataModel locationDataModel = data.getParcelableExtra(LogisticConstant.RESULT_DATA_STORE_LOCATION);
            RecipientAddressModel recipientAddressModel = shipmentAdapter.getAddressShipmentData();
            if (recipientAddressModel != null) {
                recipientAddressModel.setLocationDataModel(locationDataModel);
                recipientAddressModel.setDropOffAddressName(locationDataModel.getAddrName());
                recipientAddressModel.setDropOffAddressDetail(locationDataModel.getAddress1());
                shipmentPresenter.changeShippingAddress(recipientAddressModel, true, true, true);
            }
        }
    }

    @Override
    public boolean isTradeInByDropOff() {
        RecipientAddressModel recipientAddressModel = shipmentAdapter.getAddressShipmentData();
        if (recipientAddressModel == null) return false;
        return recipientAddressModel.getSelectedTabIndex() == 1;
    }

    @Override
    public void onTradeInAddressTabChanged(int shipmentItemTradeInPosition) {
        onNeedUpdateViewItem(shipmentItemTradeInPosition);
        RecipientAddressModel recipientAddressModel = shipmentAdapter.getAddressShipmentData();
        if (recipientAddressModel.getSelectedTabIndex() == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
            if (recipientAddressModel.getLocationDataModel() != null) {
                shipmentPresenter.changeShippingAddress(recipientAddressModel, true, false, true);
            }
            checkoutAnalyticsCourierSelection.eventClickJemputTab();
        } else {
            checkoutAnalyticsCourierSelection.eventClickDropOffTab();
        }
    }

    @Override
    public void onClickPromoCheckout(LastApplyUiModel lastApplyUiModel) {
        if (lastApplyUiModel == null) return;

        ArrayList<Order> listOrder = new ArrayList<>();
        Order order = new Order();
        for (int i = 0; i < shipmentAdapter.getShipmentCartItemModelList().size(); i++) {
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelList().get(i);
            order.setShopId(shipmentCartItemModel.getShopId());
            order.setUniqueId(shipmentCartItemModel.getCartString());
            order.setChecked(true);

            ArrayList<String> listOrderCodes = new ArrayList<>();
            for (int x = 0; x < lastApplyUiModel.getVoucherOrders().size(); x++) {
                if (shipmentCartItemModel.getCartString().equalsIgnoreCase(lastApplyUiModel.getVoucherOrders().get(x).getUniqueId())) {
                    listOrderCodes.add(lastApplyUiModel.getVoucherOrders().get(x).getCode());
                    break;
                }
            }
            order.setCodes(listOrderCodes);

            ArrayList<ProductDetail> listProduct = new ArrayList<>();
            for (int j = 0; j < shipmentCartItemModel.getCartItemModels().size(); j++) {
                CartItemModel cartItemModel = shipmentCartItemModel.getCartItemModels().get(j);
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductId(cartItemModel.getProductId());
                productDetail.setQuantity(cartItemModel.getQuantity());
                listProduct.add(productDetail);
            }
            order.setProduct_details(listProduct);
            listOrder.add(order);
        }

        PromoRequest promoRequest = new PromoRequest();
        promoRequest.setState(CheckoutConstant.CHECKOUT);
        promoRequest.setCodes(new ArrayList<>(lastApplyUiModel.getCodes()));

        ValidateUsePromoRequest validateUseRequestParam = generateValidateUsePromoRequest();
        PromoRequest promoRequestParam = generateCouponListRecommendationRequest();
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE);
        intent.putExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_PAGE_SOURCE, PAGE_CHECKOUT);
        intent.putExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_PROMO_REQUEST, promoRequestParam);
        intent.putExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_VALIDATE_USE_REQUEST, validateUseRequestParam);
        intent.putStringArrayListExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_BBO_PROMO_CODES, bboPromoCodes);

        startActivityForResult(intent, REQUEST_CODE_PROMO);
    }

    @Override
    public void updateButtonPromoCheckout(PromoUiModel promoUiModel) {
        doUpdateButtonPromoCheckout(promoUiModel);
        updatePromoTrackingData(promoUiModel.getTrackingDetailUiModels());
        sendEEStep3();
        updateLogisticPromoData(promoUiModel);
        if (shipmentAdapter.hasSetAllCourier()) {
            resetPromoBenefit();
            setPromoBenefit(promoUiModel.getBenefitSummaryInfoUiModel().getSummaries());
            shipmentAdapter.updateShipmentCostModel();
        }
    }

    private void doUpdateButtonPromoCheckout(PromoUiModel promoUiModel) {
        shipmentAdapter.updatePromoCheckoutData(promoUiModel);
        onNeedUpdateViewItem(shipmentAdapter.getPromoCheckoutPosition());
    }

    private void doResetButtonPromoCheckout() {
        shipmentAdapter.resetPromoCheckoutData();
        onNeedUpdateViewItem(shipmentAdapter.getPromoCheckoutPosition());
        resetPromoBenefit();
        clearPromoTrackingData();
    }

    @Override
    public void onSendAnalyticsClickPromoCheckout(Boolean isApplied, List<String> listAllPromoCodes) {
        PromoRevampAnalytics.INSTANCE.eventCheckoutClickPromoSection(listAllPromoCodes, isApplied);
    }

    @Override
    public void onSendAnalyticsViewPromoCheckoutApplied() {
        PromoRevampAnalytics.INSTANCE.eventCheckoutViewPromoAlreadyApplied();
    }

    private void updateLogisticPromoData(PromoUiModel promoUiModel) {
        List<ShipmentCartItemModel> shipmentCartItemModels = shipmentAdapter.getShipmentCartItemModelList();
        if (shipmentCartItemModels == null) return;
        List<PromoCheckoutVoucherOrdersItemUiModel> voucherOrdersItemUiModels = promoUiModel.getVoucherOrderUiModels();
        for (PromoCheckoutVoucherOrdersItemUiModel promoCheckoutVoucherOrdersItemUiModel : voucherOrdersItemUiModels) {
            if (promoCheckoutVoucherOrdersItemUiModel.getType().equals("logistic")) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    if (shipmentCartItemModel.getCartString().equals(promoCheckoutVoucherOrdersItemUiModel.getUniqueId())) {
                        VoucherLogisticItemUiModel log = new VoucherLogisticItemUiModel();
                        log.setCode(promoCheckoutVoucherOrdersItemUiModel.getCode());
                        log.setCouponDesc(promoCheckoutVoucherOrdersItemUiModel.getTitleDescription());
                        log.setCouponAmount(Utils.getFormattedCurrency(promoCheckoutVoucherOrdersItemUiModel.getDiscountAmount()));
                        log.setCouponAmountRaw(promoCheckoutVoucherOrdersItemUiModel.getDiscountAmount());
                        MessageUiModel messageUiModel = new MessageUiModel();
                        messageUiModel.setColor(promoCheckoutVoucherOrdersItemUiModel.getMessageUiModel().getColor());
                        messageUiModel.setState(promoCheckoutVoucherOrdersItemUiModel.getMessageUiModel().getState());
                        messageUiModel.setText(promoCheckoutVoucherOrdersItemUiModel.getMessageUiModel().getText());
                        log.setMessage(messageUiModel);
                        shipmentCartItemModel.setVoucherLogisticItemUiModel(log);

                        onNeedUpdateViewItem(shipmentAdapter.getShipmentCartItemModelPosition(shipmentCartItemModel));
                    }
                }
            }
        }
    }

    @Override
    public void resetCourier(ShipmentCartItemModel shipmentCartItemModel) {
        int index = shipmentAdapter.getShipmentDataList().indexOf(shipmentCartItemModel);
        if (index != -1) {
            shipmentAdapter.resetCourier(index);
            addShippingCompletionTicker(shipmentCartItemModel.isEligibleNewShippingExperience());
        }
    }

    @Override
    public void onCheckShippingCompletionClicked() {
        checkoutAnalyticsCourierSelection.clickCekOnSummaryTransactionTickerCourierNotComplete(userSessionInterface.getUserId());
        checkShippingCompletion(false);
    }

    private void checkShippingCompletion(boolean isTriggeredByPaymentButton) {
        if (getActivity() != null) {
            List<Object> shipmentDataList = shipmentAdapter.getShipmentDataList();
            if (isTradeInByDropOff()) {
                int position = 0;
                for (int i = 0; i < shipmentDataList.size(); i++) {
                    if (shipmentDataList.get(i) instanceof ShipmentCartItemModel) {
                        position = i;
                        break;
                    }
                }

                rvShipment.smoothScrollToPosition(position);

                if (isTriggeredByPaymentButton) {
                    showToastNormal(getActivity().getString(R.string.message_error_courier_not_selected));
                }
            } else {
                int notSelectCourierCount = 0;
                int firstFoundPosition = 0;
                for (int i = 0; i < shipmentDataList.size(); i++) {
                    if (shipmentDataList.get(i) instanceof ShipmentCartItemModel) {
                        ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(i);
                        if (shipmentCartItemModel.getSelectedShipmentDetailData() == null || shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() == null) {
                            if (firstFoundPosition == 0) {
                                firstFoundPosition = i;
                            }
                            shipmentCartItemModel.setTriggerShippingVibrationAnimation(true);
                            shipmentCartItemModel.setStateAllItemViewExpanded(false);
                            shipmentCartItemModel.setShippingBorderRed(isTriggeredByPaymentButton);
                            onNeedUpdateViewItem(i);
                            notSelectCourierCount++;
                        }
                    }
                }

                rvShipment.smoothScrollToPosition(firstFoundPosition);

                if (isTriggeredByPaymentButton && notSelectCourierCount > 0) {
                    if (notSelectCourierCount == 1) {
                        showToastNormal(getActivity().getString(R.string.message_error_courier_not_selected));
                    } else {
                        showToastNormal(String.format(getString(R.string.message_error_multiple_courier_not_selected), notSelectCourierCount));
                    }
                }

            }
        }
    }

    @Override
    public void onShowTickerShippingCompletion() {
        checkoutAnalyticsCourierSelection.eventViewSummaryTransactionTickerCourierNotComplete(userSessionInterface.getUserId());
    }
}
