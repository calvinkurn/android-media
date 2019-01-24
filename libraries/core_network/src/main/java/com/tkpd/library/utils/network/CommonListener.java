package com.tkpd.library.utils.network;

/**
 * Created by normansyahputa on 1/16/17.
 * this interface used as common interface for network callback.
 * instead of defining by yourself.
 */
public interface CommonListener {
    void onError(Throwable e);
    void onFailure();
}
