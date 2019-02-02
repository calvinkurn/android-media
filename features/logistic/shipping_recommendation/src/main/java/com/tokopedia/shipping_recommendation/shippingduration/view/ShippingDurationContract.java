package com.tokopedia.shipping_recommendation.shippingduration.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.shipping_recommendation.domain.ShippingParam;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;

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

        void showNoCourierAvailable(String message);

        void stopTrace();

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadCourierRecommendation(ShipmentDetailData shipmentDetailData, int selectedServiceId, List<ShopShipment> shopShipmentList, int codHistory);

        void loadCourierRecommendation(ShippingParam shippingParam, int selectedServiceId, List<ShopShipment> shopShipmentList, int codHistory);

        List<ShippingDurationViewModel> getShippingDurationViewModels();

        CourierItemData getCourierItemData(List<ShippingCourierViewModel> shippingCourierViewModels);

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        RecipientAddressModel getRecipientAddressModel();
    }

}
