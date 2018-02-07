package com.tokopedia.core.people.listener;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.model.InputOutputData;
import com.tokopedia.core.people.model.PeopleInfoData;

/**
 * Created on 5/30/16.
 */
public interface PeopleInfoFragmentView {

    String getUserID();

    void setUserID(String userID);

    void setLoadingView(boolean isVisible);

    void renderShopInfoView(PeopleInfoData peopleInfoData);

    void renderReputationView(PeopleInfoData peopleInfoData);

    void renderPeopleInfoView(InputOutputData data);

    void renderHeaderView(InputOutputData data);

    void openCreateMessage(PeopleInfoData.UserInfo userInfo);

    void openManageUser(PeopleInfoData.UserInfo userInfo);

    void openDetailFavoritedShop(PeopleInfoData.UserInfo userInfo);

    void openFollowingPage(int userId);

    void openShopDetail(String shopId);

    void setShopnView(boolean isVisible);

    void setUserReputationView(boolean isVisible);

    void setPeopleInfoView(boolean isVisible);

    void setHeaderView(boolean isVisible);

    void showTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener);

    void showErrorMessage(String message);
}
