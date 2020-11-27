package com.tokopedia.tkpd.thankyou.data.factory;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.tkpd.thankyou.data.mapper.DigitalTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.source.DigitalTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.MarketplaceTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.ThanksTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerApi;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by okasurya on 12/5/17.
 */

public class ThanksTrackerFactory {
    private DigitalTrackerApi digitalTrackerApi;
    private DigitalTrackerMapper digitalTrackerMapper;
    private Context context;
    private Gson gson;
    private GCMHandler gcmHandler;
    private UserSessionInterface userSession;

    public ThanksTrackerFactory(DigitalTrackerApi digitalTrackerApi,
                                DigitalTrackerMapper digitalTrackerMapper,
                                Context context,
                                Gson gson,
                                UserSessionInterface userSession,
                                GCMHandler gcmHandler) {
        this.digitalTrackerApi = digitalTrackerApi;
        this.digitalTrackerMapper = digitalTrackerMapper;
        this.context = context;
        this.gson = gson;
        this.userSession = userSession;
        this.gcmHandler = gcmHandler;
    }

    public ThanksTrackerCloudSource cloudSource(RequestParams params) {
        String platform = params.getString(ThanksTrackerConst.Key.PLATFORM, "");

        if (!platform.isEmpty()) {
            if (platform.equals(ThanksTrackerConst.Platform.DIGITAL)) {
                return new DigitalTrackerCloudSource(params, digitalTrackerApi, digitalTrackerMapper, gson, userSession, gcmHandler);
            } else if (platform.equals(ThanksTrackerConst.Platform.MARKETPLACE)) {
                return new MarketplaceTrackerCloudSource(params, userSession, context);
            }
        }

        throw new RuntimeException("Platform Not Found");
    }
}
