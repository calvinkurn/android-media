package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain;

import android.content.Context;
import android.support.annotation.NonNull;


import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.ActResult;

import java.util.Map;

/**
 * Created by Nisie on 2/9/16.
 */
public interface ActReputationRetrofitInteractor {


    void postComment(@NonNull Context context,
                     @NonNull Map<String, String> params,
                     @NonNull ActReputationListener listener);

    void deleteComment(@NonNull Context context,
                       @NonNull Map<String, String> params,
                       @NonNull ActReputationListener listener);

    void postReport(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull ActReputationListener listener);

    void likeDislikeReview(@NonNull Context context,
                           @NonNull Map<String, String> params,
                           @NonNull ActReputationListener listener);

    boolean isUnsubscribed();

    void unSubscribeObservable();

    interface ActReputationListener {
        void onSuccess(ActResult result);

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }

}
