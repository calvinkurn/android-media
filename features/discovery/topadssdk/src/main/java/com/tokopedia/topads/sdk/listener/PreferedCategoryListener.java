package com.tokopedia.topads.sdk.listener;

/**
 * @author by errysuprayogi on 4/21/17.
 */

public interface PreferedCategoryListener {

    void onSuccessLoadPrefered(int randomCategoryId);

    void onErrorLoadPrefered();

}
