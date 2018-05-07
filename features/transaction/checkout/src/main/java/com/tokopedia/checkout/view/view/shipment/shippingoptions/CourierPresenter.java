package com.tokopedia.checkout.view.view.shipment.shippingoptions;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentCartData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.checkout.view.view.shipment.shippingoptions.viewmodel.ShipmentData;
import com.tokopedia.checkout.view.view.shipment.shippingoptions.viewmodel.ShipmentTypeData;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;

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

    private ShipmentDetailData shipmentDetailData;
    private List<ShipmentData> shipmentDataList = new ArrayList<>();
    private final GetRatesUseCase getRatesUseCase;

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
    public void loadCourier(ShipmentDetailData shipmentDetailData) {
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
                    CourierPresenter.this.shipmentDetailData = shipmentDetailData;
                    List<ShipmentItemData> shipmentItemDataList = shipmentDetailData.getShipmentItemData();
                    for (ShipmentItemData shipmentItemData : shipmentItemDataList) {
                        ShipmentTypeData shipmentTypeData = new ShipmentTypeData();
                        shipmentTypeData.setShipmentType(shipmentItemData.getType());
                        shipmentDataList.add(shipmentTypeData);
                        List<CourierItemData> courierItemData = shipmentItemData.getCourierItemData();
                        shipmentDataList.addAll(courierItemData);
                    }
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
    public List<ShipmentData> getShipmentDataList() {
        return shipmentDataList;
    }
}
