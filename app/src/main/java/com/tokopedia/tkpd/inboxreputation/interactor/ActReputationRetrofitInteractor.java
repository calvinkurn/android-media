package com.tokopedia.tkpd.inboxreputation.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.inboxreputation.model.actresult.ActResult;
import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;

import java.util.Map;

/**
 * Created by Nisie on 2/9/16.
 */
public interface ActReputationRetrofitInteractor {

    void skipReview(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull ActReputationListener listener);

    void editReview(@NonNull Context context,
                    @NonNull ActReviewPass params,
                    @NonNull ActReputationListener listener);

    void postComment(@NonNull Context context,
                     @NonNull Map<String, String> params,
                     @NonNull ActReputationListener listener);

    void deleteComment(@NonNull Context context,
                       @NonNull Map<String, String> params,
                       @NonNull ActReputationListener listener);

    void postReputation(@NonNull Context context,
                        @NonNull Map<String, String> params,
                        @NonNull ActReputationListener listener);

    void postReview(@NonNull Context applicationContext,
                    @NonNull ActReviewPass param,
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
