package com.tokopedia.imagesearch.search.fragment.product.adapter.listener;


import androidx.annotation.Nullable;

import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;

import java.util.List;

public interface ProductListener {
    void onItemClicked(ProductItem item, int adapterPosition);

    void onLongClick(ProductItem item, int adapterPosition);

    void onWishlistButtonClicked(final ProductItem productItem);

    void onProductImpressed(ProductItem item, int adapterPosition);

    boolean isUserHasLogin();
    String getUserId();
}
