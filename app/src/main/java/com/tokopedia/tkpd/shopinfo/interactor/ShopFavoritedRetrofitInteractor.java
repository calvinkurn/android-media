package com.tokopedia.tkpd.shopinfo.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.shopinfo.models.shopfavoritedmodel.ShopFavoritedResponse;

import java.util.Map;

/**
 * Created by Alifa on 10/5/2016.
 */
public interface ShopFavoritedRetrofitInteractor {
    void getFavoritees(@NonNull Context context, @NonNull TKPDMapParam<String, String> params,
                           @NonNull ShopFavoritedListener listener);

    void unsubscribe();

    void setRequesting(boolean isRequesting);

    boolean isRequesting();

    interface ShopFavoritedListener {

        void onSuccess(@NonNull ShopFavoritedResponse data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
