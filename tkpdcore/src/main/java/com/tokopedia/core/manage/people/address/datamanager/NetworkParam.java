package com.tokopedia.core.manage.people.address.datamanager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 5/19/16.
 */
public class NetworkParam {

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_ADDRESS_ID = "address_id";

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
