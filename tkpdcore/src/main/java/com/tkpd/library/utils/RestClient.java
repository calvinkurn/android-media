package com.tkpd.library.utils;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;

@Deprecated
public class RestClient {
    //EncoderDecoder encoder = new EncoderDecoder();
    JSONObject json = new JSONObject();
    JSONObject json_data = new JSONObject();
    private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;
    //TokenHandler token = new TokenHandler();

    private String url;
    private int responseCode;
    private String message;
    private MultipartEntity reqEntity;

    private String response;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RestClient(String url, int method)
    {
        this.url = url;
        if (method == 1) {
            reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        }else {
            params = new ArrayList<NameValuePair>();
            headers = new ArrayList<NameValuePair>();
        }
    }

    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void addEntityPart(String name, String value) throws UnsupportedEncodingException {
        reqEntity.addPart(name, new StringBody(value));
    }

    public void addEntityByte(String name,  ByteArrayBody bab) {
        reqEntity.addPart(name, bab);
    }

    public int ExecuteMultipart() {
        int status = 0;
        HttpPost request = new HttpPost(url);
        request.setEntity(reqEntity);
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
        request.setHeader("Connection", "Keep-Alive");
        status =  executeRequest(request, url);
        return status;
    }

    public Integer Execute(String method) throws Exception {
        Integer status = 0;
        if(method == "GET") {
            //add parameters
            String combinedParams = "";
            if(!params.isEmpty()){
                combinedParams += "?";
                for(NameValuePair p : params)
                {
                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                    if(combinedParams.length() > 1)
                    {
                        combinedParams  +=  "&" + paramString;
                    }
                    else
                    {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request = new HttpGet(url + combinedParams);

            //add headers
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request, url);
        } else if (method == "POST") {
            HttpPost request = new HttpPost(url);

            //add headers
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if(!params.isEmpty()){
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }

            status =  executeRequest(request, url);
        }
        return status;
    }

    private Integer executeRequest(HttpUriRequest request, String url)
    {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
        HttpConnectionParams.setSoTimeout(httpParameters, 15000);
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                Log.d("MNORMANSYAH",RestClient.class.getSimpleName()+" : "+response);

                // Closing the input stream will trigger connection release
                instream.close();
            }
        } catch (SocketTimeoutException e) {
            client.getConnectionManager().shutdown();
            return 0;
        } catch (ConnectTimeoutException e) {
            client.getConnectionManager().shutdown();
            return 0;
        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
