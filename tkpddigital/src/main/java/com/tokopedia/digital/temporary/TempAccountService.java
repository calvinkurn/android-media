//package com.tokopedia.digital.temporary;
//
///**
// * @author anggaprasetiyo on 3/9/17.
// */
//
//public class TempAccountService {
//
//    public static final String ACCOUNTS = "ACCOUNTS";
//    public static final String WS = "WS";
//
//    public static final String AUTH_KEY = "AUTH_KEY";
//    public static final String WEB_SERVICE = "WEB_SERVICE";
//    public static final String USING_HMAC = "USING_HMAC";
//
//
//    private static final String TAG = TempAccountService.class.getSimpleName();
//    protected TempAccountApi api;
//
//    public AccountsService(Bundle bundle) {
//
//        String authKey = bundle.getString(AUTH_KEY, "");
//        String webService = bundle.getString(WEB_SERVICE, ACCOUNTS);
//        boolean isUsingHMAC = bundle.getBoolean(USING_HMAC, false);
//
//        String status = TrackingUtils.eventHTTP();
//
//        OkHttpClient.Builder client = new OkHttpClient.Builder();
//        setInterceptorDebug(client);
//        client.connectTimeout(45, TimeUnit.SECONDS);
//        client.readTimeout(45, TimeUnit.SECONDS);
//        client.writeTimeout(45, TimeUnit.SECONDS);
//        if (("false").equals(status) || ("".equals(status))) {
//            client.protocols(Collections.singletonList(Protocol.HTTP_1_1));
//        }
//        Interceptor authInterceptor;
//        authInterceptor = new AccountsInterceptor(authKey, isUsingHMAC);
//        client.interceptors().add(authInterceptor);
//
//        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
//        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        client.interceptors().add(logInterceptor);
//
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .setPrettyPrinting()
//                .serializeNulls()
//                .create();
//
//        Retrofit.Builder retrofit = new Retrofit.Builder();
//        String baseUrl = getBaseUrl(webService);
//        if (baseUrl.startsWith("https://accounts") & BuildConfig.DEBUG) {
//            SharedPreferences pref = MainApplication.getAppContext()
//                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
//            if (pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).contains("alpha")) {
//                baseUrl = TkpdBaseURL.ACCOUNTS_ALPHA_DOMAIN;
//            } else if (pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).contains("staging")) {
//                baseUrl = TkpdBaseURL.ACCOUNTS_STAGING_DOMAIN;
//            }
//        }
//
//        if (baseUrl.startsWith("https://ws") & BuildConfig.DEBUG) {
//            SharedPreferences pref = MainApplication.getAppContext()
//                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
//            baseUrl = pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN);
//            retrofit.addConverterFactory(new TkpdResponseConverter());
//        }
//
//        retrofit.baseUrl(baseUrl);
//        retrofit.addConverterFactory(new GeneratedHostConverter());
//        retrofit.addConverterFactory(new TkpdResponseConverter());
//        retrofit.addConverterFactory(new StringResponseConverter());
//        retrofit.addConverterFactory(GsonConverterFactory.create(gson));
//        retrofit.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
//        retrofit.client(client.build());
//        initApiService(retrofit.build());
//    }
//
//    private void setInterceptorDebug(OkHttpClient.Builder client) {
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
//            Boolean allowLogOnNotification = cache.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false);
//            client.addInterceptor(new ChuckInterceptor(MainApplication.getAppContext())
//                    .showNotification(allowLogOnNotification));
//        }
//    }
//
//    protected void initApiService(Retrofit retrofit) {
//        api = retrofit.create(AccountsApi.class);
//    }
//
//    protected String getBaseUrl(String webService) {
//        switch (webService) {
//            case WS:
//                return TkpdBaseURL.BASE_DOMAIN;
//            case ACCOUNTS:
//                return TkpdBaseURL.ACCOUNTS_DOMAIN;
//            default:
//                throw new RuntimeException("unknown Base URL");
//        }
//    }
//
//    public AccountsApi getApi() {
//        return api;
//    }
//
//}
