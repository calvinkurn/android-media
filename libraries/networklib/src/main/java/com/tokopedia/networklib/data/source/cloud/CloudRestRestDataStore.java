package com.tokopedia.networklib.data.source.cloud;

import com.google.gson.Gson;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;
import com.tokopedia.networklib.data.source.RestDataStore;
import com.tokopedia.networklib.data.source.cloud.api.RestApi;
import com.tokopedia.networklib.util.RestClient;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

public class CloudRestRestDataStore implements RestDataStore {

    private RestApi mApi;
    private Gson mGson;

    @Inject
    public CloudRestRestDataStore() {
        this.mApi = RestClient.getApiInterface();
        this.mGson = new Gson();
    }

    @Override
    public Observable<RestResponseInternal> getResponse(RestRequest requests) {
        switch (requests.getRequestType()) {
            case GET:
                return mApi.get(requests.getUrl(),
                        requests.getQueryParams(),
                        requests.getHeaders()).map(s -> new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true));
            case POST:
                if (requests.getBody() != null && requests.getBody() instanceof Map) {
                    return mApi.post(requests.getUrl(),
                            (Map<String, Object>) requests.getBody(),
                            requests.getQueryParams(),
                            requests.getHeaders()).map(s -> new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true));
                } else {
                    String body = null;
                    if (requests.getBody() instanceof String) {
                        body = (String) requests.getBody();
                    } else {
                        try {
                            body = mGson.toJson(requests.getBody());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (body == null) {
                        throw new RuntimeException("Invalid json object provided");
                    }

                    return mApi.post(requests.getUrl(),
                            body,
                            requests.getQueryParams(),
                            requests.getHeaders()).map(s -> new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true));
                }
            case PUT:
                if (requests.getBody() != null && requests.getBody() instanceof Map) {
                    return mApi.put(requests.getUrl(),
                            (Map<String, Object>) requests.getBody(),
                            requests.getQueryParams(),
                            requests.getHeaders()).map(s -> new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true));
                } else {
                    String body = null;

                    try {
                        body = mGson.toJson(requests.getBody());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (body == null) {
                        throw new RuntimeException("Invalid json object provided");
                    }

                    return mApi.put(requests.getUrl(),
                            body,
                            requests.getQueryParams(),
                            requests.getHeaders()).map(s -> new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true));
                }
            case DELETE:
                return mApi.get(requests.getUrl(),
                        requests.getQueryParams(),
                        requests.getHeaders()).map(s -> new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true));
            default:
                //TODO add impl
        }
        return null;
    }
}
