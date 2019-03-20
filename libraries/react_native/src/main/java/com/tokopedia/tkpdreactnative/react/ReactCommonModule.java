package com.tokopedia.tkpdreactnative.react;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;

import java.io.File;
import java.util.ArrayList;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * @author ricoharisin .
 */

public class ReactCommonModule extends ReactContextBaseJavaModule {

    public Context context;

    public ReactCommonModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "CommonModule";
    }

    @ReactMethod
    public void getImageHost(Promise promise) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        promise.resolve(remoteConfig.getString(RemoteConfigKey.IMAGE_HOST, "http://ecs7.tokopedia.net"));
    }

    @ReactMethod
    public void showBootingLoaderReactPage(Promise promise) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null && getCurrentActivity() instanceof ReactNativeView) {
                    ((ReactNativeView) getCurrentActivity()).showLoaderReactPage();
                }
            }
        });
    }

    @ReactMethod
    public void hideBootingLoaderReactPage(Promise promise) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null && getCurrentActivity() instanceof ReactNativeView) {
                    ((ReactNativeView) getCurrentActivity()).hideLoaderReactPage();
                }
            }
        });
    }

    @ReactMethod
    public void getAppVersionCode(Promise promise) {
        int versionCode = GlobalConfig.VERSION_CODE;
        promise.resolve(versionCode);
    }

    @ReactMethod
    public void getAppVersionName(Promise promise) {
        String versionName = GlobalConfig.VERSION_NAME;
        promise.resolve(versionName);
    }

    @ReactMethod
    public void getYoutubeApiKey(Promise promise){
        promise.resolve(ReactConst.YOUTUBE_API_KEY);
    }

    @ReactMethod
    public void downloadImageToLocalPath(String downloadImageUrl, String description, String websiteUrl) {
        ArrayList<String> shareImageUrlList = new ArrayList<>();
        shareImageUrlList.add(downloadImageUrl);
        ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(context);
        if(shareImageUrlList != null && shareImageUrlList.size() > 0) {
            imageDownloadHelper.convertHttpPathToLocalPath(shareImageUrlList, true,
                    new ImageDownloadHelper.OnImageDownloadListener() {
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onSuccess(ArrayList<String> resultLocalPaths) {
                            if(resultLocalPaths != null && resultLocalPaths.size() > 0 ){
                                shareImageWithText(resultLocalPaths.get(0), description, websiteUrl);
                            }
                        }
                    });
        }
    }

    @ReactMethod
    public void shareImageWithText(String imageLocalPath, String description, String websiteUrl) {
        Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",
                new File(imageLocalPath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        //Add text and then Image URI
        shareIntent.putExtra(Intent.EXTRA_TEXT, description +" "+ websiteUrl);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            Intent chooserIntent = Intent.createChooser(shareIntent, context.getString(R.string.share_dialog_title));
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooserIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @ReactMethod
    public void stopTracing() {
        ReactUtils.stopTracing();
    }

    @ReactMethod
    public void getStatusBarHeight(Promise promise) {
        int result = 25;
        if(getCurrentActivity() != null) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = (int) (context.getResources().getDimensionPixelSize(resourceId) / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
            }
        }

        promise.resolve(result);
    }
}
