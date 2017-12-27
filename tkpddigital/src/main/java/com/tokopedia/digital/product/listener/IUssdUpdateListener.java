package com.tokopedia.digital.product.listener;

/**
 * Created by ashwanityagi on 28/06/17.
 */

public interface IUssdUpdateListener {
    void onReceivedUssdData(String result);
    void onUssdDataError(String errorMessage);
}