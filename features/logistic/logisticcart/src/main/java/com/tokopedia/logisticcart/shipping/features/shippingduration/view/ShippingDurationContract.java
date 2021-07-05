package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PreOrder;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel;
import com.tokopedia.logisticcart.shipping.model.PreOrderModel;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

public interface ShippingDurationContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showErrorPage(String message);

        void showData(List<ShippingDurationUiModel> serviceDataList, LogisticPromoUiModel promoViewModel, PreOrderModel preOrderModel);

        void showNoCourierAvailable(String message);

        void stopTrace();

        boolean isDisableCourierPromo();

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadCourierRecommendation(ShipmentDetailData shipmentDetailData, int selectedServiceId,
                                       List<ShopShipment> shopShipmentList, int codHistory,
                                       boolean isCorner, boolean isLeasing, String pslCode,
                                       List<Product> products, String cartString, boolean isTradeInDropOff,
                                       RecipientAddressModel recipientAddressModel, boolean isFulfillment, int preOrderTime, String mvc);

        CourierItemData getCourierItemData(List<ShippingCourierUiModel> shippingCourierUiModels);

        CourierItemData getCourierItemDataById(int spId, List<ShippingCourierUiModel> shippingCourierUiModels);

    }

}
