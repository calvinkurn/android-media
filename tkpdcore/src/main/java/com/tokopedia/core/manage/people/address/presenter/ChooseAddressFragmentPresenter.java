package com.tokopedia.core.manage.people.address.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.manage.people.address.model.Destination;

/**
 * Created by Alifa on 10/11/2016.
 */

public interface ChooseAddressFragmentPresenter {
    void onRefresh(String q);

    void setCache(String q);

    void loadMore(int lastItemPosition, int visibleItem, String q);

    RetryDataBinder.OnRetryListener onRetry(String q);

    void onDestroyView();

    void setOnAddAddressClick(Context context);

    void onSuccessCreateAddress(Context context);

    void onSuccessEditAddress(Context context);

    void setOnEditAddressClick(Context context, Destination destination);

    void setOnChooseAddressClick(Context context, Destination destination);
}
