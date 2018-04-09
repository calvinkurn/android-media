package com.tokopedia.gamification.cracktoken.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.CrackBenefit;
import com.tokopedia.gamification.cracktoken.model.CrackButton;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.model.CrackResultStatus;
import com.tokopedia.gamification.domain.GetCrackResultEggUseCase;
import com.tokopedia.gamification.domain.GetTokenTokopointsUseCase;
import com.tokopedia.gamification.floating.view.model.TokenAsset;
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

    private GetTokenTokopointsUseCase getTokenTokopointsUseCase;
    private GetCrackResultEggUseCase getCrackResultEggUseCase;

    @Inject
    public CrackTokenPresenter(GetTokenTokopointsUseCase getTokenTokopointsUseCase, GetCrackResultEggUseCase getCrackResultEggUseCase) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.getCrackResultEggUseCase = getCrackResultEggUseCase;
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
                        CrackResult errorCrackResult = createGeneralErrorCrackResult();

                        getView().onErrorCrackToken(errorCrackResult);
                    }

                    @Override
                    public void onNext(CrackResult crackResult) {
                        // check result status
                        if (crackResult.getResultStatus().getCode().equals("200")) {
                            // success
                            getView().onSuccessCrackToken(crackResult);
                        } else if (crackResult.getResultStatus().getCode().equals("42501")) {
                            // token has been cracked
                            getView().onSuccessCrackToken(crackResult);
                        } else if (crackResult.getResultStatus().getCode().equals("42502") ||
                                crackResult.getResultStatus().getCode().equals("42503")) {
                            // token user expired or invalid
                            // show expired page
                            CrackResult expiredCrackResult = createExpiredCrackResult(crackResult.getResultStatus());
                            getView().onErrorCrackToken(expiredCrackResult);
                        } else {
                            CrackResult errorCrackResult = createGeneralErrorCrackResult();

                            getView().onErrorCrackToken(errorCrackResult);
                        }
                    }
                });
    }

    private CrackResult createExpiredCrackResult(CrackResultStatus resultStatus) {
        CrackResult crackResult = new CrackResult();

        crackResult.setBenefitLabel("Maaf, Anda kurang cepat");

        List<CrackBenefit> crackBenefits = new ArrayList<>();
        crackBenefits.add(new CrackBenefit("Tunggu Kesempatan Lainnya", "#ffffff", "medium"));

        Bitmap errorBitmap = BitmapFactory.decodeResource(getView().getResources(), R.drawable.image_error_crack_result_expired);

        CrackButton returnButton = new CrackButton();
        returnButton.setTitle("Ok");
        returnButton.setType("dismiss");

        crackResult.setBenefits(crackBenefits);
        crackResult.setImageBitmap(errorBitmap);
        crackResult.setReturnButton(returnButton);

        crackResult.setResultStatus(resultStatus);

        return crackResult;
    }

    private CrackResult createGeneralErrorCrackResult() {
        CrackResult crackResult = new CrackResult();

        crackResult.setBenefitLabel("Maaf, sayang sekali sepertinya");

        List<CrackBenefit> crackBenefits = new ArrayList<>();
        crackBenefits.add(new CrackBenefit("Terjadi Kesalahan Teknis", "#ffffff", "medium"));

        Bitmap errorBitmap = BitmapFactory.decodeResource(getView().getResources(), R.drawable.image_error_crack_result);

        CrackButton returnButton = new CrackButton();
        returnButton.setTitle("Coba Lagi");
        returnButton.setType("dismiss");

        crackResult.setBenefits(crackBenefits);
        crackResult.setImageBitmap(errorBitmap);
        crackResult.setReturnButton(returnButton);

        CrackResultStatus crackResultStatus = new CrackResultStatus();
        crackResultStatus.setCode("500");

        crackResult.setResultStatus(crackResultStatus);

        return crackResult;
    }

    public void getGetTokenTokopoints() {
        getView().showLoading();
        getTokenTokopointsUseCase.execute(new Subscriber<TokenData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
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

    public void downloadAllAsset(Context context, TokenData tokenData) {
        getView().showLoading();

        final List<String> assetUrls = new ArrayList<>();

        TokenUser tokenUser = tokenData.getHome().getTokensUser();
        TokenAsset tokenAsset = tokenUser.getTokenAsset();

        List<String> imageUrls = tokenAsset.getImageUrls();
        String full = imageUrls.get(0);
        String cracked = imageUrls.get(4);
        String imageRightUrl = imageUrls.get(5);
        String imageLeftUrl = imageUrls.get(6);

        assetUrls.add(tokenUser.getBackgroundAsset().getBackgroundImgUrl());
        assetUrls.add(full);
        assetUrls.add(cracked);
        assetUrls.add(imageLeftUrl);
        assetUrls.add(imageRightUrl);
        assetUrls.add(tokenUser.getTokenAsset().getSmallImgUrl());

        RequestListener<String, GlideDrawable> requestListener = new ImageRequestListener(assetUrls.size());
        for (String assetUrl : assetUrls) {
            Glide.with(context)
                    .load(assetUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .listener(requestListener)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        }
    }

    public class ImageRequestListener implements RequestListener<String, GlideDrawable> {

        private int size;
        ImageRequestListener(int size){
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

        void onAllResourceDownloaded(){
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
