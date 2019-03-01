package com.tokopedia.tkpdreactnative.react.banner;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.banner.Banner;
import com.tokopedia.design.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by meta on 28/02/19.
 */
public class ReactBannerManager extends SimpleViewManager<Banner> implements BannerView.OnPromoAllClickListener, BannerView.OnPromoDragListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoScrolledListener {

    private static final String BANNER_CLASS = "BannerView";

    private List<String> applinkList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();

    @Override
    public String getName() {
        return BANNER_CLASS;
    }

    @Override
    protected Banner createViewInstance(ThemedReactContext reactContext) {
        return new Banner(reactContext);
    }

    @ReactProp(name = "bannerData")
    public void setBannerData(Banner banner, @Nullable ReadableArray readableArray) {
        this.mappingImageBanner(readableArray);
        banner.setOnPromoClickListener(position -> RouteManager.route(banner.getContext(), applinkList.get(position)));
        banner.setOnPromoAllClickListener(this);
        banner.setOnPromoScrolledListener(this);
        banner.setOnPromoLoadedListener(this);
        banner.setOnPromoDragListener(this);
        banner.setPromoList(imageList);
        banner.buildView();
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

    @Override
    public void onPromoLoaded() {

    }

    @Override
    public void onPromoScrolled(int position) {

    }

    @Override
    public void onPromoAllClick() {

    }

    @Override
    public void onPromoDragStart() {

    }

    @Override
    public void onPromoDragEnd() {

    }
}
