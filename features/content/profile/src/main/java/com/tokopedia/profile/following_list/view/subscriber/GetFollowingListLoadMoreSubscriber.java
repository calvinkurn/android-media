package com.tokopedia.profile.following_list.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.profile.following_list.domain.model.FollowingResultDomain;
import com.tokopedia.profile.following_list.view.listener.FollowingList;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetFollowingListLoadMoreSubscriber extends Subscriber<FollowingResultDomain> {
    private FollowingList.View mainView;

    public GetFollowingListLoadMoreSubscriber(FollowingList.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.hideLoading();
        mainView.onErrorLoadMoreKolFollowingList(
                ErrorHandler.getErrorMessage(mainView.getContext(), throwable)
        );
    }

    @Override
    public void onNext(FollowingResultDomain followingResultDomain) {
        mainView.hideLoading();
        mainView.onSuccessLoadMoreKolFollowingList(GetFollowingListSubscriber.mappingViewModel(followingResultDomain));
    }

}
