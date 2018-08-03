package com.tokopedia.profile.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.feature.post.domain.interactor.FollowKolPostGqlUseCase;
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
    private final FollowKolPostGqlUseCase followKolPostGqlUseCase;

    public TopProfilePresenter(GetTopProfileDataUseCase getTopProfileDataUseCase,
                               FollowKolPostGqlUseCase followKolPostGqlUseCase){
        this.getTopProfileDataUseCase = getTopProfileDataUseCase;
        this.followKolPostGqlUseCase = followKolPostGqlUseCase;
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
        followKolPostGqlUseCase.unsubscribe();
    }

    @Override
    public void followKol(String userId) {
        int id = Integer.parseInt(userId);
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW));
        followKolPostGqlUseCase.execute(new FollowKolSubscriber(getView()));
    }

    @Override
    public void unfollowKol(String userId) {
        int id = Integer.parseInt(userId);
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW));
        followKolPostGqlUseCase.execute(new FollowKolSubscriber(getView()));
    }
}
