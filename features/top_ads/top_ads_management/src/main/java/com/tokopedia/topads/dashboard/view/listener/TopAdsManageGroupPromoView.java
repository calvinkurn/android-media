package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;

import java.util.List;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsManageGroupPromoView extends CustomerView {
    void onCheckGroupExistError();

    void onGroupExist();

    void onGroupNotExist();

    void onGetGroupAdList(List<GroupAd> groupAds);

    void onGetGroupAdListError();

    void showLoading();

    void showErrorShouldFillGroupName();

    void dismissLoading();

    void onGroupNotExistOnSubmitNewGroup();

    void showErrorGroupNameNotValid();

    void hideErrorGroupNameNotValid();
}
