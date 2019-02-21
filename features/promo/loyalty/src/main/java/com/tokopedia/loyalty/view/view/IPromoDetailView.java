package com.tokopedia.loyalty.view.view;

import com.tokopedia.loyalty.view.data.PromoData;

/**
 * @author Aghny A. Putra on 4/4/18
 */

public interface IPromoDetailView extends IMvpView {

    void renderPromoDetail(PromoData promoData);

    void renderErrorShowingPromoDetail(String message);

    void renderErrorNoConnectionGetPromoDetail(String message);

    void renderErrorTimeoutConnectionGetPromoDetail(String message);

    void renderErrorHttpGetPromoDetail(String message);

    void stopPerformanceMonitoring();
}
