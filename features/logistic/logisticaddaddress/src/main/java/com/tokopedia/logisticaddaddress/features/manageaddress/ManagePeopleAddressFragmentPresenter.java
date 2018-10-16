package com.tokopedia.logisticaddaddress.features.manageaddress;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;


import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticaddaddress.adapter.AddressViewHolder;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;

/**
 * Created on 5/18/16.
 */
public interface ManagePeopleAddressFragmentPresenter extends AddressViewHolder.ManageAddressListener {
    void setActionOnRefreshing(@NonNull Context context);

    void resetPage();

    void setActionOnLazyLoad(@NonNull Context context);

    void setActionOnLaunchFirstTime(@NonNull Context context);

    boolean isAllowConnection();

    void setAllowConnection(boolean allowConnection);

    void setActionOnActivityKilled(@NonNull Context context);

    void setBeforeInitAddressList();

    void onErrorGetCache();

    void onSuccessGetCache(GetPeopleAddress getPeopleAddress);

    void finishRequest();

    void setOnRequestSuccess();

    void setOnResponseNull();

    void setOnRequestError(String message, NetworkErrorHelper.RetryClickedListener clickedListener);

    void setOnRequestTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener);

    void setOnSuccessInitAddressList(GetPeopleAddress data);

    void setOnEmptyAddressList(GetPeopleAddress data);

    void setOnStartActionSetDefaultAddress(String addressID);

    void setOnStartActionDeleteAddress(String addressId);

    void setBeforeLoadMoreData();

    void setOnSuccessLoadMoreData(GetPeopleAddress data);

    void setOnActionReceiveResult(Context context, int resultCode, Bundle resultData);
}
