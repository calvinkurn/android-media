package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.base.adapter.Item;

import java.util.List;

/**
 * @author by errysuprayogi on 4/4/17.
 */

public interface TopAdsListener {

    void onTopAdsLoaded(List<Item> list);

    void onTopAdsFailToLoad(int errorCode, String message);

}
