package com.tokopedia.tkpd;

import android.content.Context;

import okhttp3.mockwebserver.MockResponse;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by normansyahputa on 4/6/18.
 */

public class BaseRetrofitJsonFactory extends BaseJsonFactory {
    public BaseRetrofitJsonFactory(Context context) {
        super(context);
    }

    public MockResponse createSuccess200RawResponse(String response) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(response);
        return mockResponse;
    }

    public MockResponse createSuccess200Response(String response) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(convertFromAndroidResource(response));
        return mockResponse;
    }

    public MockResponse create200DelayedResponse(String response) {
        MockResponse mockResponse = new MockResponse()
                .setBodyDelay(3, SECONDS)
                .setResponseCode(200)
                .setBody(convertFromAndroidResource(response));
        return mockResponse;
    }

    public MockResponse create404Forbidden(String response) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(404)
                .setBody(convertFromAndroidResource(response));
        return mockResponse;
    }

    public MockResponse create403Forbidden(String response) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(403)
                .setBody(convertFromAndroidResource(response));
        return mockResponse;
    }
}
