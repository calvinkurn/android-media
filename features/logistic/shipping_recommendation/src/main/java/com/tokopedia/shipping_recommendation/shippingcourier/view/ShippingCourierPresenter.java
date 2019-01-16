package com.tokopedia.shipping_recommendation.shippingcourier.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;

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
    private RecipientAddressModel recipientAddressModel;

    @Inject
    public ShippingCourierPresenter(ShippingCourierConverter shippingCourierConverter) {
        this.shippingCourierConverter = shippingCourierConverter;
    }

    @Override
    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
    }

    @Override
    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
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

    @Override
    public void updateSelectedCourier(ShippingCourierViewModel shippingCourierViewModel) {
        if (shippingCourierViewModels != null) {
            for (ShippingCourierViewModel courierViewModel : shippingCourierViewModels) {
                if (courierViewModel.getProductData().getShipperProductId() != shippingCourierViewModel.getProductData().getShipperProductId()) {
                    courierViewModel.setSelected(false);
                } else {
                    courierViewModel.setSelected(true);
                }
            }
        }
    }

    @Override
    public void setSelectedCourier() {
        if (shippingCourierViewModels != null) {
            for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
                if (shippingCourierViewModel.getProductData().isRecommend()) {
                    updateSelectedCourier(shippingCourierViewModel);
                    break;
                }
            }
        }
    }
}
