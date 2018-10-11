package com.tokopedia.logisticaddaddress.network;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;

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
