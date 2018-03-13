package com.tokopedia.topads.sdk.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;


/**
 * @author by errysuprayogi on 3/29/17.
 */

public abstract class HttpRequestExecutorTemplate implements HttpRequestExecutor {
    protected HttpRequest httpRequest;

    protected HttpRequestExecutorTemplate(HttpRequest httpRequest) {
        super();
        this.httpRequest = httpRequest;
    }

    public final String makeRequest() throws MalformedURLException, UnsupportedEncodingException, IOException {
        String response = null;
        switch (this.httpRequest.getMethod()) {
            case GET:
                response = executeAsGetRequest();
                break;
            case POST:
                response = executeAsPostRequest();
                break;
            case POST_RAW:
                response = executeAsPostJsonRequest();
                break;
        }

        return response;
    }

}
