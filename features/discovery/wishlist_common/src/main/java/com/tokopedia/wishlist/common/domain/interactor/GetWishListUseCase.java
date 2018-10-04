package com.tokopedia.wishlist.common.domain.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.domain.repository.WishListCommonRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetWishListUseCase extends UseCase<List<String>> {

    private static final String USER_ID = "USER_ID";
    private static final String PRODUCT_ID_LIST = "PRODUCT_ID_LIST";

    private WishListCommonRepository wishListCommonRepository;

    public GetWishListUseCase(WishListCommonRepository wishListCommonRepository) {
        this.wishListCommonRepository = wishListCommonRepository;
    }

    @Override
    public Observable<List<String>> createObservable(RequestParams requestParams) {
        String userId = requestParams.getString(USER_ID, null);
        List<String> productIdList = (List<String>) requestParams.getObject(PRODUCT_ID_LIST);
        return wishListCommonRepository.getWishList(userId, productIdList);
    }

    public static RequestParams createRequestParam(String userId, List<String> productIdList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(USER_ID, userId);
        requestParams.putObject(PRODUCT_ID_LIST, productIdList);
        return requestParams;
    }
}
