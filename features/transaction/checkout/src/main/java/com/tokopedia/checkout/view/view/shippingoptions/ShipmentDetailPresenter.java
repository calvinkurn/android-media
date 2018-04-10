package com.tokopedia.checkout.view.view.shippingoptions;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.R;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentCartData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailPresenter extends BaseDaggerPresenter<IShipmentDetailView>
        implements IShipmentDetailPresenter {

    private ShipmentDetailData shipmentDetailData;
    private List<CourierItemData> couriers = new ArrayList<>();
    private GetRatesUseCase getRatesUseCase;

    @Inject
    public ShipmentDetailPresenter(GetRatesUseCase getRatesUseCase) {
        this.getRatesUseCase = getRatesUseCase;
    }

    @Override
    public void attachView(IShipmentDetailView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getRatesUseCase.unsubscribe();
    }

    @Override
    public ShipmentDetailData getShipmentDetailData() {
        return shipmentDetailData;
    }

    @Override
    public CourierItemData getSelectedCourier() {
        return shipmentDetailData.getSelectedCourier();
    }

    @Override
    public ShipmentItemData getSelectedShipment() {
        return shipmentDetailData.getSelectedShipment();
    }

    @Override
    public void setSelectedShipment(ShipmentItemData selectedShipment) {
        selectedShipment.setSelected(true);
        if (selectedShipment.getCourierItemData().size() == 1) {
            CourierItemData courier = selectedShipment.getCourierItemData().get(0);
            courier.setSelected(true);
            shipmentDetailData.setSelectedCourier(courier);
            getView().selectCourier(courier);
        }
        shipmentDetailData.setSelectedShipment(selectedShipment);
        setCourierList(selectedShipment.getCourierItemData());
    }

    @Override
    public void setSelectedCourier(CourierItemData selectedCourier) {
        for (ShipmentItemData shipmentItemData : shipmentDetailData.getShipmentItemData()) {
            for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                courierItemData.setSelected(false);
            }
        }
        shipmentDetailData.setSelectedCourier(selectedCourier);
    }

    @Override
    public void setCourierList(List<CourierItemData> couriers) {
        this.couriers.clear();
        this.couriers.addAll(couriers);
        loadAllCourier();
    }

    @Override
    public void updatePinPoint(LocationPass locationPass) {
        if (shipmentDetailData.getShipmentCartData() != null) {
            shipmentDetailData.getShipmentCartData().setDestinationLatitude(Double.parseDouble(locationPass.getLatitude()));
            shipmentDetailData.getShipmentCartData().setDestinationLongitude(Double.parseDouble(locationPass.getLongitude()));
            shipmentDetailData.getShipmentCartData().setDestinationAddress(locationPass.getGeneratedAddress());
            loadShipmentData(shipmentDetailData);
        }
    }

    @Override
    public void loadShipmentData(ShipmentDetailData shipmentDetailData) {
        getView().showLoading();
        this.shipmentDetailData = shipmentDetailData;
        if (shipmentDetailData.getShipmentCartData() == null) {
            shipmentDetailData.setShipmentCartData(new ShipmentCartData());
        }
        getRatesUseCase.setShipmentDetailData(shipmentDetailData);
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
                        message = getView().getActivity().getResources().getString(R.string.msg_no_connection);
                    } else if (e instanceof UnProcessableHttpException) {
                        message = TextUtils.isEmpty(e.getMessage()) ?
                                getView().getActivity().getResources().getString(R.string.msg_no_connection) :
                                e.getMessage();
                    } else {
                        message = getView().getActivity().getResources().getString(R.string.default_request_error_unknown);
                    }
                    getView().showNoConnection(message);
                }
            }

            @Override
            public void onNext(ShipmentDetailData shipmentDetailData) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    boolean instantCourierAvailable = checkPreviouslySelectedShipmentAndCourier(shipmentDetailData);
                    if (instantCourierAvailable) {
                        getView().renderFirstLoadedRatesData(ShipmentDetailPresenter.this.shipmentDetailData);
                    } else {
                        getView().showErrorSnackbar(getView().getActivity().getResources().getString(R.string.message_pinpoint_too_far));
                    }
                }
            }
        });

    }

    private boolean checkPreviouslySelectedShipmentAndCourier(ShipmentDetailData shipmentDetailData) {
        ShipmentItemData selectedShipmentItemData = null;
        CourierItemData selectedCourierItemData = null;
        if (this.shipmentDetailData.getSelectedShipment() != null) {
            for (ShipmentItemData shipmentItemData : shipmentDetailData.getShipmentItemData()) {
                if (shipmentItemData.getServiceId() ==
                        this.shipmentDetailData.getSelectedShipment().getServiceId()) {
                    selectedShipmentItemData = shipmentItemData;
                    for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                        if (courierItemData.getShipperProductId() ==
                                this.shipmentDetailData.getSelectedCourier().getShipperProductId()) {
                            selectedCourierItemData = courierItemData;
                            break;
                        }
                    }
                    break;
                }
            }
        }

        if (selectedShipmentItemData == null || selectedCourierItemData == null) {
            return false;
        }

        this.shipmentDetailData.setSelectedShipment(selectedShipmentItemData);
        this.shipmentDetailData.setSelectedCourier(selectedCourierItemData);
        this.shipmentDetailData.setShipmentItemData(shipmentDetailData.getShipmentItemData());

        return true;
    }

    @Override
    public void loadAllCourier() {
        chooseSelectedCourier(shipmentDetailData.getSelectedCourier());
        getView().showAllCouriers();
    }

    @Override
    public List<CourierItemData> getCouriers() {
        return couriers;
    }

    private void chooseSelectedCourier(CourierItemData currentCourier) {
        if (currentCourier != null) {
            for (int i = 0; i < couriers.size(); i++) {
                if (couriers.get(i).getShipperProductId() == currentCourier.getShipperProductId()) {
                    couriers.get(i).setSelected(true);
                } else {
                    couriers.get(i).setSelected(false);
                }
            }
        }
    }

    @Override
    public void getPinPointMapData() {
        if (shipmentDetailData != null) {
            getView().showPinPointChooserMap(shipmentDetailData);
        }
    }

}
