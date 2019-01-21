package com.tokopedia.core.network.v4;

import com.tkpd.library.kirisame.network.entity.OnNetworkResponseListener;

/**
 * Created by m.normansyah on 03/11/2015.
 */

public interface NetworkConfig {
    int DEVELOPMENT_HOST_TYPE = 0;
    int STAGING_HOST_TYPE = 1;
    int RELEASE_HOST_TYPE = 2;

    int GET = VolleyNetwork.METHOD_GET;
    int POST = VolleyNetwork.METHOD_POST;

    int LOGIN_UNSPECIFIED = 0;
    int LOGIN_NEEDED = 1;
    int LOGIN_BYPASS = 2;

    int PARAM_UNKNOWN_CONDITION = 0;
    int PARAM_ALL_PASSED = 1;
    int PARAM_ALL_NOT_PASSED = 2;

    String key = "web_service_v4";

    int TIMEOUT = 0;
    String TIMEOUT_TEXT = "TIMEOUT";
    String NO_CONNECTION_TEXT = "NO_CONNECTION";
    String SERVER_ERROR_TEXT = "SERVER_ERROR_TEXT";
    String AUTH_FAILURE_TEXT ="AUTH_FAILURE";
    String PARSE_ERROR_TEXT = "PARSE_ERROR";
    String UNKNOWN_TEXT = "UNKNOWN";

    int FORBIDDEN_NETWORK_ERROR= 403;
    int BAD_REQUEST_NETWORK_ERROR = 400;
    int INTERNAL_SERVER_ERROR = 500;
}
