package com.tokopedia.networklib.data.model;

/**
 * Cache types which will be provide while creating request object for GraphQl usecase
 */
public enum RequestType {
    GET, /* Represent HTTP Get */

    POST, /* Represent HTTP Post */

    PUT, /* Represent HTTP Put */

    DELETE, /* Represent HTTP Delete */

    POST_MULTIPART /* Represent Multipart request and invoke via HTTP Post */
}
