package com.tokopedia.core.shopinfo.presenter;

import com.tokopedia.core.customadapter.RetryDataBinder;

/**
 * Created by USER on 10/5/2016.
 */
public interface ShopFavoritedFragmentPresenter  {

        void onRefresh();

        void setCache(String shopId);

        void loadMore(int lastItemPosition, int visibleItem);

        RetryDataBinder.OnRetryListener onRetry();

        void onDestroyView();
}

