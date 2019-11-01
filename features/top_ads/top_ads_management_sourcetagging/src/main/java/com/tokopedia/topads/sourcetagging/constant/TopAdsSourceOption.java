package com.tokopedia.topads.sourcetagging.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.APPLINK;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.MA_MANAGE_LIST_PRODUCT;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.MA_PDP;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_ADD_PRODUCT;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_MANAGE_DASHBOARD_PRODUCT;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_MANAGE_GROUP;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_MANAGE_KEYWORD_NEGATIVE;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_MANAGE_KEYWORD_POSITIVE;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_MANAGE_LIST_PRODUCT;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_MANAGE_SHOP;
import static com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption.SA_PDP;

/**
 * Created by hadi.putra on 16/04/18.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({SA_MANAGE_SHOP, SA_MANAGE_GROUP, SA_MANAGE_DASHBOARD_PRODUCT, SA_MANAGE_LIST_PRODUCT, SA_ADD_PRODUCT,
        SA_MANAGE_KEYWORD_POSITIVE, SA_MANAGE_KEYWORD_NEGATIVE, SA_PDP, MA_PDP, MA_MANAGE_LIST_PRODUCT, APPLINK})
public @interface TopAdsSourceOption {
    String SA_MANAGE_SHOP = "sa_manage_shop";
    String SA_MANAGE_GROUP = "sa_manage_group";
    String SA_MANAGE_DASHBOARD_PRODUCT = "sa_dashboard_manage_product";
    String SA_MANAGE_LIST_PRODUCT = "sa_listproduct_manage_product";
    String SA_ADD_PRODUCT = "sa_addproduct_manage_product";
    String SA_MANAGE_KEYWORD_POSITIVE = "sa_manage_keyword_positive";
    String SA_MANAGE_KEYWORD_NEGATIVE = "sa_manage_keyword_negative";
    String SA_PDP = "sa_pdp";
    String MA_PDP = "ma_pdp";
    String MA_MANAGE_LIST_PRODUCT = "ma_listproduct_manage_product";
    String APPLINK = "applink";
}
