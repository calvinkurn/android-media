package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.feedplus.data.pojo.Feed;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.feedplus.data.pojo.ProductFeedType;
import com.tokopedia.feedplus.data.pojo.ShopDetail;
import com.tokopedia.feedplus.data.pojo.Wholesale;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailSubscriber extends Subscriber<GraphqlResponse> {
    private static final int MAX_RATING = 100;
    private static final int NUM_STARS = 5;
    private final FeedPlusDetail.View viewListener;
    private final int page;

    public FeedDetailSubscriber(FeedPlusDetail.View viewListener, int page) {
        this.viewListener = viewListener;
        this.page = page;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetFeedDetail(
                ErrorHandler.getErrorMessage(viewListener.getActivity(), e)
        );
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        FeedQuery feedQuery = graphqlResponse.getData(FeedQuery.class);

        if (page == 1) {
            viewListener.dismissLoading();
            if (!hasFeed(feedQuery)) {
                viewListener.onEmptyFeedDetail();
                return;
            }
        } else {
            viewListener.dismissLoadingMore();
            if (!hasFeed(feedQuery)) {
                viewListener.setHasNextPage(false);
                return;
            }
        }

        List<Feed> feedList = feedQuery.getFeed().getData();
        Feed feedDetail = feedList.get(0);
        FeedDetailHeaderViewModel headerViewModel = createHeaderViewModel(
                feedDetail.getCreateTime(),
                feedDetail.getSource().getShop(),
                feedDetail.getContent().getStatusActivity());
        viewListener.onSuccessGetFeedDetail(
                headerViewModel,
                convertToViewModel(feedDetail),
                checkHasNextPage(feedQuery));
    }
    
    private Double getRating(Float rating) {
        return (double) rating / MAX_RATING * NUM_STARS;
    }

    private boolean hasFeed(FeedQuery feedQuery) {
        return feedQuery != null
                && feedQuery.getFeed() != null
                && feedQuery.getFeed().getData() != null
                && !feedQuery.getFeed().getData().isEmpty()
                && feedQuery.getFeed().getData().get(0) != null
                && feedQuery.getFeed().getData().get(0).getContent() != null
                && feedQuery.getFeed().getData().get(0).getContent().getProducts() != null
                && !feedQuery.getFeed().getData().get(0).getContent().getProducts().isEmpty();
    }

    private boolean checkHasNextPage(FeedQuery feedQuery) {
        try {
            return feedQuery.getFeed().getData().get(0).getMeta().isHasNextPage();
        } catch (NullPointerException e) {
            return false;
        }
    }

    private ArrayList<Visitable> convertToViewModel(Feed feedDetail) {
        ArrayList<Visitable> listDetail = new ArrayList<>();

        if (feedDetail.getContent() != null && feedDetail.getContent().getProducts() != null) {
            for (ProductFeedType productFeed : feedDetail.getContent().getProducts()) {
                listDetail.add(createProductViewModel(productFeed));
            }
        }
        return listDetail;
    }

    private FeedDetailViewModel createProductViewModel(ProductFeedType productFeed) {
        return new FeedDetailViewModel(
                productFeed.getId(),
                productFeed.getName(),
                productFeed.getPrice(),
                productFeed.getImage(),
                productFeed.getProductLink(),
                productFeed.getCashback(),
                getIsWholesale(productFeed.getWholesale()),
                productFeed.getPreorder(),
                productFeed.getFreereturns(),
                productFeed.getWishlist(),
                getRating(productFeed.getRating())
        );
    }

    private boolean getIsWholesale(List<Wholesale> wholesale) {
        return wholesale.size() > 0;
    }


    private FeedDetailHeaderViewModel createHeaderViewModel(String create_time,
                                                            ShopDetail shop,
                                                            String status_activity) {
        return new FeedDetailHeaderViewModel(shop.getId(),
                shop.getName(),
                shop.getAvatar(),
                shop.getIsGold(),
                create_time,
                shop.getIsOfficial(),
                shop.getShopLink(),
                shop.getShareLinkURL(),
                shop.getShareLinkDescription(),
                status_activity
        );
    }
}
