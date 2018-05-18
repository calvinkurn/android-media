package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.feature.post.domain.interactor.GetKolPostUseCase;
import com.tokopedia.kol.feature.post.domain.interactor.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.subscriber.GetKolPostSubscriber;
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolPostPresenter extends BaseDaggerPresenter<KolPostListener.View>
        implements KolPostListener.Presenter {

    private final GetKolPostUseCase getKolPostUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;

    private String lastCursor = "";

    @Inject
    public KolPostPresenter(GetKolPostUseCase getKolPostUseCase,
                            LikeKolPostUseCase likeKolPostUseCase) {
        this.getKolPostUseCase = getKolPostUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
    }

    @Override
    public void initView(String userId) {
        getKolPost(userId);
    }

    @Override
    public void getKolPost(String userId) {
        getView().showLoading();
        getKolPostUseCase.execute(
                GetKolPostUseCase.getParams(userId, lastCursor),
                new GetKolPostSubscriber(getView())
        );

    }

    @Override
    public void updateCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    @Override
    public void followKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void unfollowKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void likeKol(int id, int rowNumber, KolPostListener.View kolListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                new LikeKolPostSubscriber(getView(), rowNumber)
        );
    }

    @Override
    public void unlikeKol(int id, int rowNumber, KolPostListener.View kolListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_UNLIKE),
                new LikeKolPostSubscriber(getView(), rowNumber)
        );
    }
}
