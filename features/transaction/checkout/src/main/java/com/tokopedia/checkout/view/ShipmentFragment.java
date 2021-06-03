package com.tokopedia.checkout.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

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
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.checkout.analytics.CheckoutEgoldAnalytics;
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics;
import com.tokopedia.checkout.analytics.CornerAnalytics;
import com.tokopedia.checkout.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData;
import com.tokopedia.checkout.domain.model.checkout.TrackerData;
import com.tokopedia.checkout.subfeature.webview.CheckoutWebViewActivity;
import com.tokopedia.checkout.view.adapter.ShipmentAdapter;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.checkout.view.di.CheckoutModule;
import com.tokopedia.checkout.view.di.DaggerCheckoutComponent;
import com.tokopedia.checkout.view.dialog.ExpireTimeDialogListener;
import com.tokopedia.checkout.view.dialog.ExpiredTimeDialog;
import com.tokopedia.checkout.view.helper.CartProtectionInfoBottomSheetHelper;
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel;
import com.tokopedia.common.payment.PaymentConstant;
import com.tokopedia.common.payment.model.PaymentPassData;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.localizationchooseaddress.common.ChosenAddress;
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel;
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils;
import com.tokopedia.logisticCommon.data.constant.LogisticConstant;
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel;
import com.tokopedia.logisticCommon.data.entity.address.Token;
import com.tokopedia.logisticCommon.data.entity.address.UserAddress;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery;
import com.tokopedia.logisticcart.shipping.model.PreOrderModel;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
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
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet;
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest;
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult;
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
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleBottomSheet;
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;
import com.tokopedia.unifycomponents.TimerUnify;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.currency.CurrencyFormatUtil;
import com.tokopedia.utils.time.TimeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.EVENT_ACTION_PILIH_PEMBAYARAN_INDOMARET;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.EVENT_ACTION_PILIH_PEMBAYARAN_NORMAL;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.EVENT_LABEL_TRADE_IN_CHECKOUT_EE;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.KEY_BUSINESS_UNIT;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.KEY_SCREEN_NAME;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.KEY_USER_ID;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.SCREEN_NAME_DROP_OFF_ADDRESS;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.SCREEN_NAME_NORMAL_ADDRESS;
import static com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics.VALUE_TRADE_IN;
import static com.tokopedia.purchase_platform.common.constant.CartConstant.SCREEN_NAME_CART_NEW_USER;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_SNIPPET;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_REF;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.KERO_TOKEN;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.PARAM_CHECKOUT;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.PARAM_DEFAULT;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.RESULT_CODE_COUPON_STATE_CHANGED;
import static com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_CHOSEN_ADDRESS;
import static com.tokopedia.purchase_platform.common.constant.PromoConstantKt.PAGE_CHECKOUT;

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View,
        ShipmentContract.AnalyticsActionListener, ShipmentAdapterActionListener,
        ShippingDurationBottomsheetListener, ShippingCourierBottomsheetListener,
        PromoNotEligibleActionListener, SellerCashbackListener,
        ExpireTimeDialogListener {

    private static final int REQUEST_CODE_EDIT_ADDRESS = 11;
    private static final int REQUEST_CODE_COURIER_PINPOINT = 13;
    private static final int REQUEST_CODE_PROMO = 954;

    private static final String SHIPMENT_TRACE = "mp_shipment";

    public static final String ARG_IS_ONE_CLICK_SHIPMENT = "ARG_IS_ONE_CLICK_SHIPMENT";
    public static final String ARG_CHECKOUT_LEASING_ID = "ARG_CHECKOUT_LEASING_ID";
    private static final String DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION = "LAST_CHOOSE_COURIER_ITEM_POSITION";
    private static final String DATA_STATE_LAST_CHOOSEN_SERVICE_ID = "DATA_STATE_LAST_CHOOSEN_SERVICE_ID";

    private RecyclerView rvShipment;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayout llNetworkErrorView;
    private AlertDialog progressDialogNormal;
    private ShippingCourierBottomsheet shippingCourierBottomsheet;
    private PerformanceMonitoring shipmentTracePerformance;
    private boolean isShipmentTraceStopped;
    private String cornerId;
    private PromoNotEligibleBottomSheet promoNotEligibleBottomsheet;

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
    CheckoutAnalyticsPurchaseProtection mTrackerPurchaseProtection;
    @Inject
    CornerAnalytics mTrackerCorner;
    @Inject
    UserSessionInterface userSessionInterface;
    @Inject
    CheckoutTradeInAnalytics checkoutTradeInAnalytics;
    @Inject
    CheckoutEgoldAnalytics checkoutEgoldAnalytics;

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

    private boolean hasClearPromoBeforeCheckout = false;
    private boolean hasRunningApiCall = false;
    private ArrayList<String> bboPromoCodes = new ArrayList<>();
    private int shipmentLoadingIndex = -1;

    private Subscription delayScrollToFirstShopSubscription;

    // count down component
    private View cdLayout;
    private TimerUnify cdView;
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
        }

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
        shippingCourierBottomsheet = null;
        CountDownTimer countDownTimer = cdView.getTimer();
        if (countDownTimer != null) countDownTimer.cancel();
        shipmentPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        shipmentAdapter.clearCompositeSubscription();
        super.onDestroy();
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

    public void onBackPressed() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickBackArrow();
        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventTradeInClickBackButton(isTradeInByDropOff());
        }
        releaseBookingIfAny();
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
        setBackground();
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

    private void setBackground() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N50));
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

    private void initRecyclerViewData(ShipmentTickerErrorModel shipmentTickerErrorModel,
                                      TickerAnnouncementHolderData tickerAnnouncementHolderData,
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

        shipmentAdapter.addTickerErrorData(shipmentTickerErrorModel);

        if (tickerAnnouncementHolderData != null) {
            shipmentAdapter.addTickerAnnouncementData(tickerAnnouncementHolderData);
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

        if (shipmentDonationModel != null) {
            shipmentAdapter.addShipmentDonationModel(shipmentDonationModel);
            if (shipmentDonationModel.isChecked() && shipmentDonationModel.isEnabled()) {
                checkoutAnalyticsCourierSelection.eventViewAutoCheckDonation(userSessionInterface.getUserId());
            }
        }

        if (egoldAttributeModel != null && egoldAttributeModel.isEligible()) {
            shipmentAdapter.updateEgold(false);
            shipmentAdapter.addEgoldAttributeData(egoldAttributeModel);
        }

        if (shipmentCartItemModelList.size() > 0) {
            // Don't show donation, egold, promo section if all shop is error
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

        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventViewCheckoutPageTradeIn();
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
                null,
                EnhancedECommerceActionField.STEP_2,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_CHECKOUT_PAGE,
                ConstantTransactionAnalytics.EventLabel.SUCCESS,
                getCheckoutLeasingId());
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
            initializeToasterLocation();
            View.OnClickListener listener = view -> {
            };
            String actionText = getActivity().getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok);
            Toaster.build(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, actionText, listener)
                    .show();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void showToastError(String message) {
        if (getView() != null && getActivity() != null) {
            initializeToasterLocation();
            if (TextUtils.isEmpty(message)) {
                message = getActivity().getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_error_global_message);
            }
            if (shipmentAdapter == null || shipmentAdapter.getItemCount() == 0) {
                renderErrorPage(message);
            } else {
                View.OnClickListener listener = view -> {
                };
                String actionText = getActivity().getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok);
                Toaster.build(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, actionText, listener)
                        .show();
            }
        }
    }

    public void initializeToasterLocation() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvShipment.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        int lastItemPosition = layoutManager.findLastVisibleItemPosition();
        if (lastItemPosition == RecyclerView.NO_POSITION ||
                shipmentAdapter.getShipmentDataList() == null ||
                lastItemPosition >= shipmentAdapter.getShipmentDataList().size()) {
            return;
        }
        if (shipmentAdapter.getShipmentDataList().get(lastItemPosition) instanceof ShipmentButtonPaymentModel) {
            Utils.setToasterCustomBottomHeight(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_48));
        } else {
            Utils.setToasterCustomBottomHeight(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16));
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
    public void onShipmentAddressFormEmpty() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void renderCheckoutPage(boolean isInitialRender, boolean isReloadAfterPriceChangeHigher, boolean isOneClickShipment) {
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        shipmentAdapter.setShowOnboarding(shipmentPresenter.isShowOnboarding());
        setCampaignTimer();
        initRecyclerViewData(
                shipmentPresenter.getShipmentTickerErrorModel(),
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
                shipmentPresenter.getShipmentTickerErrorModel(),
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
    public void renderCheckoutPageNoAddress(CartShipmentAddressFormData shipmentAddressFormData) {
        Token token = new Token();
        token.setUt(shipmentAddressFormData.getKeroUnixTime());
        token.setDistrictRecommendation(shipmentAddressFormData.getKeroDiscomToken());

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalLogistic.ADD_ADDRESS_V2);
        intent.putExtra(KERO_TOKEN, token);
        intent.putExtra(EXTRA_REF, SCREEN_NAME_CART_NEW_USER);

        startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY);
    }

    @Override
    public void renderCheckoutPageNoMatchedAddress(CartShipmentAddressFormData cartShipmentAddressFormData, int addressState) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalLogistic.MANAGE_ADDRESS);
        intent.putExtra(EXTRA_PREVIOUS_STATE_ADDRESS, addressState);
        intent.putExtra(EXTRA_IS_FROM_CHECKOUT_SNIPPET, true);
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
        intent.putExtra(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT, hasClearPromoBeforeCheckout);
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
            eventLabelBuilder.append(trackerData.getProductChangesType());
            eventLabelBuilder.append(" - ");
            eventLabelBuilder.append(trackerData.getCampaignType());
            eventLabelBuilder.append(" - ");
            eventLabelBuilder.append(TextUtils.join(",", trackerData.getProductIds()));

            checkoutAnalyticsCourierSelection.eventViewPopupPriceIncrease(eventLabelBuilder.toString());
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
            checkoutTradeInAnalytics.eventClickBayarTradeInFailed();
        }
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(errorMessage);
    }

    public void renderPromoCheckoutFromCourierSuccess(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel, int itemPosition, boolean noToast) {
        if (!noToast && !shipmentAdapter.isCourierPromoStillExist()) {
            if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                showToastNormal(validateUsePromoRevampUiModel.getMessage().get(0));
            }
        }
        setCourierPromoApplied(itemPosition);
        updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel(), true);
    }

    @Override
    public void setCourierPromoApplied(int itemPosition) {
        shipmentAdapter.setCourierPromoApplied(itemPosition);
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
            RecipientAddressModel recipientAddressModel;
            if (shipmentPresenter.getRecipientAddressModel() != null) {
                recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
                onChangeShippingDuration(shipmentCartItemModel, recipientAddressModel, position);
            }
        }
    }

    @Override
    public void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition, boolean isTradeInDropOff, boolean isForceReloadRates) {
        ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition);
        if (shipmentCartItemModel == null) return;

        shipmentCartItemModel.setStateLoadingCourierState(false);
        if (isTradeInDropOff) {
            shipmentAdapter.setSelectedCourierTradeInPickup(courierItemData);
        } else {
            if (!isForceReloadRates && courierItemData.getLogPromoCode() != null) {
                String cartString = shipmentCartItemModel.getCartString();

                ValidateUsePromoRequest validateUsePromoRequest = generateValidateUsePromoRequest();
                if (courierItemData.getLogPromoCode() != null && courierItemData.getLogPromoCode().length() > 0) {
                    for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                        if (ordersItem.getUniqueId().equals(shipmentCartItemModel.getCartString()) &&
                                !ordersItem.getCodes().contains(courierItemData.getLogPromoCode())) {
                            ordersItem.getCodes().add(courierItemData.getLogPromoCode());
                            ordersItem.setShippingId(courierItemData.getShipperId());
                            ordersItem.setSpId(courierItemData.getShipperProductId());
                            break;
                        }
                    }
                }

                List<ShipmentCartItemModel> shipmentCartItemModelLists = shipmentAdapter.getShipmentCartItemModelList();
                if (shipmentCartItemModelLists != null && shipmentCartItemModelLists.size() > 0) {
                    for (ShipmentCartItemModel tmpShipmentCartItemModel : shipmentCartItemModelLists) {
                        for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                            if (!shipmentCartItemModel.getCartString().equals(tmpShipmentCartItemModel.getCartString()) &&
                                    tmpShipmentCartItemModel.getCartString().equals(ordersItem.getUniqueId()) &&
                                    tmpShipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                                    tmpShipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                                ordersItem.getCodes().remove(tmpShipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getLogPromoCode());
                            }
                        }
                    }
                }

                shipmentPresenter.doValidateUseLogisticPromo(itemPosition, cartString, validateUsePromoRequest);
            }
            checkCourierPromo(courierItemData, itemPosition);
            shipmentAdapter.setSelectedCourier(itemPosition, courierItemData, false);
        }
        onNeedUpdateViewItem(itemPosition);
        shipmentPresenter.processSaveShipmentState(shipmentCartItemModel);
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
                                                       Map<String, String> tradeInCustomDimension,
                                                       String transactionId,
                                                       String eventCategory,
                                                       String eventAction,
                                                       String eventLabel) {
        checkoutAnalyticsCourierSelection.sendEnhancedECommerceCheckout(
                stringObjectMap, tradeInCustomDimension, transactionId, eventCategory, eventAction, eventLabel
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
    public void renderChangeAddressSuccess(boolean refreshCheckoutPage) {
        if (refreshCheckoutPage) {
            shipmentPresenter.processInitialLoadCheckoutPage(
                    true, isOneClickShipment(), isTradeIn(), true,
                    false, shipmentAdapter.getAddressShipmentData().getCornerId(), getDeviceId(), getCheckoutLeasingId()
            );
        }
    }

    @Override
    public void renderChangeAddressFailed(boolean refreshCheckoutPageIfSuccess) {
        RecipientAddressModel recipientAddressModel = shipmentAdapter.getAddressShipmentData();
        if (recipientAddressModel.getSelectedTabIndex() == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
            recipientAddressModel.setSelectedTabIndex(RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN);
            recipientAddressModel.setIgnoreSelectionAction(true);
        } else if (recipientAddressModel.getSelectedTabIndex() == RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN) {
            if (refreshCheckoutPageIfSuccess) {
                recipientAddressModel.setLocationDataModel(null);
                recipientAddressModel.setDropOffAddressDetail("");
                recipientAddressModel.setDropOffAddressName("");
            } else {
                recipientAddressModel.setSelectedTabIndex(RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT);
                recipientAddressModel.setIgnoreSelectionAction(true);
            }
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
        hideLoading();
        if (requestCode == PaymentConstant.REQUEST_CODE) {
            onResultFromPayment(resultCode, data);
        } else if (requestCode == LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY) {
            onResultFromAddNewAddress(resultCode, data);
        } else if (requestCode == CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS) {
            onResultFromRequestCodeAddressOptions(resultCode, data);
        } else if (requestCode == REQUEST_CODE_COURIER_PINPOINT) {
            onResultFromCourierPinpoint(resultCode, data);
        } else if (requestCode == REQUEST_CODE_EDIT_ADDRESS) {
            onResultFromEditAddress();
        } else if (requestCode == LogisticConstant.REQUEST_CODE_PICK_DROP_OFF_TRADE_IN) {
            onResultFromSetTradeInPinpoint(data);
        } else if (requestCode == REQUEST_CODE_PROMO) {
            onResultFromPromo(resultCode, data);
        }
    }

    // Re-fetch rates to get promo mvc icon for all order
    private void reloadCourierForMvc(ArrayList<String> appliedMvcCartStrings) {
        List<Object> object = shipmentAdapter.getShipmentDataList();
        if (object != null && object.size() > 0) {
            for (int i = 0; i < object.size(); i++) {
                if (object.get(i) instanceof ShipmentCartItemModel) {
                    ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) object.get(i);
                    if (appliedMvcCartStrings != null && appliedMvcCartStrings.contains(shipmentCartItemModel.getCartString())) {
                        prepareReloadRates(i, false);
                    } else {
                        prepareReloadRates(i, true);
                    }
                }
            }
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
                updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel(), false);
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
                shipmentAdapter.checkHasSelectAllCourier(false, -1, "");
            }

            boolean requiredToReloadRatesForMvcCourier = data.getBooleanExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_PROMO_MVC_LOCK_COURIER_FLOW, false);
            ArrayList<String> appliedMvcCartStrings = data.getStringArrayListExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_APPLIED_MVC_CART_STRINGS);
            if (requiredToReloadRatesForMvcCourier) {
                reloadCourierForMvc(appliedMvcCartStrings);
            }
        }
    }

    public void onResultFromEditAddress() {
        shipmentPresenter.processInitialLoadCheckoutPage(
                true, isOneClickShipment(), isTradeIn(), true,
                false, null, getDeviceId(), getCheckoutLeasingId()
        );
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

    private void onResultFromPayment(int resultCode, Intent data) {
        switch (resultCode) {
            case PaymentConstant.PAYMENT_FAILED:
            case PaymentConstant.PAYMENT_CANCELLED:
                if (data != null && data.getBooleanExtra(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT, false)) {
                    shipmentPresenter.processInitialLoadCheckoutPage(
                            true, isOneClickShipment(), isTradeIn(), true,
                            false, null, getDeviceId(), getCheckoutLeasingId()
                    );
                }
                break;
            default:
                Activity activity = getActivity();
                if (activity != null) activity.finish();
                break;
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

    private void onResultFromAddNewAddress(int resultCode, Intent data) {
        Activity activity = getActivity();
        if (activity != null) {
            if (resultCode == Activity.RESULT_CANCELED) {
                activity.finish();
            } else {
                if (data != null) {
                    SaveAddressDataModel addressDataModel = data.getParcelableExtra(LogisticConstant.EXTRA_ADDRESS_NEW);
                    if (addressDataModel != null) {
                        updateLocalCacheAddressData(addressDataModel);
                    }
                }
                shipmentPresenter.processInitialLoadCheckoutPage(
                        false, isOneClickShipment(), isTradeIn(), false,
                        false, null, getDeviceId(), getCheckoutLeasingId()
                );
            }
        }
    }

    private void onResultFromRequestCodeAddressOptions(int resultCode, Intent data) {
        switch (resultCode) {
            case CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS:
                RecipientAddressModel currentAddress = shipmentAdapter.getAddressShipmentData();
                ChosenAddressModel chosenAddressModel = data.getParcelableExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA);
                if (currentAddress != null && chosenAddressModel != null) {
                    shipmentPresenter.changeShippingAddress(currentAddress, chosenAddressModel, isOneClickShipment(), false, false, true);
                }
                break;

            case Activity.RESULT_CANCELED:
                if (getActivity() != null && data == null && shipmentPresenter.getShipmentCartItemModelList() == null) {
                    getActivity().finish();
                }
                break;

            default:
                shipmentPresenter.processInitialLoadCheckoutPage(
                        false, isOneClickShipment(), isTradeIn(), false,
                        false, null, getDeviceId(), getCheckoutLeasingId()
                );
                break;
        }
    }

    @Override
    public void onChangeAddress() {
        sendAnalyticsOnClickChangeAddress();
        sendAnalyticsOnClickChooseOtherAddressShipment();
        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventTradeInClickChangeAddress();
        }

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalLogistic.MANAGE_ADDRESS);
        intent.putExtra(EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true);
        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS);
    }

    private ShipmentDetailData getShipmentDetailData(ShipmentCartItemModel shipmentCartItemModel,
                                                     RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData shipmentDetailData;
        ShipmentDetailData oldShipmentDetailData = null;
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
    public void onFinishChoosingShipment(int lastSelectedCourierOrder, String lastSelectedCourierOrderCartString) {
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
            shipmentPresenter.checkPromoCheckoutFinalShipment(generateValidateUsePromoRequest(), lastSelectedCourierOrder, lastSelectedCourierOrderCartString);
        } else {
            clearPromoTrackingData();
            sendEEStep3();
        }
    }

    private void sendEEStep3() {
        List<ShipmentCartItemModel> shipmentCartItemModels = shipmentAdapter.getShipmentCartItemModelList();
        if (shipmentCartItemModels == null) return;

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
                    null,
                    EnhancedECommerceActionField.STEP_3,
                    ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                    ConstantTransactionAnalytics.EventAction.CLICK_ALL_COURIER_SELECTED,
                    "",
                    getCheckoutLeasingId());
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
    public void onCheckoutValidationResult(boolean result, Object shipmentData, int errorPosition) {
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
                                        notEligiblePromoHolderdata.setShopBadge(shipmentCartItemModel.getShopTypeInfoData().getShopBadge());
                                        break;
                                    }
                                }

                                if (i <= 0) {
                                    notEligiblePromoHolderdata.setShowShopSection(true);
                                } else if (voucherOrdersItemUiModels.get(i - 1).getUniqueId().length() > 0 && voucherOrdersItemUiModel.getUniqueId().length() > 0 && voucherOrdersItemUiModels.get(i - 1).getUniqueId().equals(voucherOrdersItemUiModel.getUniqueId())) {
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
                    hasClearPromoBeforeCheckout = true;
                    shipmentPresenter.cancelNotEligiblePromo(notEligiblePromoHolderdataList);
                } else {
                    hasClearPromoBeforeCheckout = false;
                    doCheckout();
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
                    doCheckout();
                }
            }
        } else if (shipmentData != null && !result) {
            hasRunningApiCall = false;
            hideLoading();
            sendAnalyticsDropshipperNotComplete();
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
                checkoutTradeInAnalytics.eventClickBayarCourierNotComplete();
            }
            sendAnalyticsCourierNotComplete();
            checkShippingCompletion(true);
        }
    }

    private void doCheckout() {
        shipmentPresenter.processSaveShipmentState();
        shipmentPresenter.processCheckout(isOneClickShipment(), isTradeIn(), isTradeInByDropOff(), getDeviceId(), getCornerId(), getCheckoutLeasingId());
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
    public void onNeedUpdateRequestData() {
        shipmentAdapter.checkHasSelectAllCourier(true, -1, "");
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
    public void onDonationChecked(boolean checked) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(() -> shipmentAdapter.updateDonation(checked));
        } else {
            shipmentAdapter.updateDonation(checked);
        }
        if (checked) sendAnalyticsOnClickTopDonation();
        checkoutAnalyticsCourierSelection.eventClickCheckboxDonation(checked);
        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventTradeInClickDonationOption(isTradeInByDropOff(), checked);
        }
    }

    @Override
    public void onEgoldChecked(boolean checked) {
        shipmentAdapter.updateEgold(checked);
        checkoutEgoldAnalytics.eventClickEgoldRoundup(checked);
        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventTradeInClickEgoldOption(isTradeInByDropOff(), checked);
        }
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
        Activity activity = getActivity();
        if (activity != null) {
            sendAnalyticsScreenName(getScreenName());
            if (isTradeIn()) {
                checkoutTradeInAnalytics.sendOpenScreenName(isTradeInByDropOff(), activity);
            }
        }
    }

    @Override
    public void setStateLoadingCourierStateAtIndex(int index, boolean isLoading) {
        shipmentLoadingIndex = isLoading ? index : -1;
        ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(index);
        shipmentCartItemModel.setStateLoadingCourierState(isLoading);
        onNeedUpdateViewItem(index);
    }

    @Override
    public void onLogisticPromoChosen(List<ShippingCourierUiModel> shippingCourierUiModels,
                                      CourierItemData courierData, RecipientAddressModel recipientAddressModel,
                                      int cartPosition, ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                      String promoCode, int selectedServiceId) {
        setStateLoadingCourierStateAtIndex(cartPosition, true);
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

            List<ShipmentCartItemModel> shipmentCartItemModelLists = shipmentAdapter.getShipmentCartItemModelList();
            if (shipmentCartItemModelLists != null && shipmentCartItemModelLists.size() > 0) {
                for (ShipmentCartItemModel tmpShipmentCartItemModel : shipmentCartItemModelLists) {
                    for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                        if (!shipmentCartItemModel.getCartString().equals(tmpShipmentCartItemModel.getCartString()) &&
                                tmpShipmentCartItemModel.getCartString().equals(ordersItem.getUniqueId()) &&
                                tmpShipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                            ordersItem.getCodes().remove(tmpShipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                        }
                    }
                }
            }

            for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                if (ordersItem.getUniqueId().equals(shipmentCartItemModel.getCartString())) {
                    ordersItem.setSpId(courierData.getShipperProductId());
                    ordersItem.setShippingId(courierData.getShipperId());
                    break;
                }
            }
            shipmentPresenter.doValidateUseLogisticPromo(cartPosition, cartString, validateUsePromoRequest);
        }
    }

    @Override
    public void onShippingDurationChoosen(List<ShippingCourierUiModel> shippingCourierUiModels, CourierItemData courierItemData, RecipientAddressModel recipientAddressModel, int cartPosition, int selectedServiceId, ServiceData serviceData, boolean flagNeedToSetPinpoint, boolean isDurationClick, boolean isClearPromo) {
        onShippingDurationChoosen(shippingCourierUiModels, courierItemData, recipientAddressModel,
                cartPosition, selectedServiceId, serviceData,
                flagNeedToSetPinpoint, isDurationClick, isClearPromo, false);
    }

    public void onShippingDurationChoosen(List<ShippingCourierUiModel> shippingCourierUiModels,
                                          CourierItemData recommendedCourier,
                                          RecipientAddressModel recipientAddressModel,
                                          int cartItemPosition, int selectedServiceId,
                                          ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                          boolean isDurationClick, boolean isClearPromo, boolean skipCheckAllCourier) {
        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventClickKurirTradeIn(serviceData.getServiceName());
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
                    Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(serviceDataTracker.getRangePrice().getMinPrice(), false)),
                    Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(serviceDataTracker.getRangePrice().getMaxPrice(), false))
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
                    shipmentAdapter.setSelectedCourier(cartItemPosition, recommendedCourier, true);
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

            GeneralBottomSheet generalBottomSheet = new GeneralBottomSheet();
            generalBottomSheet.setTitle(getActivity().getString(R.string.label_no_courier_bottomsheet_title));
            generalBottomSheet.setDesc(message);
            generalBottomSheet.setButtonText(getActivity().getString(R.string.label_no_courier_bottomsheet_button));
            generalBottomSheet.setIcon(R.drawable.checkout_module_ic_dropshipper);
            generalBottomSheet.setButtonOnClickListener(bottomSheet -> {
                bottomSheet.dismiss();
                return Unit.INSTANCE;
            });
            generalBottomSheet.show(getActivity(), getParentFragmentManager());
        }

        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventTradeInClickCourierGetOutOfCoverageError(isTradeInByDropOff());
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
            ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.setSelectedCourier(cartItemPosition, courierItemData, true);
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
            navigateToPinpointActivity(locationPass);
        }
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
        if (shipmentLoadingIndex == -1) {
            sendAnalyticsOnClickChangeDurationShipmentRecommendation();
            if (isTradeIn()) {
                checkoutTradeInAnalytics.eventTradeInClickCourierOption(isTradeInByDropOff());
            }
            showShippingDurationBottomsheet(shipmentCartItemModel, recipientAddressModel, cartPosition);
        }
    }

    private void showShippingDurationBottomsheet(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, int cartPosition) {
        if (shipmentCartItemModel.getShopShipmentList() == null || shipmentCartItemModel.getShopShipmentList().size() == 0) {
            onNoCourierAvailable(getString(com.tokopedia.logisticcart.R.string.label_no_courier_bottomsheet_message));
        } else {
            ShipmentDetailData shipmentDetailData = getShipmentDetailData(shipmentCartItemModel, recipientAddressModel);
            int codHistory = -1;
            if (shipmentPresenter.getCodData() != null) {
                codHistory = shipmentPresenter.getCodData().getCounterCod();
            }
            Activity activity = getActivity();
            if (shipmentDetailData != null && activity != null) {
                String pslCode = RatesDataConverter.getLogisticPromoCode(shipmentCartItemModel);
                ArrayList<Product> products = getProductForRatesRequest(shipmentCartItemModel);
                ShippingDurationBottomsheet shippingDurationBottomsheet = new ShippingDurationBottomsheet();
                shippingDurationBottomsheet.show(activity, getFragmentManager(), this,
                        shipmentDetailData, shipmentAdapter.getLastServiceId(), shipmentCartItemModel.getShopShipmentList(),
                        recipientAddressModel, cartPosition, codHistory,
                        shipmentCartItemModel.getIsLeasingProduct(), pslCode, products,
                        shipmentCartItemModel.getCartString(), shipmentCartItemModel.isOrderPrioritasDisable(),
                        isTradeInByDropOff(), shipmentCartItemModel.isFulfillment(),
                        shipmentCartItemModel.getShipmentCartData().getPreOrderDuration(), shipmentPresenter.generateRatesMvcParam(shipmentCartItemModel.getCartString()));
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
                product.setFreeShippingTc(cartItemModel.isFreeShippingExtra());

                products.add(product);
            }
        }

        return products;
    }

    @Override
    public void onChangeShippingCourier(RecipientAddressModel recipientAddressModel,
                                        ShipmentCartItemModel shipmentCartItemModel,
                                        int cartPosition) {
        if (shipmentLoadingIndex == -1) {
            List<ShippingCourierUiModel> shippingCourierUiModels = shipmentCartItemModel.getSelectedShipmentDetailData().getShippingCourierViewModels();
            sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel);
            if (shippingCourierUiModels == null || shippingCourierUiModels.size() == 0 &&
                    shipmentPresenter.getShippingCourierViewModelsState(shipmentCartItemModel.getOrderNumber()) != null) {
                shippingCourierUiModels = shipmentPresenter.getShippingCourierViewModelsState(shipmentCartItemModel.getOrderNumber());
            }
            Activity activity = getActivity();
            if (activity != null) {
                shippingCourierBottomsheet = new ShippingCourierBottomsheet();
                shippingCourierBottomsheet.show(activity, getFragmentManager(), this,
                        shippingCourierUiModels, recipientAddressModel, cartPosition);
                if (shippingCourierUiModels != null) {
                    checkHasCourierPromo(shippingCourierUiModels);
                }
            }
        }
    }

    private void reloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition, boolean skipMvc) {
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
                        shipmentCartItemModel, shipmentCartItemModel.getShopShipmentList(), false,
                        getProductForRatesRequest(shipmentCartItemModel), shipmentCartItemModel.getCartString(),
                        isTradeInByDropOff(), shipmentAdapter.getAddressShipmentData(), cartPosition > -1, skipMvc);
            }
        }
    }

    @Override
    public void onRetryReloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition) {
        reloadCourier(shipmentCartItemModel, cartPosition, false);
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
                    shipmentAdapter.getAddressShipmentData(), false, false);
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
        CartProtectionInfoBottomSheetHelper.openWebviewInBottomSheet(this, getActivityContext(), url, getString(R.string.title_activity_checkout_webview));
    }

    @Override
    public void onProcessToPayment() {
        showLoading();
        shipmentAdapter.checkDropshipperValidation();
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
    public void updateCourierBottomsheetHasNoData(int cartPosition, ShipmentCartItemModel shipmentCartItemModel) {
        if (shippingCourierBottomsheet != null) {
            shippingCourierBottomsheet.setShippingCourierViewModels(null, cartPosition, shipmentCartItemModel, null);
        }
    }

    @Override
    public void updateCourierBottomssheetHasData(List<ShippingCourierUiModel> shippingCourierUiModels, int cartPosition, ShipmentCartItemModel shipmentCartItemModel, PreOrderModel preOrderModel) {
        if (shippingCourierBottomsheet != null) {
            shippingCourierBottomsheet.setShippingCourierViewModels(shippingCourierUiModels, cartPosition, shipmentCartItemModel, preOrderModel);
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
                        setValidateUseSpIdParam(shipmentCartItemModel, ordersItem);
                    }
                }
            }
            if (isTradeIn()) {
                validateUsePromoRequest.setTradeIn(1);
                validateUsePromoRequest.setTradeInDropOff(isTradeInByDropOff() ? 1 : 0);
            }
            if (isOneClickShipment()) {
                validateUsePromoRequest.setCartType("ocs");
            } else {
                validateUsePromoRequest.setCartType("default");
            }
            shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest);
            this.bboPromoCodes = bboPromoCodes;
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
                    setValidateUseSpIdParam(shipmentCartItemModel, ordersItem);
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
            if (isOneClickShipment()) {
                validateUsePromoRequest.setCartType("ocs");
            } else {
                validateUsePromoRequest.setCartType("default");
            }
            shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest);
            this.bboPromoCodes = bboPromoCodes;
            return validateUsePromoRequest;
        }
    }

    private void setValidateUseSpIdParam(ShipmentCartItemModel shipmentCartItemModel, OrdersItem ordersItem) {
        if (shipmentCartItemModel.getSelectedShipmentDetailData() == null) {
            ordersItem.setShippingId(0);
            ordersItem.setSpId(0);
        } else {
            if (isTradeInByDropOff()) {
                if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                    ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperId());
                    ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperProductId());
                } else {
                    ordersItem.setShippingId(0);
                    ordersItem.setSpId(0);
                }
            } else {
                if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                    ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId());
                    ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId());
                } else {
                    ordersItem.setShippingId(0);
                    ordersItem.setSpId(0);
                }
            }
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
                if (shipmentCartItemModel.getSelectedShipmentDetailData() == null) {
                    ordersItem.setShippingId(0);
                    ordersItem.setSpId(0);
                } else {
                    if (isTradeInByDropOff()) {
                        if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                            ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperId());
                            ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperProductId());
                        } else {
                            ordersItem.setShippingId(0);
                            ordersItem.setSpId(0);
                        }
                    } else {
                        if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                            ordersItem.setShippingId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId());
                            ordersItem.setSpId(shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId());
                        } else {
                            ordersItem.setShippingId(0);
                            ordersItem.setSpId(0);
                        }
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
            shipmentAdapter.checkHasSelectAllCourier(false, -1, "");
        }
    }

    @Override
    public void triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(String transactionId,
                                                                                 String deviceModel,
                                                                                 long devicePrice,
                                                                                 String diagnosticId) {
        List<DataCheckoutRequest> dataCheckoutRequests = shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentAdapter.getShipmentCartItemModelList());

        String eventCategory = ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION;
        String eventAction = ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN;
        String eventLabel = ConstantTransactionAnalytics.EventLabel.SUCCESS;
        Map<String, String> tradeInCustomDimension = new HashMap<>();
        if (isTradeIn()) {
            eventCategory = EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN;
            eventLabel = String.format(Locale.getDefault(), EVENT_LABEL_TRADE_IN_CHECKOUT_EE,
                    deviceModel, devicePrice, diagnosticId);
            tradeInCustomDimension.put(KEY_USER_ID, userSessionInterface.getUserId());
            tradeInCustomDimension.put(KEY_BUSINESS_UNIT, VALUE_TRADE_IN);
            if (isTradeInByDropOff()) {
                eventAction = EVENT_ACTION_PILIH_PEMBAYARAN_INDOMARET;
                tradeInCustomDimension.put(KEY_SCREEN_NAME, SCREEN_NAME_DROP_OFF_ADDRESS);
            } else {
                eventAction = EVENT_ACTION_PILIH_PEMBAYARAN_NORMAL;
                tradeInCustomDimension.put(KEY_SCREEN_NAME, SCREEN_NAME_NORMAL_ADDRESS);
            }
        }

        shipmentPresenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
                dataCheckoutRequests,
                tradeInCustomDimension,
                EnhancedECommerceActionField.STEP_4,
                eventCategory,
                eventAction,
                eventLabel,
                getCheckoutLeasingId());
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
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (promoNotEligibleBottomsheet == null) {
                promoNotEligibleBottomsheet = new PromoNotEligibleBottomSheet(notEligiblePromoHolderdataList, this);
            }
            promoNotEligibleBottomsheet.setDismissListener(() -> {
                checkoutAnalyticsCourierSelection.eventClickBatalOnErrorPromoConfirmation();
                return Unit.INSTANCE;
            });
            promoNotEligibleBottomsheet.setNotEligiblePromoHolderDataList(notEligiblePromoHolderdataList);
            promoNotEligibleBottomsheet.show(activity, getParentFragmentManager());
            checkoutAnalyticsCourierSelection.eventViewPopupErrorPromoConfirmation();
            PromoRevampAnalytics.INSTANCE.eventCheckoutViewBottomsheetPromoError();
        }
    }

    @Override
    public void onButtonContinueClicked() {
        if (promoNotEligibleBottomsheet != null) {
            checkoutAnalyticsCourierSelection.eventClickLanjutkanOnErrorPromoConfirmation();
            ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList = promoNotEligibleBottomsheet.getNotEligiblePromoHolderDataList();
            promoNotEligibleBottomsheet.dismiss();
            shipmentPresenter.cancelNotEligiblePromo(notEligiblePromoHolderdataArrayList);
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
            setChosenAddressForTradeInDropOff(intent);
            setPromoExtraMvcLockCourierFlow(intent);

            startActivityForResult(intent, REQUEST_CODE_PROMO);
        }
    }

    @Override
    public void onShow() {
        //no op
    }

    @Override
    public void removeIneligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList) {
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

        doCheckout();
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
            shipmentAdapter.addTickerAnnouncementData(tickerAnnouncementHolderData);
            if (rvShipment.isComputingLayout()) {
                rvShipment.post(this::onAddTickerAnnouncementMessage);
            } else {
                onAddTickerAnnouncementMessage();
            }
        }
    }

    private void onAddTickerAnnouncementMessage() {
        if (!rvShipment.canScrollVertically(-1)) {
            shipmentAdapter.notifyItemInserted(ShipmentAdapter.SECOND_HEADER_POSITION);
            rvShipment.scrollToPosition(0);
        } else {
            shipmentAdapter.notifyItemInserted(ShipmentAdapter.SECOND_HEADER_POSITION);
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
        checkoutTradeInAnalytics.eventTradeInClickPilihIndomaret();
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
            showCampaignTimerExpiredDialog(timer, diff, checkoutAnalyticsCourierSelection);
        }
    }

    private void showCampaignTimerExpiredDialog(CampaignTimerUi timer, long diff, CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection) {
        if (isAdded()) {
            FragmentManager fragmentManager = getParentFragmentManager();
            if (diff <= 0 && fragmentManager != null) {
                ExpiredTimeDialog dialog = ExpiredTimeDialog.newInstance(timer, checkoutAnalyticsCourierSelection, this);
                dialog.show(fragmentManager, "expired dialog");
            }
        }
    }

    private void setCampaignTimer() {
        CampaignTimerUi timer = shipmentPresenter.getCampaignTimer();
        if (timer != null && timer.getShowTimer()) {
            long diff = TimeHelper.timeBetweenRFC3339(timer.getTimerServer(), timer.getTimerExpired());
            cdLayout.setVisibility(View.VISIBLE);
            cdText.setText(timer.getTimerDescription());
            cdView.setRemainingMilliseconds(diff);

            cdView.setOnFinish(() -> {
                ExpiredTimeDialog dialog = ExpiredTimeDialog.newInstance(timer, checkoutAnalyticsCourierSelection, ShipmentFragment.this);
                dialog.show(getFragmentManager(), "expired dialog");
                return Unit.INSTANCE;
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
                shipmentPresenter.changeShippingAddress(recipientAddressModel, null, true, true, true, true);
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
    public boolean hasSelectTradeInLocation() {
        RecipientAddressModel recipientAddressModel = shipmentAdapter.getAddressShipmentData();
        if (recipientAddressModel == null) return false;
        return recipientAddressModel.getLocationDataModel() != null;
    }

    @Override
    public void onTradeInAddressTabChanged(int addressPosition) {
        int cartItemPosition = addressPosition + 1;
        onNeedUpdateViewItem(cartItemPosition);
        onNeedUpdateViewItem(addressPosition);

        RecipientAddressModel recipientAddressModel = shipmentAdapter.getAddressShipmentData();
        if (recipientAddressModel.getSelectedTabIndex() == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
            checkoutTradeInAnalytics.eventClickJemputTab();
            if (recipientAddressModel.getLocationDataModel() != null) {
                shipmentPresenter.changeShippingAddress(recipientAddressModel, null, true, false, true, true);
            }
        } else {
            checkoutTradeInAnalytics.eventClickDropOffTab();
            if (recipientAddressModel.getLocationDataModel() != null) {
                shipmentPresenter.changeShippingAddress(recipientAddressModel, null, true, true, true, true);
            }
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
        setChosenAddressForTradeInDropOff(intent);
        setPromoExtraMvcLockCourierFlow(intent);

        startActivityForResult(intent, REQUEST_CODE_PROMO);

        if (isTradeIn()) {
            checkoutTradeInAnalytics.eventTradeInClickPromo(isTradeInByDropOff());
        }
    }

    private void setChosenAddressForTradeInDropOff(Intent intent) {
        Activity activity = getActivity();
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        if (activity != null && isTradeInByDropOff() && ChooseAddressUtils.INSTANCE.isRollOutUser(activity) && recipientAddressModel != null) {
            LocationDataModel locationDataModel = recipientAddressModel.getLocationDataModel();
            ChosenAddress chosenAddress;
            if (locationDataModel != null) {
                chosenAddress = new ChosenAddress(
                        ChosenAddress.MODE_ADDRESS,
                        locationDataModel.getAddrId(),
                        locationDataModel.getDistrict(),
                        locationDataModel.getPostalCode(),
                        (!TextUtils.isEmpty(locationDataModel.getLatitude()) && !TextUtils.isEmpty(locationDataModel.getLongitude())) ? locationDataModel.getLatitude() + "," + locationDataModel.getLongitude() : ""
                );
            } else {
                chosenAddress = new ChosenAddress(
                        ChosenAddress.MODE_ADDRESS,
                        recipientAddressModel.getId(),
                        recipientAddressModel.getDestinationDistrictId(),
                        recipientAddressModel.getPostalCode(),
                        (!TextUtils.isEmpty(recipientAddressModel.getLatitude()) && !TextUtils.isEmpty(recipientAddressModel.getLongitude())) ? recipientAddressModel.getLatitude() + "," + recipientAddressModel.getLongitude() : ""
                );
            }
            intent.putExtra(ARGS_CHOSEN_ADDRESS, chosenAddress);
        }
    }

    private void setPromoExtraMvcLockCourierFlow(Intent intent) {
        boolean promoMvcLockCourierFlow = false;
        if (shipmentPresenter.getValidateUsePromoRevampUiModel() != null) {
            if (!shipmentPresenter.getValidateUsePromoRevampUiModel().getPromoUiModel().getAdditionalInfoUiModel().getPromoSpIds().isEmpty()) {
                promoMvcLockCourierFlow = true;
            }
        } else if (shipmentPresenter.getLastApplyData() != null) {
            if (!shipmentPresenter.getLastApplyData().getAdditionalInfo().getPromoSpIds().isEmpty()) {
                promoMvcLockCourierFlow = true;
            }
        }
        intent.putExtra(com.tokopedia.purchase_platform.common.constant.PromoConstantKt.ARGS_PROMO_MVC_LOCK_COURIER_FLOW, promoMvcLockCourierFlow);
    }

    @Override
    public void updateButtonPromoCheckout(PromoUiModel promoUiModel, boolean isNeedToHitValidateFinal) {
        doUpdateButtonPromoCheckout(promoUiModel);
        updatePromoTrackingData(promoUiModel.getTrackingDetailUiModels());
        sendEEStep3();
        updateLogisticPromoData(promoUiModel);
        if (shipmentAdapter.hasSetAllCourier()) {
            resetPromoBenefit();
            setPromoBenefit(promoUiModel.getBenefitSummaryInfoUiModel().getSummaries());
            shipmentAdapter.updateShipmentCostModel();

            // Check if need to hit validate final, if so then hit validate final by checking is all courier have been selected
            if (isNeedToHitValidateFinal) {
                shipmentAdapter.checkHasSelectAllCourier(false, -1, "");
            }
        }
    }

    private void doUpdateButtonPromoCheckout(PromoUiModel promoUiModel) {
        shipmentAdapter.updatePromoCheckoutData(promoUiModel);
        onNeedUpdateViewItem(shipmentAdapter.getPromoCheckoutPosition());
    }

    @Override
    public void doResetButtonPromoCheckout() {
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
            if (promoCheckoutVoucherOrdersItemUiModel.getSuccess() && promoCheckoutVoucherOrdersItemUiModel.getType().equals("logistic")) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    if (shipmentCartItemModel.getCartString().equals(promoCheckoutVoucherOrdersItemUiModel.getUniqueId())) {
                        VoucherLogisticItemUiModel log = new VoucherLogisticItemUiModel();
                        log.setCode(promoCheckoutVoucherOrdersItemUiModel.getCode());
                        log.setCouponDesc(promoCheckoutVoucherOrdersItemUiModel.getTitleDescription());
                        log.setCouponAmount(getFormattedCurrency(promoCheckoutVoucherOrdersItemUiModel.getDiscountAmount()));
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
            } else if (!promoCheckoutVoucherOrdersItemUiModel.getSuccess() && UtilsKt.isNullOrEmpty(promoCheckoutVoucherOrdersItemUiModel.getType())) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    if (shipmentCartItemModel.getCartString().equals(promoCheckoutVoucherOrdersItemUiModel.getUniqueId()) &&
                            shipmentCartItemModel.getSelectedShipmentDetailData() != null && shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                            shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getLogPromoCode().equals(promoCheckoutVoucherOrdersItemUiModel.getCode())) {
                        resetCourier(shipmentCartItemModel);
                    }
                }
            }
        }
    }

    private String getFormattedCurrency(int price) {
        if (price == 0) {
            return "";
        }
        return com.tokopedia.utils.currency.CurrencyFormatUtil.INSTANCE.getThousandSeparatorString(price, false, 0).getFormattedString();
    }

    @Override
    public void resetCourier(ShipmentCartItemModel shipmentCartItemModel) {
        int index = shipmentAdapter.getShipmentDataList().indexOf(shipmentCartItemModel);
        if (index != -1) {
            ValidateUsePromoRequest validateUsePromoRequest = shipmentPresenter.getLastValidateUseRequest();
            if (validateUsePromoRequest != null) {
                for (OrdersItem ordersItem : validateUsePromoRequest.getOrders()) {
                    if (ordersItem.getUniqueId().equals(shipmentCartItemModel.getCartString()) &&
                            shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                            shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                        String redStateBBOCode = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getLogPromoCode();
                        ordersItem.getCodes().remove(redStateBBOCode);
                    }
                }
            }

            shipmentAdapter.resetCourier(index);
            addShippingCompletionTicker(shipmentCartItemModel.isEligibleNewShippingExperience());
        }
    }

    @Override
    public void resetAllCourier() {
        shipmentAdapter.resetAllCourier();
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

                if (isTriggeredByPaymentButton) {
                    showToastNormal(getActivity().getString(R.string.message_error_courier_not_selected));
                }

                rvShipment.smoothScrollToPosition(position);
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

                if (isTriggeredByPaymentButton && notSelectCourierCount > 0) {
                    if (notSelectCourierCount == 1) {
                        showToastNormal(getActivity().getString(R.string.message_error_courier_not_selected));
                    } else {
                        showToastNormal(String.format(getString(R.string.message_error_multiple_courier_not_selected), notSelectCourierCount));
                    }
                }

                rvShipment.smoothScrollToPosition(firstFoundPosition);
            }
        }
    }

    @Override
    public void onShowTickerShippingCompletion() {
        checkoutAnalyticsCourierSelection.eventViewSummaryTransactionTickerCourierNotComplete(userSessionInterface.getUserId());
    }

    @Override
    public void onCashbackUpdated(int amount) {
        // No-op
    }

    @Override
    public void onPrimaryCTAClicked() {
        releaseBookingIfAny();
    }

    @Override
    public void onClickTradeInInfo() {
        checkoutTradeInAnalytics.eventTradeInClickInformation(isTradeInByDropOff());
        FragmentManager fragmentManager = getFragmentManager();
        Context context = getContext();
        if (fragmentManager != null && context != null) {
            TradeInInfoBottomsheetHelperKt.showTradeInInfoBottomsheet(fragmentManager, context);
        }
    }

    @Override
    public void onClickSwapInIndomaret() {
        checkoutTradeInAnalytics.eventTradeInClickTukarDiIndomaret();
    }

    @Override
    public void onSwapInUserAddress() {
        checkoutTradeInAnalytics.eventTradeInClickTukarDiAlamatmu();
    }

    @Override
    public FragmentManager getCurrentFragmentManager() {
        return getParentFragmentManager();
    }

    @Override
    public void scrollToPositionWithOffset(int position, float dy) {
        RecyclerView.LayoutManager layoutManager = rvShipment.getLayoutManager();
        if (layoutManager != null) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, (int) dy);
        }
    }

    @Override
    public void prepareReloadRates(int lastSelectedCourierOrder, boolean skipMvc) {
        ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(lastSelectedCourierOrder);
        if (shipmentCartItemModel != null) {
            reloadCourier(shipmentCartItemModel, lastSelectedCourierOrder, skipMvc);
        }
    }

    @Override
    public void updateLocalCacheAddressData(UserAddress userAddress) {
        Activity activity = getActivity();
        if (activity != null) {
            ChooseAddressUtils.INSTANCE.updateLocalizingAddressDataFromOther(
                    activity,
                    userAddress.getAddressId(),
                    userAddress.getCityId(),
                    userAddress.getDistrictId(),
                    userAddress.getLatitude(),
                    userAddress.getLongitude(),
                    String.format("%s %s", userAddress.getAddressName(), userAddress.getReceiverName()),
                    userAddress.getPostalCode(),
                    userAddress.getShopId(), userAddress.getWarehouseId()
            );
        }
    }

    private void updateLocalCacheAddressData(SaveAddressDataModel saveAddressDataModel) {
        Activity activity = getActivity();
        if (activity != null) {
            ChooseAddressUtils.INSTANCE.updateLocalizingAddressDataFromOther(
                    activity,
                    String.valueOf(saveAddressDataModel.getId()),
                    String.valueOf(saveAddressDataModel.getCityId()),
                    String.valueOf(saveAddressDataModel.getDistrictId()),
                    saveAddressDataModel.getLatitude(),
                    saveAddressDataModel.getLongitude(),
                    String.format("%s %s", saveAddressDataModel.getAddressName(), saveAddressDataModel.getReceiverName()),
                    saveAddressDataModel.getPostalCode(),
                            "", ""
            );
        }
    }

}