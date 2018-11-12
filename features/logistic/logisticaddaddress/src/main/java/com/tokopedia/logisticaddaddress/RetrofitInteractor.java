package com.tokopedia.logisticaddaddress;

import android.content.Context;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.logisticaddaddress.model.GetPeopleAddress;

import java.util.Map;

/**
 * Created on 5/19/16.
 */
public interface RetrofitInteractor {

    void unsubscribe();

    void getPeopleAddress(Context context, Map<String, String> params, GetPeopleAddressListener listener);

    interface GetPeopleAddressListener {

        void onPreExecute(Context context, Map<String, String> params);

        void onSuccess(Context context, Map<String, String> params, GetPeopleAddress data);

        void onEmptyList(Context context, Map<String, String> params, GetPeopleAddress data);

        void onTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener);

        void onError(String message, NetworkErrorHelper.RetryClickedListener clickedListener);

        void onNullData();

        void onComplete();
    }
}
