package com.tokopedia.checkout.view.feature.shipment.subscriber;

import android.text.TextUtils;

import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;

import java.util.List;

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
    private final ShipmentCartItemModel shipmentCartItemModel;
    private final List<ShopShipment> shopShipmentList;
    private final boolean isInitialLoad;

    public GetCourierRecommendationSubscriber(ShipmentContract.View view, ShipmentContract.Presenter presenter,
                                              int shipperId, int spId, int itemPosition,
                                              ShippingCourierConverter shippingCourierConverter,
                                              ShipmentCartItemModel shipmentCartItemModel,
                                              List<ShopShipment> shopShipmentList,
                                              boolean isInitialLoad) {
        this.view = view;
        this.presenter = presenter;
        this.shipperId = shipperId;
        this.spId = spId;
        this.itemPosition = itemPosition;
        this.shippingCourierConverter = shippingCourierConverter;
        this.shipmentCartItemModel = shipmentCartItemModel;
        this.shopShipmentList = shopShipmentList;
        this.isInitialLoad = isInitialLoad;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isInitialLoad) {
            view.renderCourierStateFailed(itemPosition);
        } else {
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel, shopShipmentList);
        }
    }

    @Override
    public void onNext(ShippingRecommendationData shippingRecommendationData) {
        if (isInitialLoad) {
            if (shippingRecommendationData != null &&
                    shippingRecommendationData.getShippingDurationViewModels() != null &&
                    shippingRecommendationData.getShippingDurationViewModels().size() > 0) {
                for (ShippingDurationViewModel shippingDurationViewModel : shippingRecommendationData.getShippingDurationViewModels()) {
                    if (shippingDurationViewModel.getShippingCourierViewModelList() != null &&
                            shippingDurationViewModel.getShippingCourierViewModelList().size() > 0) {
                        for (ShippingCourierViewModel shippingCourierViewModel : shippingDurationViewModel.getShippingCourierViewModelList()) {
                            shippingCourierViewModel.setSelected(false);
                        }
                        for (ShippingCourierViewModel shippingCourierViewModel : shippingDurationViewModel.getShippingCourierViewModelList()) {
                            if (shippingCourierViewModel.getProductData().getShipperProductId() == spId &&
                                    shippingCourierViewModel.getProductData().getShipperId() == shipperId) {
                                if (shippingCourierViewModel.getProductData().getError() != null &&
                                        !TextUtils.isEmpty(shippingCourierViewModel.getProductData().getError().getErrorMessage())) {
                                    view.renderCourierStateFailed(itemPosition);
                                    return;
                                } else {
                                    shippingCourierViewModel.setSelected(true);
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
        } else {
            if (shippingRecommendationData != null &&
                    shippingRecommendationData.getShippingDurationViewModels() != null &&
                    shippingRecommendationData.getShippingDurationViewModels().size() > 0) {
                for (ShippingDurationViewModel shippingDurationViewModel : shippingRecommendationData.getShippingDurationViewModels()) {
                    for (ProductData productData : shippingDurationViewModel.getServiceData().getProducts()) {
                        if (productData.getShipperId() == shipperId && productData.getShipperProductId() == spId) {
                            view.updateCourierBottomssheetHasData(
                                    shippingDurationViewModel.getShippingCourierViewModelList(),
                                    itemPosition, shipmentCartItemModel, shopShipmentList
                            );
                            return;
                        }
                    }
                }
            }
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel, shopShipmentList);
        }
    }

}
