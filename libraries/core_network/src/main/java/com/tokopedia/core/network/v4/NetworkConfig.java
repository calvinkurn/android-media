package com.tokopedia.core.network.v4;

/**
 * Created by m.normansyah on 03/11/2015.
 */

@Deprecated
public interface NetworkConfig {

    int GET = 0;
    int POST = 1;

    String key = "web_service_v4";

    int FORBIDDEN_NETWORK_ERROR= 403;
    int BAD_REQUEST_NETWORK_ERROR = 400;
    int INTERNAL_SERVER_ERROR = 500;
}
