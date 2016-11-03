package com.tokopedia.tkpd.people.retrofit;

import android.content.Context;

import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.people.model.InputOutputData;
import com.tokopedia.tkpd.people.model.PeopleInfoData;

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
