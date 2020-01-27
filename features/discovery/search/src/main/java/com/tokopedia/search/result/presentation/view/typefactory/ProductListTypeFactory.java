package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.result.presentation.model.*;

public interface ProductListTypeFactory extends SearchSectionTypeFactory {
    int type(ProductItemViewModel productItem);

    int type(CpmViewModel cpmViewModel);

    int type(TickerViewModel tickerViewModel);

    int type(SuggestionViewModel suggestionViewModel);

    int type(QuickFilterViewModel quickFilterViewModel);

    int type(TopAdsViewModel topAdsViewModel);

    int type(RelatedSearchViewModel relatedSearchModel);

    int type(GlobalNavViewModel globalNavViewModel);

    int type(RecommendationTitleViewModel titleViewModel);

    int type(RecommendationItemViewModel recommendationItemViewModel);

    int type(BannedProductsEmptySearchViewModel bannedProductsEmptySearchViewModel);

    int type(BannedProductsTickerViewModel bannedProductsTickerViewModel);

    AbstractViewHolder createViewHolder(View view, int type);
}
