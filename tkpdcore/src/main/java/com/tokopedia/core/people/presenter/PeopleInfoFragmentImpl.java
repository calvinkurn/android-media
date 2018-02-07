package com.tokopedia.core.people.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.core.people.datamanager.DataManager;
import com.tokopedia.core.people.datamanager.DataManagerImpl;
import com.tokopedia.core.people.fragment.PeopleInfoFragment;
import com.tokopedia.core.people.listener.PeopleInfoFragmentView;
import com.tokopedia.core.people.model.InputOutputData;
import com.tokopedia.core.people.model.PeopleInfoData;

/**
 * Created on 5/31/16.
 */
public class PeopleInfoFragmentImpl implements PeopleInfoFragmentPresenter {

    @SuppressWarnings("unused")
    private static final String TAG = PeopleInfoFragmentPresenter.class.getSimpleName();
    @SuppressWarnings("FieldCanBeLocal")
    private final PeopleInfoFragmentView listener;
    private final DataManager dataManager;

    public PeopleInfoFragmentImpl(PeopleInfoFragment fragment) {
        this.listener = fragment;
        this.dataManager = new DataManagerImpl(this);
    }

    @Override
    public void setOnFirstTimeLaunch(@NonNull Context context, @NonNull String userID) {
        dataManager.requestPeopleInfo(context, userID);
    }

    @Override
    public void refreshPeopleInfo(@NonNull Context context, String userID) {
        listener.setLoadingView(true);
        listener.setHeaderView(false);
        listener.setPeopleInfoView(false);
        listener.setUserReputationView(false);
        listener.setShopnView(false);
        dataManager.requestPeopleInfo(context, userID);
    }

    @Override
    public void onActionMessageClicked(@NonNull Context context, PeopleInfoData.UserInfo userInfo) {
        listener.openCreateMessage(userInfo);
    }

    @Override
    public void onActionManageClicked(@NonNull Context context, PeopleInfoData.UserInfo userInfo) {
        listener.openManageUser(userInfo);
    }

    @Override
    public void onFavoritedShoplicked(@NonNull Context context, PeopleInfoData.UserInfo userInfo) {
        listener.openDetailFavoritedShop(userInfo);
    }

    @Override
    public void onFollowingClicked(@NonNull Context context, PeopleInfoData.UserInfo userInfo) {
        listener.openFollowingPage(Integer.valueOf(userInfo.getUserId()));
    }

    @Override
    public void onShopClicked(@NonNull Context context, String shopId) {
        listener.openShopDetail(shopId);
    }

    @Override
    public void setOnRequestSuccess(InputOutputData data) {
        listener.setLoadingView(false);
        listener.renderHeaderView(data);
        listener.renderPeopleInfoView(data);
        listener.renderReputationView(data.getPeopleInfoData());
        if (data.getPeopleInfoData().getShopInfo() != null) {
            listener.renderShopInfoView(data.getPeopleInfoData());
        }
    }

    @Override
    public void setOnRequestError() {
        listener.setLoadingView(false);
        listener.setHeaderView(false);
        listener.setPeopleInfoView(false);
        listener.setUserReputationView(false);
        listener.setShopnView(false);
        listener.showTimeOut(null);
    }

    @Override
    public void setOnRequestError(String message) {
        listener.setLoadingView(false);
        listener.setHeaderView(false);
        listener.setPeopleInfoView(false);
        listener.setUserReputationView(false);
        listener.setShopnView(false);
        listener.showErrorMessage(message);
    }

    @Override
    public void setOnRequestTimeOut(RetryClickedListener clickedListener) {
        listener.setLoadingView(false);
        listener.setHeaderView(false);
        listener.setPeopleInfoView(false);
        listener.setUserReputationView(false);
        listener.setShopnView(false);
        listener.showTimeOut(clickedListener);
    }

    @Override
    public void setOnDestroyView(Context context) {
        dataManager.unSubscribe();
    }

}
