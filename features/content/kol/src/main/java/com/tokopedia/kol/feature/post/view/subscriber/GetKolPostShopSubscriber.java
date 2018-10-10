package com.tokopedia.kol.feature.post.view.subscriber;

import com.tokopedia.kol.common.network.GraphqlErrorHandler;
import com.tokopedia.kol.feature.post.domain.model.ContentListDomain;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;

import rx.Subscriber;

/**
 * @author by milhamj on 23/08/18.
 */

public class GetKolPostShopSubscriber extends Subscriber<ContentListDomain> {

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
        //TODO milhamj
//        view.getAbstractionRouter().getAnalyticTracker().sendEventTracking(
//                KolEventTracking.Event.EVENT_SHOP_PAGE,
//                KolEventTracking.Category.SHOP_PAGE_FEED,
//                KolEventTracking.Action.SHOP_ITEM_IMPRESSION,
//                String.valueOf(post.getId()));
    }

}
