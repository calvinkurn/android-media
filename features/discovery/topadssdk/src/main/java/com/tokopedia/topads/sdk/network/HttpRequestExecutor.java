package com.tokopedia.topads.sdk.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;


/**
 * @author by errysuprayogi on 3/29/17.
 */

public interface HttpRequestExecutor {
 
	abstract String executeAsGetRequest() throws MalformedURLException, UnsupportedEncodingException,IOException;
	abstract String executeAsPostRequest() throws MalformedURLException, UnsupportedEncodingException,IOException;
	abstract String executeAsPostJsonRequest() throws MalformedURLException, UnsupportedEncodingException, IOException;
}
