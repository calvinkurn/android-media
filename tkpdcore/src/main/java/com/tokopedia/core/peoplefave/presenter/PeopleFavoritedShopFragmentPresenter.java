package com.tokopedia.core.peoplefave.presenter;

import android.content.Context;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.peoplefave.model.PeopleFavoritedShopData;

/**
 * Created by hangnadi on 10/11/16.
 */
public interface PeopleFavoritedShopFragmentPresenter {
    void postUnfavoriteClick(Context context, String shopID);

    void setOnFirstTimeLaunch(Context context);

    void setNextPageStatus(PeopleFavoritedShopData data);

    void setOnTimeOutConnection(NetworkErrorHelper.RetryClickedListener listener);

    void renderErrorMessage(String message);

    void setOnRefreshing(Context context);

    void getNextPage(Context context, int lastItemPosition, int visibleItem);

    void setOnDestroyView(Context context);
}
