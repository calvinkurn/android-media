package com.tokopedia.core.manage.people.address.datamanager;

import com.tokopedia.core.manage.people.address.model.GetAddressDataPass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 5/19/16.
 */
public class NetworkParam {

    public static final String PARAM_ORDER_BY = "order_by";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_ADDRESS_ID = "address_id";

    public static Map<String, String> paramGetAddress(GetAddressDataPass pass) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ORDER_BY, String.valueOf(pass.getSortID()));
        params.put(PARAM_QUERY, String.valueOf(pass.getQuery()));
        params.put(PARAM_PAGE, String.valueOf(pass.getPage()));
        return params;
    }

    public static Map<String, String> paramEditDefaultAddress(String addressId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ADDRESS_ID, addressId);
        return params;
    }

    public static Map<String, String> paramDeleteAddress(String addressId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ADDRESS_ID, addressId);
        return params;
    }
}
