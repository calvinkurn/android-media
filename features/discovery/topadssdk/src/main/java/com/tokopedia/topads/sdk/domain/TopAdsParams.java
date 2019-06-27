package com.tokopedia.topads.sdk.domain;

import com.tokopedia.topads.sdk.base.TKPDMapParam;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsParams {

    public static final String KEY_EP = "ep";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_DEVICE = "device";
    public static final String KEY_ITEM = "item";
    public static final String KEY_WITH_TEMPLATE = "with_template";
    public static final String KEY_XPARAMS = "xparams";
    public static final String KEY_PARAMS = "params";
    public static final String KEY_SRC = "src";
    public static final String KEY_TEMPLATE_ID = "template_id";
    public static final String KEY_SEARCH_NF = "search_nf";
    public static final String KEY_PAGE = "page";
    public static final String KEY_DEPARTEMENT_ID = "dep_id";
    public static final String KEY_HOTLIST_ID = "h";
    public static final String KEY_QUERY = "q";
    public static final String KEY_PMIN = "pmin";
    public static final String KEY_PMAX = "pmax";
    public static final String KEY_FSHOP = "fshop";
    public static final String KEY_FLOC = "floc";
    public static final String KEY_FCITY = "fcity";
    public static final String KEY_WHOLESALE = "wholesale";
    public static final String KEY_SHIPPING = "shipping";
    public static final String KEY_PREORDER = "preorder";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_FRERETURNS = "freereturns";
    public static final String KEY_CASHBACK = "cashback";
    public static final String KEY_VARIANT = "variant";

    public static final String SRC_DIRECTORY_VALUE = "directory";
    public static final String SRC_INTERMEDIARY_VALUE = "intermediary";
    public static final String SRC_PDP_VALUE = "pdp";

    public static final String DEFAULT_KEY_ITEM = "2";
    public static final String DEFAULT_KEY_EP = "product";
    public static final String DEFAULT_KEY_DEVICE = "android";
    public static final String DEFAULT_KEY_PAGE = "1";
    public static final String DEFAULT_KEY_SRC = "search";

    public static final String SRC_PRODUCT_FEED = "fav_product";



    private final TKPDMapParam<String, String> param;
    private int adsPosition;


    public TopAdsParams() {
        param = new TKPDMapParam<>();
        param.put(KEY_ITEM, DEFAULT_KEY_ITEM);
        param.put(KEY_DEVICE, DEFAULT_KEY_DEVICE);
        param.put(KEY_PAGE, DEFAULT_KEY_PAGE);
    }

    public TKPDMapParam<String, String> getParam() {
        return param;
    }

    public int getAdsPosition() {
        return adsPosition;
    }

    public void setAdsPosition(int adsPosition) {
        this.adsPosition = adsPosition;
    }
}
