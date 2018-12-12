package com.tokopedia.events.view.presenter;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.events.domain.model.scanticket.ScanTicketResponse;
import com.tokopedia.events.domain.scanTicketUsecase.RedeemTicketUseCase;
import com.tokopedia.events.domain.scanTicketUsecase.ScanBarCodeUseCase;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.ScanCodeContract;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Subscriber;

public class ScanCodeDataPresenter extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements ScanCodeContract.ScanPresenter {

    private ScanBarCodeUseCase scanBarCodeUseCase;
    private RedeemTicketUseCase redeemTicketUseCase;
    private ScanCodeContract.ScanCodeDataView mView;

    public ScanCodeDataPresenter(ScanBarCodeUseCase scanBarCodeUseCase, RedeemTicketUseCase redeemTicketUseCase) {
        this.scanBarCodeUseCase = scanBarCodeUseCase;
        this.redeemTicketUseCase = redeemTicketUseCase;
    }

    @Override
    public void getScanData(String url) {
        if(mView == null){
            return;
        }
        mView.showProgressBar();
        scanBarCodeUseCase.setRequestUrl(url);
        scanBarCodeUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<ScanTicketResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);

                DataResponse data = restResponse.getData();

                ScanTicketResponse scanTicketResponse = (ScanTicketResponse) data.getData();

                mView.renderScannedData(scanTicketResponse);
                mView.hideProgressBar();
            }
        });

    }


    @Override
    public void redeemTicket(String url) {
        if(mView == null){
            return;
        }
        mView.showProgressBar();
        redeemTicketUseCase.setRequestUrl(url);
        redeemTicketUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<JsonObject>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);

                DataResponse data = restResponse.getData();

                if(data != null) {
                    mView.ticketRedeemed();
                    mView.hideProgressBar();
                }

            }
        });

    }

    @Override
    public boolean onClickOptionMenu(int id) {
        mView.getActivity().onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {

    }

    @Override
    public void onDestroy() {
        scanBarCodeUseCase.unsubscribe();
        redeemTicketUseCase.unsubscribe();
    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (ScanCodeContract.ScanCodeDataView) view;
    }
}
