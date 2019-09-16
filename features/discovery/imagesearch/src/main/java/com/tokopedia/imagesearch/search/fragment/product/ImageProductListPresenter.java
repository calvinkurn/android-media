package com.tokopedia.imagesearch.search.fragment.product;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

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
