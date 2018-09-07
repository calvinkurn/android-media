package com.tokopedia.feedplus.view.viewmodel.feeddetail;

import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;

/**
 * @author by nisie on 7/10/17.
 */

public class SingleFeedDetailViewModel extends FeedDetailViewModel{


    @Override
    public int type(FeedPlusDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    public SingleFeedDetailViewModel(Integer productId, String name, String price,
                                     String imageSource, String url, String cashback,
                                     boolean isWholesale, boolean isPreorder, boolean isFreeReturn,
                                     boolean isWishlist, Double rating) {
        super(productId, name, price, imageSource, url, cashback,
                isWholesale, isPreorder, isFreeReturn, isWishlist, rating);
    }

}
