package com.tokopedia.topads.dashboard.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.HIGHEST_DAILY_BUDGET;
import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.HIGHEST_MAX_PRICE;
import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.LATEST;
import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.LEAST_CLICKED;
import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.LEAST_SHOWN;
import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.MOST_CLICKED;
import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.MOST_SHOWN;
import static com.tokopedia.topads.dashboard.constant.SortTopAdsOption.MOST_USED;

/**
 * Created by nakama on 10/04/18.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({LATEST, MOST_USED, MOST_SHOWN, LEAST_SHOWN, MOST_CLICKED, LEAST_CLICKED, HIGHEST_MAX_PRICE, HIGHEST_DAILY_BUDGET})
public @interface SortTopAdsOption {
    String LATEST = "1";
    String MOST_USED = "2";
    String MOST_SHOWN = "3";
    String LEAST_SHOWN = "4";
    String MOST_CLICKED = "5";
    String LEAST_CLICKED = "6";
    String HIGHEST_MAX_PRICE = "7";
    String HIGHEST_DAILY_BUDGET = "8";
}
