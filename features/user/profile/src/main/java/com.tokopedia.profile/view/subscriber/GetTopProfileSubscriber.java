package com.tokopedia.profile.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.profile.view.listener.TopProfileActivityListener;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;

import rx.Subscriber;

/**
 * @author by alvinatin on 28/02/18.
 */

public class GetTopProfileSubscriber extends Subscriber<TopProfileViewModel>{

    private final TopProfileActivityListener.View view;

    public GetTopProfileSubscriber(TopProfileActivityListener.View view){
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        view.hideLoading();
        view.onErrorGetProfileData(
                ErrorHandler.getErrorMessage(view.getContext(), throwable)
        );
    }

    @Override
    public void onNext(TopProfileViewModel topProfileViewModel) {
        view.hideLoading();
        view.showMainView();
        view.onSuccessGetProfileData(topProfileViewModel);
    }
}
