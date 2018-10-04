package com.tokopedia.wishlist.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.wishlist.common.data.source.cloud.api.WishListCommonApi;
import com.tokopedia.wishlist.common.data.source.cloud.mapper.WishListProductListMapper;
import com.tokopedia.wishlist.common.data.source.cloud.model.WishListData;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author hendry on 4/4/17.
 */

public class WishListCommonCloudDataSource {

    private final WishListCommonApi wishListCommonApi;

    public WishListCommonCloudDataSource(WishListCommonApi reputationCommonApi) {
        this.wishListCommonApi = reputationCommonApi;
    }

    public Observable<Response<DataResponse<WishListData>>> getWishList(String userId, List<String> productIdList) {
        return wishListCommonApi.getWishList(userId, WishListProductListMapper.convertCommaValue(productIdList));
    }

}
