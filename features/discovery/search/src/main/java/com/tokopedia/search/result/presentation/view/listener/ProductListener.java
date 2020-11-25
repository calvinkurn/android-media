package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.ProductItemViewModel;

public interface ProductListener {
    void onItemClicked(ProductItemViewModel item, int adapterPosition);

    void onThreeDotsClick(ProductItemViewModel item, int adapterPosition);

    void onProductImpressed(ProductItemViewModel item);
}