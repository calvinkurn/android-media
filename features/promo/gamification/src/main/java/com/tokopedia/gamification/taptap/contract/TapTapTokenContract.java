package com.tokopedia.gamification.taptap.contract;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.taptap.data.entiity.GamiTapEggHome;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public interface TapTapTokenContract {

    interface View extends CustomerView {
        void onSuccessGetToken(TokenDataEntity tokenData);

        void onSuccessGetToken(GamiTapEggHome gamiTapEggHome, boolean isRefetchEgg);

        void onSuccessDownloadAllAsset();

        void onErrorGetToken(CrackResultEntity throwable);

        void onSuccessCrackToken(CrackResultEntity crackResult);

        void onErrorCrackToken(CrackResultEntity crackResult);

        void onFinishCrackToken();

        void showLoading();

        void hideLoading();

        Resources getResources();

        Context getContext();

        void navigateToLoginPage();

        void closePage();

        String getSuccessRewardLabel();

        void showErrorSnackBar(String errormessage);

        void showErrorSnackBar();
    }

    interface Presenter extends CustomerPresenter<View> {
        void crackToken(long tokenUserId, long campaignId);

        void getGetTokenTokopoints(boolean showLoading, boolean isRefetchEgg);

        void downloadAllAsset(Context context, GamiTapEggHome tokenData);

        void onLoginDataReceived();

        void initializePage();

        void playWithPoints();
    }
}
