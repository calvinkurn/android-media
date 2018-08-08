package com.tkpd.library.utils.network;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

/**
 * Created by normansyahputa on 8/31/16.
 */

public abstract class BaseNetworkController {
    protected Context context;
    protected Gson gson;

    public BaseNetworkController(Context context, Gson gson){
        this.context = context;
        this.gson = gson;
    }

    public static void onResponseError(int code, final CommonListener commonListener) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {
                commonListener.onError(new MessageErrorException("unknown"));
            }

            @Override
            public void onTimeout() {
                commonListener.onError(new MessageErrorException("timeout"));
            }

            @Override
            public void onServerError() {
                commonListener.onError(new MessageErrorException("server_error"));
            }

            @Override
            public void onBadRequest() {
                commonListener.onError(new MessageErrorException("bad_request"));
            }

            @Override
            public void onForbidden() {
                commonListener.onError(new MessageErrorException("forbidden"));
            }
        }, code);
    }
}
