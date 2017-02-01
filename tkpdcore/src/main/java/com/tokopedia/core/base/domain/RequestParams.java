package com.tokopedia.core.base.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author kulomady on 12/24/16.
 */
public final class RequestParams {
    public static final RequestParams EMPTY = RequestParams.create();

    private final TKPDMapParam<String, Object> parameters = new TKPDMapParam<>();

    private RequestParams() {
    }

    public static RequestParams create() {
        return new RequestParams();
    }

    public void putInt(String key, int value) {
        parameters.put(key, value);
    }

    public void putString(String key, String value) {
        parameters.put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        parameters.put(key, value);
    }

    public int getInt(String key, int defaultValue) {
        final Object object = parameters.get(key);
        if (object == null) {
            return defaultValue;
        }
        try {
            return (int) object;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue) {
        final Object object = parameters.get(key);
        if (object == null) {
            return defaultValue;
        }
        try {
            return (String) object;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        final Object object = parameters.get(key);
        if (object == null) {
            return defaultValue;
        }
        try {
            return (Boolean) object;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void clearValue(String key) {
        parameters.remove(key);
    }

    public TKPDMapParam<String, Object> getParameters() {
        return parameters;
    }
}
