package com.tokopedia.events.view.presenter;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.domain.model.scanticket.CheckScanOption;
import com.tokopedia.events.domain.model.scanticket.ScanTicketResponse;
import com.tokopedia.events.domain.scanTicketUsecase.RedeemTicketUseCase;
import com.tokopedia.events.domain.scanTicketUsecase.ScanBarCodeUseCase;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.contractor.ScanCodeContract;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ScanCodeDataPresenter extends BaseDaggerPresenter<ScanCodeContract.ScanCodeDataView>
        implements ScanCodeContract.Presenter {

    private ScanBarCodeUseCase scanBarCodeUseCase;
    private RedeemTicketUseCase redeemTicketUseCase;

    @Inject
    public ScanCodeDataPresenter(ScanBarCodeUseCase scanBarCodeUseCase, RedeemTicketUseCase redeemTicketUseCase) {
        this.scanBarCodeUseCase = scanBarCodeUseCase;
        this.redeemTicketUseCase = redeemTicketUseCase;
    }

    @Override
    public void attachView(ScanCodeContract.ScanCodeDataView view) {
        super.attachView(view);
    }

    @Override
    public void getScanData(String url) {
        if(getView() == null){
            return;
        }
        getView().showProgressBar();
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

                getView().renderScannedData(scanTicketResponse);
                getView().hideProgressBar();
            }
        });

    }


    @Override
    public void redeemTicket(String url) {
        if(getView() == null){
            return;
        }
        getView().showProgressBar();
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
                    getView().ticketRedeemed();
                    getView().hideProgressBar();
                }

            }
        });

    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        scanBarCodeUseCase.unsubscribe();
        redeemTicketUseCase.unsubscribe();
    }
}
