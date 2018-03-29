package com.tokopedia.gamification.data;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.gamification.cracktoken.presentation.model.CrackResult;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.domain.IGamificationRepository;
import com.tokopedia.gamification.floatingtoken.model.TokenData;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class GamificationRepository implements IGamificationRepository {

    private GamificationDataStore gamificationDataStore;

    @Inject
    public GamificationRepository(@ApplicationContext Context context, GamificationApi gamificationApi) {
        this.gamificationDataStore = new GamificationCloudDataStore(context, gamificationApi);
    }

    @Override
    public Observable<TokenData> getTokenTokopoints() {
        return gamificationDataStore.getTokenTokopoints()
                .map(new Func1<TokenDataEntity, TokenData>() {
                    @Override
                    public TokenData call(TokenDataEntity tokenDataEntity) {
                        return null;
                    }
                });
    }

    @Override
    public Observable<CrackResult> getCrackResult(String tokenIdUser, String campaignId) {
        return gamificationDataStore.getCrackResult(tokenIdUser, campaignId)
                .map(new Func1<CrackResultEntity, CrackResult>() {
                    @Override
                    public CrackResult call(CrackResultEntity crackResultEntity) {
                        return null;
                    }
                });
    }
}
