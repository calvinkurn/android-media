package com.tokopedia.core.home.presenter;

import com.tokopedia.core.home.facade.FacadePromo;
import com.tokopedia.core.home.model.Ticker;

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
