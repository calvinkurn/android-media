package com.tokopedia.gamification.domain;

import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.data.GamificationRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class GetCrackResultEggUseCase extends UseCase<CrackResult> {

    private static final String TOKEN_ID = "token_id";
    private static final String CAMPAIGN_ID = "campaign_id";

    private IGamificationRepository gamificationRepository;

    public GetCrackResultEggUseCase(GamificationRepository gamificationRepository) {
        this.gamificationRepository = gamificationRepository;
    }

    @Override
    public Observable<CrackResult> createObservable(RequestParams requestParams) {
        return gamificationRepository.getCrackResult(requestParams.getInt(TOKEN_ID, 0),
                requestParams.getInt(CAMPAIGN_ID, 0));
    }

    public RequestParams createRequestParam(int tokenUserId, int campaignId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(TOKEN_ID, tokenUserId);
        requestParams.putInt(CAMPAIGN_ID, campaignId);
        return requestParams;
    }
}
