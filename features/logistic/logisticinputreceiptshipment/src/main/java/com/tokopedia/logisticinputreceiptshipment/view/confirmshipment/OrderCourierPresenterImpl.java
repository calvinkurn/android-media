package com.tokopedia.logisticinputreceiptshipment.view.confirmshipment;

import android.content.Context;

import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.common.data.order.OrderDetailShipmentModel;
import com.tokopedia.user.session.UserSession;

import rx.Subscriber;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class OrderCourierPresenterImpl implements OrderCourierPresenter {

    private static final String ORDER_ID = "order_id";
    private static final String ACTION_TYPE = "action_type";
    private static final String SHIPMENT_ID = "shipment_id";
    private static final String AGENCY_ID = "agency_id";
    private static final String SHIPMENT_NAME = "shipment_name";
    private static final String SHIPPING_REF = "shipping_ref";
    private static final String SP_ID = "sp_id";
    private static final String CONFIRM_ACTION_CONSTANT = "confirm";
    private static final String CREATE_BY = "create_by";
    private final UserSession userSession;

    private OrderCourierInteractor interactor;

    private ConfirmShippingView view;

    public OrderCourierPresenterImpl(OrderCourierInteractorImpl interactor, UserSession userSession) {
        this.interactor = interactor;
        this.userSession = userSession;
    }

    @Override
    public void setView(ConfirmShippingView view) {
        this.view = view;
    }

    @Override
    public void onGetCourierList(Context context, OrderDetailData data) {
        view.showLoading();
        interactor.onGetCourierList(
                data.getShipmentId(),
                AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), new TKPDMapParam<>()),
                new Subscriber<ListCourierViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.onShowError(e.getMessage());
                    }

                    @Override
                    public void onNext(ListCourierViewModel courierViewModel) {
                        view.hideLoading();
                        view.receiveShipmentData(courierViewModel);
                    }
                });
    }

    @Override
    public void onProcessCourier(Context context,
                                 OrderDetailShipmentModel editableModel,
                                 boolean isChangeCourier) {
        if (isChangeCourier) {
            //TODO Relese Later when ws ready
            //onChangeCourier(context, editableModel);
            onConfirmShipping(context, editableModel);
        } else onConfirmShipping(context, editableModel);
    }

    @Override
    public void onConfirmShipping(Context context, OrderDetailShipmentModel editableModel) {
        view.showLoading();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(ACTION_TYPE, CONFIRM_ACTION_CONSTANT);
        params.put(ORDER_ID, editableModel.getOrderId());
        params.put(SHIPPING_REF, editableModel.getShippingRef());
        params.put(SHIPMENT_ID, editableModel.getShipmentId());
        params.put(SHIPMENT_NAME, editableModel.getShipmentName());
        params.put(SP_ID, editableModel.getPackageId());
        interactor.confirmShipping(
                AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), params),
                processCourierSubscriber());
    }

    private void onChangeCourier(Context context, OrderDetailShipmentModel editableModel) {
        view.showLoading();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(ACTION_TYPE, CONFIRM_ACTION_CONSTANT);
        params.put(ORDER_ID, editableModel.getOrderId());
        params.put(CREATE_BY, userSession.getUserId());
        params.put(SHIPPING_REF, editableModel.getShippingRef());
        params.put(AGENCY_ID, editableModel.getShipmentId());
        params.put(SHIPMENT_NAME, editableModel.getShipmentName());
        params.put(SP_ID, editableModel.getPackageId());
        interactor.changeCourier(
                AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), params),
                processCourierSubscriber());
    }

    private Subscriber<String> processCourierSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideLoading();
                view.onShowErrorConfirmShipping(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                view.hideLoading();
                view.onSuccessConfirm(s);
            }
        };
    }
}
