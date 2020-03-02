package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener;
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsTickerViewModel;
import com.tokopedia.search.result.presentation.model.CpmViewModel;
import com.tokopedia.search.result.presentation.model.EmptySearchProductViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.QuickFilterViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationTitleViewModel;
import com.tokopedia.search.result.presentation.model.RelatedSearchViewModel;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;
import com.tokopedia.search.result.presentation.model.TickerViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannedProductsEmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannedProductsTickerViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.CpmViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.GlobalNavViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductEmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.QuickFilterViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationTitleViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RelatedSearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SuggestionViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TickerViewHolder;
import com.tokopedia.search.result.presentation.view.listener.BannedProductsRedirectToBrowserListener;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterListener;
import com.tokopedia.search.result.presentation.view.listener.RelatedSearchListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.search.result.presentation.view.listener.TickerListener;
import com.tokopedia.topads.sdk.base.Config;

public class ProductListTypeFactoryImpl extends BaseAdapterTypeFactory implements ProductListTypeFactory {

    private final ProductListener productListener;
    private final TickerListener tickerListener;
    private final SuggestionListener suggestionListener;
    private final RelatedSearchListener relatedSearchListener;
    private final QuickFilterListener quickFilterListener;
    private final GlobalNavListener globalNavListener;
    private final BannerAdsListener bannerAdsListener;
    private final EmptyStateListener emptyStateListener;
    private final RecommendationListener recommendationListener;
    private final BannedProductsRedirectToBrowserListener bannedProductsRedirectToBrowserListener;
    private final Config topAdsConfig;
    private int recyclerViewItem;

    public ProductListTypeFactoryImpl(ProductListener productListener,
                                      TickerListener tickerListener,
                                      SuggestionListener suggestionListener,
                                      RelatedSearchListener relatedSearchListener,
                                      QuickFilterListener quickFilterListener,
                                      GlobalNavListener globalNavListener,
                                      BannerAdsListener bannerAdsListener,
                                      EmptyStateListener emptyStateListener,
                                      RecommendationListener recommendationListener,
                                      BannedProductsRedirectToBrowserListener bannedProductsRedirectToBrowserListener,
                                      Config config) {

        this.productListener = productListener;
        this.tickerListener = tickerListener;
        this.suggestionListener = suggestionListener;
        this.relatedSearchListener = relatedSearchListener;
        this.quickFilterListener = quickFilterListener;
        this.globalNavListener = globalNavListener;
        this.bannerAdsListener = bannerAdsListener;
        this.emptyStateListener = emptyStateListener;
        this.recommendationListener = recommendationListener;
        this.bannedProductsRedirectToBrowserListener = bannedProductsRedirectToBrowserListener;
        this.topAdsConfig = config;
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
    public int type(CpmViewModel cpmViewModel) {
        return CpmViewHolder.LAYOUT;
    }

    @Override
    public int type(TickerViewModel tickerViewModel) {
        return TickerViewHolder.LAYOUT;
    }

    @Override
    public int type(SuggestionViewModel suggestionViewModel) {
        return SuggestionViewHolder.LAYOUT;
    }

    @Override
    public int type(QuickFilterViewModel quickFilterViewModel) {
        return QuickFilterViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductItemViewModel productItem) {
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
    public int type(EmptySearchProductViewModel emptySearchModel) {
        return ProductEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(RelatedSearchViewModel relatedSearchModel) {
        return RelatedSearchViewHolder.LAYOUT;
    }

    @Override
    public int type(GlobalNavViewModel globalNavViewModel) {
        return GlobalNavViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingMoreModel loadingMoreModel) {
        return SearchLoadingMoreViewHolder.LAYOUT;
    }

    @Override
    public int type(RecommendationTitleViewModel titleViewModel) {
        return RecommendationTitleViewHolder.LAYOUT;
    }

    @Override
    public int type(RecommendationItemViewModel recommendationItemViewModel) {
        return RecommendationItemViewHolder.LAYOUT;
    }

    @Override
    public int type(BannedProductsEmptySearchViewModel bannedProductsEmptySearchViewModel) {
        return BannedProductsEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(BannedProductsTickerViewModel bannedProductsTickerViewModel) {
        return BannedProductsTickerViewHolder.LAYOUT;
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
        } else if(type == QuickFilterViewHolder.LAYOUT){
            viewHolder = new QuickFilterViewHolder(view, quickFilterListener);
        } else if (type == ProductEmptySearchViewHolder.LAYOUT) {
            viewHolder = new ProductEmptySearchViewHolder(view, emptyStateListener, bannerAdsListener, topAdsConfig);
        } else if (type == RelatedSearchViewHolder.LAYOUT) {
            viewHolder = new RelatedSearchViewHolder(view, relatedSearchListener);
        } else if (type == GlobalNavViewHolder.LAYOUT) {
            viewHolder = new GlobalNavViewHolder(view, globalNavListener);
        } else if (type == SearchLoadingMoreViewHolder.LAYOUT) {
            viewHolder = new SearchLoadingMoreViewHolder(view);
        } else if(type == RecommendationTitleViewHolder.LAYOUT){
            viewHolder = new RecommendationTitleViewHolder(view);
        } else if(type == RecommendationItemViewHolder.LAYOUT){
            viewHolder = new RecommendationItemViewHolder(view, recommendationListener);
        } else if (type == BannedProductsEmptySearchViewHolder.LAYOUT) {
            viewHolder = new BannedProductsEmptySearchViewHolder(view, bannedProductsRedirectToBrowserListener);
        } else if (type == BannedProductsTickerViewHolder.LAYOUT) {
            viewHolder = new BannedProductsTickerViewHolder(view, bannedProductsRedirectToBrowserListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
