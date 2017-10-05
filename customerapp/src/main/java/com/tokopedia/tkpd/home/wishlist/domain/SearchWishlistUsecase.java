package com.tokopedia.tkpd.home.wishlist.domain;

import com.tokopedia.core.base.domain.DefaultUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.home.wishlist.data.WishlistDataRepository;
import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;

import rx.Observable;

/**
 * @author Kulomady on 2/20/17.
 */

public class SearchWishlistUsecase extends DefaultUseCase<DataWishlist> {
    public static final String KEY_QUERY = "query";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_PAGE = "page";

    private final WishlistRepository wishlistRepository;

    public SearchWishlistUsecase() {
        this.wishlistRepository = new WishlistDataRepository();
    }

    @Override
    public Observable<DataWishlist> createObservable(RequestParams requestParams) {
        String query = requestParams.getString(KEY_QUERY, "");
        String userId = requestParams.getString(KEY_USER_ID, "");
        int page = requestParams.getInt(KEY_PAGE, 1);
        return wishlistRepository.search_wishlist(userId, query, page);
    }
}
