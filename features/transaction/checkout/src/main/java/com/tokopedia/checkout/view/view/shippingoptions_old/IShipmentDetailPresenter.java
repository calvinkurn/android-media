package com.tokopedia.checkout.view.view.shippingoptions_old;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailPresenter extends CustomerPresenter<IShipmentDetailView> {

    void loadShipmentData(ShipmentDetailData shipmentDetailData);

    void loadAllCourier();

    List<CourierItemData> getCouriers();

    void getPinPointMapData();

    ShipmentDetailData getShipmentDetailData();

    CourierItemData getSelectedCourier();

    ShipmentItemData getSelectedShipment();

    void setSelectedShipment(ShipmentItemData selectedShipment);

    void setSelectedCourier(CourierItemData selectedCourier);

    void setCourierList(List<CourierItemData> couriers);

    void updatePinPoint(LocationPass locationPass);

}
