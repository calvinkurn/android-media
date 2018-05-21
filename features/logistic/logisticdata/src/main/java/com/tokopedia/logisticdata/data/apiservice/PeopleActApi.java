package com.tokopedia.logisticdata.data.apiservice;

import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface PeopleActApi {

    @FormUrlEncoded
    @POST(LogisticDataConstantUrl.PeopleAction.PATH_EDIT_ADDRESS)
    Observable<String> editAddress(@FieldMap Map<String, String> params);

}
