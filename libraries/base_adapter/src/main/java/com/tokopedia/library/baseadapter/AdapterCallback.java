package com.tokopedia.library.baseadapter;

import androidx.annotation.Nullable;

public interface AdapterCallback {

    /**
     * Trigger when user click on retry button,
     *
     * @param pageNumber
     */
    void onRetryPageLoad(int pageNumber);

    /**
     * Trigger if recyclerview has zero element, This method will be useful for showing empty page UI
     *
     * @param rawObject
     */
    void onEmptyList(Object rawObject);

    /**
     * Trigger once in recyclerview lifecycle when it try to load first page.
     */
    void onStartFirstPageLoad();

    /**
     * Trigger once in recyclerview lifecycle when first page loaded successfully
     *
     * @param itemCount
     * @param rawObject
     */
    void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject);

    /**
     * Trigger when new page going to start load data(exclude for first page)
     *
     * @param pageNumber
     */
    void onStartPageLoad(int pageNumber);

    /**
     * Trigger when any page successfully loaded(exclude for first page)
     *
     * @param itemCount
     * @param pageNumber
     * @param rawObject
     */
    void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject);

    /**
     * Trigger in case of any error while page loading.
     *
     * @param pageNumber
     */
    void onError(int pageNumber);
}
