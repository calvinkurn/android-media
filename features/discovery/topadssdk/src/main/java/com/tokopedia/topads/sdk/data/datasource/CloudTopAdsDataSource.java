package com.tokopedia.topads.sdk.data.datasource;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.network.authentication.AuthConstant;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.TKPDMapParam;
import com.tokopedia.topads.sdk.domain.mapper.TopAdsBannerMapper;
import com.tokopedia.topads.sdk.domain.mapper.TopAdsMapper;
import com.tokopedia.topads.sdk.domain.mapper.WishListCheckMapper;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.Data;
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

    private static final String URL_DISPLAY_ADS_V1_3 = "promo/v1.3/display/ads";
    private static final String TKPD_SESSION_ID = "Tkpd-SessionId";
    private static final String X_DEVICE = "X-Device";
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
                .addHeader(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
                .setMethod(HttpMethod.GET)
                .addParameters(params)
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        return new TopAdsBannerMapper(executor).getModel();
    }

}
