package com.tokopedia.transactiondata.repository;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.transactiondata.apiservice.TxActApi;
import com.tokopedia.transactiondata.entity.response.thankstoppaydata.ThanksTopPayData;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;


/**
 * @author anggaprasetiyo on 06/03/18.
 */

public class TopPayRepository implements ITopPayRepository {

    private final TxActApi txActApi;

    @Inject
    public TopPayRepository(TxActApi txActApi) {
        this.txActApi = txActApi;
    }

    @Override
    public Observable<ThanksTopPayData> getThanksTopPay(Map<String, String> param) {
        return txActApi.getThanksDynamicPayment(param).map(
                new Func1<Response<TokopediaWsV4Response>, ThanksTopPayData>() {
                    @Override
                    public ThanksTopPayData call(Response<TokopediaWsV4Response> tkpdResponseResponse) {
                        return tkpdResponseResponse.body().convertDataObj(ThanksTopPayData.class);
                    }
                });
    }
}
