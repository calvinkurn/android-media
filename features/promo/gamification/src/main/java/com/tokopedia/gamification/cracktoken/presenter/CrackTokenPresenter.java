package com.tokopedia.gamification.cracktoken.presenter;

import android.content.Context;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.model.CrackResultStatus;
import com.tokopedia.gamification.cracktoken.model.ExpiredCrackResult;
import com.tokopedia.gamification.cracktoken.model.GeneralErrorCrackResult;
import com.tokopedia.gamification.domain.GetCrackResultEggUseCase;
import com.tokopedia.gamification.domain.GetTokenTokopointsUseCase;
import com.tokopedia.gamification.floating.view.model.TokenAsset;
import com.tokopedia.gamification.floating.view.model.TokenBackgroundAsset;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public class CrackTokenPresenter extends BaseDaggerPresenter<CrackTokenContract.View>
        implements CrackTokenContract.Presenter {

    private final int NUMBER_OF_BACKGROUND_IMAGE = 1;

    private final int INDEX_TOKEN_FULL = 0;
    private final int INDEX_TOKEN_CRACKED = 4;
    private final int INDEX_TOKEN_RIGHT = 6;
    private final int INDEX_TOKEN_LEFT = 5;

    private GetTokenTokopointsUseCase getTokenTokopointsUseCase;
    private GetCrackResultEggUseCase getCrackResultEggUseCase;
    private UserSession userSession;

    @Inject
    public CrackTokenPresenter(GetTokenTokopointsUseCase getTokenTokopointsUseCase,
                               GetCrackResultEggUseCase getCrackResultEggUseCase,
                               UserSession userSession) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.getCrackResultEggUseCase = getCrackResultEggUseCase;
        this.userSession = userSession;
    }

    @Override
    public void initializePage() {
        if (userSession.isLoggedIn()) {
            getGetTokenTokopoints();
        } else {
            getView().navigateToLoginPage();
        }
    }

    @Override
    public void onLoginDataReceived() {
        if (userSession.isLoggedIn()) {
            getGetTokenTokopoints();
        } else {
            getView().closePage();
        }
    }

    @Override
    public void crackToken(int tokenUserId, int campaignId) {
        getCrackResultEggUseCase.execute(getCrackResultEggUseCase.createRequestParam(tokenUserId, campaignId),
                new Subscriber<CrackResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        CrackResult errorCrackResult = createGeneralErrorCrackResult();

                        getView().onErrorCrackToken(errorCrackResult);
                    }

                    @Override
                    public void onNext(CrackResult crackResult) {
                        crackResult.setBenefitLabel(getView().getSuccessRewardLabel());
                        if (crackResult.isCrackTokenSuccess() || crackResult.isTokenHasBeenCracked()) {
                            getView().onSuccessCrackToken(crackResult);
                        } else if (crackResult.isCrackTokenExpired()) {
                            CrackResult expiredCrackResult = createExpiredCrackResult(
                                    crackResult.getResultStatus());
                            getView().onErrorCrackToken(expiredCrackResult);
                        } else {
                            CrackResult errorCrackResult = createGeneralErrorCrackResult();
                            getView().onErrorCrackToken(errorCrackResult);
                        }
                    }
                });
    }

    private CrackResult createExpiredCrackResult(CrackResultStatus resultStatus) {
        return new ExpiredCrackResult(getView().getContext(), resultStatus);
    }

    private CrackResult createGeneralErrorCrackResult() {
        return new GeneralErrorCrackResult(getView().getContext());
    }

    @Override
    public void getGetTokenTokopoints() {
        getView().showLoading();
        getTokenTokopointsUseCase.execute(new Subscriber<TokenData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }

                getView().hideLoading();
                CrackResult crackResult = createGeneralErrorCrackResult();
                getView().onErrorGetToken(crackResult);
            }

            @Override
            public void onNext(TokenData tokenData) {
                getView().hideLoading();
                getView().onSuccessGetToken(tokenData);
            }
        });
    }

    @Override
    public void downloadAllAsset(Context context, TokenData tokenData) {
        getView().showLoading();

        TokenUser tokenUser = tokenData.getHome().getTokensUser();
        TokenBackgroundAsset tokenBackgroundAsset = tokenUser.getBackgroundAsset();

        RequestListener<String, GlideDrawable> backgroundImgRequestListener = new ImageRequestListener(NUMBER_OF_BACKGROUND_IMAGE);
        ImageHandler.downloadOriginalSizeImageWithSignature(
                context,
                tokenBackgroundAsset.getBackgroundImgUrl(),
                new StringSignature(tokenBackgroundAsset.getVersion()),
                backgroundImgRequestListener);

        TokenAsset tokenAsset = tokenUser.getTokenAsset();

        List<String> tokenAssetImageUrls = tokenAsset.getImageUrls();
        String full = tokenAssetImageUrls.get(INDEX_TOKEN_FULL);
        String cracked = tokenAssetImageUrls.get(INDEX_TOKEN_CRACKED);
        String imageRightUrl = tokenAssetImageUrls.get(INDEX_TOKEN_RIGHT);
        String imageLeftUrl = tokenAssetImageUrls.get(INDEX_TOKEN_LEFT);

        final List<String> assetUrls = new ArrayList<>();

        assetUrls.add(full);
        assetUrls.add(cracked);
        assetUrls.add(imageLeftUrl);
        assetUrls.add(imageRightUrl);
        assetUrls.add(tokenAsset.getSmallImgUrl());

        RequestListener<String, GlideDrawable> tokenAssetRequestListener = new ImageRequestListener(assetUrls.size());
        for (String assetUrl : assetUrls) {
            ImageHandler.downloadOriginalSizeImageWithSignature(
                    context, assetUrl, new StringSignature(String.valueOf(tokenAsset.getVersion())),
                    tokenAssetRequestListener);
        }
    }

    public class ImageRequestListener implements RequestListener<String, GlideDrawable> {

        private int size;

        ImageRequestListener(int size) {
            this.size = size;
        }

        int counter = 0;

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            counter++;
            if (counter == size) {
                onAllResourceDownloaded();
            }
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            counter++;
            if (counter == size) {
                onAllResourceDownloaded();
            }
            return false;
        }

        void onAllResourceDownloaded() {
            if (isViewAttached()) {
                getView().hideLoading();
                getView().onSuccessDownloadAllAsset();
            }
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        getCrackResultEggUseCase.unsubscribe();
        getTokenTokopointsUseCase.unsubscribe();
    }
}
