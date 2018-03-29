package com.tokopedia.gamification.data;

import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;

import rx.Observable;


/**
 * Created by nabillasabbaha on 3/28/18.
 */

public interface GamificationDataStore {

    Observable<TokenDataEntity> getTokenTokopoints();

    Observable<CrackResultEntity> getCrackResult(String tokenIdUser, String campaignId);
}
