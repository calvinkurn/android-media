package com.tokopedia.tkpdreactnative.react;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.utils.AuthUtil;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpdreactnative.react.di.DaggerReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.tkpdreactnative.react.domain.UnifyReactNetworkRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ricoharisin .
 */
public class ReactNetworkModule extends ReactContextBaseJavaModule {

    private static final String PATH = "path";
    private static final String PARAM = "param";
    private static final String METHOD = "method";
    private static final String CONTENT_TYPE = "contentType";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
    public static final String METHOD_GET = "GET";

    @Inject
    ReactNetworkRepository reactNetworkRepository;
    @Inject
    UnifyReactNetworkRepository unifyReactNetworkRepository;

    ReactNativeNetworkComponent daggerRnNetworkComponent;

    private CompositeSubscription compositeSubscription;
    private Context context;

    public ReactNetworkModule(ReactApplicationContext reactContext) {
        super(reactContext);

        this.context = reactContext;
        if (reactContext.getApplicationContext() instanceof MainApplication) {
            AppComponent appComponent = ((MainApplication) reactContext.getApplicationContext()).getApplicationComponent();
            daggerRnNetworkComponent = DaggerReactNativeNetworkComponent.builder()
                    .appComponent(appComponent).build();
            daggerRnNetworkComponent.inject(this);
            compositeSubscription = new CompositeSubscription();
        } else {
            throw new RuntimeException("Current context unsupported");
        }
    }

    private static TKPDMapParam<String, String> convertStringRequestToHashMap(String request) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        try {
            JSONObject jsonObject = new JSONObject(request);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = jsonObject.get(key);
                    params.put(key, value.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    @Override
    public String getName() {
        return "NetworkModule";
    }

    @Deprecated
    @ReactMethod
    public void getResponse(String url, String method, String request, Boolean isAuth, final Promise promise) {
        try {
            compositeSubscription.add(reactNetworkRepository.getResponse(url, method, convertStringRequestToHashMap(request), isAuth)
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
                            if (getCurrentActivity() != null) {
                                promise.resolve(s);
                            } else {
                                promise.resolve("");
                            }
                        }
                    }));
        } catch (UnknownMethodException e) {
            promise.reject(e);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    /**
     *
     * @param reactParam will consists of
     * {
     *  path: string of url path
     *  param: string of graphql query with all id / params included
     *  method: string of request method like POST, GET, etc
     *  contentType: string of content type,
     *               if empty / null the default value is "application/json; charset=UTF-8"
     * }
     * @param promise
     */
    @ReactMethod
    public void getAuthHeader(ReadableMap reactParam, Promise promise) {
        Map<String, Object> param = reactParam.toHashMap();
        String contentType = APPLICATION_JSON_CHARSET_UTF_8;
        if(param.containsKey(CONTENT_TYPE) && TextUtils.isEmpty(String.valueOf(param.get(CONTENT_TYPE)))) {
            contentType = String.valueOf(param.get(CONTENT_TYPE));
        }

        Map<String, String> headers = AuthUtil.getAuthHeaderReact(
                context,
                param.containsKey(PATH) ? String.valueOf(param.get(PATH)) : "",
                param.containsKey(PARAM) ? String.valueOf(param.get(PARAM)) : "",
                param.containsKey(METHOD) ? String.valueOf(param.get(METHOD)) : METHOD_GET,
                contentType
        );
        WritableMap writableMap = Arguments.createMap();
        for(Map.Entry<String, String> item : headers.entrySet()) {
           writableMap.putString(item.getKey(), item.getValue());
        }
        promise.resolve(writableMap);
    }

    @ReactMethod
    public void getResponseJson(String url, String method, String request, Boolean isAuth, final Promise promise) {
        try {
            CommonUtils.dumper(url + " " + request);
            compositeSubscription.add(reactNetworkRepository.getResponseJson(url, method, request, isAuth)
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
                            if (getCurrentActivity() != null) {
                                promise.resolve(s);
                            } else {
                                promise.resolve("");
                            }
                        }
                    }));
        } catch (UnknownMethodException e) {
            promise.reject(e);
        } catch (Exception e) {
            promise.reject(e);
        }
    }


    /**
     * call api with <b>encoded</b> parameter query
     * @param url
     * @param method POST or GET
     * @param encodedRequest the request data must be encoded
     * @param isAuth
     * @param promise
     */
    @ReactMethod
    public void getResponseParam(String url, String method, String encodedRequest, Boolean isAuth, final Promise promise) {
        try {
            CommonUtils.dumper(url + " " + encodedRequest);
            compositeSubscription.add(reactNetworkRepository
                    .getResponseParam(url, method, encodedRequest, isAuth)
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
                            if (getCurrentActivity() != null) {
                                promise.resolve(s);
                            } else {
                                promise.resolve("");
                            }
                        }
                    })
            );
        } catch (UnknownMethodException e) {
            promise.reject(e);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void getBaseApiUrl(String param, Promise promise){
        if (param.equals("mojito")){
            promise.resolve(TkpdBaseURL.MOJITO_DOMAIN);
        } else if (param.equals("ace")){
            promise.resolve(TkpdBaseURL.ACE_DOMAIN);
        } else if (param.equals("gql")) {
            promise.resolve(TkpdBaseURL.HOME_DATA_BASE_URL);
        } else if (param.equals("pulsa")){
            promise.resolve(TkpdBaseURL.DIGITAL_API_DOMAIN);
        } else if (param.equals("tome")) {
            promise.resolve(TkpdBaseURL.TOME_DOMAIN);
        } else if (param.equals("tokopedia")) {
            promise.resolve(TkpdBaseURL.WEB_DOMAIN);
        } else {
            promise.reject("Base api url param is not found!");
        }
    }





    @ReactMethod
    public void request(ReadableMap readableMap, final Promise promise) {
        try {
            HashMap<String, Object> maps = readableMap.toHashMap();
            ReactNetworkingConfiguration.Builder builder = new ReactNetworkingConfiguration.Builder();
            for (Map.Entry<String, Object> map : maps.entrySet()) {
                switch (map.getKey()) {
                    case ReactConst.Networking.URL:
                        builder.setUrl(String.valueOf(map.getValue()));
                        break;
                    case ReactConst.Networking.METHOD:
                        builder.setMethod(String.valueOf(map.getValue()));
                        break;
                    case ReactConst.Networking.ENCODING:
                        builder.setEncoding(String.valueOf(map.getValue()));
                        break;
                    case ReactConst.Networking.AUTHORIZATIONMODE:
                        builder.setAuthorizationMode(String.valueOf(map.getValue()));
                        break;
                    case ReactConst.Networking.HEADERS:
                        if (map.getValue() instanceof HashMap) {
                            builder.setHeaders((HashMap<String, Object>) map.getValue());
                        }
                        break;
                    case ReactConst.Networking.PARAMS:
                        if (map.getValue() instanceof HashMap) {
                            builder.setParams((HashMap<String, Object>) map.getValue());
                        }
                        break;

                }
            }
            ReactNetworkingConfiguration configuration = builder.build();

            compositeSubscription.add(unifyReactNetworkRepository.request(configuration)
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
                            if (getCurrentActivity() != null) {
                                promise.resolve(s);
                            } else {
                                promise.resolve("");
                            }
                        }
                    })
            );
        } catch (Exception e) {
            promise.reject(e);
        }
    }
}
