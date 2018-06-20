package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import rx.Subscriber;

/**
 * @author by milhamj on 20/06/18.
 */

public class SendVoteSubscriber extends Subscriber<VoteStatisticDomainModel> {
    private FeedPlus.View view;

    public SendVoteSubscriber(FeedPlus.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(VoteStatisticDomainModel voteStatisticDomainModel) {

    }
}
