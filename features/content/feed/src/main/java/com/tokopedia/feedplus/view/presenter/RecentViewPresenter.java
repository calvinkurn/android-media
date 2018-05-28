package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.feedplus.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.feedplus.domain.usecase.RemoveWishlistUseCase;
import com.tokopedia.feedplus.view.listener.RecentView;
import com.tokopedia.feedplus.view.listener.WishlistListener;
import com.tokopedia.feedplus.view.subscriber.AddWishlistSubscriber;
import com.tokopedia.feedplus.view.subscriber.RecentViewSubscriber;
import com.tokopedia.feedplus.view.subscriber.RemoveWishlistSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewPresenter extends BaseDaggerPresenter<RecentView.View>
        implements RecentView.Presenter {

    private final GetRecentViewUseCase getRecentProductUseCase;
    private final AddWishlistUseCase addWishlistUseCase;
    private final RemoveWishlistUseCase removeWishlistUseCase;
    private final UserSession userSession;
    private RecentView.View viewListener;
    private WishlistListener wishlistListener;

    @Inject
    RecentViewPresenter(GetRecentViewUseCase getRecentProductUseCase,
                        AddWishlistUseCase addWishlistUseCase,
                        RemoveWishlistUseCase removeWishlistUseCase,
                        UserSession userSession) {
        this.getRecentProductUseCase = getRecentProductUseCase;
        this.addWishlistUseCase = addWishlistUseCase;
        this.removeWishlistUseCase = removeWishlistUseCase;
        this.userSession = userSession;
    }

    public void attachView(RecentView.View view, WishlistListener wishlistListener) {
        this.viewListener = view;
        this.wishlistListener = wishlistListener;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getRecentProductUseCase.unsubscribe();
        addWishlistUseCase.unsubscribe();
        removeWishlistUseCase.unsubscribe();
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
        addWishlistUseCase.execute(
                AddWishlistUseCase.generateParam(productId, userSession),
                new AddWishlistSubscriber(wishlistListener, adapterPosition));
    }


    @Override
    public void removeFromWishlist(int adapterPosition, String productId) {
        viewListener.showLoadingProgress();
        removeWishlistUseCase.execute(
                RemoveWishlistUseCase.generateParam(productId, userSession),
                new RemoveWishlistSubscriber(wishlistListener, adapterPosition));
    }
}
