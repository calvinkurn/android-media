package com.tokopedia.gamification.cracktoken.contract;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.floating.view.model.TokenData;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public interface CrackTokenContract {

    interface View extends CustomerView {
        void onSuccessGetToken(TokenData tokenData);

        void onSuccessDownloadAllAsset();

        void onErrorGetToken(CrackResult throwable);

        void onSuccessCrackToken(CrackResult crackResult);

        void onErrorCrackToken(CrackResult crackResult);

        void showLoading();

        void hideLoading();

        Resources getResources();

        Context getContext();

        void navigateToLoginPage();

        void closePage();

        String getSuccessRewardLabel();

        void updateRewards(int points, int coupons, int loyalty);
    }

    interface Presenter extends CustomerPresenter<View> {
        void crackToken(int tokenUserId, int campaignId);

        void getGetTokenTokopoints();

        void downloadAllAsset(Context context, TokenData tokenData);

        void onLoginDataReceived();

        void initializePage();
    }
}
