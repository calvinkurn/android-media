package com.tokopedia.tkpd.beranda.data.source;

import android.content.Context;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.beranda.data.mapper.TickerMapper;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerDataSource {

    private final Context context;
    private final CategoryApi categoryApi;
    private final TickerMapper tickerMapper;

    public TickerDataSource(Context context, CategoryApi categoryApi, TickerMapper tickerMapper) {
        this.context = context;
        this.categoryApi = categoryApi;
        this.tickerMapper = tickerMapper;
    }

    public Observable<Ticker> getTicker(){
        return categoryApi.getTickers(SessionHandler.getLoginID(context),
                CategoryApi.size,
                CategoryApi.FILTER_ANDROID_DEVICE).map(tickerMapper);
    }
}
