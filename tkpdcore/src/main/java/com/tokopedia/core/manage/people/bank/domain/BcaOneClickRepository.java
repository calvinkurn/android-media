package com.tokopedia.core.manage.people.bank.domain;

import com.google.gson.Gson;
import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public class BcaOneClickRepository implements IBcaOneClickRepository {

    private BcaOneClickService bcaOneClickService;

    public BcaOneClickRepository(BcaOneClickService bcaOneClickService) {
        this.bcaOneClickService = bcaOneClickService;
    }

    @Override
    public Observable<BcaOneClickData> getBcaOneClickAccessToken() {
        return bcaOneClickService.getApi().getBcaOneClickAccessToken().map(new Func1<Response<String>, BcaOneClickData>() {
            @Override
            public BcaOneClickData call(Response<String> stringResponse) {
                return new Gson().fromJson(stringResponse.body(), BcaOneClickData.class);
            }
        });
    }
}
