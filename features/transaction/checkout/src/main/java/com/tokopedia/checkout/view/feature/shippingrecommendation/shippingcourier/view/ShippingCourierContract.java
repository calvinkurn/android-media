package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentCartItemModel;

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

    interface Presenter extends CustomerPresenter<ShippingCourierContract.View> {
        void setData(List<ShippingCourierViewModel> shippingCourierViewModels);

        List<ShippingCourierViewModel> getShippingCourierViewModels();

        CourierItemData getCourierItemData(ShippingCourierViewModel shippingCourierViewModel);

        void updateSelectedCourier(ShippingCourierViewModel shippingCourierViewModel);

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        RecipientAddressModel getRecipientAddressModel();
    }

}
