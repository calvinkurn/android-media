package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.QrPaymentEntity;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class QrPaymentCloudDataSource implements QrPaymentDataSource {

    private static final String IDENTIFIER = "identifier";
    private WalletApi walletApi;

    public QrPaymentCloudDataSource(WalletApi walletApi) {
        this.walletApi = walletApi;
    }

    @Override
    public Observable<InfoQrEntity> getInfoQrTokoCash(HashMap<String, Object> mapParams) {
        return walletApi.getInfoQrTokoCash(mapParams.get(IDENTIFIER).toString())
                .flatMap(new Func1<Response<DataResponse<InfoQrEntity>>, Observable<InfoQrEntity>>() {
                    @Override
                    public Observable<InfoQrEntity> call(Response<DataResponse<InfoQrEntity>> dataResponse) {
                        return Observable.just(dataResponse.body().getData());
                    }
                });
    }

    @Override
    public Observable<QrPaymentEntity> postQrPaymentTokoCash(HashMap<String, Object> mapParams) {
        return walletApi.postQrPaymentTokoCash(mapParams)
                .flatMap(new Func1<Response<DataResponse<QrPaymentEntity>>, Observable<QrPaymentEntity>>() {
                    @Override
                    public Observable<QrPaymentEntity> call(Response<DataResponse<QrPaymentEntity>> response) {
                        return Observable.just(response.body().getData());
                    }
                });
    }
}
