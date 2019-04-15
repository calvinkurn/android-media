package com.tokopedia.tkpdreactnative.react.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.react.uimanager.SimpleViewManager;
import com.tokopedia.tkpdreactnative.R;
import com.facebook.react.uimanager.ThemedReactContext;

public class RctImageRemoteManager extends SimpleViewManager<ImageView> {
    @Override
    public String getName() {
        return "ReactImageRemote";
    }

    @Override
    protected ImageView createViewInstance(ThemedReactContext reactContext) {
        ImageView imageView = new ImageView(reactContext);
        imageView.setImageResource(R.drawable.qc_launcher2);
        Glide.with(reactContext).load("https://cdn.idntimes.com/content-images/community/2018/08/3cb0dc633c3d0fb082aed2bb0b0340c3_600x400.jpg")
                .into(imageView);
        return imageView;
    }
}
