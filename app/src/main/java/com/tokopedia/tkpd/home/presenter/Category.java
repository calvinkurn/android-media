package com.tokopedia.tkpd.home.presenter;

import com.tokopedia.tkpd.home.facade.FacadePromo;
import com.tokopedia.tkpd.home.model.Ticker;

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
        void onSuccess(Ticker.Tickers[] tickers);
        void onError();

    }
}
