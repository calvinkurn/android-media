package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import rx.Subscriber;

/**
 * @author by milhamj on 20/06/18.
 */

public class SendVoteSubscriber extends Subscriber<VoteStatisticDomainModel> {
    private int rowNumber;
    private String optionId;
    private FeedPlus.View view;

    public SendVoteSubscriber(int rowNumber, String optionId, FeedPlus.View view) {
        this.rowNumber = rowNumber;
        this.optionId = optionId;
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {
            view.onErrorSendVote(ErrorHandler.getErrorMessage(view.getContext(), e));
        }
    }

    @Override
    public void onNext(VoteStatisticDomainModel voteStatisticDomainModel) {
        if (view != null) {
            view.onSuccessSendVote(rowNumber, optionId, voteStatisticDomainModel);
        }
    }
}
