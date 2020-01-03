package com.tokopedia.tkpd.tkpdreputation.review.shop.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductApi;
import com.tokopedia.tkpd.tkpdreputation.utils.GetData;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewShopGetListReviewCloud {
    private ReviewProductService reviewProductService;

    public ReviewShopGetListReviewCloud(ReviewProductService reviewProductService) {
        this.reviewProductService = reviewProductService;
    }

    public Observable<DataResponseReviewShop> getReviewShopList(HashMap<String, String> params) {
        return reviewProductService.getApi().getReviewShopList(params)
                .map(new GetData<DataResponse<DataResponseReviewShop>>())
                .map(new Func1<DataResponse<DataResponseReviewShop>, DataResponseReviewShop>() {
                    @Override
                    public DataResponseReviewShop call(DataResponse<DataResponseReviewShop> dataResponseReviewShopDataResponse) {
                        return dataResponseReviewShopDataResponse.getData();
                    }
                });
    }
}
