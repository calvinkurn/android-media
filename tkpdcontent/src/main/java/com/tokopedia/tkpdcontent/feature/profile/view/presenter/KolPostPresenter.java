package com.tokopedia.tkpdcontent.feature.profile.view.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tkpdcontent.feature.profile.domain.interactor.GetProfileKolDataUseCase;
import com.tokopedia.tkpdcontent.feature.profile.domain.model.KolProfileModel;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolPostPresenter extends BaseDaggerPresenter<KolPostListener.View>
        implements KolPostListener.Presenter {
    private final GetProfileKolDataUseCase getProfileKolDataUseCase;

    private String lastCursor = "";

    @Inject
    public KolPostPresenter(GetProfileKolDataUseCase getProfileKolDataUseCase) {
        this.getProfileKolDataUseCase = getProfileKolDataUseCase;
    }

    @Override
    public void initView(String userId) {
        getKolPost(userId);
    }

    @Override
    public void getKolPost(String userId) {
        getProfileKolDataUseCase.execute(
                GetProfileKolDataUseCase.getParams(userId),
                new Subscriber<KolProfileModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorGetProfileData(
                                ErrorHandler.getErrorMessage(getView().getContext(), e)
                        );
                    }

                    @Override
                    public void onNext(KolProfileModel kolProfileModel) {
                        getView().onSuccessGetProfileData(
                                new ArrayList<Visitable>(kolProfileModel.getKolPostViewModels())
                        );
                        getView().updateCursor(kolProfileModel.getLastCursor());
                    }
                });
    }

    @Override
    public void updateCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    //TODO milhamj do something with these actions
    @Override
    public void followKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void unfollowKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void likeKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void unlikeKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }
}
