package com.tokopedia.gamification.cracktoken.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationConstants;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.ExpiredCrackResult;
import com.tokopedia.gamification.cracktoken.model.GeneralErrorCrackResult;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.GamificationSumCouponOuter;
import com.tokopedia.gamification.data.entity.HomeSmallButton;
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseTokenTokopointEntity;
import com.tokopedia.gamification.data.entity.ResultStatusEntity;
import com.tokopedia.gamification.data.entity.TokenAssetEntity;
import com.tokopedia.gamification.data.entity.TokenBackgroundAssetEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.data.entity.TokenUserEntity;
import com.tokopedia.gamification.data.entity.TokoPointDetailEntity;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public class CrackTokenPresenter extends BaseDaggerPresenter<CrackTokenContract.View>
        implements CrackTokenContract.Presenter {
    private GraphqlUseCase getTokenTokopointsUseCase;
    private GraphqlUseCase getCrackResultEggUseCase;
    private UserSessionInterface userSession;
    private GraphqlUseCase getRewardsUseCase;

    @Inject
    public CrackTokenPresenter(GraphqlUseCase getTokenTokopointsUseCase,
                               GraphqlUseCase getCrackResultEggUseCase,
                               UserSessionInterface userSession,
                               GraphqlUseCase getRewardsUseCase) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.getCrackResultEggUseCase = getCrackResultEggUseCase;
        this.userSession = userSession;
        this.getRewardsUseCase = getRewardsUseCase;
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
    public void crackToken(String tokenUserId, int campaignId) {
        if(true){
            String res = "{\n" +
                    "      \"crackResult\": {\n" +
                    "        \"resultStatus\": {\n" +
                    "          \"code\": \"200\",\n" +
                    "          \"message\": [\n" +
                    "            \"success\"\n" +
                    "          ],\n" +
                    "          \"status\": \"\"\n" +
                    "        },\n" +
                    "        \"imageUrl\": \"https://ecs7.tokopedia.net/assets/images/gamification/benefit/points-loyalty.png\",\n" +
                    "        \"benefitType\": \"loyalty_reward_point\",\n" +
                    "        \"benefits\": [\n" +
                    "          {\n" +
                    "            \"text\": \"+14 Points\",\n" +
                    "            \"color\": \"#FFDC00\",\n" +
                    "            \"size\": \"large\",\n" +
                    "            \"benefitType\": \"reward_point\",\n" +
                    "            \"templateText\": \"+\\u003cvalue\\u003e Points\",\n" +
                    "            \"animationType\": \"increasing\",\n" +
                    "            \"valueBefore\": 12,\n" +
                    "            \"valueAfter\": 14,\n" +
                    "            \"tierInformation\": \"Gold\",\n" +
                    "            \"multiplier\": \"1.1\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"text\": \"+3 Loyalty\",\n" +
                    "            \"color\": \"#FFDC00\",\n" +
                    "            \"size\": \"small\",\n" +
                    "            \"benefitType\": \"loyalty_point\",\n" +
                    "            \"templateText\": \"\",\n" +
                    "            \"animationType\": \"\",\n" +
                    "            \"valueBefore\": 0,\n" +
                    "            \"valueAfter\": 3,\n" +
                    "            \"tierInformation\": \"\",\n" +
                    "            \"multiplier\": \"\"\n" +
                    "          }\n" +
                    "        ],\n" +
                    "        \"ctaButton\": {\n" +
                    "          \"title\": \"Cek TokoPoints Anda\",\n" +
                    "          \"url\": \"https://www.tokopedia.com/tokopoints\",\n" +
                    "          \"applink\": \"tokopedia://tokopoints\",\n" +
                    "          \"type\": \"redirect\"\n" +
                    "        },\n" +
                    "        \"returnButton\": {\n" +
                    "          \"title\": \"Pecahkan Lucky Egg Lain\",\n" +
                    "          \"url\": \"\",\n" +
                    "          \"applink\": \"\",\n" +
                    "          \"type\": \"dismiss\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }";
            ResponseCrackResultEntity responseCrackResultEntity = new Gson().fromJson(res, ResponseCrackResultEntity.class);
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
            return;
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(GamificationConstants.GraphQlVariableKeys.TOKEN_ID_STR, tokenUserId);
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

    //todo Rahul remove
    public void getRewardsCount() {

        if(true){
            String res = "{\n" +
                    "      \"tokopoints\": {\n" +
                    "        \"resultStatus\": {\n" +
                    "          \"code\": \"200\"\n" +
                    "        },\n" +
                    "        \"status\": {\n" +
                    "          \"tier\": {\n" +
                    "            \"nameDesc\": \"Gold Member\",\n" +
                    "            \"eggImageURL\": \"https://ecs7.tokopedia.net/tokopointsimprovement/TokopointsTiering/gold.png\"\n" +
                    "          },\n" +
                    "          \"points\": {\n" +
                    "            \"reward\": 1254,\n" +
                    "            \"rewardStr\": \"1.254 Points\",\n" +
                    "            \"loyalty\": 340,\n" +
                    "            \"loyaltyStr\": \"340 Loyalty\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }";
            GamificationSumCouponOuter gamificationSumCouponOuter = new Gson().fromJson(res, GamificationSumCouponOuter.class);

            String res2 ="{\n" +
                    "      \"tokopointsSumCoupon\": {\n" +
                    "        \"sumCoupon\": 136,\n" +
                    "        \"sumCouponStr\": \"99+\"\n" +
                    "      }\n" +
                    "    }";
            TokoPointDetailEntity tokoPointDetailEntity = new Gson().fromJson(res2, TokoPointDetailEntity.class);
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
            return;
        }
        getRewardsUseCase.clearRequest();
        GraphqlRequest sumTokenRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_sum_coupon),
                GamificationSumCouponOuter.class, false);
        getRewardsUseCase.addRequest(sumTokenRequest);
        GraphqlRequest graphqlRequestPoints = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.gf_current_points),
                TokoPointDetailEntity.class, false);
        getRewardsUseCase.addRequest(graphqlRequestPoints);
        getRewardsUseCase.execute(new Subscriber<GraphqlResponse>() {
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

    //todo Rahul fake
    @Override
    public void getGetTokenTokopoints() {

        if(true){
            String res = "{\n" +
                    "      \"tokopointsToken\": {\n" +
                    "        \"resultStatus\": {\n" +
                    "          \"code\": \"200\",\n" +
                    "          \"message\": [\n" +
                    "            \"success\"\n" +
                    "          ],\n" +
                    "          \"status\": \"\"\n" +
                    "        },\n" +
                    "        \"offFlag\": false,\n" +
                    "        \"sumToken\": 4,\n" +
                    "        \"sumTokenStr\": \"4\",\n" +
                    "        \"tokenUnit\": \"Lucky Egg\",\n" +
                    "        \"floating\": {\n" +
                    "          \"tokenId\": 4,\n" +
                    "          \"pageUrl\": \"https://www.tokopedia.com/tokopoints/hadiah\",\n" +
                    "          \"applink\": \"tokopedia://gamification\",\n" +
                    "          \"timeRemainingSeconds\": 4606541,\n" +
                    "          \"isShowTime\": false,\n" +
                    "          \"unixTimestamp\": 1575883459,\n" +
                    "          \"tokenAsset\": {\n" +
                    "            \"name\": \"gold\",\n" +
                    "            \"version\": 25,\n" +
                    "            \"floatingImgUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/10/btsfltier03.gif\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"home\": {\n" +
                    "          \"emptyState\": {\n" +
                    "            \"title\": \"Dapatkan Lucky Egg dengan bertransaksi di Tokopedia\",\n" +
                    "            \"buttonText\": \"Belanja Sekarang\",\n" +
                    "            \"buttonURL\": \"https://www.tokopedia.com\",\n" +
                    "            \"buttonApplink\": \"tokopedia://home\",\n" +
                    "            \"backgroundImgUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/10/btsbg07.jpg\",\n" +
                    "            \"seamlessImgUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/10/btssl07.jpg\",\n" +
                    "            \"imageUrl\": \"https://ecs7.tokopedia.net/assets-tokopoints/prod/images/2019/02/emptyegg.png\",\n" +
                    "            \"version\": 2\n" +
                    "          },\n" +
                    "          \"countingMessage\": [\n" +
                    "            \"Tersisa\",\n" +
                    "            \"4 Lucky Egg\",\n" +
                    "            \"untuk dipecahkan\"\n" +
                    "          ],\n" +
                    "          \"tokenSourceMessage\": [\n" +
                    "            \"Lucky Egg ini Anda dapatkan dari\",\n" +
                    "            \"Pembelian Tiket Event\"\n" +
                    "          ],\n" +
                    "          \"tokensUser\": {\n" +
                    "            \"tokenUserIDstr\": \"2884188134\",\n" +
                    "            \"campaignID\": 1,\n" +
                    "            \"title\": \"Pecahkan Lucky Egg sekarang!\",\n" +
                    "            \"unixTimestampFetch\": 1575883459,\n" +
                    "            \"timeRemainingSeconds\": 4606541,\n" +
                    "            \"isShowTime\": false,\n" +
                    "            \"backgroundAsset\": {\n" +
                    "              \"name\": \"regular\",\n" +
                    "              \"version\": 25,\n" +
                    "              \"backgroundImgUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/10/btsbg07.jpg\",\n" +
                    "              \"seamlessImgUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/10/btssl07.jpg\"\n" +
                    "            },\n" +
                    "            \"tokenAsset\": {\n" +
                    "              \"name\": \"gold\",\n" +
                    "              \"version\": 25,\n" +
                    "              \"floatingImgUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/10/btsfltier03.gif\",\n" +
                    "              \"smallImgUrl\": \"https://ecs7.tokopedia.net/assets/images/gamification/remainder/egg/gold.png\",\n" +
                    "              \"smallImgv2Url\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/gold.png\",\n" +
                    "              \"spriteUrl\": \"https://ecs7.tokopedia.net/assets-tokopoints/prod/images/2019/02/gold-sprites.png\",\n" +
                    "              \"imageUrls\": [\n" +
                    "                \"https://ecs7.tokopedia.net/assets/images/gamification/heroes/egg-icon/gold/1.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/assets/images/gamification/heroes/egg-icon/gold/2.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/assets/images/gamification/heroes/egg-icon/gold/3.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/assets/images/gamification/heroes/egg-icon/gold/4.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/assets/images/gamification/heroes/egg-icon/gold/5.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/assets/images/gamification/heroes/egg-icon/gold/kiri.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/assets/images/gamification/heroes/egg-icon/gold/kanan.png\"\n" +
                    "              ],\n" +
                    "              \"imagev2Urls\": [\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/14.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/23.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/33.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/43.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/53.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/63.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/73.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/83.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/93.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/103.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/kiri3.png\",\n" +
                    "                \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/kanan3.png\"\n" +
                    "              ],\n" +
                    "              \"tokenSourceUrl\": \"https://ecs7.tokopedia.net/assets-tokopoints/prod/images/2019/02/tokensource.png\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"homeActionButton\": [\n" +
                    "            {\n" +
                    "              \"backgroundColor\": \"green\",\n" +
                    "              \"text\": \"Main Games Lainnya\",\n" +
                    "              \"appLink\": \"tokopedia://webview?titlebar\\u003dfalse\\u0026url\\u003dhttps%3A%2F%2Fwww.tokopedia.com%2Fseru%2F%3Fflag_app\",\n" +
                    "              \"url\": \"https://www.tokopedia.com/seru/\",\n" +
                    "              \"type\": \"redirect\"\n" +
                    "            }\n" +
                    "          ],\n" +
                    "          \"homeSmallButton\": {\n" +
                    "            \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/09/daily-prize.png\",\n" +
                    "            \"appLink\": \"tokopedia://webview?titlebar\\u003dfalse\\u0026url\\u003dhttps%3A%2F%2Fwww.tokopedia.com%2Fseru%2F%3Fflag_app\",\n" +
                    "            \"url\": \"https://www.tokopedia.com/seru\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "  }";
            ResponseTokenTokopointEntity responseTokenTokopointEntity = new Gson().fromJson(res, ResponseTokenTokopointEntity.class);
            getView().onSuccessGetToken(responseTokenTokopointEntity.getTokopointsToken());

            return;
        }
        getView().showLoading();
        getRewardsCount();
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
        HomeSmallButton homeSmallButton = tokenData.getHome().getHomeSmallButton();
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
        if(!TextUtils.isEmpty(homeSmallButton.getImageURL()))
            assetUrls.add(new Pair<>(homeSmallButton.getImageURL(), tokenAssetVersion));

        RequestListener<Drawable> tokenAssetRequestListener = new ImageRequestListener(assetUrls.size());
        for (Pair<String, String> assetUrlPair : assetUrls) {
            ObjectKey signature = new ObjectKey(assetUrlPair.second);
            ImageHandler.downloadOriginalSizeImageWithSignature(
                    context,
                    assetUrlPair.first,
                    signature,
                    tokenAssetRequestListener
            );
        }
    }

    public class ImageRequestListener implements RequestListener<Drawable> {

        private int size;

        ImageRequestListener(int size) {
            this.size = size;
        }

        int counter = 0;


        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            counter++;
            if (counter == size) {
                onAllResourceDownloaded();
            }
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            counter++;
            if (counter == size) {
                onAllResourceDownloaded();
            }
            return false;
        }

        void onAllResourceDownloaded() {
            if (isViewAttached()) {
                getView().onSuccessDownloadAllAsset();
            }
        }
    }

    @Override
    public void detachView() {
        getCrackResultEggUseCase.unsubscribe();
        getTokenTokopointsUseCase.unsubscribe();
        getRewardsUseCase.unsubscribe();
        super.detachView();
    }
}
