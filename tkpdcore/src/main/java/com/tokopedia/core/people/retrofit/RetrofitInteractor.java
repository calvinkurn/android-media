package com.tokopedia.core.people.retrofit;

import android.content.Context;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.model.InputOutputData;

import java.util.Map;

/**
 * Created on 5/31/16.
 */
public interface RetrofitInteractor {

    void unsubscribe();

    void getPeopleInfo(Context context, Map<String, String> params, GetPeopleInfoListener listener);

    interface GetPeopleInfoListener {

        void onSuccess(InputOutputData data);

        void onTimeout(NetworkErrorHelper.RetryClickedListener clickedListener);

        void onError(String message);

        void onNullData();

        void onComplete();
    }

}
