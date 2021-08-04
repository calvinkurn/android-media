package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.RatesParam;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingParam;
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.network.utils.ErrorHandler;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationPresenter extends BaseDaggerPresenter<ShippingDurationContract.View>
        implements ShippingDurationContract.Presenter {

    private final GetRatesUseCase ratesUseCase;
    private final GetRatesApiUseCase ratesApiUseCase;
    private final RatesResponseStateConverter stateConverter;
    private final ShippingCourierConverter shippingCourierConverter;
    private ShippingDurationContract.View view;

    @Inject
    public ShippingDurationPresenter(GetRatesUseCase ratesUseCase,
                                     GetRatesApiUseCase ratesApiUseCase,
                                     RatesResponseStateConverter stateTransformer,
                                     ShippingCourierConverter shippingCourierConverter) {
        this.ratesUseCase = ratesUseCase;
        this.ratesApiUseCase = ratesApiUseCase;
        this.stateConverter = stateTransformer;
        this.shippingCourierConverter = shippingCourierConverter;
    }

    @Override
    public void attachView(ShippingDurationContract.View view) {
        super.attachView(view);
        this.view = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        ratesUseCase.unsubscribe();
        ratesApiUseCase.unsubscribe();
    }

    /**
     * Calls rates
     */
    @Override
    public void loadCourierRecommendation(ShipmentDetailData shipmentDetailData,
                                          int selectedServiceId,
                                          List<ShopShipment> shopShipmentList,
                                          int codHistory, boolean isCorner,
                                          boolean isLeasing, String pslCode,
                                          List<Product> products, String cartString,
                                          boolean isTradeInDropOff,
                                          RecipientAddressModel recipientAddressModel,
                                          boolean isFulfillment, int preOrderTime,
                                          String mvc) {
        if (view != null) {
            view.showLoading();
            ShippingParam shippingParam = getShippingParam(shipmentDetailData, products, cartString,
                    isTradeInDropOff, recipientAddressModel);
            int selectedSpId = 0;
            if (shipmentDetailData.getSelectedCourier() != null) {
                selectedSpId = shipmentDetailData.getSelectedCourier().getShipperProductId();
            }
            loadDuration(selectedSpId, selectedServiceId, codHistory, isCorner, isLeasing,
                    shopShipmentList, isTradeInDropOff, shippingParam, pslCode, isFulfillment, preOrderTime, mvc);
        }
    }

    private void loadDuration(int selectedSpId, int selectedServiceId, int codHistory,
                              boolean isCorner, boolean isLeasing,
                              List<ShopShipment> shopShipmentList, boolean isRatesTradeInApi,
                              ShippingParam shippingParam, String pslCode,
                              boolean isFulfillment, int preOrderTime, String mvc) {
        RatesParam param = new RatesParam.Builder(shopShipmentList, shippingParam)
                .isCorner(isCorner)
                .codHistory(codHistory)
                .isLeasing(isLeasing)
                .promoCode(pslCode)
                .mvc(mvc)
                .build();

        Observable<ShippingRecommendationData> observable;
        if (isRatesTradeInApi) {
            observable = ratesApiUseCase.execute(param);
        } else {
            observable = ratesUseCase.execute(param);
        }

        observable
                .map(shippingRecommendationData ->
                        stateConverter.fillState(shippingRecommendationData, shopShipmentList,
                                selectedSpId, selectedServiceId))
                .subscribe(
                        new Subscriber<ShippingRecommendationData>() {
                            @Override
                            public void onCompleted() {
                                //no-op
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null) {
                                    view.showErrorPage(ErrorHandler.getErrorMessage(view.getActivity(), e));
                                    view.stopTrace();
                                }
                            }

                            @Override
                            public void onNext(ShippingRecommendationData shippingRecommendationData) {
                                if (view != null) {
                                    view.hideLoading();
                                    if (shippingRecommendationData.getErrorId() != null &&
                                            shippingRecommendationData.getErrorId().equals(ErrorProductData.ERROR_RATES_NOT_AVAILABLE)) {
                                        view.showNoCourierAvailable(shippingRecommendationData.getErrorMessage());
                                        view.stopTrace();
                                    } else if (shippingRecommendationData.getShippingDurationViewModels() != null &&
                                            !shippingRecommendationData.getShippingDurationViewModels().isEmpty()) {
                                        if (view.isDisableCourierPromo()) {
                                            for (ShippingDurationUiModel shippingDurationUiModel : shippingRecommendationData.getShippingDurationViewModels()) {
                                                shippingDurationUiModel.getServiceData().setIsPromo(0);
                                                for (ProductData productData : shippingDurationUiModel.getServiceData().getProducts()) {
                                                    productData.setPromoCode("");
                                                }
                                            }
                                        }
                                        view.showData(shippingRecommendationData.getShippingDurationViewModels(), shippingRecommendationData.getLogisticPromo(), shippingRecommendationData.getPreOrderModel());
                                        view.stopTrace();
                                    } else {
                                        view.showNoCourierAvailable(view.getActivity().getString(R.string.label_no_courier_bottomsheet_message));
                                        view.stopTrace();
                                    }
                                }
                            }
                        }
                );
    }

    @NonNull
    private ShippingParam getShippingParam(ShipmentDetailData shipmentDetailData,
                                           List<Product> products,
                                           String cartString,
                                           boolean isTradeInDropOff,
                                           RecipientAddressModel recipientAddressModel) {
        ShippingParam shippingParam = new ShippingParam();
        shippingParam.setOriginDistrictId(shipmentDetailData.getShipmentCartData().getOriginDistrictId());
        shippingParam.setOriginPostalCode(shipmentDetailData.getShipmentCartData().getOriginPostalCode());
        shippingParam.setOriginLatitude(shipmentDetailData.getShipmentCartData().getOriginLatitude());
        shippingParam.setOriginLongitude(shipmentDetailData.getShipmentCartData().getOriginLongitude());
        shippingParam.setWeightInKilograms(shipmentDetailData.getShipmentCartData().getWeight() / 1000);
        shippingParam.setWeightActualInKilograms(shipmentDetailData.getShipmentCartData().getWeightActual() / 1000);
        shippingParam.setShopId(shipmentDetailData.getShopId());
        shippingParam.setShopTier(shipmentDetailData.getShipmentCartData().getShopTier());
        shippingParam.setToken(shipmentDetailData.getShipmentCartData().getToken());
        shippingParam.setUt(shipmentDetailData.getShipmentCartData().getUt());
        shippingParam.setInsurance(shipmentDetailData.getShipmentCartData().getInsurance());
        shippingParam.setProductInsurance(shipmentDetailData.getShipmentCartData().getProductInsurance());
        shippingParam.setOrderValue(shipmentDetailData.getShipmentCartData().getOrderValue());
        shippingParam.setCategoryIds(shipmentDetailData.getShipmentCartData().getCategoryIds());
        shippingParam.setIsBlackbox(shipmentDetailData.getIsBlackbox());
        shippingParam.setIsPreorder(shipmentDetailData.getPreorder());
        shippingParam.setAddressId(shipmentDetailData.getAddressId());
        shippingParam.setTradein(shipmentDetailData.isTradein());
        shippingParam.setProducts(products);
        shippingParam.setUniqueId(cartString);
        shippingParam.setTradeInDropOff(isTradeInDropOff);
        shippingParam.setPreOrderDuration(shipmentDetailData.getShipmentCartData().getPreOrderDuration());
        shippingParam.setFulfillment(shipmentDetailData.getShipmentCartData().isFulfillment());
        shippingParam.setBoMetadata(shipmentDetailData.getShipmentCartData().getBoMetadata());

        if (isTradeInDropOff && recipientAddressModel.getLocationDataModel() != null) {
            shippingParam.setDestinationDistrictId(String.valueOf(recipientAddressModel.getLocationDataModel().getDistrict()));
            shippingParam.setDestinationPostalCode(recipientAddressModel.getLocationDataModel().getPostalCode());
            shippingParam.setDestinationLatitude(recipientAddressModel.getLocationDataModel().getLatitude());
            shippingParam.setDestinationLongitude(recipientAddressModel.getLocationDataModel().getLongitude());
        } else {
            shippingParam.setDestinationDistrictId(shipmentDetailData.getShipmentCartData().getDestinationDistrictId());
            shippingParam.setDestinationPostalCode(shipmentDetailData.getShipmentCartData().getDestinationPostalCode());
            shippingParam.setDestinationLatitude(shipmentDetailData.getShipmentCartData().getDestinationLatitude());
            shippingParam.setDestinationLongitude(shipmentDetailData.getShipmentCartData().getDestinationLongitude());
        }
        return shippingParam;
    }

    @Override
    public CourierItemData getCourierItemData(List<ShippingCourierUiModel> shippingCourierUiModels) {
        for (ShippingCourierUiModel shippingCourierUiModel : shippingCourierUiModels) {
            if (shippingCourierUiModel.getProductData().isRecommend()) {
                return shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel);
            }
        }
        return null;
    }

    @Override
    public CourierItemData getCourierItemDataById(int spId, List<ShippingCourierUiModel> shippingCourierUiModels) {
        for (ShippingCourierUiModel shippingCourierUiModel : shippingCourierUiModels) {
            if (shippingCourierUiModel.getProductData().getShipperProductId() == spId) {
                return shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel);
            }
        }
        return null;
    }
}
