package com.tokopedia.home.explore.view.adapter;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.viewholder.CategoryFavoriteViewHolder;
import com.tokopedia.home.explore.view.adapter.viewholder.CategoryGridListViewHolder;
import com.tokopedia.home.explore.view.adapter.viewholder.MyShopViewHolder;
import com.tokopedia.home.explore.view.adapter.viewholder.SellViewHolder;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryFavoriteViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.MyShopViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreAdapter extends BaseAdapterTypeFactory implements TypeFactory {

    private final CategoryAdapterListener listener;
    private final FragmentManager fragmentManager;

    public ExploreAdapter(FragmentManager fragmentManager, CategoryAdapterListener listener) {
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int type(CategoryGridListViewModel viewModel) {
        return CategoryGridListViewHolder.LAYOUT;
    }

    @Override
    public int type(SellViewModel viewModel) {
        return SellViewHolder.LAYOUT;
    }

    @Override
    public int type(CategoryFavoriteViewModel viewModel) {
        return CategoryFavoriteViewHolder.LAYOUT;
    }

    @Override
    public int type(MyShopViewModel viewModel) {
        return MyShopViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == CategoryGridListViewHolder.LAYOUT) {
            return new CategoryGridListViewHolder(parent, listener);
        } if (type == SellViewHolder.LAYOUT) {
            return new SellViewHolder(parent, listener);
        } if (type == CategoryFavoriteViewHolder.LAYOUT) {
            return new CategoryFavoriteViewHolder(parent, listener);
        } if (type == MyShopViewHolder.LAYOUT) {
            return new MyShopViewHolder(parent, listener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
