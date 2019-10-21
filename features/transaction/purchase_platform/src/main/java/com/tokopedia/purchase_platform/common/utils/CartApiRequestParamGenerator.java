package com.tokopedia.purchase_platform.common.utils;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 16/05/18.
 */
public class CartApiRequestParamGenerator {

    @Inject
    public CartApiRequestParamGenerator() {
    }

    private final String PARAM_KEY_LANG = "lang";

    private final String PARAM_VALUE_LANG_ID = "id";

    private final String PARAM_CART_IDS = "cart_ids";

    public TKPDMapParam<String, String> generateParamMapGetCartList(String cartId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_KEY_LANG, PARAM_VALUE_LANG_ID);
        if (cartId != null) {
            param.put(PARAM_CART_IDS, cartId);
        }
        return param;
    }
}
