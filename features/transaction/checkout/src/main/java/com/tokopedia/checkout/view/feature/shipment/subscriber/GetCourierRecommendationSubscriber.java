package com.tokopedia.checkout.view.feature.shipment.subscriber;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.ShippingRecommendationData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view.ShippingDurationViewModel;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 02/10/18.
 */

public class GetCourierRecommendationSubscriber extends Subscriber<ShippingRecommendationData> {

    private final ShipmentContract.View view;
    private final ShipmentContract.Presenter presenter;
    private final int shipperId;
    private final int spId;
    private final int itemPosition;
    private final ShippingCourierConverter shippingCourierConverter;

    public GetCourierRecommendationSubscriber(ShipmentContract.View view, ShipmentContract.Presenter presenter,
                                              int shipperId, int spId, int itemPosition,
                                              ShippingCourierConverter shippingCourierConverter) {
        this.view = view;
        this.presenter = presenter;
        this.shipperId = shipperId;
        this.spId = spId;
        this.itemPosition = itemPosition;
        this.shippingCourierConverter = shippingCourierConverter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.renderCourierStateFailed(itemPosition);
    }

    @Override
    public void onNext(ShippingRecommendationData shippingRecommendationData) {
        if (shippingRecommendationData != null &&
                shippingRecommendationData.getShippingDurationViewModels() != null &&
                shippingRecommendationData.getShippingDurationViewModels().size() > 0) {
            for (ShippingDurationViewModel shippingDurationViewModel : shippingRecommendationData.getShippingDurationViewModels()) {
                if (shippingDurationViewModel.getShippingCourierViewModelList() != null &&
                        shippingDurationViewModel.getShippingCourierViewModelList().size() > 0) {
                    for (ShippingCourierViewModel shippingCourierViewModel : shippingDurationViewModel.getShippingCourierViewModelList()) {
                        if (shippingCourierViewModel.getProductData().getShipperProductId() == spId &&
                                shippingCourierViewModel.getProductData().getShipperId() == shipperId) {
                            if (shippingCourierViewModel.getProductData().getError() != null &&
                                    !TextUtils.isEmpty(shippingCourierViewModel.getProductData().getError().getErrorMessage())) {
                                view.renderCourierStateFailed(itemPosition);
                                return;
                            } else {
                                presenter.setShippingCourierViewModelsState(shippingDurationViewModel.getShippingCourierViewModelList(), itemPosition);
                                CourierItemData courierItemData = shippingCourierConverter.convertToCourierItemData(shippingCourierViewModel);
                                view.renderCourierStateSuccess(courierItemData, itemPosition);
                                return;
                            }
                        }
                    }
                }
            }
        }
        view.renderCourierStateFailed(itemPosition);
    }

}
