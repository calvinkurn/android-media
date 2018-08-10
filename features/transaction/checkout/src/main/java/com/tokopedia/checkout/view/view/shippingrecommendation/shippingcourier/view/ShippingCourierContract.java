package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierContract {

    interface View extends CustomerView {
        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<ShippingCourierContract.View> {
        void setData(List<ShippingCourierViewModel> shippingCourierViewModels);

        List<ShippingCourierViewModel> getShippingCourierViewModels();

        CourierItemData getCourierItemData(ShippingCourierViewModel shippingCourierViewModel);

        void updateSelectedCourier(ShippingCourierViewModel shippingCourierViewModel);
    }

}
