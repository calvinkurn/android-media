package com.tokopedia.core.shopinfo.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalkResult;

import java.util.Map;

/**
 * Created by nisie on 11/18/16.
 */

public interface ShopTalkRetrofitInteractor {

    void getShopTalk(@NonNull Context context, @NonNull Map<String, String> params,
                     @NonNull GetShopTalkListener listener);

    void deleteTalk(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull ActionShopTalkListener listener);

    void followTalk(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull ActionShopTalkListener listener);

    void unfollowTalk(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull ActionShopTalkListener listener);

    void reportTalk(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull ActionShopTalkListener listener);

    void unSubscribeObservable();

    boolean isRequesting();

    interface GetShopTalkListener {

        void onSuccess(ShopTalkResult result);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();

        void onTimeout();

        void onFailAuth();
    }

    interface ActionShopTalkListener {

        void onSuccess();

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();

        void onTimeout();

        void onFailAuth();
    }
}
