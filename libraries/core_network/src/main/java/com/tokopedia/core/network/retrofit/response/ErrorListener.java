package com.tokopedia.core.network.retrofit.response;

/**
 * Created by Angga.Prasetiyo on 01/12/2015.
 */

@Deprecated
public interface ErrorListener {

    void onUnknown();

    void onTimeout();

    void onServerError();

    void onBadRequest();

    void onForbidden();

}
