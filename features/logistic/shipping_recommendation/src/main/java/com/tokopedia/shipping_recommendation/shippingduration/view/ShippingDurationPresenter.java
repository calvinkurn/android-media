package com.tokopedia.shipping_recommendation.shippingduration.view;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.shipping_recommendation.R;
import com.tokopedia.shipping_recommendation.domain.ShippingParam;
import com.tokopedia.shipping_recommendation.domain.shipping.LogisticPromoViewModel;
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationPresenter extends BaseDaggerPresenter<ShippingDurationContract.View>
        implements ShippingDurationContract.Presenter {

    private final GetCourierRecommendationUseCase getCourierRecommendationUseCase;
    private final ShippingCourierConverter shippingCourierConverter;
    private RecipientAddressModel recipientAddressModel;

    @Inject
    public ShippingDurationPresenter(GetCourierRecommendationUseCase getCourierRecommendationUseCase,
                                     ShippingCourierConverter shippingCourierConverter) {
        this.getCourierRecommendationUseCase = getCourierRecommendationUseCase;
        this.shippingCourierConverter = shippingCourierConverter;
    }

    @Override
    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
    }

    @Override
    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
    }

    @Override
    public void attachView(ShippingDurationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getCourierRecommendationUseCase.unsubscribe();
    }

    @Override
    public void loadCourierRecommendation(ShippingParam shippingParam, int selectedServiceId, List<ShopShipment> shopShipmentList, int codHistory, boolean isCorner) {
        if (getView() != null) {
            getView().showLoading();
            String query = GraphqlHelper.loadRawString(getView().getActivity().getResources(), R.raw.rates_v3_query);
            loadDuration(0, selectedServiceId, codHistory, isCorner, shopShipmentList, query, shippingParam);
        }
    }

    @Override
    public void loadCourierRecommendation(ShipmentDetailData shipmentDetailData,
                                          int selectedServiceId,
                                          List<ShopShipment> shopShipmentList, int codHistory, boolean isCorner) {
        if (getView() != null) {
            getView().showLoading();
            String query = GraphqlHelper.loadRawString(getView().getActivity().getResources(), R.raw.rates_v3_query);
            ShippingParam shippingParam = getShippingParam(shipmentDetailData);
            int selectedSpId = 0;
            if (shipmentDetailData.getSelectedCourier() != null) {
                selectedSpId = shipmentDetailData.getSelectedCourier().getShipperProductId();
            }
            loadDuration(selectedSpId, selectedServiceId, codHistory, isCorner, shopShipmentList, query, shippingParam);
        }
    }

    private void loadDuration(int selectedSpId, int selectedServiceId, int codHistory, boolean isCorner, List<ShopShipment> shopShipmentList, String query, ShippingParam shippingParam) {
        getCourierRecommendationUseCase.execute(query, codHistory, isCorner, shippingParam, selectedSpId, selectedServiceId, shopShipmentList,
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
                });
    }

    @NonNull
    private ShippingParam getShippingParam(ShipmentDetailData shipmentDetailData) {
        ShippingParam shippingParam = new ShippingParam();
        shippingParam.setOriginDistrictId(shipmentDetailData.getShipmentCartData().getOriginDistrictId());
        shippingParam.setOriginPostalCode(shipmentDetailData.getShipmentCartData().getOriginPostalCode());
        shippingParam.setOriginLatitude(shipmentDetailData.getShipmentCartData().getOriginLatitude());
        shippingParam.setOriginLongitude(shipmentDetailData.getShipmentCartData().getOriginLongitude());
        shippingParam.setDestinationDistrictId(shipmentDetailData.getShipmentCartData().getDestinationDistrictId());
        shippingParam.setDestinationPostalCode(shipmentDetailData.getShipmentCartData().getDestinationPostalCode());
        shippingParam.setDestinationLatitude(shipmentDetailData.getShipmentCartData().getDestinationLatitude());
        shippingParam.setDestinationLongitude(shipmentDetailData.getShipmentCartData().getDestinationLongitude());
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
        return shippingParam;
    }

    @Override
    public CourierItemData convertToCourierModel(LogisticPromoViewModel promoModel) {
        CourierItemData result = new CourierItemData();
        result.setShipperId(promoModel.getShipperId());
        result.setShipperProductId(promoModel.getShipperProductId());
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
