package com.tokopedia.core.network.v4;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.tkpd.library.kirisame.network.entity.VolleyNetwork;
import com.tkpd.library.kirisame.network.util.VolleyNetworkRequestQueue;

@Deprecated
public abstract class VolleyNetworkJSON extends VolleyNetwork {

    protected Map<String, Object> param = new HashMap<>();
    private int method = 0;

    public VolleyNetworkJSON(Context context, String url) {
        super(context, url);
    }

    public void addParamJSON(String key, JSONObject value) {
        this.param.put(key, value);
    }

    public void setMethodJSON(int method) {
        this.method = method;
    }

    private Listener<JSONObject> onRequestListener() {
        return new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyNetworkJSON.this.onRequestResponse(response.toString());
            }
        };
    }

    private ErrorListener onRequestErrorListener() {
        return new ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    VolleyNetworkJSON.this.onRequestError(VolleyNetworkJSON.this.onResponseRequestError(volleyError), volleyError.networkResponse.statusCode);
                } catch (Exception var3) {
                    volleyError.printStackTrace();
                    VolleyNetworkJSON.this.onRequestError(VolleyNetworkJSON.this.onResponseRequestError(volleyError), 0);
                }

            }
        };
    }

    @Override
    public void commit() {
        JSONPOSTRequest request = new JSONPOSTRequest(method, url, getParam(), this.onRequestListener(), this.onRequestErrorListener());
        request.setHeader(this.header);
        request.setParam(getParam());
        request.setRetryPolicy(this.getRetryPolicy());
        VolleyNetworkRequestQueue.getInstance(this.context).addToRequestQueue(request);
    }

    private JSONObject getParam() {
        return new JSONObject(param);
    }

    public class JSONPOSTRequest extends JsonObjectRequest {
        JSONObject param = new JSONObject();
        Map<String, String> header = new HashMap();

        public JSONPOSTRequest(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        public void setParam(JSONObject param) {
            this.param = param;
        }

        public void setHeader(Map<String, String> header) {
            this.header = header;
        }

        public Map<String, String> getHeaders() throws AuthFailureError {
            return this.header;
        }

        public String getBodyContentType() {
            return null;
        }
    }
}