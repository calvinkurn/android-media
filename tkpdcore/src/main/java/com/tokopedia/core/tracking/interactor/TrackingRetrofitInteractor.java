package com.tokopedia.core.tracking.interactor;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.tracking.model.tracking.TrackingResponse;

/**
 * Created by Alifa on 10/12/2016.
 */

public interface TrackingRetrofitInteractor {

    void getDataTracking(@NonNull Context context, @NonNull TKPDMapParam<String, String> params,
                           @NonNull TrackingListener listener);
    void unsubscribe();

    void setRequesting(boolean isRequesting);

    boolean isRequesting();

    interface TrackingListener {

        void onSuccess(@NonNull TrackingResponse data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
