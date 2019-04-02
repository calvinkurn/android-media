package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.subscriber.FeedDetailSubscriber;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailPresenter extends BaseDaggerPresenter<FeedPlusDetail.View>
        implements FeedPlusDetail.Presenter {


    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private WishListActionListener wishListActionListener;

    private final GetFeedsDetailUseCase getFeedsDetailUseCase;
    private final UserSessionInterface userSession;


    @Inject
    public FeedPlusDetailPresenter(GetFeedsDetailUseCase getFeedsDetailUseCase,
                            AddWishListUseCase addWishlistUseCase,
                            RemoveWishListUseCase removeWishlistUseCase, UserSessionInterface userSession) {
        this.getFeedsDetailUseCase = getFeedsDetailUseCase;
        this.addWishListUseCase = addWishlistUseCase;
        this.removeWishListUseCase = removeWishlistUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(FeedPlusDetail.View view, WishListActionListener wishlistListener) {
        this.wishListActionListener = wishlistListener;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getFeedsDetailUseCase.unsubscribe();
        if (removeWishListUseCase != null) {
            removeWishListUseCase.unsubscribe();
        }

        if (addWishListUseCase != null) {
            addWishListUseCase.unsubscribe();
        }
    }

    @Override
    public void getFeedDetail(String detailId, int page) {
        if (page == 1) {
            getView().showLoading();
        } else {
            getView().showLoadingMore();
        }

        getFeedsDetailUseCase.execute(
                GetFeedsDetailUseCase.getFeedDetailParam(userSession.getUserId(), detailId, page),
                new FeedDetailSubscriber(getView(), page)
        );
    }

    @Override
    public void addToWishlist(int adapterPosition, String productId) {
        getView().showLoadingProgress();

        addWishListUseCase.createObservable(productId,
                userSession.getUserId(), wishListActionListener);
    }


    @Override
    public void removeFromWishlist(int adapterPosition, String productId) {
        getView().showLoadingProgress();

        removeWishListUseCase.createObservable(productId,
                userSession.getUserId(), wishListActionListener);
    }
}
