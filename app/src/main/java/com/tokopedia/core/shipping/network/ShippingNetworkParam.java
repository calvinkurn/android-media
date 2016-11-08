package com.tokopedia.core.shipping.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kris on 2/23/2016.
 */
public class ShippingNetworkParam {

    private static final String SHOP_DISTRICT = "district_id";

    public static Map<String, String> paramShopShipping(String districtId){
        Map<String, String> params = new HashMap<>();
        params.put(SHOP_DISTRICT, districtId);
        return params;
    }

}
