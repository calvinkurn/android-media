package com.tokopedia.gamification.domain;

import com.tokopedia.gamification.cracktoken.presentation.model.CrackResult;
import com.tokopedia.gamification.floatingtoken.model.TokenData;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public interface IGamificationRepository {

    Observable<TokenData> getTokenTokopoints();

    Observable<CrackResult> getCrackResult(String tokenIdUser, String campaignId);
}
