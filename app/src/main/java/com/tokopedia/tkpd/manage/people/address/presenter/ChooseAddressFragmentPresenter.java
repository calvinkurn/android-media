package com.tokopedia.tkpd.manage.people.address.presenter;

import com.tokopedia.tkpd.customadapter.RetryDataBinder;

/**
 * Created by Alifa on 10/11/2016.
 */

public interface ChooseAddressFragmentPresenter {
    void onRefresh(String q);

    void setCache(String q);

    void loadMore(int lastItemPosition, int visibleItem, String q);

    RetryDataBinder.OnRetryListener onRetry(String q);

    void onDestroyView();
}
