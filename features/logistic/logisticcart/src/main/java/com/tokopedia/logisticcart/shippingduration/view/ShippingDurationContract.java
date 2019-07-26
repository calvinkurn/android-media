package com.tokopedia.logisticcart.shippingduration.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticcart.domain.ShippingParam;
import com.tokopedia.logisticcart.domain.shipping.CourierItemData;
import com.tokopedia.logisticcart.domain.shipping.LogisticPromoViewModel;
import com.tokopedia.logisticcart.domain.shipping.RecipientAddressModel;
import com.tokopedia.logisticcart.domain.shipping.ShipmentDetailData;
import com.tokopedia.logisticcart.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.logisticcart.domain.shipping.ShippingDurationViewModel;
import com.tokopedia.logisticcart.domain.shipping.ShopShipment;

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
