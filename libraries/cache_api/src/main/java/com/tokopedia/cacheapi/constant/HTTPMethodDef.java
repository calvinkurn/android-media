package com.tokopedia.cacheapi.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_DELETE;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_GET;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_HEAD;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_OPTIONS;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_PATCH;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_POST;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_PUT;

/**
 * @author normansyahputa on 4/25/17.
 */
@StringDef({TYPE_GET, TYPE_POST, TYPE_PUT, TYPE_DELETE, TYPE_HEAD, TYPE_OPTIONS, TYPE_PATCH})
public @interface HTTPMethodDef {
    String TYPE_GET = "GET";
    String TYPE_POST = "POST";
    String TYPE_PUT = "PUT";
    String TYPE_DELETE = "DELETE";
    String TYPE_HEAD = "HEAD";
    String TYPE_OPTIONS = "OPTIONS";
    String TYPE_PATCH = "PATCH";
}
