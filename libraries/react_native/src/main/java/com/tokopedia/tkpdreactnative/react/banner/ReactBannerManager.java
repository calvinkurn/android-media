package com.tokopedia.tkpdreactnative.react.banner;

import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by meta on 28/02/19.
 */
public class ReactBannerManager extends SimpleViewManager<Banner> implements View.OnClickListener {

    private static final String BANNER_CLASS = "BannerView";

    private Banner banner;
    private List<String> applinkList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();

    @Override
    public String getName() {
        return BANNER_CLASS;
    }

    @Override
    protected Banner createViewInstance(ThemedReactContext reactContext) {
        banner = new Banner(reactContext);
        banner.getBannerSeeAll().setOnClickListener(this);
        banner.buildView();
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

    @ReactProp(name = "bannerData")
    public void setBannerData(Banner banner, @Nullable ReadableArray readableArray) {
        this.mappingImageBanner(readableArray);
        banner.setItems(imageList);
        banner.setOnItemClickListener(position ->
                RouteManager.route(banner.getContext(), applinkList.get(position)));
    }

    private void mappingImageBanner(ReadableArray readableArray) {
        if (readableArray != null && readableArray.size() > 0) {
            for (int i = 0; i < readableArray.size(); i++) {
                ReadableMap map = readableArray.getMap(i);
                String imageUrl = map.getString("imageUrl");
                String applink = map.getString("applink");

                this.imageList.add(imageUrl);
                this.applinkList.add(applink);
            }
        }
    }
}
