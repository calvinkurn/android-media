package com.tokopedia.gamification.cracktoken.presenter;

import android.content.Context;
import android.util.Pair;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.model.CrackResultStatus;
import com.tokopedia.gamification.cracktoken.model.ExpiredCrackResult;
import com.tokopedia.gamification.cracktoken.model.GeneralErrorCrackResult;
import com.tokopedia.gamification.data.entity.GamificationSumCouponOuter;
import com.tokopedia.gamification.data.entity.TokoPointDetailEntity;
import com.tokopedia.gamification.domain.GetCrackResultEggUseCase;
import com.tokopedia.gamification.domain.GetTokenTokopointsUseCase;
import com.tokopedia.gamification.floating.view.model.TokenAsset;
import com.tokopedia.gamification.floating.view.model.TokenBackgroundAsset;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenUser;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public class CrackTokenPresenter extends BaseDaggerPresenter<CrackTokenContract.View>
        implements CrackTokenContract.Presenter {

    private static final int INDEX_TOKEN_FULL = 0;
    private static final int INDEX_TOKEN_CRACKED = 4;
    private static final int INDEX_TOKEN_RIGHT = 6;
    private static final int INDEX_TOKEN_LEFT = 5;

    private GetTokenTokopointsUseCase getTokenTokopointsUseCase;
    private GetCrackResultEggUseCase getCrackResultEggUseCase;
    private UserSession userSession;
    private GraphqlUseCase mGetRewardsUseCase;

    @Inject
    public CrackTokenPresenter(GetTokenTokopointsUseCase getTokenTokopointsUseCase,
                               GetCrackResultEggUseCase getCrackResultEggUseCase,
                               UserSession userSession, GraphqlUseCase mGetRewardsUseCase) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.getCrackResultEggUseCase = getCrackResultEggUseCase;
        this.userSession = userSession;
        this.mGetRewardsUseCase = mGetRewardsUseCase;
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

    public void getRewardsCount() {
        mGetRewardsUseCase.clearRequest();
        GraphqlRequest sumTokenRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_sum_coupon),
                GamificationSumCouponOuter.class);
        mGetRewardsUseCase.addRequest(sumTokenRequest);
        GraphqlRequest graphqlRequestPoints = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_current_points),
                TokoPointDetailEntity.class);
        mGetRewardsUseCase.addRequest(graphqlRequestPoints);
        mGetRewardsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                GamificationSumCouponOuter gamificationSumCouponOuter = graphqlResponse.getData(GamificationSumCouponOuter.class);
                TokoPointDetailEntity tokoPointDetailEntity = graphqlResponse.getData(TokoPointDetailEntity.class);
                int points = 0;
                int loyalty = 0;
                int coupons = 0;
                if (gamificationSumCouponOuter != null && gamificationSumCouponOuter.getTokopointsSumCoupon() != null)
                    coupons = gamificationSumCouponOuter.getTokopointsSumCoupon().getSumCoupon();
                if (tokoPointDetailEntity != null && tokoPointDetailEntity.getTokoPoints() != null && tokoPointDetailEntity.getTokoPoints().getStatus() != null && tokoPointDetailEntity.getTokoPoints().getStatus().getPoints() != null) {
                    loyalty = tokoPointDetailEntity.getTokoPoints().getStatus().getPoints().getLoyalty();
                    points = tokoPointDetailEntity.getTokoPoints().getStatus().getPoints().getReward();
                }
                getView().updateRewards(points, coupons, loyalty);
            }
        });

    }

    @Override
    public void getGetTokenTokopoints() {
        getView().showLoading();
        getRewardsCount();
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

        TokenAsset tokenAsset = tokenUser.getTokenAsset();

        List<String> tokenAssetImageUrls = tokenAsset.getImageUrls();
        String full = tokenAssetImageUrls.get(INDEX_TOKEN_FULL);
        String cracked = tokenAssetImageUrls.get(INDEX_TOKEN_CRACKED);
        String imageRightUrl = tokenAssetImageUrls.get(INDEX_TOKEN_RIGHT);
        String imageLeftUrl = tokenAssetImageUrls.get(INDEX_TOKEN_LEFT);

        final List<Pair<String, String>> assetUrls = new ArrayList<>();

        assetUrls.add(new Pair<>(tokenBackgroundAsset.getBackgroundImgUrl(), tokenBackgroundAsset.getVersion()));

        String tokenAssetVersion = String.valueOf(tokenAsset.getVersion());
        assetUrls.add(new Pair<>(full, tokenAssetVersion));
        assetUrls.add(new Pair<>(cracked, tokenAssetVersion));
        assetUrls.add(new Pair<>(imageLeftUrl, tokenAssetVersion));
        assetUrls.add(new Pair<>(imageRightUrl, tokenAssetVersion));
        assetUrls.add(new Pair<>(tokenAsset.getSmallImgUrl(), tokenAssetVersion));

        RequestListener<String, GlideDrawable> tokenAssetRequestListener = new ImageRequestListener(assetUrls.size());
        for (Pair<String, String> assetUrlPair : assetUrls) {
            ImageHandler.downloadOriginalSizeImageWithSignature(
                    context, assetUrlPair.first, new StringSignature(assetUrlPair.second),
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
        getCrackResultEggUseCase.unsubscribe();
        getTokenTokopointsUseCase.unsubscribe();
        super.detachView();
    }
}
