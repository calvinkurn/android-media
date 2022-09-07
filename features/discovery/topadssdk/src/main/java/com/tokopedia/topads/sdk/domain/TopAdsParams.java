package com.tokopedia.topads.sdk.domain;

import com.tokopedia.topads.sdk.base.TKPDMapParam;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsParams {

    public static final String KEY_EP = "ep";
    public static final String KEY_DEVICE = "device";
    public static final String KEY_ITEM = "item";
    public static final String KEY_HEADLINE_PRODUCT_COUNT = "headline_product_count";
    public static final String KEY_WITH_TEMPLATE = "with_template";
    public static final String KEY_SRC = "src";
    public static final String KEY_TEMPLATE_ID = "template_id";
    public static final String KEY_PAGE = "page";
    public static final String KEY_DEPARTEMENT_ID = "dep_id";

    public static final String DEFAULT_KEY_ITEM = "2";
    public static final String DEFAULT_KEY_EP = "product";
    public static final String DEFAULT_KEY_DEVICE = "android";
    public static final String DEFAULT_KEY_PAGE = "1";
    public static final String DEFAULT_KEY_SRC = "search";


    private final TKPDMapParam<String, String> param;


    public TopAdsParams() {
        param = new TKPDMapParam<>();
        param.put(KEY_ITEM, DEFAULT_KEY_ITEM);
        param.put(KEY_DEVICE, DEFAULT_KEY_DEVICE);
        param.put(KEY_PAGE, DEFAULT_KEY_PAGE);
    }

    public TKPDMapParam<String, String> getParam() {
        return param;
    }

}
