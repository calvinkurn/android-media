package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.feedplus.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.feedplus.view.listener.RecentView;
import com.tokopedia.feedplus.view.subscriber.RecentViewSubscriber;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import javax.inject.Inject;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewPresenter extends BaseDaggerPresenter<RecentView.View>
        implements RecentView.Presenter {

    private final GetRecentViewUseCase getRecentProductUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final UserSession userSession;
    private RecentView.View viewListener;
    private WishListActionListener wishListActionListener;

    @Inject
    RecentViewPresenter(GetRecentViewUseCase getRecentProductUseCase,
                        AddWishListUseCase addWishListUseCase,
                        RemoveWishListUseCase removeWishListUseCase,
                        UserSession userSession) {
        this.getRecentProductUseCase = getRecentProductUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.userSession = userSession;
    }

    public void attachView(RecentView.View view, WishListActionListener wishListActionListener) {
        this.viewListener = view;
        this.wishListActionListener = wishListActionListener;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getRecentProductUseCase.unsubscribe();
        if (removeWishListUseCase != null) {
            removeWishListUseCase.unsubscribe();
        }

        if (addWishListUseCase != null) {
            addWishListUseCase.unsubscribe();
        }
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

        addWishListUseCase.createObservable(productId,
                userSession.getUserId(), wishListActionListener);

    }

    @Override
    public void removeFromWishlist(int adapterPosition, String productId) {
        viewListener.showLoadingProgress();

        removeWishListUseCase.createObservable(productId,
                userSession.getUserId(), wishListActionListener);

    }
}
