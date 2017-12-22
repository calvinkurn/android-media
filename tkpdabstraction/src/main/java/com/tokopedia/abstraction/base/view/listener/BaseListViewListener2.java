package com.tokopedia.abstraction.base.view.listener;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface BaseListViewListener2<T> extends CustomerView {

    void renderList(@NonNull List<T> list);

    void renderAddList(@NonNull List<T> list);

    void showGetListError();

}