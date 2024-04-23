package com.tokopedia.topads.sdk.common.adapter;

/**
 * @author errysuprayogi on 1/24/17.
 */

public interface Item<T> {

    int type(T typeFactory);

    int originalPos();
}
