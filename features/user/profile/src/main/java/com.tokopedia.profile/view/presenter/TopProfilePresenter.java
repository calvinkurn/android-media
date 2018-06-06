package com.tokopedia.profile.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.profile.usecase.GetTopProfileDataUseCase;
import com.tokopedia.profile.view.listener.TopProfileActivityListener;
import com.tokopedia.profile.view.subscriber.FollowKolSubscriber;
import com.tokopedia.profile.view.subscriber.GetTopProfileSubscriber;

/**
 * @author by alvinatin on 28/02/18.
 */

public class TopProfilePresenter extends BaseDaggerPresenter<TopProfileActivityListener.View>
        implements TopProfileActivityListener.Presenter {

    private final GetTopProfileDataUseCase getTopProfileDataUseCase;

    public TopProfilePresenter(GetTopProfileDataUseCase getTopProfileDataUseCase){
        this.getTopProfileDataUseCase = getTopProfileDataUseCase;
    }

    @Override
    public void initView(String userId) {
        getView().showLoading();
        getTopProfileData(userId);
    }

    @Override
    public void getTopProfileData(String userId) {
        getTopProfileDataUseCase.execute(
                GetTopProfileDataUseCase.getParams(userId),
                new GetTopProfileSubscriber(getView())
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        getTopProfileDataUseCase.unsubscribe();
    }

    @Override
    public void followKol(String userId) {
        getView().getProfileRouter().doFollowKolPost(
                Integer.valueOf(userId),
                new FollowKolSubscriber(getView())
        );
    }

    @Override
    public void unfollowKol(String userId) {
        getView().getProfileRouter().doUnfollowKolPost(
                Integer.valueOf(userId),
                new FollowKolSubscriber(getView())
        );
    }
}
