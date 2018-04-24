package com.tokopedia.kol.feature.following_list.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingResultDomain;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetKolFollowingListLoadMoreSubscriber extends Subscriber<KolFollowingResultDomain> {
    private KolFollowingList.View mainView;

    public GetKolFollowingListLoadMoreSubscriber(KolFollowingList.View mainView) {
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
    public void onNext(KolFollowingResultDomain kolFollowingResultDomain) {
        mainView.hideLoading();
        mainView.onSuccessLoadMoreKolFollowingList(GetKolFollowingListSubscriber.mappingViewModel(kolFollowingResultDomain));
    }

}
