package com.tokopedia.gamification.taptap.presenter;

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
import com.tokopedia.gamification.GamificationConstants;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.ExpiredCrackResult;
import com.tokopedia.gamification.cracktoken.model.GeneralErrorCrackResult;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.GamificationSumCouponOuter;
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseTokenTokopointEntity;
import com.tokopedia.gamification.data.entity.ResultStatusEntity;
import com.tokopedia.gamification.data.entity.TokenAssetEntity;
import com.tokopedia.gamification.data.entity.TokenBackgroundAssetEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.data.entity.TokenUserEntity;
import com.tokopedia.gamification.data.entity.TokoPointDetailEntity;
import com.tokopedia.gamification.taptap.contract.TapTapTokenContract;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class TapTapTokenPresenter extends BaseDaggerPresenter<TapTapTokenContract.View>
        implements TapTapTokenContract.Presenter {
    private GraphqlUseCase getTokenTokopointsUseCase;
    private GraphqlUseCase getCrackResultEggUseCase;
    private UserSession userSession;

    @Inject
    public TapTapTokenPresenter(GraphqlUseCase getTokenTokopointsUseCase,
                                GraphqlUseCase getCrackResultEggUseCase,
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
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(GamificationConstants.GraphQlVariableKeys.TOKEN_ID, tokenUserId);
        queryParams.put(GamificationConstants.GraphQlVariableKeys.CAMPAIGN_ID, campaignId);
        getCrackResultEggUseCase.clearRequest();
        GraphqlRequest sumTokenRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.crack_egg_result_mutation),
                ResponseCrackResultEntity.class, queryParams, false);
        getCrackResultEggUseCase.addRequest(sumTokenRequest);
        getCrackResultEggUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                CrackResultEntity errorCrackResult = createGeneralErrorCrackResult();

                getView().onErrorCrackToken(errorCrackResult);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ResponseCrackResultEntity responseCrackResultEntity = graphqlResponse.getData(ResponseCrackResultEntity.class);
                if (responseCrackResultEntity != null && responseCrackResultEntity.getCrackResultEntity() != null) {
                    CrackResultEntity crackResult = responseCrackResultEntity.getCrackResultEntity();
                    crackResult.setBenefitLabel(getView().getSuccessRewardLabel());
                    if (crackResult.isCrackTokenSuccess() || crackResult.isTokenHasBeenCracked()) {
                        getView().onSuccessCrackToken(crackResult);
                    } else if (crackResult.isCrackTokenExpired()) {
                        CrackResultEntity expiredCrackResult = createExpiredCrackResult(
                                crackResult.getResultStatus());
                        getView().onErrorCrackToken(expiredCrackResult);
                    } else {
                        CrackResultEntity errorCrackResult = createGeneralErrorCrackResult();
                        getView().onErrorCrackToken(errorCrackResult);
                        getView().onFinishCrackToken();
                    }
                }
            }
        });

    }

    private CrackResultEntity createExpiredCrackResult(ResultStatusEntity resultStatus) {
        return new ExpiredCrackResult(getView().getContext(), resultStatus);
    }

    private CrackResultEntity createGeneralErrorCrackResult() {
        return new GeneralErrorCrackResult(getView().getContext());
    }

    @Override
    public void getGetTokenTokopoints() {
        getView().showLoading();
        getTokenTokopointsUseCase.clearRequest();
        GraphqlRequest tokenTokopointsRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.token_tokopoint_query),
                ResponseTokenTokopointEntity.class, false);
        getTokenTokopointsUseCase.addRequest(tokenTokopointsRequest);
        getTokenTokopointsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }

                getView().hideLoading();
                CrackResultEntity crackResult = createGeneralErrorCrackResult();
                getView().onErrorGetToken(crackResult);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ResponseTokenTokopointEntity responseTokenTokopointEntity = graphqlResponse.getData(ResponseTokenTokopointEntity.class);
                getView().hideLoading();
                if (responseTokenTokopointEntity != null && responseTokenTokopointEntity.getTokopointsToken() != null)
                    getView().onSuccessGetToken(responseTokenTokopointEntity.getTokopointsToken());
            }
        });
    }

    @Override
    public void downloadAllAsset(Context context, TokenDataEntity tokenData) {
        getView().showLoading();

        TokenUserEntity tokenUser = tokenData.getHome().getTokensUser();
        TokenBackgroundAssetEntity tokenBackgroundAsset = tokenUser.getBackgroundAsset();

        TokenAssetEntity tokenAsset = tokenUser.getTokenAsset();

        List<String> tokenAssetImageUrls = tokenAsset.getImagev2Urls();
        String full = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_FULL);
        String cracked = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_CRACKED);
        String imageLeftUrl = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_LEFT);
        String imageRightUrl = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_RIGHT);

        final List<Pair<String, String>> assetUrls = new ArrayList<>();

        assetUrls.add(new Pair<>(tokenBackgroundAsset.getBackgroundImgUrl(), tokenBackgroundAsset.getVersion()));

        String tokenAssetVersion = String.valueOf(tokenAsset.getVersion());
        assetUrls.add(new Pair<>(full, tokenAssetVersion));
        assetUrls.add(new Pair<>(cracked, tokenAssetVersion));
        assetUrls.add(new Pair<>(imageLeftUrl, tokenAssetVersion));
        assetUrls.add(new Pair<>(imageRightUrl, tokenAssetVersion));
        assetUrls.add(new Pair<>(tokenAsset.getSmallImgv2Url(), tokenAssetVersion));

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
