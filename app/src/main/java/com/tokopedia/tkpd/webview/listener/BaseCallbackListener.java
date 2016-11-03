package com.tokopedia.tkpd.webview.listener;

/**
 * Created by Angga.Prasetiyo on 14/09/2015.
 */
public interface BaseCallbackListener<S, P, E> {

    void onSuccessResult(S successResult);

    void onProgressResult(P progressResult);

    void onErrorResult(E errorResult);
}