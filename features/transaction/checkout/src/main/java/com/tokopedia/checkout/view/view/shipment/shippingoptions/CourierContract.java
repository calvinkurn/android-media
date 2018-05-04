package com.tokopedia.checkout.view.view.shipment.shippingoptions;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.checkout.view.view.shipment.shippingoptions.viewmodel.ShipmentData;

import java.util.List;

public interface CourierContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showNoConnection(String message);

        void showData();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadCourier(ShipmentDetailData shipmentDetailData);

        String getShipmentTickerInfo();

        List<ShipmentData> getShipmentDataList();
    }

}
