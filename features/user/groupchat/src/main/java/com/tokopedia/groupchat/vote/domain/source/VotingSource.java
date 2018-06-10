package com.tokopedia.groupchat.vote.domain.source;

import com.tokopedia.groupchat.common.data.VoteApi;
import com.tokopedia.groupchat.common.di.scope.GroupChatScope;
import com.tokopedia.groupchat.vote.domain.mapper.SendVoteMapper;
import com.tokopedia.groupchat.vote.view.model.VoteStatisticViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VotingSource {

    private VoteApi voteApi;
    private SendVoteMapper votingMapper;

    @Inject
    public VotingSource(@GroupChatScope VoteApi voteApi, SendVoteMapper votingMapper) {
        this.voteApi = voteApi;
        this.votingMapper = votingMapper;
    }

    public Observable<VoteStatisticViewModel> sendVote(String pollId,
                                                       HashMap<String, Object> parameters) {
        return voteApi.sendVote(pollId, parameters).map(votingMapper);
    }
}
