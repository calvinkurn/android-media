package com.tokopedia.gamification.taptap.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationConstants;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity;
import com.tokopedia.gamification.taptap.compoundview.NetworkErrorHelper;
import com.tokopedia.gamification.taptap.contract.TapTapTokenContract;
import com.tokopedia.gamification.taptap.data.entiity.GamiTapEggHome;
import com.tokopedia.gamification.taptap.data.entiity.PlayWithPointsEntity;
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity;
import com.tokopedia.gamification.taptap.data.entiity.TokenAsset;
import com.tokopedia.gamification.taptap.utils.TapTapAnalyticsTrackerUtil;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.net.UnknownHostException;
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
    private UserSessionInterface userSession;

    @Inject
    public TapTapTokenPresenter(GraphqlUseCase getTokenTokopointsUseCase,
                                GraphqlUseCase getCrackResultEggUseCase,
                                UserSessionInterface userSession) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.getCrackResultEggUseCase = getCrackResultEggUseCase;
        this.userSession = userSession;
    }

    @Override
    public void initializePage() {
        if (userSession.isLoggedIn()) {
            getGetTokenTokopoints(true, false);
        } else {
            getView().navigateToLoginPage();
        }
    }

    @Override
    public void onLoginDataReceived() {
        if (userSession.isLoggedIn()) {
            getGetTokenTokopoints(true, false);
        } else {
            getView().closePage();
        }
    }

    @Override
    public void crackToken(long tokenUserId, long campaignId) {
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
                if (isViewNotAttached()) {
                    return;
                }
                if (e instanceof UnknownHostException) {
                    showErrorView(R.drawable.gf_internet_not_connected_error
                            , getView().getResources().getString(R.string.internet_not_connected_error_occured), true);
                } else {
                    getView().showErrorSnackBarOnCrackError(getView().getResources().getString(R.string.gf_server_error_crack_token_tap_tap), true);

                }

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewNotAttached())
                    return;
                ResponseCrackResultEntity responseCrackResultEntity = graphqlResponse.getData(ResponseCrackResultEntity.class);
                if (responseCrackResultEntity != null && responseCrackResultEntity.getCrackResultEntity() != null) {
                    CrackResultEntity crackResult = responseCrackResultEntity.getCrackResultEntity();
                    crackResult.setBenefitLabel(getView().getSuccessRewardLabel());
                    if (crackResult.isCrackTokenSuccess() || crackResult.isTokenHasBeenCracked()) {
                        getView().onSuccessCrackToken(crackResult);
                    } else if (crackResult.isCrackTokenExpired()) {
                        getView().showErrorSnackBarOnCrackError(getView().getResources().getString(R.string.gf_server_error_crack_token_tap_tap), true);
                    } else if (crackResult.getResultStatus() != null
                            && crackResult.isCrackButtonErrorTapTap()) {
                        if(crackResult.getResultStatus().getMessage() != null
                                && crackResult.getResultStatus().getMessage().size() != 0){
                            getView().showErrorSnackBarOnCrackError(TextUtils.join(",", crackResult.getResultStatus().getMessage()), false);
                        }else{
                            getView().showErrorSnackBarOnCrackError(getView().getResources().getString(R.string.error_campaign_expired), false);
                        }
                    } else {
                        getView().showErrorSnackBarOnCrackError(getView().getResources().getString(R.string.gf_server_error_crack_token_tap_tap), true);
                    }
                } else {
                    getView().showErrorSnackBarOnCrackError(getView().getResources().getString(R.string.gf_server_error_crack_token_tap_tap), true);
                }
                getView().onFinishCrackToken();
            }

            private void showErrorView(int errorImage, String string, boolean showRetryButton) {
                if(isViewNotAttached())
                    return;
                NetworkErrorHelper.showEmptyState(getView().getContext(),
                        getView().getRootView(),
                        errorImage,
                        string,
                        showRetryButton,
                        true,
                        new NetworkErrorHelper.ErrorButtonsListener() {
                            @Override
                            public void onRetryClicked(String buttonText) {
                                crackToken(tokenUserId, campaignId);
                                sendErrorEventOnErrorButton(buttonText);
                            }

                            @Override
                            public void onHomeClick(String buttonText) {
                                getView().navigateToHomePage();
                                sendErrorEventOnErrorButton(buttonText);
                            }
                        });
                sendErrorImpressionEvent();
            }
        });

    }

    private void sendErrorImpressionEvent() {
        TapTapAnalyticsTrackerUtil.sendEvent(getView().getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.VIEW_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                TapTapAnalyticsTrackerUtil.ActionKeys.POPUP_AND_ERROR_IMPRESSION,
                "");
    }

    private void sendErrorEventOnErrorButton(String buttonText) {
        TapTapAnalyticsTrackerUtil.sendEvent(getView().getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                TapTapAnalyticsTrackerUtil.ActionKeys.POPUP_AND_ERROR_CLICK,
                buttonText);
    }

    @Override
    public void getGetTokenTokopoints(boolean showLoading, boolean isRefetchEgg) {
        if (isViewNotAttached()) {
            return;
        }
        if (showLoading)
            getView().showLoading();
        getTokenTokopointsUseCase.clearRequest();
        GraphqlRequest tokenTokopointsRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_token_tap_tap),
                TapTapBaseEntity.class, false);
        getTokenTokopointsUseCase.addRequest(tokenTokopointsRequest);
        getTokenTokopointsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewNotAttached()) {
                    return;
                }
                getView().hideLoading();
                if (e instanceof UnknownHostException) {
                    showErrorView(R.drawable.gf_internet_not_connected_error
                            , getView().getResources().getString(R.string.internet_not_connected_error_occured));
                } else {
                    showErrorView(R.drawable.gf_server_full_error
                            , getView().getResources().getString(R.string.error_server_full));

                }


            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewNotAttached())
                    return;
                TapTapBaseEntity responseTokenTokopointEntity = graphqlResponse.getData(TapTapBaseEntity.class);
                getView().hideLoading();
                if (responseTokenTokopointEntity != null && responseTokenTokopointEntity.getGamiTapEggHome() != null)
                    getView().onSuccessGetToken(responseTokenTokopointEntity.getGamiTapEggHome(), isRefetchEgg);
            }

            private void showErrorView(int errorImage, String string) {
                if(isViewNotAttached())
                    return;
                NetworkErrorHelper.showEmptyState(getView().getContext(),
                        getView().getRootView(),
                        errorImage,
                        string,
                        true,
                        true,
                        new NetworkErrorHelper.ErrorButtonsListener() {
                            @Override
                            public void onRetryClicked(String buttonText) {
                                getGetTokenTokopoints(showLoading, isRefetchEgg);
                                sendErrorEventOnErrorButton(buttonText);
                            }

                            @Override
                            public void onHomeClick(String buttonText) {
                                getView().navigateToHomePage();
                                sendErrorEventOnErrorButton(buttonText);
                            }
                        });
                sendErrorImpressionEvent();
            }
        });
    }

    @Override
    public void downloadAllAsset(Context context, GamiTapEggHome tokenData) {

        if (isViewNotAttached()
                || tokenData == null
                || tokenData.getTokenAsset() == null
                || tokenData.getTokenAsset().getImageV2URLs() == null
                || tokenData.getTokenAsset().getImageV2URLs().size() < GamificationConstants.EggImageUrlIndex.IMAGE_ARRAY_SIZE_NORMAL) {
            return;
        }
        getView().showLoading();

        TokenAsset tokenAsset = tokenData.getTokenAsset();

        List<String> tokenAssetImageUrls = tokenAsset.getImageV2URLs();
        String full = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_FULL);
        String cracked = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_CRACKED);
        String imageLeftUrl = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_LEFT);
        String imageRightUrl = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_RIGHT);

        final List<Pair<String, String>> assetUrls = new ArrayList<>();

        assetUrls.add(new Pair<>(tokenAsset.getBackgroundImgURL(), tokenAsset.getVersion()));
        assetUrls.add(new Pair<>(tokenAsset.getGlowShadowImgURL(), tokenAsset.getVersion()));
        assetUrls.add(new Pair<>(tokenAsset.getGlowImgURL(), tokenAsset.getVersion()));

        String tokenAssetVersion = tokenAsset.getVersion();
        assetUrls.add(new Pair<>(full, tokenAssetVersion));
        assetUrls.add(new Pair<>(cracked, tokenAssetVersion));
        assetUrls.add(new Pair<>(imageLeftUrl, tokenAssetVersion));
        assetUrls.add(new Pair<>(imageRightUrl, tokenAssetVersion));

        RequestListener<String, GlideDrawable> tokenAssetRequestListener = new ImageRequestListener(assetUrls.size());
        for (Pair<String, String> assetUrlPair : assetUrls) {
            ImageHandler.downloadOriginalSizeImageWithSignature(
                    context, assetUrlPair.first, new StringSignature(assetUrlPair.second),
                    tokenAssetRequestListener);
        }
    }

    public void downloadEmptyAssets(Context context, GamiTapEggHome tokenData) {

        if (isViewNotAttached()
                || tokenData == null
                || tokenData.getTokenAsset() == null
                || tokenData.getTokenAsset().getImageV2URLs() == null
                || tokenData.getTokenAsset().getImageV2URLs().size() < GamificationConstants.EggImageUrlIndex.IMAGE_ARRAY_SIZE_EMPTY) {
            return;
        }

        getView().showLoading();

        TokenAsset tokenAsset = tokenData.getTokenAsset();

        List<String> tokenAssetImageUrls = tokenAsset.getImageV2URLs();
        String empty = tokenAssetImageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_EMPTY);

        final List<Pair<String, String>> assetUrls = new ArrayList<>();


        String tokenAssetVersion = String.valueOf(1);
        assetUrls.add(new Pair<>(empty, tokenAssetVersion));

        RequestListener<String, GlideDrawable> tokenAssetRequestListener = new ImageRequestListener(assetUrls.size());
        for (Pair<String, String> assetUrlPair : assetUrls) {
            ImageHandler.downloadOriginalSizeImageWithSignature(
                    context, assetUrlPair.first, new StringSignature(assetUrlPair.second),
                    tokenAssetRequestListener);
        }

    }

    @Override
    public void playWithPoints(boolean fromSummaryPage) {
        getView().showLoading();

        getCrackResultEggUseCase.clearRequest();
        GraphqlRequest tokenTokopointsRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_play_with_tokopoints),
                PlayWithPointsEntity.class, false);
        getCrackResultEggUseCase.addRequest(tokenTokopointsRequest);
        getCrackResultEggUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewNotAttached()) {
                    return;
                }
                if (fromSummaryPage)
                    getView().showErrorSnackBarOnSummaryPage();
                else
                    getView().showErrorSnackBar();
                getView().hideLoading();

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewNotAttached())
                    return;
                getView().hideLoading();
                if (graphqlResponse != null) {
                    PlayWithPointsEntity playWithPointsEntity = graphqlResponse.getData(PlayWithPointsEntity.class);
                    if (playWithPointsEntity != null && playWithPointsEntity.getGamiPlayWithPoints() != null) {
                        if (playWithPointsEntity.getGamiPlayWithPoints().getCode().equals("200")) {
                            if (fromSummaryPage) {
                                getView().dismissSummaryPage();
                            }
                            getGetTokenTokopoints(true, false);
                            return;
                        } else if (playWithPointsEntity.getGamiPlayWithPoints().getMessage() != null) {
                            if (fromSummaryPage)
                                getView().showErrorSnackBarOnSummaryPage(TextUtils.join(",", playWithPointsEntity.getGamiPlayWithPoints().getMessage()));
                            else
                                getView().showErrorSnackBar(TextUtils.join(",", playWithPointsEntity.getGamiPlayWithPoints().getMessage()));
                            return;
                        }

                    }
                }
                if (fromSummaryPage)
                    getView().showErrorSnackBarOnSummaryPage();
                else
                    getView().showErrorSnackBar();

            }
        });

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
