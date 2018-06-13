package com.tokopedia.networklib.domain;

import android.webkit.URLUtil;

import com.tokopedia.networklib.data.model.RequestType;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponse;
import com.tokopedia.networklib.data.source.cloud.CloudRestRestDataStore;
import com.tokopedia.networklib.data.source.repository.RestRepositoryImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public abstract class RestUseCase<T> extends UseCase<RestResponse> {

    private RestRepositoryImpl mRepository;
    protected Type mType;

    public RestUseCase(RestRepositoryImpl repository) {
        mType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.mRepository = repository;
    }

    public RestUseCase() {
        mType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.mRepository = new RestRepositoryImpl(new CloudRestRestDataStore());
    }

    @Override
    public Observable<RestResponse> createObservable(RequestParams requestParams) {
        return mRepository.getResponse(buildRequest()).map(restResponseInternal -> new RestResponse(restResponseInternal.getOriginalResponse(), restResponseInternal.isCached()));
    }

    private RestRequest buildRequest() {

//        if (mType == null) {
//            throw new RuntimeException("Please add valid class type token in order to retrieve the data");
//        }

        if (!URLUtil.isValidUrl(getUrl())) {
            throw new RuntimeException("Please set valid request url into your UseCase class");
        }

        if ((getHttpRequestType() == RequestType.POST
                || getHttpRequestType() == RequestType.DELETE)
                && getBody() == null) {
            throw new RuntimeException("Please set valid request body into your UseCase class");
        }

        return new RestRequest.Builder(mType, getUrl())
                .setBody(getBody())
                .setRequestType(getHttpRequestType() == null ? RequestType.GET : getHttpRequestType())
                .setHeaders(getHeaders() == null ? new HashMap<>() : getHeaders())
                .setQueryParams(getQueriesMap() == null ? new HashMap<>() : getQueriesMap())
                .build();
    }


    public abstract String getUrl();

    public abstract Map<String, Object> getHeaders();

    public abstract Map<String, Object> getQueriesMap();

    public abstract Object getBody();

    public abstract RequestType getHttpRequestType();
}
