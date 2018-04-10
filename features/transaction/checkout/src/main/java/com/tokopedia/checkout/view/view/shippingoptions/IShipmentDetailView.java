package com.tokopedia.checkout.view.view.shippingoptions;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void renderShipmentWithMap(ShipmentDetailData shipmentDetailData);

    void renderShipmentWithoutMap(ShipmentDetailData shipmentDetailData);

    void renderFirstLoadedRatesData(ShipmentDetailData shipmentDetailData);

    void renderAfterReloadRatesData(ShipmentDetailData shipmentDetailData);

    void showAllCouriers();

    void showPinPointChooserMap(ShipmentDetailData shipmentDetailData);

    void showPinPointMap(ShipmentDetailData shipmentDetailData);

    void renderSelectedCourier(CourierItemData courierItemData);

    void selectCourier(CourierItemData courierItemData);

    Activity getActivity();

    void showErrorSnackbar(String message);

}
