package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.feedplus.domain.usecase.RemoveWishlistUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.listener.WishlistListener;
import com.tokopedia.feedplus.view.subscriber.AddWishlistSubscriber;
import com.tokopedia.feedplus.view.subscriber.FeedDetailSubscriber;
import com.tokopedia.feedplus.view.subscriber.RemoveWishlistSubscriber;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailPresenter extends BaseDaggerPresenter<FeedPlusDetail.View>
        implements FeedPlusDetail.Presenter {

    private final GraphqlUseCase getFeedsDetailUseCase;
    private final AddWishlistUseCase addWishlistUseCase;
    private final RemoveWishlistUseCase removeWishlistUseCase;
    private final UserSession userSession;
    private WishlistListener wishlistListener;

    @Inject
    FeedPlusDetailPresenter(GraphqlUseCase getFeedsDetailUseCase,
                            AddWishlistUseCase addWishlistUseCase,
                            RemoveWishlistUseCase removeWishlistUseCase,
                            UserSession userSession) {
        this.getFeedsDetailUseCase = getFeedsDetailUseCase;
        this.addWishlistUseCase = addWishlistUseCase;
        this.removeWishlistUseCase = removeWishlistUseCase;
        this.userSession = userSession;
    }

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

    public void getFeedDetail(String detailId, int page) {
        getView().showLoading();

        getFeedsDetailUseCase.clearRequest();

        String query = GraphqlHelper.loadRawString(
                getView().getResources(),
                R.raw.query_feed_detail);

        Map<String, Object> variables = GetFeedsDetailUseCase
                .getFeedDetailParam(userSession.getUserId(), detailId, page)
                .getParameters();

        GraphqlRequest feedDetailGraphqlRequest =
                new GraphqlRequest(query,
                        FeedQuery.class,
                        variables);

        getFeedsDetailUseCase.addRequest(feedDetailGraphqlRequest);
        getFeedsDetailUseCase.execute(new FeedDetailSubscriber(getView()));
    }

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
