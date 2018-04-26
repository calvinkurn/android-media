package com.tokopedia.topads.dashboard.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.dashboard.constant.TopAdsAddingOption.HEADLINE_OPT;
import static com.tokopedia.topads.dashboard.constant.TopAdsAddingOption.KEYWORDS_OPT;
import static com.tokopedia.topads.dashboard.constant.TopAdsAddingOption.PRODUCT_OPT;
import static com.tokopedia.topads.dashboard.constant.TopAdsAddingOption.SHOP_OPT;

/**
 * Created by hadi.putra on 26/04/18.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PRODUCT_OPT, SHOP_OPT, HEADLINE_OPT, KEYWORDS_OPT})
public @interface TopAdsAddingOption {
    int PRODUCT_OPT = 1;
    int SHOP_OPT = 2;
    int HEADLINE_OPT = 3;
    int KEYWORDS_OPT = 4;
}
