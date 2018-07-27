package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase;
import com.tokopedia.kol.feature.comment.view.subscriber.GetKolCommentFirstTimeSubscriber;
import com.tokopedia.kol.feature.post.domain.interactor.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailPresenter implements KolPostDetailContract.Presenter {

    private final GetKolCommentsUseCase getKolCommentsUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;

    @Inject
    public KolPostDetailPresenter(GetKolCommentsUseCase getKolCommentsUseCase,
                           LikeKolPostUseCase likeKolPostUseCase) {
        this.getKolCommentsUseCase = getKolCommentsUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
    }

    @Override
    public void attachView(KolPostDetailContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getCommentFirstTime(int id) {
        getKolCommentsUseCase.execute(
                GetKolCommentsUseCase.getFirstTimeParam(id),
                new GetKolCommentFirstTimeSubscriber()
        );
    }

    @Override
    public void followKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void unfollowKol(int id, int rowNumber, KolPostListener.View kolListener) {

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
