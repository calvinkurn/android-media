package com.tokopedia.core.inboxreputation.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputation;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.core.inboxreputation.model.inboxreputationsingle.SingleReview;
import com.tokopedia.core.reputationproduct.model.LikeDislike;

import java.util.Map;

/**
 * Created by Nisie on 21/01/16.
 */
public interface InboxReputationRetrofitInteractor {

    void getInboxReputation(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull InboxReputationListener listener);

    interface InboxReputationListener {
        void onSuccess(@NonNull InboxReputation data);

        void onTimeout();

        void onFailAuth();

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    void getInboxReputationDetail(@NonNull Context context, @NonNull Map<String, String> params,
                                  @NonNull InboxReputationRetrofitInteractor.InboxReputationDetailListener listener);

    interface InboxReputationDetailListener {
        void onSuccess(@NonNull InboxReputationDetail data);

        void onTimeout();

        void onFailAuth();

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    void getSingleReview(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull InboxReputationRetrofitInteractor.InboxReputationSingleListener listener);

    interface InboxReputationSingleListener {
        void onSuccess(@NonNull SingleReview data);

        void onTimeout();

        void onFailAuth();

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    void getLikeDislikeReview(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull LikeDislikeListener listener);

    interface LikeDislikeListener {
        void onSuccess(@NonNull LikeDislike result);

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }

    boolean isUnsubscribed();

    void unSubscribeObservable();

    boolean isRequesting();

    void setRequesting(boolean isRequesting);


}
