package com.tokopedia.oms.data.source;


import com.tokopedia.url.TokopediaUrl;

public class OmsUrl {

    //BaseUrl
    public static String OMS_DOMAIN = TokopediaUrl.Companion.getInstance().getOMSCART();

    public interface HelperUrl {

        String OMS_VERIFY = "/v1/api/expresscart/verify";
        String OMS_CHECKOUT = "/v1/api/expresscart/checkout";
    }
}
