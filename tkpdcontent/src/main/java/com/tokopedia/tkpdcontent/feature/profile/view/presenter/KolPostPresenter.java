package com.tokopedia.tkpdcontent.feature.profile.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdcontent.feature.profile.domain.interactor.GetKolPostUseCase;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.subscriber.GetKolPostSubscriber;
import com.tokopedia.tkpdcontent.feature.profile.view.subscriber.LikeKolPostSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolPostPresenter extends BaseDaggerPresenter<KolPostListener.View>
        implements KolPostListener.Presenter {
    private final GetKolPostUseCase getKolPostUseCase;

    private String lastCursor = "";

    @Inject
    public KolPostPresenter(GetKolPostUseCase getKolPostUseCase) {
        this.getKolPostUseCase = getKolPostUseCase;
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
        getView().getKolRouter().doLikeKolPost(
                id, new LikeKolPostSubscriber(getView(), rowNumber));
    }

    @Override
    public void unlikeKol(int id, int rowNumber, KolPostListener.View kolListener) {
        getView().getKolRouter().doUnlikeKolPost(
                id, new LikeKolPostSubscriber(getView(), rowNumber));
    }
}
