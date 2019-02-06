package com.tokopedia.logisticaddaddress.utils;

import com.tokopedia.logisticdata.data.entity.address.GetAddressDataPass;
import com.tokopedia.network.utils.TKPDMapParam;

/**
 * Created on 5/19/16.
 */
public class NetworkParam {

    public static final String PARAM_ORDER_BY = "order_by";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_ADDRESS_ID = "address_id";

    public static TKPDMapParam<String, String> paramGetAddress(GetAddressDataPass pass) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PARAM_ORDER_BY, String.valueOf(pass.getSortID()));
        params.put(PARAM_QUERY, String.valueOf(pass.getQuery()));
        params.put(PARAM_PAGE, String.valueOf(pass.getPage()));
        return params;
    }

    public static TKPDMapParam<String, String> paramEditDefaultAddress(String addressId) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PARAM_ADDRESS_ID, addressId);
        return params;
    }

    public static TKPDMapParam<String, String> paramDeleteAddress(String addressId) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PARAM_ADDRESS_ID, addressId);
        return params;
    }
}
