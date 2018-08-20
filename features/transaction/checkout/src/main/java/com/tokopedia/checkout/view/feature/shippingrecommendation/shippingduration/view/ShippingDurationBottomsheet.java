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
            setupRecyclerView(cartPosition);
            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
            List<ShopShipment> shopShipments = getArguments().getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST);
            if (shipmentDetailData != null) {
                presenter.loadCourierRecommendation(shipmentDetailData, shopShipments);
            }
        }
    }

    @Override
    protected void onCloseButtonClick() {
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
                            if (shipmentDetailData != null) {
                                presenter.loadCourierRecommendation(shipmentDetailData, shopShipments);
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
                                          int cartPosition) {
        for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
            shippingCourierViewModel.setSelected(shippingCourierViewModel.getProductData().isRecommend());
        }
        shippingDurationBottomsheetListener.onShippingDurationChoosen(
                shippingCourierViewModels, presenter.getCourierItemData(shippingCourierViewModels),
                presenter.getRecipientAddressModel(), cartPosition);
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
}
