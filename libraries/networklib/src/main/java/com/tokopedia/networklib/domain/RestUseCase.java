package com.tokopedia.networklib.domain;

import android.webkit.URLUtil;

import com.tokopedia.networklib.data.model.RequestType;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponse;
import com.tokopedia.networklib.data.model.RestResponseInternal;
import com.tokopedia.networklib.data.source.cloud.CloudRestRestDataStore;
import com.tokopedia.networklib.data.source.repository.RestRepositoryImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public abstract class RestUseCase extends UseCase<RestResponse> {

    private RestRepositoryImpl mRepository;
    protected String mRequestUrl;
    protected Map<String, String> mQueryParams;
    protected Map<String, String> mHeaders;
    protected Object mBody;
    protected Type mType;
    protected RequestType mRequestType;

    public RestUseCase(RestRepositoryImpl repository) {
        this.mRepository = repository;
    }

    public RestUseCase() {
        this.mRepository = new RestRepositoryImpl(new CloudRestRestDataStore());
    }

    @Override
    public Observable<RestResponse> createObservable(RequestParams requestParams) {
        return mRepository.getResponse(buildRequest()).map(new Func1<RestResponseInternal, RestResponse>() {
            @Override
            public RestResponse call(RestResponseInternal restResponseInternal) {
                return new RestResponse(restResponseInternal.getOriginalResponse(), restResponseInternal.isCached());
            }
        });
    }

    private RestRequest buildRequest() {

        if (mType == null) {
            throw new RuntimeException("Please add valid class type token in order to retrieve the data");
        }

        if (URLUtil.isValidUrl(mRequestUrl)) {
            throw new RuntimeException("Please set valid request url into your UseCase class");
        }

        if ((mRequestType == RequestType.POST || mRequestType == RequestType.DELETE)
                && mBody == null) {
            throw new RuntimeException("Please set valid request body into your UseCase class");
        }

        return new RestRequest.Builder(mType, mRequestUrl)
                .setBody(mBody)
                .setRequestType(mRequestType == null ? RequestType.GET : mRequestType)
                .setHeaders(mHeaders)
                .setQueryParams(mQueryParams)
                .build();
    }

    public abstract void initRequestData();
}
