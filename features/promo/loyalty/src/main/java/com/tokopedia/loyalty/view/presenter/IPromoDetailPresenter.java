package com.tokopedia.loyalty.view.presenter;


import android.content.res.Resources;

/**
 * @author Aghny A. Putra on 05/04/18.
 */

public interface IPromoDetailPresenter {

    void getPromoDetail(String slug);

    void cachePromoCodeData(String promoData, Resources resources);

}
