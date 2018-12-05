package com.tokopedia.checkout.view.feature.shippingoptions;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShopShipment;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentOptionData;

import java.util.List;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public interface CourierContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showNoConnection(String message);

        void showData();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadCourier(ShipmentDetailData shipmentDetailData, List<ShopShipment> shopShipmentList);

        String getShipmentTickerInfo();

        List<ShipmentOptionData> getShipmentDataList();

        void setSelectedCourier(CourierItemData selectedCourier);

        boolean isHasSelectedCourier();
    }

}
