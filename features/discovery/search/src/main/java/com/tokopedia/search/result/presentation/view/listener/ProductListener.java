package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.ProductItemViewModel;

public interface ProductListener extends EmptyStateListener {
    void onItemClicked(ProductItemViewModel item, int adapterPosition);

    void onLongClick(ProductItemViewModel item, int adapterPosition);

    void onWishlistButtonClicked(final ProductItemViewModel productItem);

    void onProductImpressed(ProductItemViewModel item, int adapterPosition);
}