package com.tokopedia.topads.common.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.common.constant.TopAdsAddingOption.GROUP_OPT;
import static com.tokopedia.topads.common.constant.TopAdsAddingOption.HEADLINE_OPT;
import static com.tokopedia.topads.common.constant.TopAdsAddingOption.KEYWORDS_OPT;
import static com.tokopedia.topads.common.constant.TopAdsAddingOption.PRODUCT_OPT;
import static com.tokopedia.topads.common.constant.TopAdsAddingOption.SHOP_OPT;

/**
 * Created by hadi.putra on 26/04/18.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({SHOP_OPT, GROUP_OPT, PRODUCT_OPT, HEADLINE_OPT, KEYWORDS_OPT})
public @interface TopAdsAddingOption {
    int SHOP_OPT = 1;
    int GROUP_OPT = 2;
    int PRODUCT_OPT = 3;
    int HEADLINE_OPT = 4;
    int KEYWORDS_OPT = 5;

}
