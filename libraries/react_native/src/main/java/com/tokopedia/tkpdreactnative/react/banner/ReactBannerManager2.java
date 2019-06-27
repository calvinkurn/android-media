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
import com.tokopedia.banner.Indicator;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.design.banner.BannerView;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by meta on 28/02/19.
 */
public class ReactBannerManager2 extends SimpleViewManager<Banner> implements BannerView.OnPromoClickListener, BannerView.OnPromoAllClickListener, BannerView.OnPromoDragListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoScrolledListener {

    private static final String BANNER_CLASS = "BannerView2";
    private static final String IMAGE_URL = "imageUrl";
    private static final String APPLINK = "applink";
    private static final String INDICATOR_STYLE = "indicatorStyle";
    private static final String BANNER_LIST = "bannerList";

    private List<String> applinkList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private int indicatorType = Indicator.WHITE;
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
    public void setData(Banner banner, @Nullable ReadableMap data) {
        parseData(data);
        banner.setBannerIndicator(indicatorType);
        banner.setOnPromoClickListener(this);
        banner.setOnPromoAllClickListener(this);
        banner.setOnPromoScrolledListener(this);
        banner.setOnPromoLoadedListener(this);
        banner.setOnPromoDragListener(this);
        banner.setPromoList(imageList);
        banner.buildView();
    }

    @ReactProp(name = "buttonTextColor")
    public void buttonTextColor(Banner banner, @Nullable String string) {
        try {
            banner.setBannerSeeAllTextColor(Color.parseColor(string));
        } catch (Exception ignored) { }
    }

    private void parseData(ReadableMap data) {
        imageList.clear();
        applinkList.clear();

        indicatorType = data.getInt(INDICATOR_STYLE);
        ReadableArray readableArray = data.getArray(BANNER_LIST);
        if (readableArray != null && readableArray.size() > 0) {
            for (int i = 0; i < readableArray.size(); i++) {
                ReadableMap map = readableArray.getMap(i);
                String imageUrl = map.getString(IMAGE_URL);
                String redirectUrl = map.getString(APPLINK);

                imageList.add(imageUrl);
                applinkList.add(redirectUrl);
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
        String applink = applinkList.get(position);
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
