package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierPresenter extends BaseDaggerPresenter<ShippingCourierContract.View>
        implements ShippingCourierContract.Presenter {

    private final ShippingCourierConverter shippingCourierConverter;
    private List<ShippingCourierViewModel> shippingCourierViewModels;

    @Inject
    public ShippingCourierPresenter(ShippingCourierConverter shippingCourierConverter) {
        this.shippingCourierConverter = shippingCourierConverter;
    }

    @Override
    public void attachView(ShippingCourierContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void setData(List<ShippingCourierViewModel> shippingCourierViewModels) {
        this.shippingCourierViewModels = shippingCourierViewModels;
    }

    @Override
    public List<ShippingCourierViewModel> getShippingCourierViewModels() {
        if (shippingCourierViewModels == null) {
            shippingCourierViewModels = new ArrayList<>();
        }
        return shippingCourierViewModels;
    }

    @Override
    public CourierItemData getCourierItemData(ShippingCourierViewModel shippingCourierViewModel) {
        return shippingCourierConverter.convertToCourierItemData(shippingCourierViewModel);
    }
}
