package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.subscriber.FeedDetailSubscriber;
import com.tokopedia.wishlist.common.listener.TkpdWishListActionListener;
import com.tokopedia.wishlist.common.subscriber.TkpdAddWishlistSubscriber;
import com.tokopedia.wishlist.common.subscriber.TkpdRemoveWishlistSubscriber;
import com.tokopedia.wishlist.common.usecase.TkpdAddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.TkpdRemoveWishListUseCase;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailPresenter extends BaseDaggerPresenter<FeedPlusDetail.View>
        implements FeedPlusDetail.Presenter {


    private final TkpdAddWishListUseCase tkpdAddWishListUseCase;
    private final TkpdRemoveWishListUseCase tkpdRemoveWishListUseCase;
    private TkpdWishListActionListener tkpdWishListActionListener;

    private final GetFeedsDetailUseCase getFeedsDetailUseCase;
    private final UserSession userSession;
    private FeedPlusDetail.View viewListener;


    @Inject
    FeedPlusDetailPresenter(GetFeedsDetailUseCase getFeedsDetailUseCase,
                            TkpdAddWishListUseCase addWishlistUseCase,
                            TkpdRemoveWishListUseCase removeWishlistUseCase,
                            UserSession userSession) {
        this.getFeedsDetailUseCase = getFeedsDetailUseCase;
        this.tkpdAddWishListUseCase = addWishlistUseCase;
        this.tkpdRemoveWishListUseCase = removeWishlistUseCase;
        this.userSession = userSession;
    }

    public void attachView(FeedPlusDetail.View view, TkpdWishListActionListener wishlistListener) {
        this.viewListener = view;
        this.tkpdWishListActionListener = wishlistListener;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getFeedsDetailUseCase.unsubscribe();
        if (tkpdRemoveWishListUseCase != null) {
            tkpdRemoveWishListUseCase.unsubscribe();
        }

        if (tkpdAddWishListUseCase != null) {
            tkpdAddWishListUseCase.unsubscribe();
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

        tkpdAddWishListUseCase.createObservable(productId, userSession.getUserId(),
                new TkpdAddWishlistSubscriber(tkpdWishListActionListener, productId));
    }


    @Override
    public void removeFromWishlist(int adapterPosition, String productId) {
        viewListener.showLoadingProgress();

        tkpdRemoveWishListUseCase.createObservable(productId, userSession.getUserId(),
                new TkpdRemoveWishlistSubscriber(tkpdWishListActionListener, productId));
    }
}
