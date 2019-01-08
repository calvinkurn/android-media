package com.tokopedia.tkpd.thankyou.data.factory;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.thankyou.data.mapper.DigitalTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.mapper.MarketplaceTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.source.DigitalTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.MarketplaceTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.ThanksTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerApi;
import com.tokopedia.tkpd.thankyou.data.source.api.MarketplaceTrackerApi;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;

/**
 * Created by okasurya on 12/5/17.
 */

public class ThanksTrackerFactory {
    private DigitalTrackerApi digitalTrackerApi;
    private DigitalTrackerMapper digitalTrackerMapper;
    private MarketplaceTrackerApi marketplaceTrackerApi;
    private MarketplaceTrackerMapper marketplaceTrackerMapper;
    private Context context;
    private Gson gson;
    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;

    public ThanksTrackerFactory(DigitalTrackerApi digitalTrackerApi,
                                DigitalTrackerMapper digitalTrackerMapper,
                                MarketplaceTrackerApi marketplaceTrackerApi,
                                Context context,
                                Gson gson,
                                SessionHandler sessionHandler,
                                GCMHandler gcmHandler) {
        this.digitalTrackerApi = digitalTrackerApi;
        this.digitalTrackerMapper = digitalTrackerMapper;
        this.marketplaceTrackerApi = marketplaceTrackerApi;
        this.marketplaceTrackerMapper = marketplaceTrackerMapper;
        this.context = context;
        this.gson = gson;
        this.sessionHandler = sessionHandler;
        this.gcmHandler = gcmHandler;
    }

    public ThanksTrackerCloudSource cloudSource(RequestParams params) {
        String platform = params.getString(ThanksTrackerConst.Key.PLATFORM, "");

        if (!platform.isEmpty()) {
            if (platform.equals(ThanksTrackerConst.Platform.DIGITAL)) {
                return new DigitalTrackerCloudSource(params, digitalTrackerApi, digitalTrackerMapper, gson, sessionHandler, gcmHandler);
            } else if (platform.equals(ThanksTrackerConst.Platform.MARKETPLACE)) {
                return new MarketplaceTrackerCloudSource(params, marketplaceTrackerApi, sessionHandler, context);
            }
        }

        throw new RuntimeException("Platform Not Found");
    }
}
