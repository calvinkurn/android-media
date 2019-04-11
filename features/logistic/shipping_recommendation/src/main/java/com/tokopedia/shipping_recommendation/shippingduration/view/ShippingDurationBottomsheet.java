package com.tokopedia.shipping_recommendation.shippingduration.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.shipping_recommendation.R;
import com.tokopedia.shipping_recommendation.domain.ShippingParam;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.LogisticPromoViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.shipping_recommendation.shippingduration.di.DaggerShippingDurationComponent;
import com.tokopedia.shipping_recommendation.shippingduration.di.ShippingDurationComponent;
import com.tokopedia.shipping_recommendation.shippingduration.di.ShippingDurationModule;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationBottomsheet extends BottomSheets
        implements ShippingDurationContract.View, ShippingDurationAdapterListener {

    public static final String ARGUMENT_SHIPPING_PARAM = "ARGUMENT_SHIPPING_PARAM";
    public static final String ARGUMENT_SHIPMENT_DETAIL_DATA = "ARGUMENT_SHIPMENT_DETAIL_DATA";
    public static final String ARGUMENT_SHOP_SHIPMENT_LIST = "ARGUMENT_SHOP_SHIPMENT_LIST";
    public static final String ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION";
    public static final String ARGUMENT_RECIPIENT_ADDRESS_MODEL = "ARGUMENT_RECIPIENT_ADDRESS_MODEL";
    public static final String ARGUMENT_SELECTED_SERVICE_ID = "ARGUMENT_SELECTED_SERVICE_ID";
    public static final String ARGUMENT_COD_HISTORY = "ARGUMENT_COD_HISTORY";
    public static final String ARGUMENT_DISABLE_PROMO_COURIER = "ARGUMENT_DISABLE_PROMO_COURIER";

    private static final String CHOOSE_COURIER_TRACE = "mp_choose_courier";

    private ProgressBar pbLoading;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llContent;
    private RecyclerView rvDuration;

    private ShippingDurationBottomsheetListener shippingDurationBottomsheetListener;

    private PerformanceMonitoring chooseCourierTracePerformance;
    private boolean isChooseCourierTraceStopped;

    private boolean isDisableCourierPromo;
    private int mCartPosition = -1;

    @Inject
    ShippingDurationContract.Presenter presenter;
    @Inject
    ShippingDurationAdapter shippingDurationAdapter;
    @Inject
    CheckoutAnalyticsCourierSelection mPromoTracker;
    private String mCornerId = "";

    public static ShippingDurationBottomsheet newInstance(ShipmentDetailData shipmentDetailData,
                                                          int selectedServiceId,
                                                          List<ShopShipment> shopShipmentList,
                                                          RecipientAddressModel recipientAddressModel,
                                                          int cartPosition, int codHistory) {
        ShippingDurationBottomsheet shippingDurationBottomsheet = new ShippingDurationBottomsheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        bundle.putParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST, new ArrayList<>(shopShipmentList));
        bundle.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel);
        bundle.putInt(ARGUMENT_CART_POSITION, cartPosition);
        bundle.putInt(ARGUMENT_SELECTED_SERVICE_ID, selectedServiceId);
        bundle.putInt(ARGUMENT_COD_HISTORY, codHistory);
        shippingDurationBottomsheet.setArguments(bundle);

        return shippingDurationBottomsheet;
    }

    public static ShippingDurationBottomsheet newInstance() {
        return new ShippingDurationBottomsheet();
    }

    public void setShippingDurationBottomsheetListener(ShippingDurationBottomsheetListener shippingDurationBottomsheetListener) {
        this.shippingDurationBottomsheetListener = shippingDurationBottomsheetListener;
    }

    public void updateArguments(ShippingParam shippingParam, int selectedServiceId, int codHistory, boolean disableCourierPromo, List<ShopShipment> shopShipmentList) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_SHIPPING_PARAM, shippingParam);
        bundle.putParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST, new ArrayList<>(shopShipmentList));
        bundle.putInt(ARGUMENT_SELECTED_SERVICE_ID, selectedServiceId);
        bundle.putInt(ARGUMENT_COD_HISTORY, codHistory);
        bundle.putBoolean(ARGUMENT_DISABLE_PROMO_COURIER, disableCourierPromo);
        setArguments(bundle);
    }

    private void initializeInjector() {
        ShippingDurationComponent component = DaggerShippingDurationComponent.builder()
                .shippingDurationModule(new ShippingDurationModule())
                .build();

        component.inject(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_shipment_duration_choice;
    }

    @Override
    protected String title() {
        return getString(R.string.title_bottomsheet_shipment);
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
            RecipientAddressModel recipientAddressModel = getArguments().getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL);
            presenter.setRecipientAddressModel(recipientAddressModel);
            mCartPosition = getArguments().getInt(ARGUMENT_CART_POSITION);
            int selectedServiceId = getArguments().getInt(ARGUMENT_SELECTED_SERVICE_ID);
            int codHistory = getArguments().getInt(ARGUMENT_COD_HISTORY);
            if (recipientAddressModel != null) {
                mCornerId = recipientAddressModel.isCornerAddress() ? recipientAddressModel.getId() : "";
            }
            isDisableCourierPromo = getArguments().getBoolean(ARGUMENT_DISABLE_PROMO_COURIER);
            setupRecyclerView(mCartPosition);
            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
            ShippingParam shippingParam = getArguments().getParcelable(ARGUMENT_SHIPPING_PARAM);
            List<ShopShipment> shopShipments = getArguments().getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST);
            if (shipmentDetailData != null) {
                presenter.loadCourierRecommendation(shipmentDetailData, selectedServiceId, shopShipments, codHistory, mCornerId);
            } else if (shippingParam != null) {
                presenter.loadCourierRecommendation(shippingParam, selectedServiceId, shopShipments, codHistory, mCornerId);
            }
        }
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
                            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
                            List<ShopShipment> shopShipments = getArguments().getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST);
                            int selectedServiceId = getArguments().getInt(ARGUMENT_SELECTED_SERVICE_ID);
                            int codHistory = getArguments().getInt(ARGUMENT_COD_HISTORY);
                            if (shipmentDetailData != null) {
                                presenter.loadCourierRecommendation(shipmentDetailData, selectedServiceId, shopShipments, codHistory, mCornerId);
                            }
                        }
                    }
                });
        updateHeight();
    }

    @Override
    public void showData(List<ShippingDurationViewModel> shippingDurationViewModelList, LogisticPromoViewModel promoViewModel) {
        shippingDurationAdapter.setHasCourierPromo(checkHasCourierPromo(shippingDurationViewModelList));
        shippingDurationAdapter.setShippingDurationViewModels(shippingDurationViewModelList, promoViewModel);
        shippingDurationAdapter.initiateShowcase();
        updateHeight();
        boolean hasCourierPromo = checkHasCourierPromo(shippingDurationViewModelList);
        if (hasCourierPromo) {
            sendAnalyticCourierPromo(shippingDurationViewModelList);
        }
    }

    private boolean checkHasCourierPromo(List<ShippingDurationViewModel> shippingDurationViewModelList) {
        boolean hasCourierPromo = false;
        for (ShippingDurationViewModel shippingDurationViewModel : shippingDurationViewModelList) {
            if (shippingDurationViewModel.getServiceData().getIsPromo() == 1) {
                hasCourierPromo = true;
                break;
            }
        }
        return hasCourierPromo;
    }

    private void sendAnalyticCourierPromo(List<ShippingDurationViewModel> shippingDurationViewModelList) {
        for (ShippingDurationViewModel shippingDurationViewModel : shippingDurationViewModelList) {
            shippingDurationBottomsheetListener.onShowDurationListWithCourierPromo(
                    shippingDurationViewModel.getServiceData().getIsPromo() == 1,
                    shippingDurationViewModel.getServiceData().getServiceName()
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
    public void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                          int cartPosition, ServiceData serviceData, boolean hasCourierPromo) {
        boolean flagNeedToSetPinpoint = false;
        int selectedServiceId = 0;
        if (isToogleYearEndPromotionOn()) {
            if (serviceData.getError() != null && serviceData.getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED) &&
                    !TextUtils.isEmpty(serviceData.getError().getErrorMessage())) {
                flagNeedToSetPinpoint = true;
                selectedServiceId = serviceData.getServiceId();
            }
        } else {
            for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
                shippingCourierViewModel.setSelected(shippingCourierViewModel.getProductData().isRecommend());
                if (shippingCourierViewModel.getProductData().getError() != null &&
                        shippingCourierViewModel.getProductData().getError().getErrorMessage() != null &&
                        shippingCourierViewModel.getProductData().getError().getErrorId() != null &&
                        shippingCourierViewModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                    flagNeedToSetPinpoint = true;
                    selectedServiceId = shippingCourierViewModel.getServiceData().getServiceId();
                    shippingCourierViewModel.getServiceData().getTexts().setTextRangePrice(
                            shippingCourierViewModel.getProductData().getError().getErrorMessage());
                }
            }
        }
        if (shippingDurationBottomsheetListener != null) {
            shippingDurationBottomsheetListener.onShippingDurationChoosen(
                    shippingCourierViewModels, presenter.getCourierItemData(shippingCourierViewModels),
                    presenter.getRecipientAddressModel(), cartPosition, selectedServiceId, serviceData.getServiceName(),
                    flagNeedToSetPinpoint, hasCourierPromo, true);
        }
        dismiss();
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
    public void onLogisticPromoClicked(LogisticPromoViewModel data) {
        mPromoTracker.eventClickPromoLogisticTicker(data.getPromoCode());
        Dialog tkpdDialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        tkpdDialog.setTitle(getString(R.string.tkpd_promo_brand));
        tkpdDialog.setDesc(MethodChecker.fromHtml(data.getDialogMsg()));
        tkpdDialog.setBtnOk(getString(R.string.shiprecc_next));
        tkpdDialog.setBtnCancel(getString(R.string.shiprecc_cancel));
        tkpdDialog.setOnCancelClickListener(view -> {
            mPromoTracker.eventClickBatalTerapkanPromo(data.getPromoCode());
            tkpdDialog.dismiss();
        });
        tkpdDialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShippingDurationViewModel serviceData = shippingDurationAdapter.getRatesDataFromLogisticPromo(data.getServiceId());
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
                        presenter.getRecipientAddressModel(), mCartPosition, data.getServiceId(),
                        serviceData.getServiceData().getServiceName(), false, data.getPromoCode());
                tkpdDialog.dismiss();
                dismiss();
            }
        });
        tkpdDialog.show();
    }

}
