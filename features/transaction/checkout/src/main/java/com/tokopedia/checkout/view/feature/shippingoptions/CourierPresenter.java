package com.tokopedia.checkout.view.feature.shippingoptions;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.R;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShopShipment;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentCartData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentOptionData;
import com.tokopedia.checkout.view.feature.shippingoptions.viewmodel.ShipmentTickerInfoData;
import com.tokopedia.checkout.view.feature.shippingoptions.viewmodel.ShipmentTypeData;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.logisticdata.data.constant.CourierConstant;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public class CourierPresenter extends BaseDaggerPresenter<CourierContract.View>
        implements CourierContract.Presenter {

    private final GetRatesUseCase getRatesUseCase;
    private ShipmentDetailData shipmentDetailData;
    private List<ShipmentOptionData> shipmentDataList = new ArrayList<>();
    private boolean hasSelectedCourier;

    @Inject
    public CourierPresenter(GetRatesUseCase getRatesUseCase) {
        this.getRatesUseCase = getRatesUseCase;
    }

    @Override
    public void attachView(CourierContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getRatesUseCase.unsubscribe();
    }

    @Override
    public void loadCourier(ShipmentDetailData shipmentDetailData, List<ShopShipment> shopShipmentList) {
        getView().showLoading();
        this.shipmentDetailData = shipmentDetailData;
        if (shipmentDetailData.getShipmentCartData() == null) {
            shipmentDetailData.setShipmentCartData(new ShipmentCartData());
        }
        getRatesUseCase.setShipmentDetailData(shipmentDetailData);
        getRatesUseCase.setShopShipmentList(shopShipmentList);
        getRatesUseCase.execute(getRatesUseCase.getParams(), new Subscriber<ShipmentDetailData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoading();
                    String message;
                    if (e instanceof UnknownHostException || e instanceof ConnectException ||
                            e instanceof SocketTimeoutException) {
                        message = getView().getContext().getResources().getString(R.string.msg_no_connection);
                    } else if (e instanceof UnProcessableHttpException) {
                        message = TextUtils.isEmpty(e.getMessage()) ?
                                getView().getContext().getResources().getString(R.string.msg_no_connection) :
                                e.getMessage();
                    } else {
                        message = getView().getContext().getResources().getString(R.string.default_request_error_unknown);
                    }
                    getView().showNoConnection(message);
                }
            }

            @Override
            public void onNext(ShipmentDetailData shipmentDetailData) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    List<ShipmentItemData> shipmentItemDataList = shipmentDetailData.getShipmentItemData();
                    boolean hasInstantShippingService = false;
                    for (ShipmentItemData shipmentItemData : shipmentItemDataList) {
                        ShipmentTypeData shipmentTypeData = new ShipmentTypeData();
                        shipmentTypeData.setShipmentType(shipmentItemData.getType());
                        shipmentTypeData.setEtd(shipmentItemData.getDeliveryTimeRange());
                        shipmentDataList.add(shipmentTypeData);
                        if (!hasInstantShippingService &&
                                (shipmentItemData.getServiceId() == CourierConstant.SERVICE_ID_INSTANT ||
                                        shipmentItemData.getServiceId() == CourierConstant.SERVICE_ID_SAME_DAY)) {
                            hasInstantShippingService = true;
                        }
                        List<CourierItemData> courierItemDataList = shipmentItemData.getCourierItemData();
                        if (CourierPresenter.this.shipmentDetailData.getSelectedCourier() != null) {
                            for (CourierItemData courierItemData : courierItemDataList) {
                                int courierShipperProductId = courierItemData.getShipperProductId();
                                int selectedCourierShipperProductId = CourierPresenter.this
                                        .shipmentDetailData.getSelectedCourier().getShipperProductId();
                                if (courierShipperProductId == selectedCourierShipperProductId) {
                                    courierItemData.setSelected(true);
                                    break;
                                }
                            }
                        }
                        shipmentDataList.addAll(courierItemDataList);
                    }
                    if (hasInstantShippingService) {
                        ShipmentTickerInfoData shipmentTickerInfoData = new ShipmentTickerInfoData();
                        shipmentTickerInfoData.setTickerInfo(getView().getContext().getResources().getString(R.string.label_hardcoded_courier_ticker));
                        shipmentDataList.add(0, shipmentTickerInfoData);
                    }
                    CourierPresenter.this.shipmentDetailData = shipmentDetailData;
                    getView().showData();
                }
            }
        });

    }

    @Override
    public String getShipmentTickerInfo() {
        if (shipmentDetailData != null) {
            return shipmentDetailData.getShipmentTickerInfo();
        }
        return null;
    }

    @Override
    public List<ShipmentOptionData> getShipmentDataList() {
        return shipmentDataList;
    }

    @Override
    public void setSelectedCourier(CourierItemData selectedCourier) {
        hasSelectedCourier = true;
        if (shipmentDetailData != null && shipmentDetailData.getShipmentItemData() != null) {
            for (ShipmentItemData shipmentItemData : shipmentDetailData.getShipmentItemData()) {
                if (shipmentItemData.getCourierItemData() != null) {
                    for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                        if (courierItemData.getShipperProductId() == selectedCourier.getShipperProductId() &&
                                courierItemData.getShipperId() == selectedCourier.getShipperId()) {
                            courierItemData.setSelected(true);
                        } else {
                            courierItemData.setSelected(false);
                        }
                    }
                }
            }
        }
    }

    public boolean isHasSelectedCourier() {
        return hasSelectedCourier;
    }
}
