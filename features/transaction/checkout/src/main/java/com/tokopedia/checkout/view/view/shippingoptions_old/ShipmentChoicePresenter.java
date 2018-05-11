package com.tokopedia.checkout.view.view.shippingoptions_old;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoicePresenter extends BaseDaggerPresenter<IShipmentChoiceView>
        implements IShipmentChoicePresenter {

    private List<ShipmentItemData> shipments = new ArrayList<>();
    private ShipmentItemData selectedShipment;
    private ShipmentDetailData shipmentDetailData;

    @Override
    public void attachView(IShipmentChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadShipmentChoice(ShipmentDetailData shipmentDetailData, ShipmentItemData selectedShipment) {
        getView().showLoading();
        if (shipmentDetailData != null) {
            this.shipmentDetailData = shipmentDetailData;
            shipments = shipmentDetailData.getShipmentItemData();
            if (selectedShipment != null) {
                this.selectedShipment = selectedShipment;
                for (int i = 0; i < shipments.size(); i++) {
                    if (shipments.get(i).getServiceId() == selectedShipment.getServiceId()) {
                        shipments.get(i).setSelected(true);
                    }
                }
            }
            getView().showData();
            getView().hideLoading();
        }

    }

    @Override
    public List<ShipmentItemData> getShipmentChoices() {
        return shipments;
    }

    @Override
    public void setShipmentChoices(List<ShipmentItemData> shipmentChoices) {
        shipments.clear();
        shipments.addAll(shipmentChoices);
    }

    @Override
    public ShipmentDetailData getShipmentDetailData() {
        return shipmentDetailData;
    }

}
