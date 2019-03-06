package com.tokopedia.tkpdreactnative.react.banner;

import android.content.Context;
import android.graphics.Color;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.banner.Banner;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.design.banner.BannerView;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by meta on 28/02/19.
 */
public class ReactBannerManager extends SimpleViewManager<Banner> implements BannerView.OnPromoClickListener, BannerView.OnPromoAllClickListener, BannerView.OnPromoDragListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoScrolledListener {

    private static final String BANNER_CLASS = "BannerView";

    private List<String> redirectUrlList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();

    private Context context;

    @Override
    public String getName() {
        return BANNER_CLASS;
    }

    @Override
    protected Banner createViewInstance(ThemedReactContext reactContext) {
        this.context = reactContext;
        return new Banner(reactContext);
    }

    @ReactProp(name = "bannerData")
    public void setBannerData(Banner banner, @Nullable ReadableArray readableArray) {
        this.mappingImageBanner(readableArray);
        banner.setOnPromoClickListener(this);
        banner.setOnPromoAllClickListener(this);
        banner.setOnPromoScrolledListener(this);
        banner.setOnPromoLoadedListener(this);
        banner.setOnPromoDragListener(this);
        banner.setPromoList(imageList);
        banner.buildView();
    }

    @ReactProp(name = "setSeeAllPromoTextColor")
    public void setSeeAllPromoTextColor(Banner banner, @Nullable String string) {
        try {
            banner.setBannerSeeAllTextColor(Color.parseColor(string));
        } catch (Exception ignored) { }
    }

    private void mappingImageBanner(ReadableArray readableArray) {
        if (readableArray != null && readableArray.size() > 0) {
            for (int i = 0; i < readableArray.size(); i++) {
                ReadableMap map = readableArray.getMap(i);
                String imageUrl = map.getString("imageUrl");
                String redirectUrl = map.getString("redirectUrl");

                this.imageList.add(imageUrl);
                this.redirectUrlList.add(redirectUrl);
            }
        }
    }

    @Override
    public void onPromoLoaded() { }

    @Override
    public void onPromoScrolled(int position) { }

    @Override
    public void onPromoAllClick() {
        RouteManager.route(context, ApplinkConst.PROMO_LIST);
    }

    @Override
    public void onPromoDragStart() { }

    @Override
    public void onPromoDragEnd() { }

    @Override
    public void onPromoClick(int position) {
        String applink = redirectUrlList.get(position);
        if (applink.toLowerCase().contains("tokopedia://")) {
            RouteManager.route(context, applink);
        } else {
            if (context.getApplicationContext() instanceof TkpdCoreRouter) {
                context.startActivity(((ReactNativeRouter) context.getApplicationContext())
                        .getBrandsWebViewIntent(context, applink));
            }
        }
    }
}
