package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.di.DaggerShippingDurationComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.di.ShippingDurationComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.di.ShippingDurationModule;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationBottomsheet extends BottomSheets
        implements ShippingDurationContract.View, HasComponent<CartComponent>,
        ShippingDurationAdapterListener {

    public static final String ARGUMENT_SHIPMENT_DETAIL_DATA = "ARGUMENT_SHIPMENT_DETAIL_DATA";

    private ProgressBar pbLoading;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llContent;
    private TextView tvDurationHeaderInfo;
    private RecyclerView rvDuration;

    private ShippingDurationBottomsheetListener shippingDurationBottomsheetListener;

    @Inject
    ShippingDurationContract.Presenter presenter;
    @Inject
    ShippingDurationAdapter shippingDurationAdapter;

    public static ShippingDurationBottomsheet newInstance(ShipmentDetailData shipmentDetailData) {
        ShippingDurationBottomsheet shippingDurationBottomsheet =
                new ShippingDurationBottomsheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA, shipmentDetailData);
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
        setupRecyclerView();
        if (getArguments() != null) {
            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
            if (shipmentDetailData != null) {
                presenter.loadCourierRecommendation(shipmentDetailData);
            }
        }
    }

    @Override
    protected void onCloseButtonClick() {
        dismiss();
    }

    private void setupRecyclerView() {
        shippingDurationAdapter.setShippingDurationAdapterListener(this);
        shippingDurationAdapter.setShippingDurationViewModels(presenter.getShippingDurationViewModels());
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
    public void showNoConnection(String message) {
        pbLoading.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(getContext(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (getArguments() != null) {
                            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
                            if (shipmentDetailData != null) {
                                presenter.loadCourierRecommendation(shipmentDetailData);
                            }
                        }
                    }
                });
    }

    @Override
    public void showData(List<ShippingDurationViewModel> shippingDurationViewModelList) {
        shippingDurationAdapter.notifyDataSetChanged();
        updateHeight();
    }

    @Override
    public void onShippingDurationChoosen(ServiceData serviceData) {
        shippingDurationBottomsheetListener.onShippingDurationChoosen(serviceData);
        dismiss();
    }
}
