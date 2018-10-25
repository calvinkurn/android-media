package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

public interface ShippingDurationContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showErrorPage(String message);

        void showData(List<ShippingDurationViewModel> serviceDataList);

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadCourierRecommendation(ShipmentDetailData shipmentDetailData, int selectedServiceId, List<ShopShipment> shopShipmentList);

        List<ShippingDurationViewModel> getShippingDurationViewModels();

        CourierItemData getCourierItemData(List<ShippingCourierViewModel> shippingCourierViewModels);

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        RecipientAddressModel getRecipientAddressModel();
    }

}
