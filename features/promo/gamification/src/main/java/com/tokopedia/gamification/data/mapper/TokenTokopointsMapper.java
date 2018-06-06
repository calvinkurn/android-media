package com.tokopedia.gamification.data.mapper;

import com.tokopedia.gamification.data.entity.TokenAssetEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.floating.view.model.TokenAsset;
import com.tokopedia.gamification.floating.view.model.TokenBackgroundAsset;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenEmptyState;
import com.tokopedia.gamification.floating.view.model.TokenFloating;
import com.tokopedia.gamification.floating.view.model.TokenHome;
import com.tokopedia.gamification.floating.view.model.TokenUser;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class TokenTokopointsMapper implements Func1<TokenDataEntity, TokenData> {

    @Inject
    public TokenTokopointsMapper() {
    }

    @Override
    public TokenData call(TokenDataEntity tokenDataEntity) {
        if (tokenDataEntity != null) {
            TokenData tokenData = new TokenData();

            if (tokenDataEntity.getFloating() != null) {
                TokenFloating tokenFloating = new TokenFloating();

                if (tokenDataEntity.getFloating().getTokenAsset() != null) {
                    tokenFloating.setTokenAsset(setTokenAsset(tokenDataEntity.getFloating().getTokenAsset()));
                }

                tokenFloating.setApplink(tokenDataEntity.getFloating().getApplink());
                tokenFloating.setPageUrl(tokenDataEntity.getFloating().getPageUrl());
                tokenFloating.setShowTime(tokenDataEntity.getFloating().getShowTime());
                tokenFloating.setTimeRemainingSeconds(tokenDataEntity.getFloating().getTimeRemainingSeconds());
                tokenFloating.setTokenId(tokenDataEntity.getFloating().getTokenId());
                tokenFloating.setUnixTimestamp(tokenDataEntity.getFloating().getUnixTimestamp());
                tokenData.setFloating(tokenFloating);
            }

            if (tokenDataEntity.getHome() != null) {
                TokenHome tokenHome = new TokenHome();

                if (tokenDataEntity.getHome().getTokensUser() != null) {
                    TokenUser tokenUser = new TokenUser();

                    if (tokenDataEntity.getHome().getTokensUser().getTokenAsset() != null) {
                        tokenUser.setTokenAsset(setTokenAsset(tokenDataEntity.getHome().getTokensUser().getTokenAsset()));
                    }

                    if (tokenDataEntity.getHome().getTokensUser().getBackgroundAsset() != null) {
                        TokenBackgroundAsset tokenBackgroundAsset = new TokenBackgroundAsset();
                        tokenBackgroundAsset.setName(tokenDataEntity.getHome().getTokensUser().getBackgroundAsset().getName());
                        tokenBackgroundAsset.setVersion(tokenDataEntity.getHome().getTokensUser().getBackgroundAsset().getVersion());
                        tokenBackgroundAsset.setBackgroundImgUrl(tokenDataEntity.getHome().getTokensUser().getBackgroundAsset().getBackgroundImgUrl());
                        tokenUser.setBackgroundAsset(tokenBackgroundAsset);
                    }

                    tokenUser.setCampaignID(tokenDataEntity.getHome().getTokensUser().getCampaignID());
                    tokenUser.setShowTime(tokenDataEntity.getHome().getTokensUser().getShowTime());
                    tokenUser.setTimeRemainingSeconds(tokenDataEntity.getHome().getTokensUser().getTimeRemainingSeconds());
                    tokenUser.setTitle(tokenDataEntity.getHome().getTokensUser().getTitle());
                    tokenUser.setTokenUserID(tokenDataEntity.getHome().getTokensUser().getTokenUserID());
                    tokenUser.setUnixTimestampFetch(tokenDataEntity.getHome().getTokensUser().getUnixTimestampFetch());
                    tokenHome.setTokensUser(tokenUser);
                }

                if (tokenDataEntity.getHome().getEmptyState() != null) {
                    TokenEmptyState tokenEmptyState = new TokenEmptyState();
                    tokenEmptyState.setTitle(tokenDataEntity.getHome().getEmptyState().getTitle());
                    tokenEmptyState.setButtonText(tokenDataEntity.getHome().getEmptyState().getButtonText());
                    tokenEmptyState.setButtonApplink(tokenDataEntity.getHome().getEmptyState().getButtonApplink());
                    tokenEmptyState.setButtonURL(tokenDataEntity.getHome().getEmptyState().getButtonURL());
                    tokenEmptyState.setImageUrl(tokenDataEntity.getHome().getEmptyState().getImageUrl());
                    tokenEmptyState.setBackgroundImgUrl(tokenDataEntity.getHome().getEmptyState().getBackgroundImgUrl());
                    tokenEmptyState.setVersion(tokenDataEntity.getHome().getEmptyState().getVersion());
                    tokenHome.setTokenEmptyState(tokenEmptyState);
                }

                tokenHome.setButtonApplink(tokenDataEntity.getHome().getButtonApplink());
                tokenHome.setButtonURL(tokenDataEntity.getHome().getButtonURL());
                tokenHome.setCountingMessage(tokenDataEntity.getHome().getCountingMessage());
                tokenData.setHome(tokenHome);
            }

            tokenData.setOffFlag(tokenDataEntity.getOffFlag());
            tokenData.setSumToken(tokenDataEntity.getSumToken());
            tokenData.setSumTokenStr(tokenDataEntity.getSumTokenStr());
            tokenData.setTokenUnit(tokenDataEntity.getTokenUnit());

            return tokenData;
        }
        return null;
    }

    private TokenAsset setTokenAsset(TokenAssetEntity tokenAssetEntity) {
        TokenAsset tokenAsset = new TokenAsset();
        tokenAsset.setSpriteUrl(tokenAssetEntity.getSpriteUrl());
        tokenAsset.setSmallImgUrl(tokenAssetEntity.getSmallImgUrl());
        tokenAsset.setImageUrls(tokenAssetEntity.getImageUrls());
        tokenAsset.setName(tokenAssetEntity.getName());
        tokenAsset.setVersion(tokenAssetEntity.getVersion());
        tokenAsset.setFloatingImgUrl(tokenAssetEntity.getFloatingImgUrl());
        return tokenAsset;
    }
}
