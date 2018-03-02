package com.tokopedia.tkpdcontent.feature.profile.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tkpdcontent.feature.profile.domain.model.KolProfileModel;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * @author by milhamj on 26/02/18.
 */

public class GetProfileKolDataSubscriber extends Subscriber<KolProfileModel> {
    private final KolPostListener.View view;

    public GetProfileKolDataSubscriber(KolPostListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
        view.onErrorGetProfileData(
                ErrorHandler.getErrorMessage(view.getContext(), e)
        );
    }

    @Override
    public void onNext(KolProfileModel kolProfileModel) {
        view.hideLoading();
        if (kolProfileModel.getKolPostViewModels() != null &&
                !kolProfileModel.getKolPostViewModels().isEmpty()) {
            view.onSuccessGetProfileData(
                    new ArrayList<Visitable>(kolProfileModel.getKolPostViewModels())
            );
        } else {
            view.onEmptyKolPost();
        }
        view.updateCursor(kolProfileModel.getLastCursor());
    }
}
