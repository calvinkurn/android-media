package com.tokopedia.cacheapi.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_GET;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_PATCH;
import static com.tokopedia.cacheapi.constant.HTTPMethodDef.TYPE_POST;

/**
 * @author normansyahputa on 4/25/17.
 */
@StringDef({TYPE_GET, TYPE_POST, TYPE_PATCH})
public @interface HTTPMethodDef {
    String TYPE_GET = "GET";
    String TYPE_POST = "POST";
    String TYPE_PATCH = "PATCH";
}
