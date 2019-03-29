package com.tokopedia.kol.feature.postdetail.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber;
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.subscriber.FollowUnfollowDetailSubscriber;
import com.tokopedia.kol.feature.postdetail.view.subscriber.GetKolPostDetailSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailPresenter extends BaseDaggerPresenter<KolPostDetailContract.View>
        implements KolPostDetailContract.Presenter {

    private final GetKolPostDetailUseCase getKolPostDetailUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;
    private final FollowKolPostGqlUseCase followKolPostGqlUseCase;

    @Inject
    public KolPostDetailPresenter(GetKolPostDetailUseCase getKolPostDetailUseCase,
                                  LikeKolPostUseCase likeKolPostUseCase, FollowKolPostGqlUseCase
                                              followKolPostGqlUseCase) {
        this.getKolPostDetailUseCase = getKolPostDetailUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
        this.followKolPostGqlUseCase = followKolPostGqlUseCase;
    }

    @Override
    public void attachView(KolPostDetailContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getKolPostDetailUseCase.unsubsribe();
        likeKolPostUseCase.unsubscribe();
        followKolPostGqlUseCase.unsubscribe();
    }

    @Override
    public void getCommentFirstTime(int id) {
        getView().showLoading();
        getKolPostDetailUseCase.execute(
                GetKolPostDetailUseCase.getVariables(id),
                new GetKolPostDetailSubscriber(getView())
        );
    }

    @Override
    public void followKol(int id, int rowNumber) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
        );
        followKolPostGqlUseCase.execute(
                new FollowUnfollowDetailSubscriber(getView(),
                        rowNumber,
                        id,
                        FollowKolPostGqlUseCase.PARAM_FOLLOW)
        );
    }

    @Override
    public void unfollowKol(int id, int rowNumber) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        );
        followKolPostGqlUseCase.execute(
                new FollowUnfollowDetailSubscriber(getView(),
                        rowNumber,
                        id,
                        FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        );
    }

    @Override
    public void likeKol(int id, int rowNumber, KolPostListener.View.Like likeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                new LikeKolPostSubscriber(likeListener, rowNumber)
        );
    }

    @Override
    public void unlikeKol(int id, int rowNumber, KolPostListener.View.Like likeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_UNLIKE),
                new LikeKolPostSubscriber(likeListener, rowNumber)
        );
    }
}
