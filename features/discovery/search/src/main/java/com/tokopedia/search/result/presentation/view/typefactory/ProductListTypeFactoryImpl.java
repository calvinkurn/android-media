package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.search.result.presentation.model.GuidedSearchViewModel;
import com.tokopedia.search.result.presentation.model.HeaderViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.HeaderViewHolder;
import com.tokopedia.topads.sdk.base.Config;

public class ProductListTypeFactoryImpl extends ProductListTypeFactory {

    private final ProductListener itemClickListener;
    private final Config topAdsConfig;
    private final String searchQuery;

    public ProductListTypeFactoryImpl(ProductListener itemClickListener, Config config, String searchQuery) {
        this.itemClickListener = itemClickListener;
        this.topAdsConfig = config;
        this.searchQuery = searchQuery;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(GuidedSearchViewModel guidedSearchViewModel) {
        return GuidedSearchViewHolder.LAYOUT;
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
                return GridProductItemViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchModel emptySearchModel) {
        return EmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(TopAdsViewModel topAdsViewModel) {
        return TopAdsViewHolder.LAYOUT;
    }

    @Override
    public int type(RelatedSearchModel relatedSearchModel) {
        return RelatedSearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ListProductItemViewHolder.LAYOUT) {
            viewHolder = new ListProductItemViewHolder(view, itemClickListener, searchQuery);
        } else if (type == GridProductItemViewHolder.LAYOUT) {
            viewHolder = new GridProductItemViewHolder(view, itemClickListener, searchQuery);
        } else if (type == BigGridProductItemViewHolder.LAYOUT) {
            viewHolder = new BigGridProductItemViewHolder(view, itemClickListener, searchQuery);
        } else if(type == HeaderViewHolder.LAYOUT){
            viewHolder = new HeaderViewHolder(view, itemClickListener, searchQuery);
        } else if (type == EmptySearchViewHolder.LAYOUT) {
            viewHolder = new EmptySearchViewHolder(view, itemClickListener, topAdsConfig);
        } else if (type == GuidedSearchViewHolder.LAYOUT) {
            viewHolder = new GuidedSearchViewHolder(view, itemClickListener);
        } else if (type == TopAdsViewHolder.LAYOUT) {
            viewHolder = new TopAdsViewHolder(view, itemClickListener);
        } else if (type == RelatedSearchViewHolder.LAYOUT) {
            viewHolder = new RelatedSearchViewHolder(view, itemClickListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
