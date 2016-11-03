package com.tokopedia.tkpd.people.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.tkpd.people.model.InputOutputData;
import com.tokopedia.tkpd.people.model.PeopleInfoData;

/**
 * Created on 5/30/16.
 */
public interface PeopleInfoFragmentPresenter {

    void setOnFirstTimeLaunch(@NonNull Context context, @NonNull String userID);

    void onActionMessageClicked(@NonNull Context context, PeopleInfoData.UserInfo userInfo);

    void onActionManageClicked(@NonNull Context context, PeopleInfoData.UserInfo userInfo);

    void onFavoritedShoplicked(@NonNull Context context, PeopleInfoData.UserInfo userInfo);

    void onShopClicked(@NonNull Context context, String shopId);

    void setOnRequestSuccess(InputOutputData data);

    void setOnRequestError();

    void setOnRequestError(String message);

    void setOnRequestTimeOut(RetryClickedListener clickedListener);

    void refreshPeopleInfo(@NonNull Context context, String userID);

    void setOnDestroyView(Context context);
}
