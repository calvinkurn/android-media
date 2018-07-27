package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.feature.post.domain.interactor.GetKolPostDetailUseCase;
import com.tokopedia.kol.feature.post.domain.interactor.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.subscriber.GetKolPostDetailSubscriber;
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailPresenter extends BaseDaggerPresenter<KolPostDetailContract.View>
        implements KolPostDetailContract.Presenter {

    private final GetKolPostDetailUseCase getKolPostDetailUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;

    @Inject
    public KolPostDetailPresenter(GetKolPostDetailUseCase getKolPostDetailUseCase,
                           LikeKolPostUseCase likeKolPostUseCase) {
        this.getKolPostDetailUseCase = getKolPostDetailUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
    }

    @Override
    public void attachView(KolPostDetailContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getKolPostDetailUseCase.unsubsribe();
    }

    @Override
    public void getCommentFirstTime(int id) {
        getKolPostDetailUseCase.execute(
                GetKolPostDetailUseCase.getVariables(id),
                new GetKolPostDetailSubscriber(getView())
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
