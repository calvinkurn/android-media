package com.tokopedia.tkpd.tkpdreputation.review.product.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.utils.GetData;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewProductGetStarCountCloud {

    public static final String PRODUCT_ID = "product_id";
    private ReviewProductService reviewProductService;

    public ReviewProductGetStarCountCloud(ReviewProductService reviewProductService) {
        this.reviewProductService = reviewProductService;
    }

    public Observable<DataResponseReviewStarCount> getReviewStarCount(String productId) {
        return reviewProductService.getApi().getReviewStarCount(generateParams(productId))
                .map(new GetData<DataResponse<DataResponseReviewStarCount>>())
                .map(new Func1<DataResponse<DataResponseReviewStarCount>, DataResponseReviewStarCount>() {
                    @Override
                    public DataResponseReviewStarCount call(DataResponse<DataResponseReviewStarCount> dataResponseReviewStarCountDataResponse) {
                        return dataResponseReviewStarCountDataResponse.getData();
                    }
                });
    }

    private Map<String,String> generateParams(String productId) {
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_ID, productId);
        return params;
    }
}
