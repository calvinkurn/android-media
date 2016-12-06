package com.tokopedia.core.peoplefave.retrofit;

import android.content.Context;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.peoplefave.model.PeopleFavoritedShopData;

import java.util.Map;

/**
 * Created by hangnadi on 10/11/16.
 */
public interface PeopleFavoritedShopInteractor {

    void requestDataService(Context context, Map<String, String> params, PeopleFavoritedShopListener listener);

    void unsubscribe();

    void updateDataService(Context context, Map<String, String> param, ActionFavoritedShopListener listener);

    interface PeopleFavoritedShopListener {

        void onStart();

        void onSuccess(PeopleFavoritedShopData data);

        void onTimeout(NetworkErrorHelper.RetryClickedListener listener);

        void onError(String message);

        void onNullData();

        void onComplete();
    }

    interface ActionFavoritedShopListener {

        void onStart();

        void onSuccess();

        void onTimeOut();

        void onError(String message);

    }
}
