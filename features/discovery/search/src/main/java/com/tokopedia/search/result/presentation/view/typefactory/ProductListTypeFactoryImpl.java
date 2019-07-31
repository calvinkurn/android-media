package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.HeaderViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.RelatedSearchViewModel;
import com.tokopedia.search.result.presentation.model.TopAdsViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.EmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.GlobalNavViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.HeaderViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductEmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RelatedSearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TopAdsViewHolder;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.GlobalNavWidgetListener;
import com.tokopedia.search.result.presentation.view.listener.GuidedSearchListener;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterListener;
import com.tokopedia.search.result.presentation.view.listener.RelatedSearchListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.topads.sdk.base.Config;

public class ProductListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements ProductListTypeFactory {

    private final ProductListener productListener;
    private final SuggestionListener suggestionListener;
    private final GuidedSearchListener guidedSearchListener;
    private final RelatedSearchListener relatedSearchListener;
    private final QuickFilterListener quickFilterListener;
    private final GlobalNavWidgetListener globalNavWidgetListener;
    private final BannerAdsListener bannerAdsListener;
    private final EmptyStateListener emptyStateListener;
    private final Config topAdsConfig;

    public ProductListTypeFactoryImpl(ProductListener productListener,
                                      SuggestionListener suggestionListener,
                                      GuidedSearchListener guidedSearchListener,
                                      RelatedSearchListener relatedSearchListener,
                                      QuickFilterListener quickFilterListener,
                                      GlobalNavWidgetListener globalNavWidgetListener,
                                      BannerAdsListener bannerAdsListener,
                                      EmptyStateListener emptyStateListener,
                                      Config config) {

        this.productListener = productListener;
        this.suggestionListener = suggestionListener;
        this.guidedSearchListener = guidedSearchListener;
        this.relatedSearchListener = relatedSearchListener;
        this.quickFilterListener = quickFilterListener;
        this.globalNavWidgetListener = globalNavWidgetListener;
        this.bannerAdsListener = bannerAdsListener;
        this.emptyStateListener = emptyStateListener;
        this.topAdsConfig = config;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductItemViewModel productItem) {
        switch (getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_PRODUCT:
                return ListProductItemViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_1:
                return BigGridProductItemViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return SmallGridProductItemViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchViewModel emptySearchModel) {
        return ProductEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(TopAdsViewModel topAdsViewModel) {
        return TopAdsViewHolder.LAYOUT;
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
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;

        if (type == ListProductItemViewHolder.LAYOUT) {
            viewHolder = new ListProductItemViewHolder(view, productListener);
        } else if(type == SmallGridProductItemViewHolder.LAYOUT) {
            viewHolder = new SmallGridProductItemViewHolder(view, productListener);
        } else if (type == BigGridProductItemViewHolder.LAYOUT) {
            viewHolder = new BigGridProductItemViewHolder(view, productListener);
        } else if(type == HeaderViewHolder.LAYOUT){
            viewHolder = new HeaderViewHolder(view, suggestionListener, quickFilterListener,
                    guidedSearchListener, bannerAdsListener);
        } else if (type == ProductEmptySearchViewHolder.LAYOUT) {
            viewHolder = new ProductEmptySearchViewHolder(view, emptyStateListener, bannerAdsListener, topAdsConfig);
        } else if (type == TopAdsViewHolder.LAYOUT) {
            viewHolder = new TopAdsViewHolder(view, productListener);
        } else if (type == RelatedSearchViewHolder.LAYOUT) {
            viewHolder = new RelatedSearchViewHolder(view, relatedSearchListener);
        } else if (type == GlobalNavViewHolder.LAYOUT) {
            viewHolder = new GlobalNavViewHolder(view, globalNavWidgetListener);
        } else if (type == SearchLoadingMoreViewHolder.LAYOUT) {
            viewHolder = new SearchLoadingMoreViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
