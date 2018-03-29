package com.tokopedia.gamification.domain;

import com.tokopedia.gamification.cracktoken.presentation.model.CrackResult;
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
        return gamificationRepository.getCrackResult(requestParams.getParamsAllValueInString().get(TOKEN_ID),
                requestParams.getParamsAllValueInString().get(CAMPAIGN_ID));
    }

    public RequestParams createRequestParam(String tokenUserId, String campaignId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TOKEN_ID, tokenUserId);
        requestParams.putString(CAMPAIGN_ID, campaignId);
        return requestParams;
    }
}
