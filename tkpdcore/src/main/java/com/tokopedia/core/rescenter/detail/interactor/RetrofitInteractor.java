package com.tokopedia.core.rescenter.detail.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.rescenter.detail.model.detailresponsedata.ResCenterKurir;
import com.tokopedia.core.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;
import com.tokopedia.core.rescenter.detail.model.detailresponsedata.DetailResCenterData;

import java.util.Map;

/**
 * Created by hangnadi on 2/9/16.
 */
public interface RetrofitInteractor {

    void getResCenterDetail(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull ResCenterDetailListener listener);

    void trackShipping(@NonNull Context context, @NonNull Map<String, String> params,
                       @NonNull TrackShippingListener listener);

    void getKurirList(@NonNull Context context,
                      @NonNull RetrofitInteractorImpl.GetKurirListListener listener);

    void editAddress(@NonNull Context context,
                     @NonNull Map<String, String> params,
                     @NonNull OnPostAddressListener listener);

    interface ResCenterDetailListener {

        void onSuccess(@NonNull DetailResCenterData data);

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String message);

        void onNullData();
    }

    interface TrackShippingListener {

        void onSuccess(ResCenterTrackShipping resCenterTrackShipping);

        void onTimeOut(String message, NetworkErrorHelper.RetryClickedListener listener);

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String message);

        void onNullData();
    }

    interface GetKurirListListener {

        void onSuccess(ResCenterKurir resCenterKurirList);

        void onTimeOut(String message, NetworkErrorHelper.RetryClickedListener listener);

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String message);

        void onNullData();
    }

    void unsubscribe();

    void postAddress(@NonNull Context context, @NonNull Map<String, String> params,
                     @NonNull OnPostAddressListener listener);

    interface OnPostAddressListener {
        void onStart();

        void onSuccess();

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onFailAuth();

        void onError(String message);

    }
}
