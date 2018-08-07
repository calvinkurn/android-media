package com.tokopedia.checkout.view.view.shippingrecommendation;

import android.os.Bundle;
import android.view.View;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.view.shippingrecommendation.di.DaggerShipmentRecommendationComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.di.ShipmentRecommendationComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.di.ShipmentRecommendationModule;
import com.tokopedia.design.component.BottomSheets;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShipmentRecommendationDurationBottomsheet extends BottomSheets
        implements ShipmentRecommendationContract.View, HasComponent<CartComponent> {

    public static final String ARGUMENT_SHIPMENT_DETAIL_DATA = "ARGUMENT_SHIPMENT_DETAIL_DATA";

    @Inject
    ShipmentRecommendationContract.Presenter presenter;

    public static ShipmentRecommendationDurationBottomsheet newInstance(ShipmentDetailData shipmentDetailData) {
        ShipmentRecommendationDurationBottomsheet shipmentRecommendationDurationBottomsheet =
                new ShipmentRecommendationDurationBottomsheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        shipmentRecommendationDurationBottomsheet.setArguments(bundle);

        return shipmentRecommendationDurationBottomsheet;
    }

    private void initializeInjector() {
        ShipmentRecommendationComponent component = DaggerShipmentRecommendationComponent.builder()
                .cartComponent(getComponent())
                .shipmentRecommendationModule(new ShipmentRecommendationModule())
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
        initializeInjector();
        presenter.attachView(this);
        if (getArguments() != null) {
            ShipmentDetailData shipmentDetailData = getArguments().getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA);
            if (shipmentDetailData != null) {
                presenter.loadCourierRecommendation(shipmentDetailData);
            }
        }
    }

    @Override
    public CartComponent getComponent() {
        return CartComponentInjector.newInstance(getActivity().getApplication()).getCartApiServiceComponent();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNoConnection(String message) {

    }

    @Override
    public void showData() {

    }
}
