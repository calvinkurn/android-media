package com.tkpd.library.utils.network;

import android.content.Context;

import com.google.gson.Gson;

/**
 * Created by normansyahputa on 8/31/16.
 */
@Deprecated
public abstract class BaseNetworkController {
    protected Context context;
    protected Gson gson;

    public BaseNetworkController(Context context, Gson gson){
        this.context = context;
        this.gson = gson;
    }
}
