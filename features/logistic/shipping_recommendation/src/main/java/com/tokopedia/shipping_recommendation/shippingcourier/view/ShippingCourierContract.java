package com.tokopedia.shipping_recommendation.shippingcourier.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.RecipientAddressModel;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShopShipment;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showErrorPage(String message, ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList);

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void setData(List<ShippingCourierViewModel> shippingCourierViewModels);

        List<ShippingCourierViewModel> getShippingCourierViewModels();

        CourierItemData getCourierItemData(ShippingCourierViewModel shippingCourierViewModel);

        void updateSelectedCourier(ShippingCourierViewModel shippingCourierViewModel);

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        RecipientAddressModel getRecipientAddressModel();
    }

}
