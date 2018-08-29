package com.tokopedia.transactiondata.utils;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;

/**
 * @author anggaprasetiyo on 16/05/18.
 */
public class CartApiRequestParamGenerator {
    private final String PARAM_KEY_LANG = "lang";

    private final String PARAM_VALUE_LANG_ID = "id";


    public TKPDMapParam<String, String> generateParamMapGetCartList() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_KEY_LANG, PARAM_VALUE_LANG_ID);
        return param;
    }
}
