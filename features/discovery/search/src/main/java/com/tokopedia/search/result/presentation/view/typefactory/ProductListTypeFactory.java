package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchDataView;
import com.tokopedia.search.result.presentation.model.BannedProductsTickerDataView;
import com.tokopedia.search.result.presentation.model.BannerDataView;
import com.tokopedia.search.result.presentation.model.BroadMatchDataView;
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView;
import com.tokopedia.search.result.presentation.model.CpmDataView;
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView;
import com.tokopedia.search.result.presentation.model.GlobalNavDataView;
import com.tokopedia.search.result.presentation.model.InspirationCardDataView;
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView;
import com.tokopedia.search.result.presentation.model.ProductItemDataView;
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView;
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView;
import com.tokopedia.search.result.presentation.model.SearchInTokopediaDataView;
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView;
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView;
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView;
import com.tokopedia.search.result.presentation.model.SeparatorDataView;
import com.tokopedia.search.result.presentation.model.SuggestionDataView;
import com.tokopedia.search.result.presentation.model.TickerDataView;

public interface ProductListTypeFactory {
    int type(ProductItemDataView productItem);

    int type(CpmDataView cpmDataView);

    int type(TickerDataView tickerDataView);

    int type(SuggestionDataView suggestionDataView);

    int type(GlobalNavDataView globalNavDataView);

    int type(InspirationCarouselDataView inspirationCarouselDataView);

    int type(RecommendationTitleDataView titleViewModel);

    int type(RecommendationItemDataView recommendationItemDataView);

    int type(BannedProductsEmptySearchDataView bannedProductsEmptySearchDataView);

    int type(BannedProductsTickerDataView bannedProductsTickerDataView);

    int type(EmptySearchProductDataView emptySearchViewModel);

    int type(BroadMatchDataView broadMatchDataView);

    int type(InspirationCardDataView inspirationCardDataView);

    int type(SearchProductTitleDataView searchProductTitleDataView);

    int type(SeparatorDataView separatorDataView);

    int type(SearchInTokopediaDataView searchInTokopediaDataView);

    int type(SearchProductCountDataView searchProductCountDataView);

    int type(SearchProductTopAdsImageDataView searchProductTopAdsImageDataView);

    int type(ChooseAddressDataView chooseAddressDataView);

    int type(BannerDataView bannerDataView);

    int getRecyclerViewItem();

    void setRecyclerViewItem(int recyclerViewItem);

    AbstractViewHolder createViewHolder(View view, int type);
}
