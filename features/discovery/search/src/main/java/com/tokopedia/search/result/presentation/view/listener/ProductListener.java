package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;

public interface ProductListener extends EmptyStateListener {
    void onItemClicked(ProductItemViewModel item, int adapterPosition);

    void onLongClick(ProductItemViewModel item, int adapterPosition);

    void onWishlistButtonClicked(final ProductItemViewModel productItem);

    void onSuggestionClicked(String suggestedQuery);

    void onSearchGuideClicked(String keyword);

    void onRelatedSearchClicked(String keyword);

    void onQuickFilterSelected(Option option);

    boolean isQuickFilterSelected(Option option);

    void onProductImpressed(ProductItemViewModel item, int adapterPosition);

    void onGlobalNavWidgetClicked(GlobalNavViewModel.Item item, String keyword);

    void onGlobalNavWidgetClickSeeAll(String applink, String url);
}