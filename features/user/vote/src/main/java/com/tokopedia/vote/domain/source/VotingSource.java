package com.tokopedia.vote.domain.source;


import com.tokopedia.vote.data.VoteApi;
import com.tokopedia.vote.domain.mapper.SendVoteMapper;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VotingSource {

    @Inject
    VoteApi voteApi;

    @Inject
    SendVoteMapper votingMapper;

    @Inject
    public VotingSource() {

    }

    public Observable<VoteStatisticDomainModel> sendVote(String pollId,
                                                         HashMap<String, Object> parameters,
                                                         boolean useV1) {
        if (useV1) {
            return voteApi.sendVoteV1(pollId, parameters).map(votingMapper);
        }
        return voteApi.sendVote(pollId, parameters).map(votingMapper);
    }
}
