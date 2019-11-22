package com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.CategoryFilterListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder.GridProductItemViewHolder;
import com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder.ImageSearchLoadingMoreViewHolder;
import com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder.CategoryFilterViewHolder;
import com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder.ImageSearchLoadingViewHolder;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListTypeFactoryImpl extends BaseAdapterTypeFactory implements ImageProductListTypeFactory {

    private final ProductListener itemClickListener;
    private CategoryFilterListener categoryFilterListener;

    public ImageProductListTypeFactoryImpl(ProductListener itemClickListener,
                                           CategoryFilterListener categoryFilterListener) {
        this.itemClickListener = itemClickListener;
        this.categoryFilterListener = categoryFilterListener;
    }

    @Override
    public int type(ProductItem productItem) {
        return GridProductItemViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel loadingModel) {
        return ImageSearchLoadingViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingMoreModel loadingMoreModel) {
        return ImageSearchLoadingMoreViewHolder.LAYOUT;
    }

    @Override
    public int type(CategoryFilterModel categoryFilterModel) {
        return CategoryFilterViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == GridProductItemViewHolder.LAYOUT) {
            viewHolder = new GridProductItemViewHolder(view, itemClickListener);
        } else if (type == ImageSearchLoadingMoreViewHolder.LAYOUT) {
            viewHolder = new ImageSearchLoadingMoreViewHolder(view);
        } else if (type == ImageSearchLoadingViewHolder.LAYOUT) {
            viewHolder = new ImageSearchLoadingViewHolder(view);
        } else if (type == CategoryFilterViewHolder.LAYOUT) {
            viewHolder = new CategoryFilterViewHolder(view, categoryFilterListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }

}
