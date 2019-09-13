package com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder.GridProductItemViewHolder;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListTypeFactoryImpl extends BaseAdapterTypeFactory implements ImageProductListTypeFactory {

    private final ProductListener itemClickListener;
    private final Config topAdsConfig;
    private final String searchQuery;

    public ImageProductListTypeFactoryImpl(ProductListener itemClickListener, Config config, String searchQuery) {
        this.itemClickListener = itemClickListener;
        this.topAdsConfig = config;
        this.searchQuery = searchQuery;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(TopAdsViewModel topAdsViewModel) {
        return TopAdsViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductItem productItem) {
        return GridProductItemViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptySearchModel emptySearchModel) {
        return ImageEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == GridProductItemViewHolder.LAYOUT) {
            viewHolder = new GridProductItemViewHolder(view, itemClickListener, searchQuery);
        } else if(type == HeaderViewHolder.LAYOUT){
            viewHolder = new HeaderViewHolder(view, itemClickListener, searchQuery);
        } else if (type == ImageEmptySearchViewHolder.LAYOUT) {
            viewHolder = new ImageEmptySearchViewHolder(view, itemClickListener, topAdsConfig);
        } else if (type == TopAdsViewHolder.LAYOUT) {
            viewHolder = new TopAdsViewHolder(view, itemClickListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }

}
