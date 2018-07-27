package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.SendKolCommentUseCase;
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
    private final SendKolCommentUseCase sendKolCommentUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;

    @Inject
    KolPostDetailPresenter(GetKolCommentsUseCase getKolCommentsUseCase,
                           SendKolCommentUseCase sendKolCommentUseCase,
                           LikeKolPostUseCase likeKolPostUseCase) {
        this.getKolCommentsUseCase = getKolCommentsUseCase;
        this.sendKolCommentUseCase = sendKolCommentUseCase;
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

    }

    @Override
    public void sendComment(int id, String comment) {

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
