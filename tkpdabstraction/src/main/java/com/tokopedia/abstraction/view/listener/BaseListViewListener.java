package com.tokopedia.abstraction.view.listener;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface BaseListViewListener<T> extends CustomerView {

    void onSearchLoaded(@NonNull List<T> list, int totalItem);

    void onLoadSearchError(Throwable t);

}