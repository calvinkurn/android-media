package com.tokopedia.tkpdreactnative.react;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;

import java.io.File;
import java.util.ArrayList;

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
    public void showBootingLoaderReactPage() {
        if (getCurrentActivity() != null && getCurrentActivity() instanceof ReactNativeView) {
            ((ReactNativeView) getCurrentActivity()).showLoaderReactPage();
        }
    }

    @ReactMethod
    public void hideBootingLoaderReactPage() {
        if (getCurrentActivity() != null && getCurrentActivity() instanceof ReactNativeView) {
            ((ReactNativeView) getCurrentActivity()).hideLoaderReactPage();
        }
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
    public void shareImageWithText(String imageLocalPath, String description, String websiteUrl){
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
}
