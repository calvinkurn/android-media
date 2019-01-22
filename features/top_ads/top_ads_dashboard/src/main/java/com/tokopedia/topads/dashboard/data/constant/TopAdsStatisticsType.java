package com.tokopedia.topads.dashboard.data.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType.ALL_ADS;
import static com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType.HEADLINE_ADS;
import static com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType.PRODUCT_ADS;
import static com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType.SHOP_ADS;

/**
 * Created by hadi.putra on 25/04/18.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({ALL_ADS, PRODUCT_ADS, SHOP_ADS, HEADLINE_ADS})
public @interface TopAdsStatisticsType {
    int ALL_ADS = 0;
    int PRODUCT_ADS = 1;
    int SHOP_ADS = 2;
    int HEADLINE_ADS = 3;
}
