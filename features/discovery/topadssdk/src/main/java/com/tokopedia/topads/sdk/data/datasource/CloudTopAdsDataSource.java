package com.tokopedia.topads.sdk.data.datasource;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.TKPDMapParam;
import com.tokopedia.topads.sdk.domain.mapper.MerlinCategoryMapper;
import com.tokopedia.topads.sdk.domain.mapper.PreferedCategoryMapper;
import com.tokopedia.topads.sdk.domain.mapper.TopAdsBannerMapper;
import com.tokopedia.topads.sdk.domain.mapper.TopAdsMapper;
import com.tokopedia.topads.sdk.domain.mapper.WishListCheckMapper;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.MerlinRecomendation;
import com.tokopedia.topads.sdk.domain.model.PreferedCategory;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.network.HttpMethod;
import com.tokopedia.topads.sdk.network.HttpRequest;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author by errysuprayogi on 3/27/17.
 */

public class CloudTopAdsDataSource implements TopAdsDataSource {

    public static final String SCHEME = "https";
    public static final String MOJITO_URL = "mojito.tokopedia.com";
    public static final String V1_USERS = "v1/users";
    public static final String WISHLIST_CHECK = "wishlist/check";
    private static final String URL_DISPLAY_ADS = "promo/v1.1/display/ads";
    private static final String URL_DISPLAY_ADS_V1_3 = "promo/v1.3/display/ads";
    private static final String URL_INFO_USER = "promo/v1/info/user";
    private static final String URL_MERLIN = "https://merlin.tokopedia.com/v4/product/category/recommendation";
    private static final String TKPD_SESSION_ID = "Tkpd-SessionId";
    private static final String TKPD_USER_ID = "Tkpd-UserId";
    private static final String X_DEVICE = "X-Device";
    private static final String DEFAULT_X_DEVICE = "android";
    private Context context;
    private Config config;

    public CloudTopAdsDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public CpmModel getTopAdsBanner(TKPDMapParam<String, String> params) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(config.getBaseUrl() + URL_DISPLAY_ADS_V1_3)
                .addHeader(TKPD_SESSION_ID, config.getSessionId())
                .addHeader(X_DEVICE, "android-" + GlobalConfig.VERSION_NAME)
                .setMethod(HttpMethod.GET)
                .addParameters(params)
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        return new TopAdsBannerMapper(executor).getModel();
    }

    @Override
    public TopAdsModel getTopAds(TKPDMapParam<String, String> params, int position) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(config.getBaseUrl() + URL_DISPLAY_ADS)
                .addHeader(TKPD_SESSION_ID, config.getSessionId())
                .addHeader(X_DEVICE, "android-" + GlobalConfig.VERSION_NAME)
                .setMethod(HttpMethod.GET)
                .addParameters(params)
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        return new TopAdsMapper(context, executor, position).getModel();
    }

    @Override
    public PreferedCategory getPreferenceCategory() {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(config.getBaseUrl() + URL_INFO_USER)
                .setMethod(HttpMethod.GET)
                .addHeader(TKPD_SESSION_ID, config.getSessionId())
                .addHeader(TKPD_USER_ID, config.getUserId())
                .addHeader(X_DEVICE, "android-" + GlobalConfig.VERSION_NAME)
                .addParameter("pub_id", "14")
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        return new PreferedCategoryMapper(context, executor).getModel();
    }

    @Override
    public MerlinRecomendation getMerlinRecomendation(String query) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(URL_MERLIN)
                .setMethod(HttpMethod.POST_RAW)
                .addHeader(X_DEVICE, "android-" + GlobalConfig.VERSION_NAME)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addJsonBodyParameter(String.format("{\"parcel\":[{\"data\":{\"product_title\":\"%s\"}}],\"size\":1,\"expect\":1}", query))
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        return new MerlinCategoryMapper(context, executor).getModel();
    }

    @Override
    public String clickTopAdsUrl(String url) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(url)
                .addHeader(X_DEVICE, "android-" + GlobalConfig.VERSION_NAME)
                .setMethod(HttpMethod.GET)
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        try {
            return executor.executeAsGetRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; //just return empty string.
    }

    @Override
    public TopAdsModel checkWishlist(TopAdsModel model) {
        List<String> ids = new ArrayList<>();
        for (Data data : model.getData()) {
            if (data.getProduct() != null && !TextUtils.isEmpty(data.getProduct().getId())) {
                ids.add(data.getProduct().getId());
            }
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(MOJITO_URL)
                .appendEncodedPath(V1_USERS)
                .appendPath(config.getUserId())
                .appendEncodedPath(WISHLIST_CHECK)
                .appendEncodedPath(TextUtils.join(",", ids));
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(builder.build().toString())
                .setMethod(HttpMethod.GET)
                .addHeader(TKPD_SESSION_ID, config.getSessionId())
                .addHeader(TKPD_USER_ID, config.getUserId())
                .addHeader(X_DEVICE, DEFAULT_X_DEVICE)
                .build();

        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        return new WishListCheckMapper(executor, model).getModel();
    }
}
