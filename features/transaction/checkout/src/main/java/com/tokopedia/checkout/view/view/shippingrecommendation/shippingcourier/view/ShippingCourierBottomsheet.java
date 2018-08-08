package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.di.DaggerShippingCourierComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.di.ShippingCourierComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.di.ShippingCourierModule;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierBottomsheet extends BottomSheets
        implements ShippingCourierContract.View, HasComponent<CartComponent>, ShippingCourierAdapterListener {

    public static final String ARGUMENT_SERVICE_DATA = "ARGUMENT_SERVICE_DATA";

    private LinearLayout llContent;
    private RecyclerView rvCourier;
    private LinearLayout llNetworkErrorView;

    private ShippingCourierBottomsheetListener shippingCourierBottomsheetListener;

    @Inject
    ShippingCourierContract.Presenter presenter;
    @Inject
    ShippingCourierAdapter shippingDurationAdapter;

    public static ShippingCourierBottomsheet newInstance(ServiceData serviceData) {
        ShippingCourierBottomsheet shippingCourierBottomsheet =
                new ShippingCourierBottomsheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_SERVICE_DATA, serviceData);
        shippingCourierBottomsheet.setArguments(bundle);

        return shippingCourierBottomsheet;
    }

    public void setShippingCourierBottomsheetListener(ShippingCourierBottomsheetListener shippingCourierBottomsheetListener) {
        this.shippingCourierBottomsheetListener = shippingCourierBottomsheetListener;
    }

    private void initializeInjector() {
        ShippingCourierComponent component = DaggerShippingCourierComponent.builder()
                .cartComponent(getComponent())
                .shippingCourierModule(new ShippingCourierModule())
                .build();

        component.inject(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_shipment_courier_choice;
    }

    @Override
    protected String title() {
        return getString(R.string.title_shipment_courier_bottomsheet);
    }

    @Override
    public void initView(View view) {
        llContent = view.findViewById(R.id.ll_content);
        rvCourier = view.findViewById(R.id.rv_courier);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);

        initializeInjector();
        presenter.attachView(this);
        if (getArguments() != null) {
            ServiceData serviceData = getArguments().getParcelable(ARGUMENT_SERVICE_DATA);
            if (serviceData != null) {
                presenter.setData(serviceData);
                setupRecyclerView();
            }
        }
    }

    private void setupRecyclerView() {
        shippingDurationAdapter.setShippingCourierAdapterListener(this);
        shippingDurationAdapter.setShippingCourierViewModels(presenter.getShippingCourierViewModels());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourier.setLayoutManager(linearLayoutManager);
        rvCourier.setAdapter(shippingDurationAdapter);
    }

    @Override
    public CartComponent getComponent() {
        return CartComponentInjector.newInstance(getActivity().getApplication()).getCartApiServiceComponent();
    }

    @Override
    public void onCourierChoosen(ProductData productData) {
        shippingCourierBottomsheetListener.onCourierChoosen(productData);
        dismiss();
    }
}
