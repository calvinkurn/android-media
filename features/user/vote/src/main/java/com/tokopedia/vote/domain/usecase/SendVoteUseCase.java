package com.tokopedia.vote.domain.usecase;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;
import com.tokopedia.vote.domain.source.VotingSource;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class SendVoteUseCase extends UseCase<VoteStatisticDomainModel> {

    private static final String PARAM_OPTION_ID = "option_id";
    private static final String PARAM_POLL_ID = "poll_id";
    private static final String PARAM_GC_TOKEN = "gc_token";
    private static final String PARAM_USE_V1 = "use_v1";
    private VotingSource votingSource;

    @Inject
    public SendVoteUseCase(VotingSource votingSource) {
        this.votingSource = votingSource;
    }

    @Override
    public Observable<VoteStatisticDomainModel> createObservable(RequestParams requestParams) {
        return votingSource.sendVote(requestParams.getString(PARAM_POLL_ID, ""),
                getRequestParamToSend(requestParams), requestParams.getBoolean(PARAM_USE_V1, false));
    }

    private HashMap<String, Object> getRequestParamToSend(RequestParams requestParams) {
        requestParams.getParameters().remove(PARAM_POLL_ID);
        return requestParams.getParameters();
    }

    public static RequestParams createParams(String pollId, String optionId, String gcToken) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_OPTION_ID, optionId);
        requestParams.putString(PARAM_POLL_ID, pollId);
        requestParams.putString(PARAM_GC_TOKEN, gcToken);
        requestParams.putBoolean(PARAM_USE_V1, false);
        return requestParams;
    }


    public static RequestParams createParamsV1(String pollId, String optionId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_OPTION_ID, optionId);
        requestParams.putString(PARAM_POLL_ID, pollId);
        requestParams.putBoolean(PARAM_USE_V1, true);
        return requestParams;
    }
}
