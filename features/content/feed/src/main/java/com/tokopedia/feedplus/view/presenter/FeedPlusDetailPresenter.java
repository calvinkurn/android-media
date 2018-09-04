package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.subscriber.FeedDetailSubscriber;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.subscriber.RemoveWishListSubscriber;
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
    private final UserSession userSession;
    private FeedPlusDetail.View viewListener;


    @Inject
    FeedPlusDetailPresenter(GetFeedsDetailUseCase getFeedsDetailUseCase,
                            AddWishListUseCase addWishlistUseCase,
                            RemoveWishListUseCase removeWishlistUseCase,
                            UserSession userSession) {
        this.getFeedsDetailUseCase = getFeedsDetailUseCase;
        this.addWishListUseCase = addWishlistUseCase;
        this.removeWishListUseCase = removeWishlistUseCase;
        this.userSession = userSession;
    }

    public void attachView(FeedPlusDetail.View view, WishListActionListener wishlistListener) {
        this.viewListener = view;
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

    public void getFeedDetail(String detailId, int page) {
        viewListener.showLoading();
        getFeedsDetailUseCase.execute(
                getFeedsDetailUseCase.getFeedDetailParam(
                        userSession.getUserId(),
                        detailId,
                        page),
                new FeedDetailSubscriber(viewListener));
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
