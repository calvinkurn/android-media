package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

public interface ShippingDurationContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showNoConnection(String message);

        void showData(List<ShippingDurationViewModel> serviceDataList);

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadCourierRecommendation(ShipmentDetailData shipmentDetailData);

        List<ShippingDurationViewModel> getShippingDurationViewModels();
    }

}
