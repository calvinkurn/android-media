package com.tokopedia.instantloan.data.soruce.cloud;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.instantloan.data.mapper.PhoneDataMapper;
import com.tokopedia.instantloan.data.model.response.ResponsePhoneData;
import com.tokopedia.instantloan.data.soruce.PhoneDetailsDataStore;
import com.tokopedia.instantloan.data.soruce.cloud.api.InstantLoanApi;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;

import javax.inject.Inject;

import retrofit2.Response;
import retrofit2.http.Body;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by lavekush on 21/03/18.
 */

public class PhoneDetailsDataCloud implements PhoneDetailsDataStore {

    private final InstantLoanApi mApi;

    private PhoneDataMapper mMapper;

    @Inject
    public PhoneDetailsDataCloud(InstantLoanApi api, PhoneDataMapper mapper) {
        this.mApi = api;
        this.mMapper = mapper;
    }

    @Override
    public Observable<PhoneDataModelDomain> postPhoneData(@Body JsonObject data) {
        return mApi.postPhoneData(data).map(new Func1<Response<ResponsePhoneData>, PhoneDataModelDomain>() {
            @Override
            public PhoneDataModelDomain call(Response<ResponsePhoneData> response) {
                if (response.isSuccessful()) {
                    ResponsePhoneData phoneData = response.body();
                    return mMapper.transform(phoneData);
                } else {
                    throw new RuntimeException(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                    );
                }
            }
        });
    }
}
