package com.tokopedia.gamification.data;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.gamification.cracktoken.presentation.model.CrackResult;
import com.tokopedia.gamification.data.mapper.CrackResultMapper;
import com.tokopedia.gamification.data.mapper.TokenTokopointsMapper;
import com.tokopedia.gamification.domain.IGamificationRepository;
import com.tokopedia.gamification.floating.view.model.TokenData;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class GamificationRepository implements IGamificationRepository {

    private GamificationDataStore gamificationDataStore;
    private CrackResultMapper crackResultMapper;
    private TokenTokopointsMapper tokenTokopointsMapper;

    @Inject
    public GamificationRepository(@ApplicationContext Context context,
                                  GamificationApi gamificationApi,
                                  CrackResultMapper crackResultMapper,
                                  TokenTokopointsMapper tokenTokopointsMapper) {
        this.gamificationDataStore = new GamificationCloudDataStore(context, gamificationApi);
        this.crackResultMapper = crackResultMapper;
        this.tokenTokopointsMapper = tokenTokopointsMapper;
    }

    @Override
    public Observable<TokenData> getTokenTokopoints() {
        return gamificationDataStore.getTokenTokopoints()
                .map(tokenTokopointsMapper);
    }

    @Override
    public Observable<CrackResult> getCrackResult(String tokenIdUser, String campaignId) {
        return gamificationDataStore.getCrackResult(tokenIdUser, campaignId)
                .map(crackResultMapper);
    }
}
