package com.tokopedia.tkpdreactnative.react.banner;

import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.tokopedia.banner.Banner;

/**
 * Created by meta on 28/02/19.
 */
public class ReactBannerManager extends SimpleViewManager<Banner> implements View.OnClickListener {

    private static final String BANNER_CLASS = "BannerView";

    private Banner banner;

    @Override
    public String getName() {
        return BANNER_CLASS;
    }

    @Override
    protected Banner createViewInstance(ThemedReactContext reactContext) {
        banner = new Banner(reactContext);
        banner.getBannerSeeAll().setOnClickListener(this);
        return banner;
    }

    @Override
    public void onClick(View view) {
        if (view == banner.getBannerSeeAll()) {
            WritableMap map = Arguments.createMap();
            ReactContext reactContext = (ReactContext) view.getContext();
            reactContext.getJSModule(RCTEventEmitter.class)
                    .receiveEvent(view.getId(), "bannerSeeAllClick", map);
        }
    }
}
