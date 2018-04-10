package com.tokopedia.checkout.view.view.shippingoptions;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentChoicePresenter extends CustomerPresenter<IShipmentChoiceView> {

    void loadShipmentChoice(ShipmentDetailData shipmentDetailData, ShipmentItemData selectedShipment);

    List<ShipmentItemData> getShipmentChoices();

    ShipmentDetailData getShipmentDetailData();
}
