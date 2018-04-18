package com.tokopedia.topads.common.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.common.constant.TopAdsSourceOption.APPLINK;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.MA_MANAGE_SHOP;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.MA_PDP;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.SA_MANAGE_GROUP;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.SA_MANAGE_KEYWORD_NEGATIVE;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.SA_MANAGE_KEYWORD_POSITIVE;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.SA_MANAGE_PRODUCT;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.SA_MANAGE_SHOP;
import static com.tokopedia.topads.common.constant.TopAdsSourceOption.SA_PDP;

/**
 * Created by hadi.putra on 16/04/18.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({SA_MANAGE_SHOP, SA_MANAGE_GROUP, SA_MANAGE_PRODUCT, SA_MANAGE_KEYWORD_POSITIVE, SA_MANAGE_KEYWORD_NEGATIVE,
SA_PDP, MA_PDP, MA_MANAGE_SHOP, APPLINK})
public @interface TopAdsSourceOption {
    String SA_MANAGE_SHOP = "sa_manage_shop";
    String SA_MANAGE_GROUP = "sa_manage_group";
    String SA_MANAGE_PRODUCT = "sa_manage_product";
    String SA_MANAGE_KEYWORD_POSITIVE = "sa_manage_keyword_positive";
    String SA_MANAGE_KEYWORD_NEGATIVE = "sa_manage_keyword_negative";
    String SA_PDP = "sa_pdp";
    String MA_PDP = "ma_pdp";
    String MA_MANAGE_SHOP = "ma_manage_shop";
    String APPLINK = "applink";
}
