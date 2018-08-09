package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.di.DaggerShippingCourierComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.di.ShippingCourierComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.di.ShippingCourierModule;
import com.tokopedia.design.component.BottomSheets;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierBottomsheet extends BottomSheets
        implements ShippingCourierContract.View, HasComponent<CartComponent>, ShippingCourierAdapterListener {

    public static final String ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST = "ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST";
    public static final String ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION";

    private LinearLayout llContent;
    private RecyclerView rvCourier;
    private LinearLayout llNetworkErrorView;

    private ShippingCourierBottomsheetListener shippingCourierBottomsheetListener;

    @Inject
    ShippingCourierContract.Presenter presenter;
    @Inject
    ShippingCourierAdapter shippingDurationAdapter;

    public static ShippingCourierBottomsheet newInstance(List<ShippingCourierViewModel> shippingCourierViewModels,
                                                         int cartPosition) {
        ShippingCourierBottomsheet shippingCourierBottomsheet =
                new ShippingCourierBottomsheet();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST, new ArrayList<>(shippingCourierViewModels));
        bundle.putInt(ARGUMENT_CART_POSITION, cartPosition);
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
            int cartPosition = getArguments().getInt(ARGUMENT_CART_POSITION);
            List<ShippingCourierViewModel> shippingCourierViewModels =
                    getArguments().getParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST);
            if (shippingCourierViewModels != null) {
                presenter.setData(shippingCourierViewModels);
                setupRecyclerView(cartPosition);
            }
        }
    }

    private void setupRecyclerView(int cartPosition) {
        shippingDurationAdapter.setShippingCourierAdapterListener(this);
        shippingDurationAdapter.setShippingCourierViewModels(presenter.getShippingCourierViewModels());
        shippingDurationAdapter.setCartPosition(cartPosition);
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
    public void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, int cartPosition) {
        CourierItemData courierItemData = presenter.getCourierItemData(shippingCourierViewModel);
        shippingCourierBottomsheetListener.onCourierChoosen(courierItemData, cartPosition);
        dismiss();
    }
}
