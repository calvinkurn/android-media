package com.tokopedia.loyalty.view.analytics;

public interface IPromoDetailAnalytics {

    void userViewPromo(String promoName, String id, String page, String position, String creative,
                       String creativeUrl, String promoCode);

    void userClickCopyIcon(String promoName);

    void userClickCta(String promoName, String id, String page, String position, String creative,
                      String creativeUrl, String promoCode);

    void userSharePromo(String socialMediaName);

    void userClickTooltip();

    void userCloseTooltip();

}
