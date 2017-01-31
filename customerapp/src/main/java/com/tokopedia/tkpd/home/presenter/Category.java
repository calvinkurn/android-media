package com.tokopedia.tkpd.home.presenter;

import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.home.facade.FacadePromo;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 2/24/16.
 */
public interface Category {
    String TAG = "MNORMANSYAH";
    String messageTAG = "CategoryApi ";

    void subscribe();
    void unSubscribe();
    void fetchBanners(final FacadePromo.GetPromoListener listener);
    void fetchSlides(final FacadePromo.GetPromoListener listener);
    void fetchTickers(final FetchTickersListener listener);

    interface FetchTickersListener{
        void onSuccess(ArrayList<Ticker.Tickers> tickers);
        void onError();

    }
}
