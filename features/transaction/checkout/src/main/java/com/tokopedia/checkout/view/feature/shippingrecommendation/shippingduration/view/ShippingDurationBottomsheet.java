package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.di.DaggerShippingDurationComponent;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.di.ShippingDurationComponent;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.di.ShippingDurationModule;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.logisticdata.data.constant.CourierConstant;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationBottomsheet extends BottomSheets
        implements ShippingDurationContract.View, HasComponent<CartComponent>,
        ShippingDurationAdapterListener {

    public static final String ARGUMENT_SHIPMENT_DETAIL_DATA = "ARGUMENT_SHIPMENT_DETAIL_DATA";
    public static final String ARGUMENT_SHOP_SHIPMENT_LIST = "ARGUMENT_SHOP_SHIPMENT_LIST";
    public static final String ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION";
    public static final String ARGUMENT_RECIPIENT_ADDRESS_MODEL = "ARGUMENT_RECIPIENT_ADDRESS_MODEL";
    public static final String ARGUMENT_SELECTED_SERVICE_ID = "ARGUMENT_SELECTED_SERVICE_ID";

    private ProgressBar pbLoading;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llContent;
    private RecyclerView rvDuration;

    private ShippingDurationBottomsheetListener shippingDurationBottomsheetListener;

    @Inject
    ShippingDurationContract.Presenter presenter;
    @Inject
    ShippingDurationAdapter shippingDurationAdapter;

    public static ShippingDurationBottomsheet newInstance(ShipmentDetailData shipmentDetailData,
                                                          int selectedServiceId,
                                                          List<ShopShipment> shopShipmentList,
                                                          RecipientAddressModel recipientAddressModel,
                                                          int cartPosition) {
        ShippingDurationBottomsheet shippingDurationBottomsheet =
                new ShippingDurationBottomsheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        bundle.putParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST, new ArrayList<>(shopShipmentList));
        bundle.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel);
        bundle.putInt(ARGUMENT_CART_POSITION, cartPosition);
        bundle.putInt(ARGUMENT_SELECTED_SERVICE_ID, selectedServiceId);
        shippingDurationBottomsheet.setArguments(bundle);

        return shippingDurationBottomsheet;
    }

    public void setShippingDurationBottomsheetListener(ShippingDurationBottomsheetListener shippingDurationBottomsheetListener) {
        this.shippingDurationBottomsheetListener = shippingDurationBottomsheetListener;
    }

    private void initializeInjector() {
        ShippingDurationComponent component = DaggerShippingDurationComponent.builder()
                .cartComponent(getComponent())
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
        return getString(R.string.title_bottomsheet_shipment_duration);
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
            int cartPosition = getArguments().getInt(ARGUMENT_CART_POSITION);
            int selectedServiceId = getArguments().getInt(ARGUMENT_SELECTED_SERVICE_ID);
            setupRecyclerView(cartPosition);
            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
            List<ShopShipment> shopShipments = getArguments().getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST);
            if (shipmentDetailData != null) {
                presenter.loadCourierRecommendation(shipmentDetailData, selectedServiceId, shopShipments);
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
        shippingDurationAdapter.setShippingDurationViewModels(presenter.getShippingDurationViewModels());
        shippingDurationAdapter.setCartPosition(cartPosition);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        rvDuration.setLayoutManager(linearLayoutManager);
        rvDuration.setAdapter(shippingDurationAdapter);
    }

    @Override
    public CartComponent getComponent() {
        return CartComponentInjector.newInstance(getActivity().getApplication()).getCartApiServiceComponent();
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
                            if (shipmentDetailData != null) {
                                presenter.loadCourierRecommendation(shipmentDetailData, selectedServiceId, shopShipments);
                            }
                        }
                    }
                });
        updateHeight();
    }

    @Override
    public void showData(List<ShippingDurationViewModel> shippingDurationViewModelList) {
        shippingDurationAdapter.notifyDataSetChanged();
        updateHeight();
    }

    @Override
    public void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                          int cartPosition, String serviceName) {
        boolean flagNeedToSetPinpoint = false;
        int selectedServiceId = 0;
        for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
            shippingCourierViewModel.setSelected(shippingCourierViewModel.getProductData().isRecommend());
            if (shippingCourierViewModel.getServiceData().getServiceId() == CourierConstant.SERVICE_ID_INSTANT ||
                    shippingCourierViewModel.getServiceData().getServiceId() == CourierConstant.SERVICE_ID_SAME_DAY) {
                if (shippingCourierViewModel.getProductData().getError() != null &&
                        shippingCourierViewModel.getProductData().getError().getErrorMessage() != null &&
                        shippingCourierViewModel.getProductData().getError().getErrorId() != null &&
                        shippingCourierViewModel.getProductData().getError().getErrorId().equals(ErrorData.ERROR_PINPOINT_NEEDED)) {
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
                    presenter.getRecipientAddressModel(), cartPosition, selectedServiceId, serviceName, flagNeedToSetPinpoint);
        }
        dismiss();
    }

    @Override
    public void onAllShippingDurationItemShown() {
        if (presenter.getShippingDurationViewModels() != null &&
                presenter.getShippingDurationViewModels().size() > 0) {
            presenter.getShippingDurationViewModels().get(0).setShowShowCase(true);
            if (rvDuration.isComputingLayout()) {
                rvDuration.post(new Runnable() {
                    @Override
                    public void run() {
                        shippingDurationAdapter.notifyItemChanged(0);
                    }
                });
            } else {
                shippingDurationAdapter.notifyItemChanged(0);
            }
        }
    }

    @Override
    public void onDurationShipmentRecommendationShowCaseClosed() {
        if (shippingDurationBottomsheetListener != null) {
            shippingDurationBottomsheetListener.onShippingDurationButtonShowCaseDoneClicked();
        }
    }
}
