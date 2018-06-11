package com.tokopedia.networklib.data.source.cloud;

import com.google.gson.Gson;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;
import com.tokopedia.networklib.data.source.RestDataStore;
import com.tokopedia.networklib.data.source.cloud.api.RestApi;
import com.tokopedia.networklib.util.RestClient;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

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
                        requests.getHeaders()).map(new Func1<String, RestResponseInternal>() {
                    @Override
                    public RestResponseInternal call(String s) {
                        return new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true);
                    }
                });
            case POST:

                String body = null;
                try {
                    body = mGson.toJson(requests.getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (body == null) {
                    throw new RuntimeException("Invalid json object provided");
                }

                return mApi.post(requests.getUrl(),
                        body,
                        requests.getQueryParams(),
                        requests.getHeaders()).map(new Func1<String, RestResponseInternal>() {
                    @Override
                    public RestResponseInternal call(String s) {
                        return new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true);
                    }
                });
            case PUT:
                body = null;

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
                        requests.getHeaders()).map(new Func1<String, RestResponseInternal>() {
                    @Override
                    public RestResponseInternal call(String s) {
                        return new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true);
                    }
                });
            case DELETE:
                return mApi.get(requests.getUrl(),
                        requests.getQueryParams(),
                        requests.getHeaders()).map(new Func1<String, RestResponseInternal>() {
                    @Override
                    public RestResponseInternal call(String s) {
                        return new RestResponseInternal(mGson.fromJson(s, requests.getTypeOfT()), true);
                    }
                });
            default:
                //TODO add impl
        }
        return null;
    }
}
