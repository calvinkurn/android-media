package com.tokopedia.core.manage.people.address.presenter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.manage.people.address.model.GetPeopleAddress;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created on 5/18/16.
 */
public interface ManagePeopleAddressFragmentPresenter {
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

    void setActionDefaultButtonClicked(AddressModel data);

    void setOnStartActionSetDefaultAddress(String addressID);

    void setActionDeleteButton(AddressModel data);

    void setOnStartActionDeleteAddress(String addressId);

    void setActionEditButton(AddressModel data);

    void setBeforeLoadMoreData();

    void setOnSuccessLoadMoreData(GetPeopleAddress data);

    void setOnActionReceiveResult(Context context, int resultCode, Bundle resultData);
}
