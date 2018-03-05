package com.tokopedia.tkpdcontent.feature.profile.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tkpdcontent.analytics.KolTracking;
import com.tokopedia.tkpdcontent.feature.profile.domain.model.KolProfileModel;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

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

            for (int index = 0; index <= kolProfileModel.getKolPostViewModels().size(); index++) {
                KolPostViewModel kolPostViewModel =
                        kolProfileModel.getKolPostViewModels().get(index);

                List<KolTracking.Promotion> promotionList = new ArrayList<>();
                promotionList.add(new KolTracking.Promotion(
                        kolPostViewModel.getId(),
                        KolTracking.Promotion.createContentNameKolPost(
                                kolPostViewModel.getTagsType()),
                        TextUtils.isEmpty(kolPostViewModel.getName()) ? "-" :
                                kolPostViewModel.getName(),
                        index,
                        TextUtils.isEmpty(kolPostViewModel.getLabel()) ? "-" :
                                kolPostViewModel.getLabel(),
                        kolPostViewModel.getContentId(),
                        TextUtils.isEmpty(kolPostViewModel.getContentLink()) ? "-" :
                                kolPostViewModel.getContentLink(),
                        Integer.valueOf(view.getUserSession().getUserId())
                ));


                view.getAbstractionRouter()
                        .getAnalyticTracker()
                        .sendEnhancedEcommerce(KolTracking.getKolImpressionTracking(promotionList));
            }
        } else {
            view.onEmptyKolPost();
        }
        view.updateCursor(kolProfileModel.getLastCursor());
    }
}
