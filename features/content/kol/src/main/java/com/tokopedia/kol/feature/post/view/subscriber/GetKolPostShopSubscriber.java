package com.tokopedia.kol.feature.post.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.common.network.GraphqlErrorHandler;
import com.tokopedia.kol.feature.post.domain.model.ContentListDomain;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import rx.Subscriber;

/**
 * @author by milhamj on 23/08/18.
 */

public class GetKolPostShopSubscriber extends Subscriber<ContentListDomain> {

    private static final String PARAM_COUNT = "{count}";
    private static final String SINGLE = "single";
    private static final String MULTIPLE = "multiple";
    private static final String PARAM_TYPE = "{type}";

    private final KolPostShopContract.View view;

    public GetKolPostShopSubscriber(KolPostShopContract.View view) {
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
    public void onNext(ContentListDomain contentListDomain) {
        view.hideLoading();
        view.onSuccessGetKolPostShop(
                contentListDomain.getVisitableList(),
                contentListDomain.getLastCursor()
        );
        for (Visitable visitable: contentListDomain.getVisitableList()) {
            if (visitable instanceof KolPostViewModel) {
                KolPostViewModel kolPostViewModel = (KolPostViewModel) visitable;
                view.getAbstractionRouter().getAnalyticTracker().sendEventTracking(
                        KolEventTracking.Event.EVENT_SHOP_PAGE,
                        KolEventTracking.Category.SHOP_PAGE_FEED,
                        KolEventTracking.Action.SHOP_ITEM_IMPRESSION_DYNAMIC
                                .replace(PARAM_COUNT, kolPostViewModel.getImageList().size() == 1 ? SINGLE : MULTIPLE)
                                .replace(PARAM_TYPE, kolPostViewModel.getTagsType()),
                        String.valueOf(kolPostViewModel.getContentId()));
            }
        }
    }
}
