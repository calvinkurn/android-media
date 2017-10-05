package com.tokopedia.core.shopinfo.presenter;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

/**
 * Created by brilliant.oka on 26/04/17.
 */

public interface OsHomePresenter {
    void onRefresh();

    RetryDataBinder.OnRetryListener onRetry();

    void onDestroyView();
}
