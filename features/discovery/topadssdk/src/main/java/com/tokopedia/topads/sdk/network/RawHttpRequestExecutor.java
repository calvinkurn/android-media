package com.tokopedia.topads.sdk.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import android.util.Log;

import com.tokopedia.topads.sdk.BuildConfig;


/**
 * @author by errysuprayogi on 3/29/17.
 */

public class RawHttpRequestExecutor extends HttpRequestExecutorTemplate {
    private static final String TAG = RawHttpRequestExecutor.class.getName();

    protected RawHttpRequestExecutor(HttpRequest httpRequest) {
        super(httpRequest);
    }

    public static RawHttpRequestExecutor newInstance(HttpRequest httpRequest) {
        return new RawHttpRequestExecutor(httpRequest);
    }

    @Override
    public String executeAsGetRequest() throws IOException {
        String encodedUrl = this.httpRequest.getEncodedUrl();
        URL requestUrl = new URL(encodedUrl);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Making a GET request to : " + encodedUrl);
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
        httpURLConnection.setRequestMethod(HttpMethod.GET.getDescription());
        Set<String> requestHeadersKeys = this.httpRequest.getHeaders().keySet();

        for (String headerKey : requestHeadersKeys) {
            httpURLConnection.addRequestProperty(headerKey, this.httpRequest.getHeaders().get(headerKey));
        }
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        if (responseReader != null) {
            StringBuilder httpResponse = new StringBuilder();
            String readLine = null;
            while ((readLine = responseReader.readLine()) != null) {
                httpResponse.append(readLine);
            }
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Response from GET request to : " + encodedUrl + " is : " + httpResponse.toString());
            }
            return httpResponse.toString();
        }
        return null;
    }

    @Override
    public String executeAsPostJsonRequest() throws IOException {
        URL requestUrl = new URL(this.httpRequest.getBaseUrl());
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Making a POST RAW request to : " + this.httpRequest.getBaseUrl());
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
        httpURLConnection.setRequestMethod(HttpMethod.POST.getDescription());
        Set<String> requestHeaders = this.httpRequest.getHeaders().keySet();

        for (String headerKey : requestHeaders) {
            httpURLConnection.addRequestProperty(headerKey, this.httpRequest.getHeaders().get(headerKey));
        }
        OutputStreamWriter streamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
        streamWriter.write(this.httpRequest.getJsonBody());
        streamWriter.flush();
        streamWriter.close();
        StringBuilder stringBuilder = new StringBuilder();
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader streamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String response = null;
            while ((response = bufferedReader.readLine()) != null) {
                stringBuilder.append(response + "\n");
            }
            bufferedReader.close();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Response from POST request to : " + this.httpRequest.getEncodedUrl() + " is : " + bufferedReader.toString());
            }
            return stringBuilder.toString();
        }
        return null;

    }

    @Override
    public String executeAsPostRequest() throws IOException {
        URL requestUrl = new URL(this.httpRequest.getBaseUrl());
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Making a POST request to : " + this.httpRequest.getBaseUrl());
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
        httpURLConnection.setRequestMethod(HttpMethod.POST.getDescription());
        Set<String> requestHeaders = this.httpRequest.getHeaders().keySet();

        for (String headerKey : requestHeaders) {
            httpURLConnection.addRequestProperty(headerKey, this.httpRequest.getHeaders().get(headerKey));
        }

        String requestParameters = this.httpRequest.getEncodedParameters();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Request parameters : " + requestParameters);
        }
        httpURLConnection.setDoOutput(true);
        DataOutputStream writer = new DataOutputStream(httpURLConnection.getOutputStream());
        writer.writeBytes(requestParameters);
        writer.flush();
        writer.close();

        BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        if (responseReader != null) {
            StringBuilder httpResponse = new StringBuilder();
            String readLine = null;
            while ((readLine = responseReader.readLine()) != null) {
                httpResponse.append(readLine);
            }
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Response from POST request to : " + this.httpRequest.getEncodedUrl() + " is : " + httpResponse.toString());
            }
            return httpResponse.toString();
        }
        return null;
    }

}
