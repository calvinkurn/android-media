package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.GetKolPostUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.subscriber.DeletePostSubscriber;
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
    private final DeletePostUseCase deletePostUseCase;

    private String lastCursor;

    @Inject
    public KolPostPresenter(GetKolPostUseCase getKolPostUseCase,
                            LikeKolPostUseCase likeKolPostUseCase,
                            DeletePostUseCase deletePostUseCase) {
        this.getKolPostUseCase = getKolPostUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
        this.deletePostUseCase = deletePostUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getKolPostUseCase.unsubscribe();
        likeKolPostUseCase.unsubscribe();
    }

    @Override
    public void initView(String userId) {
        lastCursor = "";
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

    @Override
    public void deletePost(int rowNumber, int id) {
        deletePostUseCase.execute(
                DeletePostUseCase.Companion.createRequestParams(String.valueOf(id)),
                new DeletePostSubscriber(getView(), rowNumber, id)
        );
    }
}
