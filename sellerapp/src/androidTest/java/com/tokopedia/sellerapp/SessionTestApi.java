package com.tokopedia.sellerapp;

import com.tokopedia.core.network.apiservices.user.apis.SessionApi;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import rx.Observable;

/**
 * Created by normansyahputa on 8/15/16.
 */

public class SessionTestApi implements SessionApi {
    @Override
    public Observable<Response<TkpdResponse>> login(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> createPassword(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> doLogin(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> doLoginPlus(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<String>> doLoginPlusx(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> logout(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> loginBypass(@FieldMap Map<String, String> params) {
        return null;
    }
}
