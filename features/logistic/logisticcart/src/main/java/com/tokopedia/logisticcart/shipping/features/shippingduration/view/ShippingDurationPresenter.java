package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.RatesParam;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationViewModel;
import com.tokopedia.logisticcart.shipping.model.ShippingParam;
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;

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
    private final ShippingCourierConverter shippingCourierConverter;

    @Inject
    public ShippingDurationPresenter(GetRatesUseCase ratesUseCase,
                                     GetRatesApiUseCase ratesApiUseCase,
                                     ShippingCourierConverter shippingCourierConverter) {
        this.ratesUseCase = ratesUseCase;
        this.ratesApiUseCase = ratesApiUseCase;
        this.shippingCourierConverter = shippingCourierConverter;
    }

    @Override
    public void attachView(ShippingDurationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        ratesUseCase.unsubscribe();
        ratesApiUseCase.unsubscribe();
    }

    @Override
    public void loadCourierRecommendation(ShippingParam shippingParam, int selectedServiceId,
                                          List<ShopShipment> shopShipmentList, int codHistory,
                                          boolean isCorner, boolean isLeasing) {
        if (getView() != null) {
            getView().showLoading();
            loadDuration(0, selectedServiceId, codHistory, isCorner, isLeasing,
                    shopShipmentList, false, shippingParam, "");
        }
    }

    @Override
    public void loadCourierRecommendation(ShipmentDetailData shipmentDetailData,
                                          int selectedServiceId,
                                          List<ShopShipment> shopShipmentList,
                                          int codHistory, boolean isCorner,
                                          boolean isLeasing, String pslCode,
                                          List<Product> products, String cartString,
                                          boolean isTradeInDropOff,
                                          RecipientAddressModel recipientAddressModel) {
        if (getView() != null) {
            getView().showLoading();
            ShippingParam shippingParam = getShippingParam(shipmentDetailData, products, cartString,
                    isTradeInDropOff, recipientAddressModel);
            int selectedSpId = 0;
            if (shipmentDetailData.getSelectedCourier() != null) {
                selectedSpId = shipmentDetailData.getSelectedCourier().getShipperProductId();
            }
            loadDuration(selectedSpId, selectedServiceId, codHistory, isCorner, isLeasing,
                    shopShipmentList, isTradeInDropOff, shippingParam, pslCode);
        }
    }

    private void loadDuration(int selectedSpId, int selectedServiceId, int codHistory,
                              boolean isCorner, boolean isLeasing,
                              List<ShopShipment> shopShipmentList, boolean isRatesTradeInApi,
                              ShippingParam shippingParam, String pslCode) {
        RatesParam param = new RatesParam.Builder(shopShipmentList, shippingParam)
                .isCorner(isCorner)
                .codHistory(codHistory)
                .isLeasing(isLeasing)
                .promoCode(pslCode)
                .build();

        Observable<ShippingRecommendationData> observable;
        if (isRatesTradeInApi) {
            observable = ratesApiUseCase.execute(param);
        } else {
            observable = ratesUseCase.execute(param);
        }

        observable
                .map(new RatesResponseStateTransformer(shopShipmentList, selectedSpId, selectedServiceId))
                .subscribe(
                        new Subscriber<ShippingRecommendationData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                if (getView() != null) {
                                    getView().showErrorPage(ErrorHandler.getErrorMessage(getView().getActivity(), e));
                                    getView().stopTrace();
                                }
                            }

                            @Override
                            public void onNext(ShippingRecommendationData shippingRecommendationData) {
                                if (getView() != null) {
                                    getView().hideLoading();
                                    if (shippingRecommendationData.getErrorId() != null &&
                                            shippingRecommendationData.getErrorId().equals(ErrorProductData.ERROR_RATES_NOT_AVAILABLE)) {
                                        getView().showNoCourierAvailable(shippingRecommendationData.getErrorMessage());
                                        getView().stopTrace();
                                    } else if (shippingRecommendationData.getShippingDurationViewModels() != null &&
                                            shippingRecommendationData.getShippingDurationViewModels().size() > 0) {
                                        if (getView().isDisableCourierPromo()) {
                                            for (ShippingDurationViewModel shippingDurationViewModel : shippingRecommendationData.getShippingDurationViewModels()) {
                                                shippingDurationViewModel.getServiceData().setIsPromo(0);
                                                for (ProductData productData : shippingDurationViewModel.getServiceData().getProducts()) {
                                                    productData.setPromoCode("");
                                                }
                                            }
                                        }
                                        getView().showData(shippingRecommendationData.getShippingDurationViewModels(), shippingRecommendationData.getLogisticPromo());
                                        getView().stopTrace();
                                    } else {
                                        getView().showNoCourierAvailable(getView().getActivity().getString(R.string.label_no_courier_bottomsheet_message));
                                        getView().stopTrace();
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
        shippingParam.setShopId(shipmentDetailData.getShopId());
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
    public CourierItemData convertToCourierModel(LogisticPromoViewModel promoModel) {
        CourierItemData result = new CourierItemData();
        result.setShipperId(promoModel.getShipperId());
        result.setShipperProductId(promoModel.getShipperProductId());
        result.setServiceId(promoModel.getServiceId());
        result.setServiceName(promoModel.getShipperDesc());
        result.setName(promoModel.getShipperName());
        result.setLogPromoCode(promoModel.getPromoCode());
        return result;
    }

    @Override
    public CourierItemData getCourierItemData(List<ShippingCourierViewModel> shippingCourierViewModels) {
        for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
            if (shippingCourierViewModel.getProductData().isRecommend()) {
                return shippingCourierConverter.convertToCourierItemData(shippingCourierViewModel);
            }
        }
        return null;
    }

    @Override
    public CourierItemData getCourierItemDataById(int spId, List<ShippingCourierViewModel> shippingCourierViewModels) {
        for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
            if (shippingCourierViewModel.getProductData().getShipperProductId() == spId) {
                return shippingCourierConverter.convertToCourierItemData(shippingCourierViewModel);
            }
        }
        return null;
    }
}
