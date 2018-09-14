package com.tokopedia.oms.data.source;


public class OmsUrl {

    //BaseUrl
    public static String OMS_DOMAIN = "https://omscart.tokopedia.com/";

    public interface HelperUrl {

        String OMS_VERIFY = "/v1/api/expresscart/verify";
        String OMS_CHECKOUT = "/v1/api/expresscart/checkout";
    }
}
