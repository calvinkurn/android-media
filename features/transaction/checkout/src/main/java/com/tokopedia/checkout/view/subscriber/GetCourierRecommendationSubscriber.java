package com.tokopedia.checkout.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.checkout.view.ShipmentContract;
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
    private final boolean isTradeInDropOff;

    public GetCourierRecommendationSubscriber(ShipmentContract.View view, ShipmentContract.Presenter presenter,
                                              int shipperId, int spId, int itemPosition,
                                              ShippingCourierConverter shippingCourierConverter,
                                              ShipmentCartItemModel shipmentCartItemModel,
                                              List<ShopShipment> shopShipmentList,
                                              boolean isInitialLoad, boolean isTradeInDropOff) {
        this.view = view;
        this.presenter = presenter;
        this.shipperId = shipperId;
        this.spId = spId;
        this.itemPosition = itemPosition;
        this.shippingCourierConverter = shippingCourierConverter;
        this.shipmentCartItemModel = shipmentCartItemModel;
        this.shopShipmentList = shopShipmentList;
        this.isInitialLoad = isInitialLoad;
        this.isTradeInDropOff = isTradeInDropOff;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isInitialLoad) {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff);
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
                for (ShippingDurationUiModel shippingDurationUiModel : shippingRecommendationData.getShippingDurationViewModels()) {
                    if (shippingDurationUiModel.getShippingCourierViewModelList() != null &&
                            shippingDurationUiModel.getShippingCourierViewModelList().size() > 0) {
                        for (ShippingCourierUiModel shippingCourierUiModel : shippingDurationUiModel.getShippingCourierViewModelList()) {
                            shippingCourierUiModel.setSelected(false);
                        }
                        for (ShippingCourierUiModel shippingCourierUiModel : shippingDurationUiModel.getShippingCourierViewModelList()) {
                            if (isTradeInDropOff || (shippingCourierUiModel.getProductData().getShipperProductId() == spId &&
                                    shippingCourierUiModel.getProductData().getShipperId() == shipperId)) {
                                if (shippingCourierUiModel.getProductData().getError() != null &&
                                        !TextUtils.isEmpty(shippingCourierUiModel.getProductData().getError().getErrorMessage())) {
                                    view.renderCourierStateFailed(itemPosition, isTradeInDropOff);
                                    return;
                                } else {
                                    shippingCourierUiModel.setSelected(true);
                                    presenter.setShippingCourierViewModelsState(shippingDurationUiModel.getShippingCourierViewModelList(), itemPosition);
                                    CourierItemData courierItemData = shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel);
                                    if (shippingRecommendationData.getLogisticPromo() != null) {
                                        String disableMsg = shippingRecommendationData.getLogisticPromo().getDisableText();
                                        courierItemData.setLogPromoMsg(disableMsg);

                                        // Auto apply Promo Stacking Logistic
                                        if (shippingRecommendationData.getLogisticPromo().getShipperId() == shipperId
                                                && shippingRecommendationData.getLogisticPromo().getShipperProductId() == spId
                                                && !shippingRecommendationData.getLogisticPromo().getPromoCode().isEmpty()
                                                && !shippingRecommendationData.getLogisticPromo().getDisabled()) {
                                            courierItemData.setLogPromoCode(shippingRecommendationData.getLogisticPromo().getPromoCode());
                                            courierItemData.setDiscountedRate(shippingRecommendationData.getLogisticPromo().getDiscountedRate());
                                            courierItemData.setShippingRate(shippingRecommendationData.getLogisticPromo().getShippingRate());
                                            courierItemData.setBenefitAmount(shippingRecommendationData.getLogisticPromo().getBenefitAmount());
                                            courierItemData.setPromoTitle(shippingRecommendationData.getLogisticPromo().getTitle());
                                            courierItemData.setHideShipperName(shippingRecommendationData.getLogisticPromo().getHideShipperName());
                                        }
                                    }
                                    view.renderCourierStateSuccess(courierItemData, itemPosition, isTradeInDropOff);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff);
        } else {
            if (shippingRecommendationData != null &&
                    shippingRecommendationData.getShippingDurationViewModels() != null &&
                    shippingRecommendationData.getShippingDurationViewModels().size() > 0) {
                for (ShippingDurationUiModel shippingDurationUiModel : shippingRecommendationData.getShippingDurationViewModels()) {
                    for (ProductData productData : shippingDurationUiModel.getServiceData().getProducts()) {
                        if (productData.getShipperId() == shipperId && productData.getShipperProductId() == spId) {
                            view.updateCourierBottomssheetHasData(
                                    shippingDurationUiModel.getShippingCourierViewModelList(),
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
