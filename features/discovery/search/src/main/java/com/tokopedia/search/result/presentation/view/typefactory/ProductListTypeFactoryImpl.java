package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener;
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
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView;
import com.tokopedia.search.result.presentation.model.SeparatorDataView;
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView;
import com.tokopedia.search.result.presentation.model.SuggestionDataView;
import com.tokopedia.search.result.presentation.model.TickerDataView;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannedProductsEmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannedProductsTickerViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannerViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridInspirationCardViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BroadMatchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ChooseAddressViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.CpmViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.GlobalNavViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductEmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationTitleViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchInTokopediaViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductCountViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTopAdsImageViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SeparatorViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTitleViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridInspirationCardViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SuggestionViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TickerViewHolder;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.BannerListener;
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener;
import com.tokopedia.search.result.presentation.view.listener.ChooseAddressListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener;
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener;
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;
import com.tokopedia.search.result.presentation.view.listener.SearchInTokopediaListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.search.result.presentation.view.listener.TickerListener;
import com.tokopedia.search.result.presentation.view.listener.TopAdsImageViewListener;
import com.tokopedia.topads.sdk.base.Config;

public class ProductListTypeFactoryImpl extends BaseAdapterTypeFactory implements ProductListTypeFactory {

    private final ProductListener productListener;
    private final TickerListener tickerListener;
    private final SuggestionListener suggestionListener;
    private final GlobalNavListener globalNavListener;
    private final BannerAdsListener bannerAdsListener;
    private final EmptyStateListener emptyStateListener;
    private final RecommendationListener recommendationListener;
    private final InspirationCarouselListener inspirationCarouselListener;
    private final BroadMatchListener broadMatchListener;
    private final InspirationCardListener inspirationCardListener;
    private final SearchInTokopediaListener searchInTokopediaListener;
    private final SearchNavigationClickListener searchNavigationListener;
    private final TopAdsImageViewListener topAdsImageViewListener;
    private final ChooseAddressListener chooseAddressListener;
    private final BannerListener bannerListener;
    private final Config topAdsConfig;
    private int recyclerViewItem;

    public ProductListTypeFactoryImpl(ProductListener productListener,
                                      TickerListener tickerListener,
                                      SuggestionListener suggestionListener,
                                      GlobalNavListener globalNavListener,
                                      BannerAdsListener bannerAdsListener,
                                      EmptyStateListener emptyStateListener,
                                      RecommendationListener recommendationListener,
                                      InspirationCarouselListener inspirationCarouselListener,
                                      BroadMatchListener broadMatchListener,
                                      InspirationCardListener inspirationCardListener,
                                      SearchInTokopediaListener searchInTokopediaListener,
                                      SearchNavigationClickListener searchNavigationListener,
                                      TopAdsImageViewListener topAdsImageViewListener,
                                      ChooseAddressListener chooseAddressListener,
                                      BannerListener bannerListener,
                                      Config config) {

        this.productListener = productListener;
        this.tickerListener = tickerListener;
        this.suggestionListener = suggestionListener;
        this.globalNavListener = globalNavListener;
        this.bannerAdsListener = bannerAdsListener;
        this.emptyStateListener = emptyStateListener;
        this.recommendationListener = recommendationListener;
        this.topAdsConfig = config;
        this.inspirationCarouselListener = inspirationCarouselListener;
        this.broadMatchListener = broadMatchListener;
        this.inspirationCardListener = inspirationCardListener;
        this.searchInTokopediaListener = searchInTokopediaListener;
        this.searchNavigationListener = searchNavigationListener;
        this.topAdsImageViewListener = topAdsImageViewListener;
        this.chooseAddressListener = chooseAddressListener;
        this.bannerListener = bannerListener;
    }

    @Override
    public int getRecyclerViewItem() {
        return recyclerViewItem;
    }

    @Override
    public void setRecyclerViewItem(int recyclerViewItem) {
        this.recyclerViewItem = recyclerViewItem;
    }

    @Override
    public int type(CpmDataView cpmDataView) {
        return CpmViewHolder.LAYOUT;
    }

    @Override
    public int type(TickerDataView tickerDataView) {
        return TickerViewHolder.LAYOUT;
    }

    @Override
    public int type(SuggestionDataView suggestionDataView) {
        return SuggestionViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductItemDataView productItem) {
        switch (getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_LIST:
                return ListProductItemViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID:
                return BigGridProductItemViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID:
            default:
                return SmallGridProductItemViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchProductDataView emptySearchModel) {
        return ProductEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(GlobalNavDataView globalNavDataView) {
        return GlobalNavViewHolder.LAYOUT;
    }

    @Override
    public int type(InspirationCarouselDataView inspirationCarouselDataView) {
        return InspirationCarouselViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingMoreModel loadingMoreModel) {
        return SearchLoadingMoreViewHolder.LAYOUT;
    }

    @Override
    public int type(RecommendationTitleDataView titleViewModel) {
        return RecommendationTitleViewHolder.LAYOUT;
    }

    @Override
    public int type(RecommendationItemDataView recommendationItemDataView) {
        return RecommendationItemViewHolder.LAYOUT;
    }

    @Override
    public int type(BannedProductsEmptySearchDataView bannedProductsEmptySearchDataView) {
        return BannedProductsEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(BannedProductsTickerDataView bannedProductsTickerDataView) {
        return BannedProductsTickerViewHolder.LAYOUT;
    }

    @Override
    public int type(BroadMatchDataView broadMatchDataView) {
        return BroadMatchViewHolder.LAYOUT;
    }

    @Override
    public int type(InspirationCardDataView inspirationCardDataView) {
        switch (getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_LIST:
            case SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID:
                return BigGridInspirationCardViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID:
            default:
                return SmallGridInspirationCardViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(SeparatorDataView separatorDataView) {
        return SeparatorViewHolder.LAYOUT;
    }

    @Override
    public int type(SearchProductTitleDataView searchProductTitleDataView) {
        return SearchProductTitleViewHolder.LAYOUT;
    }

    @Override
    public int type(SearchInTokopediaDataView searchInTokopediaDataView) {
        return SearchInTokopediaViewHolder.LAYOUT;
    }

    @Override
    public int type(SearchProductCountDataView searchProductCountDataView) {
        return SearchProductCountViewHolder.LAYOUT;
    }

    @Override
    public int type(SearchProductTopAdsImageDataView searchProductTopAdsImageDataView) {
        return SearchProductTopAdsImageViewHolder.LAYOUT;
    }

    @Override
    public int type(ChooseAddressDataView chooseAddressDataView) {
        return ChooseAddressViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerDataView bannerDataView) {
        return BannerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;

        if (type == ListProductItemViewHolder.LAYOUT) {
            viewHolder = new ListProductItemViewHolder(view, productListener);
        } else if(type == SmallGridProductItemViewHolder.LAYOUT) {
            viewHolder = new SmallGridProductItemViewHolder(view, productListener);
        } else if (type == BigGridProductItemViewHolder.LAYOUT) {
            viewHolder = new BigGridProductItemViewHolder(view, productListener);
        } else if(type == CpmViewHolder.LAYOUT){
            viewHolder = new CpmViewHolder(view, bannerAdsListener);
        } else if(type == TickerViewHolder.LAYOUT){
            viewHolder = new TickerViewHolder(view, tickerListener);
        } else if(type == SuggestionViewHolder.LAYOUT){
            viewHolder = new SuggestionViewHolder(view, suggestionListener);
        } else if (type == ProductEmptySearchViewHolder.LAYOUT) {
            viewHolder = new ProductEmptySearchViewHolder(view, emptyStateListener, bannerAdsListener, topAdsConfig);
        } else if (type == GlobalNavViewHolder.LAYOUT) {
            viewHolder = new GlobalNavViewHolder(view, globalNavListener);
        } else if (type == InspirationCarouselViewHolder.LAYOUT) {
            viewHolder = new InspirationCarouselViewHolder(view, inspirationCarouselListener);
        } else if (type == SearchLoadingMoreViewHolder.LAYOUT) {
            viewHolder = new SearchLoadingMoreViewHolder(view);
        } else if(type == RecommendationTitleViewHolder.LAYOUT){
            viewHolder = new RecommendationTitleViewHolder(view);
        } else if(type == RecommendationItemViewHolder.LAYOUT){
            viewHolder = new RecommendationItemViewHolder(view, recommendationListener);
        } else if (type == BannedProductsEmptySearchViewHolder.LAYOUT) {
            viewHolder = new BannedProductsEmptySearchViewHolder(view);
        } else if (type == BannedProductsTickerViewHolder.LAYOUT) {
            viewHolder = new BannedProductsTickerViewHolder(view);
        } else if (type == BroadMatchViewHolder.LAYOUT) {
            viewHolder = new BroadMatchViewHolder(view, broadMatchListener);
        } else if (type == SmallGridInspirationCardViewHolder.LAYOUT) {
            viewHolder = new SmallGridInspirationCardViewHolder(view, inspirationCardListener);
        } else if (type == BigGridInspirationCardViewHolder.LAYOUT) {
            viewHolder = new BigGridInspirationCardViewHolder(view, inspirationCardListener);
        } else if (type == SeparatorViewHolder.LAYOUT) {
            viewHolder = new SeparatorViewHolder(view);
        } else if (type == SearchProductTitleViewHolder.LAYOUT) {
            viewHolder = new SearchProductTitleViewHolder(view);
        } else if (type == SearchInTokopediaViewHolder.LAYOUT) {
            viewHolder = new SearchInTokopediaViewHolder(view, searchInTokopediaListener);
        } else if (type == SearchProductCountViewHolder.LAYOUT) {
            viewHolder = new SearchProductCountViewHolder(view, searchNavigationListener);
        } else if (type == SearchProductTopAdsImageViewHolder.LAYOUT) {
            viewHolder = new SearchProductTopAdsImageViewHolder(view, topAdsImageViewListener);
        } else if (type == ChooseAddressViewHolder.LAYOUT) {
            viewHolder = new ChooseAddressViewHolder(view, chooseAddressListener, searchNavigationListener);
        } else if (type == BannerViewHolder.LAYOUT) {
            viewHolder = new BannerViewHolder(view, bannerListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
