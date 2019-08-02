package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticcart.shipping.model.ShippingParam;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationViewModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;

import java.util.List;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

public interface ShippingDurationContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showErrorPage(String message);

        void showData(List<ShippingDurationViewModel> serviceDataList, LogisticPromoViewModel promoViewModel);

        void showNoCourierAvailable(String message);

        void stopTrace();

        boolean isDisableCourierPromo();

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadCourierRecommendation(ShipmentDetailData shipmentDetailData, int selectedServiceId,
                                       List<ShopShipment> shopShipmentList, int codHistory, boolean isCorner);

        void loadCourierRecommendation(ShippingParam shippingParam, int selectedServiceId, List<ShopShipment> shopShipmentList, int codHistory, boolean isCorner);

        CourierItemData getCourierItemData(List<ShippingCourierViewModel> shippingCourierViewModels);

        CourierItemData getCourierItemDataById(int spId, List<ShippingCourierViewModel> shippingCourierViewModels);

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        RecipientAddressModel getRecipientAddressModel();

        CourierItemData convertToCourierModel(LogisticPromoViewModel promoModel);

    }

}
