package com.tokopedia.gamification.data.mapper;

import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.floating.view.model.TokenAsset;
import com.tokopedia.gamification.floating.view.model.TokenData;
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
                    TokenAsset tokenAsset = new TokenAsset();
                    tokenAsset.setImageUrls(tokenDataEntity.getFloating().getTokenAsset().getImageUrls());
                    tokenAsset.setSmallImgUrl(tokenDataEntity.getFloating().getTokenAsset().getSmallImgUrl());
                    tokenAsset.setSpriteUrl(tokenDataEntity.getFloating().getTokenAsset().getSpriteUrl());
                    tokenFloating.setTokenAsset(tokenAsset);
                }

                tokenFloating.setApplink(tokenDataEntity.getFloating().getApplink());
                tokenFloating.setPageUrl(tokenDataEntity.getFloating().getPageUrl());
                tokenFloating.setShowTime(tokenDataEntity.getFloating().getShowTime());
                tokenFloating.setTimeRemainingSeconds(tokenDataEntity.getFloating().getTimeRemainingSeconds());
                tokenFloating.setTokenClass(tokenDataEntity.getFloating().getTokenClass());
                tokenFloating.setTokenId(tokenDataEntity.getFloating().getTokenId());
                tokenFloating.setUnixTimestamp(tokenDataEntity.getFloating().getUnixTimestamp());
                tokenData.setFloating(tokenFloating);
            }

            if (tokenDataEntity.getHome() != null) {
                TokenHome tokenHome = new TokenHome();

                if (tokenDataEntity.getHome().getTokensUser() != null) {
                    TokenUser tokenUser = new TokenUser();

                    if (tokenDataEntity.getHome().getTokensUser().getTokenAsset() != null) {
                        TokenAsset tokenAsset = new TokenAsset();
                        tokenAsset.setSpriteUrl(tokenDataEntity.getHome().getTokensUser().getTokenAsset().getSpriteUrl());
                        tokenAsset.setSmallImgUrl(tokenDataEntity.getHome().getTokensUser().getTokenAsset().getSmallImgUrl());
                        tokenAsset.setImageUrls(tokenDataEntity.getHome().getTokensUser().getTokenAsset().getImageUrls());
                        tokenUser.setTokenAsset(tokenAsset);
                    }
                    tokenUser.setBackgroundImgUrl(tokenDataEntity.getHome().getTokensUser().getBackgroundImgUrl());
                    tokenUser.setCampaignID(tokenDataEntity.getHome().getTokensUser().getCampaignID());
                    tokenUser.setShowTime(tokenDataEntity.getHome().getTokensUser().getShowTime());
                    tokenUser.setTimeRemainingSeconds(tokenDataEntity.getHome().getTokensUser().getTimeRemainingSeconds());
                    tokenUser.setTitle(tokenDataEntity.getHome().getTokensUser().getTitle());
                    tokenUser.setTokenClass(tokenDataEntity.getHome().getTokensUser().getTokenClass());
                    tokenUser.setTokenUserID(tokenDataEntity.getHome().getTokensUser().getTokenUserID());
                    tokenUser.setUnixTimestampFetch(tokenDataEntity.getHome().getTokensUser().getUnixTimestampFetch());
                    tokenHome.setTokensUser(tokenUser);
                }

                tokenHome.setButtonApplink(tokenDataEntity.getHome().getButtonApplink());
                tokenHome.setButtonURL(tokenDataEntity.getHome().getButtonURL());
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
}
