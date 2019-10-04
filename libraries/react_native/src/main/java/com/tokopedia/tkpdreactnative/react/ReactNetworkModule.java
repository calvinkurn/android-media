package com.tokopedia.tkpdreactnative.react;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpdreactnative.react.di.DaggerReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.tkpdreactnative.react.domain.UnifyReactNetworkRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.akamai_bot_lib.UtilsKt.getSensorData;

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

        if (param.containsKey(CONTENT_TYPE) && TextUtils.isEmpty(String.valueOf(param.get(CONTENT_TYPE)))) {
            contentType = String.valueOf(param.get(CONTENT_TYPE));
        }

        UserSession userSession = new UserSession(context);

        String fingerprintHash = "";
        if (context.getApplicationContext() != null &&
            context.getApplicationContext() instanceof NetworkRouter) {
            fingerprintHash = ((NetworkRouter) context.getApplicationContext())
                    .getFingerprintModel()
                    .getFingerprintHash();
        }

        Map<String, String> headers = AuthHelper.getAuthHeaderReact(
                userSession,
                param.containsKey(PATH) ? String.valueOf(param.get(PATH)) : "",
                param.containsKey(PARAM) ? String.valueOf(param.get(PARAM)) : "",
                param.containsKey(METHOD) ? String.valueOf(param.get(METHOD)) : METHOD_GET,
                contentType,
                fingerprintHash
        );

        WritableMap writableMap = Arguments.createMap();
        for (Map.Entry<String, String> item : headers.entrySet()) {
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
            promise.resolve(TokopediaUrl.Companion.getInstance().getMOJITO());
        } else if (param.equals("ace")){
            promise.resolve(TokopediaUrl.Companion.getInstance().getACE());
        } else if (param.equals("gql")) {
            promise.resolve(TokopediaUrl.Companion.getInstance().getGQL());
        } else if (param.equals("pulsa")){
            promise.resolve(TokopediaUrl.Companion.getInstance().getPULSA_API());
        } else if (param.equals("tome")) {
            promise.resolve(TokopediaUrl.Companion.getInstance().getTOME());
        } else if (param.equals("tokopedia")) {
            promise.resolve(TokopediaUrl.Companion.getInstance().getWEB());
        } else {
            promise.reject("Base api url param is not found!");
        }
    }

     interface Convert<E extends Map,F>{
        F convert(E value, String key);
     }

    @ReactMethod
    public void request(ReadableMap readableMap, final Promise promise) {
        try {
            HashMap<String, Object> maps = readableMap.toHashMap();
            ReactNetworkingConfiguration.Builder builder = new ReactNetworkingConfiguration.Builder();

            Convert<Map, String> fun = (value, key) -> TextUtils.isEmpty(String.valueOf(value.get(key)))?
                    "":String.valueOf(maps.get(key));
            Convert<Map, HashMap<String, Object>> getHashMap = (value, key) -> {
                return (value.get(key) instanceof HashMap) ? (HashMap<String, Object>) value.get(key) : new HashMap<>();
            };

            String url = null;
            builder.setUrl(url = fun.convert(maps,ReactConst.Networking.URL ));
            builder.setMethod(fun.convert(maps,ReactConst.Networking.METHOD));
            builder.setEncoding(fun.convert(maps,ReactConst.Networking.ENCODING));
            builder.setAuthorizationMode(fun.convert(maps,ReactConst.Networking.AUTHORIZATIONMODE));

            HashMap<String, Object> headers = getHashMap.convert(maps, ReactConst.Networking.HEADERS);
            if(url.contains("/hoth/discovery/api/page/flash-sale")){
                if(!TextUtils.isEmpty(getSensorData()))
                headers.put("X-acf-sensor-data", getSensorData());
            }
            headers.put("User-Agent", getUserAgent());
            builder.setHeaders(headers);
            builder.setParams(getHashMap.convert(maps, ReactConst.Networking.PARAMS));
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

    private static final String userAgentFormat = "TkpdConsumer/%s (%s;)";
    public static String getUserAgent(){
        return String.format(userAgentFormat, GlobalConfig.VERSION_NAME, "Android "+ Build.VERSION.RELEASE);
    }
}
