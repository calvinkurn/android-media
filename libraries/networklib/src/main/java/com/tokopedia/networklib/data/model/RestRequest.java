package com.tokopedia.networklib.data.model;

import java.lang.reflect.Type;
import java.util.Map;

public class RestRequest {
    /*transient by nature hence it will not be part of request body*/
    private Type typeOfT; /*Mandatory parameter*/
    private String url; /*Mandatory parameter*/
    private Map<String, Object> headers;
    private Map<String, Object> queryParams;
    private RequestType requestType;
    private Object body;

    private RestRequest(Builder builder) {
        this.typeOfT = builder.typeOfT;
        this.url = builder.baseUrl;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.requestType = builder.requestType;
        this.body = builder.body;
    }

    public Type getTypeOfT() {
        return typeOfT;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getUrl() {
        return url;
    }

    public Object getBody() {
        return body;
    }

    //Builder class
    public static class Builder {
        private Type typeOfT; /*Mandatory parameter*/
        private String baseUrl; /*Mandatory parameter*/

        //Optional parameter/
        private Map<String, Object> headers;
        private Map<String, Object> queryParams;
        private RequestType requestType;
        private Object body;

        public Builder(Type classOfT, String url) {
            this.typeOfT = classOfT;
            this.baseUrl = url;
        }

        public Builder setHeaders(Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setQueryParams(Map<String, Object> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder setRequestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder setBody(Object body) {
            this.body = body;
            return this;
        }

        public RestRequest build() {
            return new RestRequest(this);
        }
    }

    //Do not rewrite on remove it
    @Override
    public String toString() {
        return "RestRequest{" +
                "typeOfT=" + typeOfT +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", queryParams=" + queryParams +
                ", requestType=" + requestType +
                ", body=" + body +
                '}';
    }
}
