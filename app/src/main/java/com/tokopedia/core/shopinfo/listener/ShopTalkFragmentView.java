package com.tokopedia.core.shopinfo.listener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalk;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalkResult;

/**
 * Created by nisie on 11/18/16.
 */

public interface ShopTalkFragmentView {
    void showLoading();

    void setActionsEnabled(boolean isEnabled);

    Activity getActivity();

    void finishLoading();

    void onGetShopTalk(ShopTalkResult result);

    void showError(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener);

    void showProgressDialog();

    void onSuccessDeleteTalk(ShopTalk shopTalk);

    void showError(String error);

    void onSuccessReportTalk(ShopTalk shopTalk);

    void onSuccessFollowTalk(ShopTalk shopTalk);
}
