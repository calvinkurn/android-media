package com.tokopedia.checkout.view.subscriber;

import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel;
import com.tokopedia.logisticcart.shipping.model.PreOrderModel;
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.checkout.view.ShipmentContract;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;

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
    private final boolean isInitialLoad;
    private final boolean isTradeInDropOff;
    private final boolean isForceReloadRates;

    public GetCourierRecommendationSubscriber(ShipmentContract.View view, ShipmentContract.Presenter presenter,
                                              int shipperId, int spId, int itemPosition,
                                              ShippingCourierConverter shippingCourierConverter,
                                              ShipmentCartItemModel shipmentCartItemModel,
                                              boolean isInitialLoad, boolean isTradeInDropOff,
                                              boolean isForceReloadRates) {
        this.view = view;
        this.presenter = presenter;
        this.shipperId = shipperId;
        this.spId = spId;
        this.itemPosition = itemPosition;
        this.shippingCourierConverter = shippingCourierConverter;
        this.shipmentCartItemModel = shipmentCartItemModel;
        this.isInitialLoad = isInitialLoad;
        this.isTradeInDropOff = isTradeInDropOff;
        this.isForceReloadRates = isForceReloadRates;
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
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel);
        }
    }

    @Override
    public void onNext(ShippingRecommendationData shippingRecommendationData) {
        if (isInitialLoad || isForceReloadRates) {
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
                                        !UtilsKt.isNullOrEmpty(shippingCourierUiModel.getProductData().getError().getErrorMessage())) {
                                    view.renderCourierStateFailed(itemPosition, isTradeInDropOff);
                                    return;
                                } else {
                                    shippingCourierUiModel.setSelected(true);
                                    presenter.setShippingCourierViewModelsState(shippingDurationUiModel.getShippingCourierViewModelList(), itemPosition);
                                    CourierItemData courierItemData = shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel);
                                    LogisticPromoUiModel logisticPromo = shippingRecommendationData.getLogisticPromo();
                                    if (logisticPromo != null) {
                                        String disableMsg = logisticPromo.getDisableText();
                                        courierItemData.setLogPromoMsg(disableMsg);

                                        // Auto apply Promo Stacking Logistic
                                        if (logisticPromo.getShipperId() == shipperId
                                                && logisticPromo.getShipperProductId() == spId
                                                && !logisticPromo.getPromoCode().isEmpty()
                                                && !logisticPromo.getDisabled()) {
                                            courierItemData.setLogPromoCode(logisticPromo.getPromoCode());
                                            courierItemData.setDiscountedRate(logisticPromo.getDiscountedRate());
                                            courierItemData.setShippingRate(logisticPromo.getShippingRate());
                                            courierItemData.setBenefitAmount(logisticPromo.getBenefitAmount());
                                            courierItemData.setPromoTitle(logisticPromo.getTitle());
                                            courierItemData.setHideShipperName(logisticPromo.getHideShipperName());
                                            courierItemData.setShipperName(logisticPromo.getShipperName());
                                            courierItemData.setEtaText(logisticPromo.getEtaData().getTextEta());
                                            courierItemData.setEtaErrorCode(logisticPromo.getEtaData().getErrorCode());
                                        }
                                    }
                                    view.renderCourierStateSuccess(courierItemData, itemPosition, isTradeInDropOff, isForceReloadRates);
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
                                    itemPosition, shipmentCartItemModel, shippingRecommendationData.getPreOrderModel()
                            );
                            return;
                        }
                    }
                }
            }
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel);
        }
    }

}
