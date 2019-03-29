package com.tokopedia.tkpd.thankyou.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.thankyou.data.mapper.MarketplaceTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.source.api.MarketplaceTrackerApi;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class MarketplaceTrackerCloudSource extends ThanksTrackerCloudSource {
    private Context context;
    private SessionHandler sessionHandler;
    private MarketplaceTrackerApi marketplaceTrackerApi;
    private MarketplaceTrackerMapper mapper;

    public MarketplaceTrackerCloudSource(RequestParams requestParams,
                                         MarketplaceTrackerApi marketplaceTrackerApi,
                                         SessionHandler sessionHandler,
                                         Context context) {
        super(requestParams);
        this.context = context;
        this.sessionHandler = sessionHandler;
        this.marketplaceTrackerApi = marketplaceTrackerApi;
    }

    @Override
    public Observable<Boolean> sendAnalytics() {
        mapper = new MarketplaceTrackerMapper(sessionHandler,
                (List<String>) requestParams.getObject(ThanksTrackerConst.Key.SHOP_TYPES),
                requestParams);
        return marketplaceTrackerApi.getTrackingData(getRequestPayload()).map(mapper);
    }

    private String getRequestPayload() {
        return String.format(
                loadRawString(context.getResources(), R.raw.payment_tracker_query),
                requestParams.getString(ThanksTrackerConst.Key.ID, "0")
        );
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {rawResource.close();} catch (IOException e) {}
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {}
        return stringBuilder.toString();
    }
}
