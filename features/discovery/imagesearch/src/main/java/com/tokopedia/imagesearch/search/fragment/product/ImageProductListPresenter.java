package com.tokopedia.imagesearch.search.fragment.product;

import java.util.List;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListPresenter extends CustomerPresenter<ImageProductListFragmentView> {

    void handleWishlistButtonClicked(ProductItem productItem);

    void attachView(ImageProductListFragmentView viewListener, WishListActionListener wishlistActionListener);

    void initData(List<Visitable> data);

    void loadMoreData(int page);
}
