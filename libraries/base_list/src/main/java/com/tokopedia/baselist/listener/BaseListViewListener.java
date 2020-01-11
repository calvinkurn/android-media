package com.tokopedia.baselist.listener;

import android.content.Context;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface BaseListViewListener<T> extends CustomerView {

    void renderList(@NonNull List<T> list, boolean hasNextPage);

    void renderList(@NonNull List<T> list);

    void showGetListError(Throwable throwable);

    Context getContext();

}