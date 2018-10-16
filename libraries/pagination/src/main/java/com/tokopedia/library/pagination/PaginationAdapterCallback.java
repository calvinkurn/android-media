package com.tokopedia.library.pagination;

import android.support.annotation.Nullable;

public interface PaginationAdapterCallback {
    void onRetryPageLoad(int pageNumber);

    void onEmptyList(Object rawObject);

    void onStartFirstPageLoad();

    void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject);

    void onStartPageLoad(int pageNumber);

    void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject);

    void onError(int pageNumber);
}
