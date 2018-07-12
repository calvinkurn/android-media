package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.feedplus.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.feedplus.view.listener.RecentView;
import com.tokopedia.feedplus.view.subscriber.RecentViewSubscriber;
import com.tokopedia.wishlist.common.listener.TkpdWishListActionListener;
import com.tokopedia.wishlist.common.subscriber.TkpdAddWishlistSubscriber;
import com.tokopedia.wishlist.common.subscriber.TkpdRemoveWishlistSubscriber;
import com.tokopedia.wishlist.common.usecase.TkpdAddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.TkpdRemoveWishListUseCase;

import javax.inject.Inject;

//import com.tokopedia.feedplus.domain.usecase.AddWishlistUseCase;
//import com.tokopedia.feedplus.domain.usecase.RemoveWishlistUseCase;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewPresenter extends BaseDaggerPresenter<RecentView.View>
        implements RecentView.Presenter {

    private final GetRecentViewUseCase getRecentProductUseCase;
    private final TkpdAddWishListUseCase tkpdAddWishListUseCase;
    private final TkpdRemoveWishListUseCase tkpdRemoveWishListUseCase;
    //    private final AddWishlistUseCase addWishlistUseCase;
//    private final RemoveWishlistUseCase removeWishlistUseCase;
    private final UserSession userSession;
    private RecentView.View viewListener;
    private TkpdWishListActionListener tkpdWishListActionListener;

    @Inject
    RecentViewPresenter(GetRecentViewUseCase getRecentProductUseCase,
                        TkpdAddWishListUseCase tkpdAddWishListUseCase,
                        TkpdRemoveWishListUseCase tkpdRemoveWishListUseCase,
//                        AddWishlistUseCase addWishlistUseCase,
//                        RemoveWishlistUseCase removeWishlistUseCase,
                        UserSession userSession) {
        this.getRecentProductUseCase = getRecentProductUseCase;
//        this.addWishlistUseCase = addWishlistUseCase;
//        this.removeWishlistUseCase = removeWishlistUseCase;
        this.tkpdAddWishListUseCase = tkpdAddWishListUseCase;
        this.tkpdRemoveWishListUseCase = tkpdRemoveWishListUseCase;
        this.userSession = userSession;
    }

    public void attachView(RecentView.View view, TkpdWishListActionListener tkpdWishListActionListener) {
        this.viewListener = view;
        this.tkpdWishListActionListener = tkpdWishListActionListener;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getRecentProductUseCase.unsubscribe();
        if (tkpdRemoveWishListUseCase != null) {
            tkpdRemoveWishListUseCase.unsubscribe();
        }

        if (tkpdAddWishListUseCase != null) {
            tkpdAddWishListUseCase.unsubscribe();
        }
//        addWishlistUseCase.unsubscribe();
//        removeWishlistUseCase.unsubscribe();
    }


    @Override
    public void getRecentViewProduct() {
        viewListener.showLoading();
        getRecentProductUseCase.execute(
                getRecentProductUseCase.getParam(
                        userSession.getUserId()),
                new RecentViewSubscriber(viewListener)
        );
    }

    public void addToWishlist(int adapterPosition, String productId) {
        viewListener.showLoadingProgress();

        tkpdAddWishListUseCase.createObservable(productId, userSession.getUserId(),
                new TkpdAddWishlistSubscriber(tkpdWishListActionListener, productId));

        /*addWishlistUseCase.execute(
                AddWishlistUseCase.generateParam(productId, userSession),
                new AddWishlistSubscriber(wishlistListener, adapterPosition));*/
    }


    @Override
    public void removeFromWishlist(int adapterPosition, String productId) {
        viewListener.showLoadingProgress();

        tkpdRemoveWishListUseCase.createObservable(productId, userSession.getUserId(),
                new TkpdRemoveWishlistSubscriber(tkpdWishListActionListener, productId));

        /*removeWishlistUseCase.execute(
                RemoveWishlistUseCase.generateParam(productId, userSession),
                new RemoveWishlistSubscriber(wishlistListener, adapterPosition));*/
    }
}
