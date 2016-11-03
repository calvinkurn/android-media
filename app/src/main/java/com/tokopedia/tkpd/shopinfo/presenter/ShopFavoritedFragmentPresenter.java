package com.tokopedia.tkpd.shopinfo.presenter;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.tkpd.customadapter.RetryDataBinder;

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

