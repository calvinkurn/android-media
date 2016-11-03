//package com.tkpd.library.utils;
//
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//
//import com.crashlytics.android.Crashlytics;
//import com.squareup.okhttp.Cache;
//import com.squareup.picasso.LruCache;
//import com.squareup.okhttp.Interceptor;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//import com.squareup.picasso.OkHttpDownloader;
//import com.squareup.picasso.Picasso;
//import com.tokopedia.tkpd.analytics.AnalyticsHandler;
//import com.tokopedia.tkpd.analytics.Type;
//import com.tokopedia.tkpd.analytics.container.GTMContainer;
//import com.tokopedia.tkpd.util.NetworkUtil;
//
//import java.io.IOException;
//import java.util.concurrent.Executors;
//
//public class PicassoHelper {
//    private static final String TAG = PicassoHelper.class.getSimpleName();
//    private static final String messageTAG = TAG + " : ";
//
//    private static Picasso mPicasso;
//
//    public static void createPicasso(final Context context) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.networkInterceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                long requestTime = System.currentTimeMillis();
//                Request request = chain.request();
//                Response response = chain.proceed(request);
//                long  responseTime = System.currentTimeMillis();
//                if(GTMContainer.getString(GTMContainer.IS_EXCEPTION_ENABLED).equals("true")){
//                    if(context != null) {
//                        CommonUtils.dumper("GTM Exception tracking enabled send image");
//                        AnalyticsHandler.init(context).type(Type.GA)
//                                .sendTiming(NetworkUtil.getNetworkType(context), (responseTime - requestTime), "Image " +
//                                        "Loading Time " + NetworkUtil.getCarrierName(context), request.urlString());
//                    }
//                }else{
//                    CommonUtils.dumper("GTM Exception tracking enabled "+GTMContainer.getString(GTMContainer.IS_EXCEPTION_ENABLED));
//                }
//                return response.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
//            }
//        });
//        okHttpClient.setCache(new Cache(context.getCacheDir(), Integer.MAX_VALUE));
//
//        mPicasso = new Picasso.Builder(context).downloader(new OkHttpDownloader(okHttpClient))
//                .memoryCache(new LruCache(8 * 1024 * 1024))
//                .executor(Executors.newFixedThreadPool(5))
//                .listener(new Picasso.Listener() {
//                    @Override
//                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                        if (exception != null) {
//                            Crashlytics.log(1, "Image Picasso", exception.getMessage());
//                        }
//                    }
//                })
//                .build();
////        mPicasso.setIndicatorsEnabled(true);
////        mPicasso.setLoggingEnabled(true);
//    }
//
//    public static Picasso getPicasso() {
//        return mPicasso;
//    }
//
//}
