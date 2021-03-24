package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.ProductItemDataView;

public interface ProductListener {
    void onItemClicked(ProductItemDataView item, int adapterPosition);

    void onThreeDotsClick(ProductItemDataView item, int adapterPosition);

    void onProductImpressed(ProductItemDataView item, int adapterPosition);
}