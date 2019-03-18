package com.tokopedia.tkpdreactnative.react.image;


import android.widget.ImageView;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.tokopedia.tkpdreactnative.R;

public class RctImageManager extends SimpleViewManager<ImageView> {
    @Override
    public String getName() {
        return "ReactImageLocal";
    }

    @Override
    protected ImageView createViewInstance(ThemedReactContext reactContext) {
        ImageView imageView = new ImageView(reactContext);
        imageView.setImageResource(R.drawable.qc_launcher2);
        imageView.setMaxWidth(150);
        imageView.setMaxHeight(150);
        return imageView;
    }

}
