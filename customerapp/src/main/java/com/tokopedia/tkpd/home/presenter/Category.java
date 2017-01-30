package com.tokopedia.tkpd.home.presenter;

import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.home.facade.FacadePromo;

/**
 * Created by m.normansyah on 2/24/16.
 * Edited by Hafizh H 30/01/2017 for Official Store
 */
public interface Category {
    String TAG = "MNORMANSYAH";
    String messageTAG = "CategoryApi ";

    void subscribe();
    void unSubscribe();
    void fetchBanners(final FacadePromo.GetPromoListener listener);
    void fetchSlides(final FacadePromo.GetPromoListener listener);
    void fetchTickers(final FetchTickersListener listener);
    void fetchBrands(final OnGetBrandsListener listener);

    interface FetchTickersListener{
        void onSuccess(Ticker.Tickers[] tickers);
        void onError();

    }

    interface OnGetBrandsListener{

    }
}
