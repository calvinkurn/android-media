package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
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
        void setData(ServiceData serviceData);

        List<ShippingCourierViewModel> getShippingCourierViewModels();
    }

}
