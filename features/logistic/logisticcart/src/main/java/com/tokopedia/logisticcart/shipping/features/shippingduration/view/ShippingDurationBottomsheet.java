package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.DaggerShippingDurationComponent;
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.ShippingDurationComponent;
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.ShippingDurationModule;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationBottomsheet extends BottomSheets
        implements ShippingDurationContract.View, ShippingDurationAdapterListener {

    private static final String ARGUMENT_SHIPMENT_DETAIL_DATA = "ARGUMENT_SHIPMENT_DETAIL_DATA";
    private static final String ARGUMENT_SHOP_SHIPMENT_LIST = "ARGUMENT_SHOP_SHIPMENT_LIST";
    private static final String ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION";
    private static final String ARGUMENT_RECIPIENT_ADDRESS_MODEL = "ARGUMENT_RECIPIENT_ADDRESS_MODEL";
    private static final String ARGUMENT_SELECTED_SERVICE_ID = "ARGUMENT_SELECTED_SERVICE_ID";
    private static final String ARGUMENT_DISABLE_PROMO_COURIER = "ARGUMENT_DISABLE_PROMO_COURIER";
    private static final String ARGUMENT_IS_LEASING = "ARGUMENT_IS_LEASING";
    private static final String ARGUMENT_PSL_CODE = "ARGUMENT_PSL_CODE";
    private static final String ARGUMENT_PRODUCTS = "ARGUMENT_PRODUCTS";
    private static final String ARGUMENT_CART_STRING = "ARGUMENT_CART_STRING";
    private static final String ARGUMENT_DISABLE_ORDER_PRIORITAS = "ARGUMENT_DISABLE_ORDER_PRIORITAS";
    private static final String ARGUMENT_IS_TRADE_IN_DROP_OFF = "ARGUMENT_IS_TRADE_IN_DROP_OFF";
    private static final String ARGUMENT_MVC = "ARGUMENT_MVC";
    private static final String ARGUMENT_IS_FULFILLMENT = "ARGUMENT_IS_FULFILLMENT";
    private static final String ARGUMENT_PO_TIME = "ARGUMENT_PO_TIME";

    private static final String CHOOSE_COURIER_TRACE = "mp_choose_courier";

    private ProgressBar pbLoading;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llContent;
    private RecyclerView rvDuration;

    private ShippingDurationBottomsheetListener shippingDurationBottomsheetListener;

    private PerformanceMonitoring chooseCourierTracePerformance;
    private boolean isChooseCourierTraceStopped;

    private boolean isDisableCourierPromo;
    private boolean isDisableOrderPrioritas;
    private int mCartPosition = -1;

    private RecipientAddressModel mRecipientAddress;

    @Inject
    ShippingDurationContract.Presenter presenter;
    @Inject
    ShippingDurationAdapter shippingDurationAdapter;
    @Inject
    CheckoutAnalyticsCourierSelection mPromoTracker;
    private boolean mIsCorner = false;

    public static ShippingDurationBottomsheet newInstance(ShipmentDetailData shipmentDetailData,
                                                          int selectedServiceId,
                                                          List<ShopShipment> shopShipmentList,
                                                          RecipientAddressModel recipientAddressModel,
                                                          int cartPosition,
                                                          boolean isLeasing, String pslCode,
                                                          ArrayList<Product> products, String cartString,
                                                          boolean isDisableOrderPrioritas,
                                                          boolean isTradeInDropOff, boolean isFulFillment,
                                                          int preOrderTime, String mvc) {
        ShippingDurationBottomsheet shippingDurationBottomsheet = new ShippingDurationBottomsheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        bundle.putParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST, new ArrayList<>(shopShipmentList));
        bundle.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel);
        bundle.putInt(ARGUMENT_CART_POSITION, cartPosition);
        bundle.putInt(ARGUMENT_SELECTED_SERVICE_ID, selectedServiceId);
        bundle.putBoolean(ARGUMENT_IS_LEASING, isLeasing);
        bundle.putString(ARGUMENT_PSL_CODE, pslCode);
        bundle.putParcelableArrayList(ARGUMENT_PRODUCTS, products);
        bundle.putString(ARGUMENT_CART_STRING, cartString);
        bundle.putBoolean(ARGUMENT_DISABLE_ORDER_PRIORITAS, isDisableOrderPrioritas);
        bundle.putBoolean(ARGUMENT_IS_TRADE_IN_DROP_OFF, isTradeInDropOff);
        bundle.putBoolean(ARGUMENT_IS_FULFILLMENT, isFulFillment);
        bundle.putInt(ARGUMENT_PO_TIME, preOrderTime);
        bundle.putString(ARGUMENT_MVC, mvc);
        shippingDurationBottomsheet.setArguments(bundle);

        return shippingDurationBottomsheet;
    }

    public static ShippingDurationBottomsheet newInstance() {
        return new ShippingDurationBottomsheet();
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FLEXIBLE;
    }

    public void setShippingDurationBottomsheetListener(ShippingDurationBottomsheetListener shippingDurationBottomsheetListener) {
        this.shippingDurationBottomsheetListener = shippingDurationBottomsheetListener;
    }

    private void initializeInjector() {
        BaseMainApplication baseMainApplication = (BaseMainApplication) getActivity().getApplication();
        ShippingDurationComponent component = DaggerShippingDurationComponent.builder()
                .baseAppComponent(baseMainApplication.getBaseAppComponent())
                .shippingDurationModule(new ShippingDurationModule())
                .build();

        component.inject(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_shipment_duration_choice;
    }

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.widget_bottomsheet_shipping;
    }

    @Override
    public void setupDialog(android.app.Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        if (dialog != null) {
            dialog.findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    protected String title() {
        return getString(R.string.title_bottomsheet_shipment_duration);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseCourierTracePerformance = PerformanceMonitoring.start(CHOOSE_COURIER_TRACE);
    }

    @Override
    public void initView(View view) {
        pbLoading = view.findViewById(R.id.pb_loading);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        llContent = view.findViewById(R.id.ll_content);
        rvDuration = view.findViewById(R.id.rv_duration);

        initializeInjector();
        presenter.attachView(this);
        if (getArguments() != null) {
            mRecipientAddress = getArguments().getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL);
            mCartPosition = getArguments().getInt(ARGUMENT_CART_POSITION);
            int selectedServiceId = getArguments().getInt(ARGUMENT_SELECTED_SERVICE_ID);
            if (mRecipientAddress != null) {
                mIsCorner = mRecipientAddress.isCornerAddress();
            }
            isDisableCourierPromo = getArguments().getBoolean(ARGUMENT_DISABLE_PROMO_COURIER);
            setupRecyclerView(mCartPosition);
            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
            List<ShopShipment> shopShipments = getArguments().getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST);
            boolean isLeasing = getArguments().getBoolean(ARGUMENT_IS_LEASING);
            String pslCode = getArguments().getString(ARGUMENT_PSL_CODE, "");
            ArrayList<Product> products = getArguments().getParcelableArrayList(ARGUMENT_PRODUCTS);
            String cartString = getArguments().getString(ARGUMENT_CART_STRING);
            isDisableOrderPrioritas = getArguments().getBoolean(ARGUMENT_DISABLE_ORDER_PRIORITAS);
            boolean isTradeInDropOff = getArguments().getBoolean(ARGUMENT_IS_TRADE_IN_DROP_OFF);
            String mvc = getArguments().getString(ARGUMENT_MVC, "");
            boolean isFulfillment = getArguments().getBoolean(ARGUMENT_IS_FULFILLMENT);
            int preOrderTime = getArguments().getInt(ARGUMENT_PO_TIME);
            if (shipmentDetailData != null) {
                // Called from checkout
                presenter.loadCourierRecommendation(shipmentDetailData, selectedServiceId,
                        shopShipments, mIsCorner, isLeasing, pslCode, products, cartString, isTradeInDropOff, mRecipientAddress, isFulfillment, preOrderTime, mvc);
            }
        }
    }

    @Override
    protected void configView(View parentView) {
        super.configView(parentView);
        parentView.findViewById(R.id.layout_title).setOnClickListener(null);
        parentView.findViewById(R.id.btn_close).setOnClickListener(view -> onCloseButtonClick());
    }

    @Override
    protected void onCloseButtonClick() {
        if (shippingDurationBottomsheetListener != null) {
            shippingDurationBottomsheetListener.onShippingDurationButtonCloseClicked();
        }
        dismiss();
    }

    private void setupRecyclerView(int cartPosition) {
        shippingDurationAdapter.setShippingDurationAdapterListener(this);
        shippingDurationAdapter.setCartPosition(cartPosition);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        rvDuration.setLayoutManager(linearLayoutManager);
        rvDuration.setAdapter(shippingDurationAdapter);
    }

    @Override
    public void showLoading() {
        llContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        llContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorPage(String message) {
        pbLoading.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(getContext(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (getArguments() != null) {
                            RecipientAddressModel recipientAddressModel = getArguments().getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL);
                            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
                            List<ShopShipment> shopShipments = getArguments().getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST);
                            int selectedServiceId = getArguments().getInt(ARGUMENT_SELECTED_SERVICE_ID);
                            boolean isLeasing = getArguments().getBoolean(ARGUMENT_IS_LEASING);
                            String pslCode = getArguments().getString(ARGUMENT_PSL_CODE, "");
                            ArrayList<Product> products = getArguments().getParcelableArrayList(ARGUMENT_PRODUCTS);
                            String cartString = getArguments().getString(ARGUMENT_CART_STRING);
                            boolean isTradeInDropOff = getArguments().getBoolean(ARGUMENT_IS_TRADE_IN_DROP_OFF);
                            boolean isFulfillment = getArguments().getBoolean(ARGUMENT_IS_FULFILLMENT);
                            int preOrderTime = getArguments().getInt(ARGUMENT_PO_TIME);
                            String mvc = getArguments().getString(ARGUMENT_MVC, "");
                            if (shipmentDetailData != null) {
                                presenter.loadCourierRecommendation(
                                        shipmentDetailData, selectedServiceId, shopShipments,
                                        mIsCorner, isLeasing, pslCode,
                                        products, cartString, isTradeInDropOff, recipientAddressModel,
                                        isFulfillment, preOrderTime, mvc
                                );
                            }
                        }
                    }
                });
        updateHeight();
    }

    @Override
    public void showData(List<ShippingDurationUiModel> shippingDurationUiModelList, LogisticPromoUiModel promoViewModel) {
        shippingDurationAdapter.setShippingDurationViewModels(shippingDurationUiModelList, promoViewModel, isDisableOrderPrioritas);
        if (promoViewModel != null && promoViewModel.getEtaData() != null && promoViewModel.getEtaData().getTextEta().isEmpty() && promoViewModel.getEtaData().getErrorCode() == 1) shippingDurationAdapter.initiateShowcase();
        updateHeight();
        boolean hasCourierPromo = checkHasCourierPromo(shippingDurationUiModelList);
        if (hasCourierPromo) {
            sendAnalyticCourierPromo(shippingDurationUiModelList);
        }
        if (promoViewModel != null) {
            mPromoTracker.eventViewPromoLogisticTicker(promoViewModel.getPromoCode());
            if (promoViewModel.getDisabled()) {
                mPromoTracker.eventViewPromoLogisticTickerDisable(promoViewModel.getPromoCode());
            }
        }
    }

    private boolean checkHasCourierPromo(List<ShippingDurationUiModel> shippingDurationUiModelList) {
        boolean hasCourierPromo = false;
        for (ShippingDurationUiModel shippingDurationUiModel : shippingDurationUiModelList) {
            if (shippingDurationUiModel.getServiceData().getIsPromo() == 1) {
                hasCourierPromo = true;
                break;
            }
        }
        return hasCourierPromo;
    }

    private void sendAnalyticCourierPromo(List<ShippingDurationUiModel> shippingDurationUiModelList) {
        for (ShippingDurationUiModel shippingDurationUiModel : shippingDurationUiModelList) {
            shippingDurationBottomsheetListener.onShowDurationListWithCourierPromo(
                    shippingDurationUiModel.getServiceData().getIsPromo() == 1,
                    shippingDurationUiModel.getServiceData().getServiceName()
            );
        }
    }

    @Override
    public void showNoCourierAvailable(String message) {
        shippingDurationBottomsheetListener.onNoCourierAvailable(message);
        dismiss();
    }

    @Override
    public void stopTrace() {
        if (!isChooseCourierTraceStopped) {
            chooseCourierTracePerformance.stopTrace();
            isChooseCourierTraceStopped = true;
        }
    }

    @Override
    public boolean isDisableCourierPromo() {
        return isDisableCourierPromo;
    }

    @Override
    public void onShippingDurationChoosen(List<ShippingCourierUiModel> shippingCourierUiModels,
                                          int cartPosition, ServiceData serviceData) {
        boolean flagNeedToSetPinpoint = false;
        int selectedServiceId = 0;
        if (isToogleYearEndPromotionOn()) {
            if (serviceData.getError() != null && serviceData.getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED) &&
                    !TextUtils.isEmpty(serviceData.getError().getErrorMessage())) {
                flagNeedToSetPinpoint = true;
                selectedServiceId = serviceData.getServiceId();
            }
        } else {
            for (ShippingCourierUiModel shippingCourierUiModel : shippingCourierUiModels) {
                shippingCourierUiModel.setSelected(shippingCourierUiModel.getProductData().isRecommend());
                if (shippingCourierUiModel.getProductData().getError() != null &&
                        shippingCourierUiModel.getProductData().getError().getErrorMessage() != null &&
                        shippingCourierUiModel.getProductData().getError().getErrorId() != null &&
                        shippingCourierUiModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                    flagNeedToSetPinpoint = true;
                    selectedServiceId = shippingCourierUiModel.getServiceData().getServiceId();
                    shippingCourierUiModel.getServiceData().getTexts().setTextRangePrice(
                            shippingCourierUiModel.getProductData().getError().getErrorMessage());
                }
            }
        }
        if (shippingDurationBottomsheetListener != null) {
            try {
                shippingDurationBottomsheetListener.onShippingDurationChoosen(
                        shippingCourierUiModels, presenter.getCourierItemData(shippingCourierUiModels),
                        mRecipientAddress, cartPosition, selectedServiceId, serviceData,
                        flagNeedToSetPinpoint, true, true);
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isToogleYearEndPromotionOn() {
        if (getActivity() != null) {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
            return remoteConfig.getBoolean("mainapp_enable_year_end_promotion");
        }
        return false;
    }

    @Override
    public void onLogisticPromoClicked(LogisticPromoUiModel data) {
        mPromoTracker.eventClickPromoLogisticTicker(data.getPromoCode());
        // Project Army
        ShippingDurationUiModel serviceData = shippingDurationAdapter.getRatesDataFromLogisticPromo(data.getServiceId());
        if (serviceData == null) {
            showErrorPage(getString(R.string.logistic_promo_serviceid_mismatch_message));
            return;
        }
        CourierItemData courierData = presenter.getCourierItemDataById(data.getShipperProductId(), serviceData.getShippingCourierViewModelList());
        if (courierData == null) {
            showErrorPage(getString(R.string.logistic_promo_serviceid_mismatch_message));
            return;
        }

        courierData.setLogPromoCode(data.getPromoCode());
        courierData.setLogPromoMsg(data.getDisableText());
        courierData.setDiscountedRate(data.getDiscountedRate());
        courierData.setShippingRate(data.getShippingRate());
        courierData.setBenefitAmount(data.getBenefitAmount());
        courierData.setPromoTitle(data.getTitle());
        courierData.setHideShipperName(data.getHideShipperName());
        courierData.setShipperName(data.getShipperName());

        try {
            shippingDurationBottomsheetListener.onLogisticPromoChosen(
                    serviceData.getShippingCourierViewModelList(), courierData,
                    mRecipientAddress, mCartPosition,
                    serviceData.getServiceData(), false, data.getPromoCode(), data.getServiceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismiss();
    }

    private void showPslDialog(LogisticPromoUiModel data) {
        Dialog tkpdDialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        tkpdDialog.setTitle(getString(R.string.tkpd_promo_brand));
        tkpdDialog.setDesc(MethodChecker.fromHtml(data.getDialogMsg()));
        tkpdDialog.setBtnOk(getString(R.string.shiprecc_next));
        tkpdDialog.setBtnCancel(getString(R.string.shiprecc_cancel));
        tkpdDialog.setOnCancelClickListener(view -> {
            mPromoTracker.eventClickBatalTerapkanPromo(data.getPromoCode());
            tkpdDialog.dismiss();
        });
        tkpdDialog.setOnOkClickListener(view -> {
            ShippingDurationUiModel serviceData = shippingDurationAdapter.getRatesDataFromLogisticPromo(data.getServiceId());
            if (serviceData == null) {
                showErrorPage(getString(R.string.logistic_promo_serviceid_mismatch_message));
                tkpdDialog.dismiss();
                return;
            }
            CourierItemData courierData = presenter.getCourierItemDataById(data.getShipperProductId(), serviceData.getShippingCourierViewModelList());
            if (courierData == null) {
                showErrorPage(getString(R.string.logistic_promo_serviceid_mismatch_message));
                tkpdDialog.dismiss();
                return;
            }
            courierData.setLogPromoCode(data.getPromoCode());
            courierData.setLogPromoMsg(data.getDisableText());
            shippingDurationBottomsheetListener.onLogisticPromoChosen(
                    serviceData.getShippingCourierViewModelList(), courierData,
                    mRecipientAddress, mCartPosition,
                    serviceData.getServiceData(), false, data.getPromoCode(), data.getServiceId());
            tkpdDialog.dismiss();
            dismiss();
        });
        tkpdDialog.show();
    }

}
