package com.tokopedia.core.react;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.network.apiservices.common.CommonService;
import com.tokopedia.core.network.apiservices.common.CommonServiceAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author ricoharisin .
 */

public class ReactNetworkModule extends ReactContextBaseJavaModule {

    private Context context;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PROMISE_REJECT_UNKNOWN_METHOD = "UNKNOWN_METHOD";

    public ReactNetworkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @Override
    public String getName() {
        return "NetworkModule";
    }

    @ReactMethod
    public void getResponse(String url, String method, String request, Boolean isAuth, final Promise promise) {
        try {
            getObservableNetwork(url, method, request, isAuth)
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    promise.reject(e);
                                }

                                @Override
                                public void onNext(String s) {
                                    promise.resolve(s);
                                }
                            });
        } catch (PromiseException e) {
           promise.reject(e.errorCode, e.getMessage());
        }

    }


    private HashMap convertStringRequestToHashMap(String request) {
        HashMap params = new HashMap();
        try {
            JSONObject jsonObject = new JSONObject(request);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = jsonObject.get(key);
                    params.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    private Observable<String> getObservableNetwork(String url, String method,
                                                    String request, Boolean isAuth)
                                                    throws PromiseException {
        if (isAuth) {
            CommonServiceAuth serviceAuth = new CommonServiceAuth();
            if (method.equals(GET)) {
                return serviceAuth.getApi().get(url, convertStringRequestToHashMap(request));
            } else if (method.equals(POST)){
                return serviceAuth.getApi().post(url, convertStringRequestToHashMap(request));
            } else {
                throw new PromiseException(PROMISE_REJECT_UNKNOWN_METHOD, "unknown method");
            }
        } else {
            CommonService service = new CommonService();
            if (method.equals(GET)) {
                return service.getApi().get(url, convertStringRequestToHashMap(request));
            } else if (method.equals(POST)){
                return service.getApi().post(url, convertStringRequestToHashMap(request));
            } else {
                throw new PromiseException(PROMISE_REJECT_UNKNOWN_METHOD, "unknown method");
            }
        }
    }


}
