package com.tokopedia.base.list.seller.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
@Deprecated
public interface BaseListViewListener<T> extends CustomerView {

    void onSearchLoaded(@NonNull List<T> list, int totalItem);

    void onLoadSearchError(Throwable t);

}