package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.feedplus.domain.usecase.RemoveWishlistUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.listener.WishlistListener;
import com.tokopedia.feedplus.view.subscriber.AddWishlistSubscriber;
import com.tokopedia.feedplus.view.subscriber.FeedDetailSubscriber;
import com.tokopedia.feedplus.view.subscriber.RemoveWishlistSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailPresenter extends BaseDaggerPresenter<FeedPlusDetail.View>
        implements FeedPlusDetail.Presenter {

    private final GetFeedsDetailUseCase getFeedsDetailUseCase;
    private final AddWishlistUseCase addWishlistUseCase;
    private final RemoveWishlistUseCase removeWishlistUseCase;
    private final UserSession userSession;
    private WishlistListener wishlistListener;

    @Inject
    public FeedPlusDetailPresenter(GetFeedsDetailUseCase getFeedsDetailUseCase,
                            AddWishlistUseCase addWishlistUseCase,
                            RemoveWishlistUseCase removeWishlistUseCase,
                            UserSession userSession) {
        this.getFeedsDetailUseCase = getFeedsDetailUseCase;
        this.addWishlistUseCase = addWishlistUseCase;
        this.removeWishlistUseCase = removeWishlistUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(FeedPlusDetail.View view, WishlistListener wishlistListener) {
        super.attachView(view);
        this.wishlistListener = wishlistListener;
    }

    @Override
    public void detachView() {
        super.detachView();
        getFeedsDetailUseCase.unsubscribe();
        addWishlistUseCase.unsubscribe();
        removeWishlistUseCase.unsubscribe();
    }

    @Override
    public void getFeedDetail(String detailId, int page) {
        getView().showLoading();
        getFeedsDetailUseCase.execute(
                GetFeedsDetailUseCase.getFeedDetailParam(userSession.getUserId(), detailId, page),
                new FeedDetailSubscriber(getView())
        );
    }

    @Override
    public void addToWishlist(int adapterPosition, String productId) {
        getView().showLoadingProgress();
        addWishlistUseCase.execute(
                AddWishlistUseCase.generateParam(productId, userSession),
                new AddWishlistSubscriber(wishlistListener, adapterPosition));
    }


    @Override
    public void removeFromWishlist(int adapterPosition, String productId) {
        getView().showLoadingProgress();
        removeWishlistUseCase.execute(
                RemoveWishlistUseCase.generateParam(productId, userSession),
                new RemoveWishlistSubscriber(wishlistListener, adapterPosition));
    }
}
