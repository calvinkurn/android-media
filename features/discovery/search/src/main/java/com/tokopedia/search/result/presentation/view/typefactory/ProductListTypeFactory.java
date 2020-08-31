package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsTickerViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel;
import com.tokopedia.search.result.presentation.model.CpmViewModel;
import com.tokopedia.search.result.presentation.model.EmptySearchProductViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCardViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationTitleViewModel;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;
import com.tokopedia.search.result.presentation.model.TickerViewModel;

public interface ProductListTypeFactory {
    int type(ProductItemViewModel productItem);

    int type(CpmViewModel cpmViewModel);

    int type(TickerViewModel tickerViewModel);

    int type(SuggestionViewModel suggestionViewModel);

    int type(GlobalNavViewModel globalNavViewModel);

    int type(InspirationCarouselViewModel inspirationCarouselViewModel);

    int type(RecommendationTitleViewModel titleViewModel);

    int type(RecommendationItemViewModel recommendationItemViewModel);

    int type(BannedProductsEmptySearchViewModel bannedProductsEmptySearchViewModel);

    int type(BannedProductsTickerViewModel bannedProductsTickerViewModel);

    int type(EmptySearchProductViewModel emptySearchViewModel);

    int type(BroadMatchViewModel broadMatchViewModel);

    int type(InspirationCardViewModel inspirationCardViewModel);

    int getRecyclerViewItem();

    void setRecyclerViewItem(int recyclerViewItem);

    AbstractViewHolder createViewHolder(View view, int type);
}
