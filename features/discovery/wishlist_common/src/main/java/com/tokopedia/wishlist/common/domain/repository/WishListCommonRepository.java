package com.tokopedia.wishlist.common.domain.repository;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface WishListCommonRepository {

    Observable<List<String>> getWishList(String userId, List<String> productIdList);

}