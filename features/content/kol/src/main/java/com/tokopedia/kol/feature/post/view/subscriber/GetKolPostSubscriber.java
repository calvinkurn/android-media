package com.tokopedia.kol.feature.post.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.analytics.KolEnhancedTracking;
import com.tokopedia.kol.common.network.GraphqlErrorHandler;
import com.tokopedia.kol.feature.post.domain.model.KolProfileModel;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * @author by milhamj on 26/02/18.
 */

public class GetKolPostSubscriber extends Subscriber<KolProfileModel> {
    private static final String DASH = "-";
    private final KolPostListener.View view;

    public GetKolPostSubscriber(KolPostListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
        view.onErrorGetProfileData(
                GraphqlErrorHandler.getErrorMessage(view.getContext(), e)
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

            doImpressionTracking(kolProfileModel.getKolPostViewModels());
        } else {
            view.onEmptyKolPost();
        }
        view.updateCursor(kolProfileModel.getLastCursor());
    }

    private void doImpressionTracking(List<KolPostViewModel> kolPostViewModels) {
        for (int index = 0; index < kolPostViewModels.size(); index++) {
            KolPostViewModel kolPostViewModel = kolPostViewModels.get(index);

            List<KolEnhancedTracking.Promotion> promotionList = new ArrayList<>();
            promotionList.add(new KolEnhancedTracking.Promotion(
                    kolPostViewModel.getContentId(),
                    KolEnhancedTracking.Promotion.createContentNameKolPost(
                            kolPostViewModel.getTagsType()),
                    TextUtils.isEmpty(kolPostViewModel.getName()) ? DASH :
                            kolPostViewModel.getName(),
                    index,
                    TextUtils.isEmpty(kolPostViewModel.getLabel()) ? DASH :
                            kolPostViewModel.getLabel(),
                    kolPostViewModel.getTagsId(),
                    TextUtils.isEmpty(kolPostViewModel.getTagsLink()) ? DASH :
                            kolPostViewModel.getTagsLink(),
                    Integer.valueOf(!TextUtils.isEmpty(view.getUserSession().getUserId()) ?
                            view.getUserSession().getUserId() : "0")
            ));

            TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(KolEnhancedTracking.getKolImpressionTracking(promotionList));
        }
    }
}
