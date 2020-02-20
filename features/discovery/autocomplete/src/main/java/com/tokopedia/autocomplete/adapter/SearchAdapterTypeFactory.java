package com.tokopedia.autocomplete.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.viewmodel.AutoCompleteSearch;
import com.tokopedia.autocomplete.viewmodel.CategorySearch;
import com.tokopedia.autocomplete.viewmodel.DigitalSearch;
import com.tokopedia.autocomplete.viewmodel.HotlistSearch;
import com.tokopedia.autocomplete.viewmodel.InCategorySearch;
import com.tokopedia.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.autocomplete.viewmodel.ProfileSearch;
import com.tokopedia.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.autocomplete.viewmodel.RecentViewSearch;
import com.tokopedia.autocomplete.viewmodel.ShopSearch;
import com.tokopedia.autocomplete.viewmodel.TitleSearch;
import com.tokopedia.autocomplete.viewmodel.TopProfileSearch;

/**
 * @author erry on 20/02/17.
 */

public class SearchAdapterTypeFactory extends BaseAdapterTypeFactory implements SearchTypeFactory {

    private static final String DEFAULT_INSTANCE_TYPE = "unknown";
    private final ItemClickListener clickListener;
    private final String tabName;

    public SearchAdapterTypeFactory(ItemClickListener clickListener) {
        this.tabName = DEFAULT_INSTANCE_TYPE;
        this.clickListener = clickListener;
    }

    public SearchAdapterTypeFactory(String tabName, ItemClickListener clickListener) {
        this.tabName = tabName;
        this.clickListener = clickListener;
    }

    @Override
    public int type(TitleSearch viewModel) {
        return TitleViewHolder.LAYOUT;
    }

    @Override
    public int type(DigitalSearch viewModel) {
        return DigitalViewHolder.LAYOUT;
    }

    @Override
    public int type(CategorySearch viewModel) {
        return CategoryViewHolder.LAYOUT;
    }

    @Override
    public int type(InCategorySearch viewModel) {
        return InCategoryViewHolder.LAYOUT;
    }

    @Override
    public int type(PopularSearch viewModel) {
        return PopularViewHolder.LAYOUT;
    }

    @Override
    public int type(RecentSearch viewModel) {
        return RecentViewHolder.LAYOUT;
    }

    @Override
    public int type(ShopSearch viewModel) {
        return ShopViewHolder.LAYOUT;
    }

    @Override
    public int type(AutoCompleteSearch viewModel) {
        return AutoCompleteViewHolder.LAYOUT;
    }

    @Override
    public int type(RecentViewSearch viewModel) {
        return RecentViewViewHolder.LAYOUT;
    }

    @Override
    public int type(HotlistSearch viewModel) {
        return HotlistViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(ProfileSearch viewModel) {
        return ProfileViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(TopProfileSearch viewModel) {
        return TopProfileViewHolder.Companion.getLAYOUT();
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if(type == DigitalViewHolder.LAYOUT) {
            viewHolder = new DigitalViewHolder(parent, clickListener);
        } else if(type == TitleViewHolder.LAYOUT) {
            viewHolder = new TitleViewHolder(parent, clickListener);
        } else if(type == CategoryViewHolder.LAYOUT) {
            viewHolder = new CategoryViewHolder(parent, clickListener);
        } else if(type == InCategoryViewHolder.LAYOUT) {
            viewHolder = new InCategoryViewHolder(parent, clickListener, tabName);
        } else if(type == ShopViewHolder.LAYOUT) {
            viewHolder = new ShopViewHolder(parent, clickListener, tabName);
        } else if(type == AutoCompleteViewHolder.LAYOUT) {
            viewHolder = new AutoCompleteViewHolder(parent, clickListener, tabName);
        } else if(type == PopularViewHolder.LAYOUT) {
            viewHolder = new PopularViewHolder(parent, clickListener);
        } else if(type == RecentViewHolder.LAYOUT) {
            viewHolder = new RecentViewHolder(parent, clickListener);
        } else if(type == RecentViewViewHolder.LAYOUT) {
            viewHolder = new RecentViewViewHolder(parent, clickListener);
        } else if(type == HotlistViewHolder.Companion.getLAYOUT()) {
            viewHolder = new HotlistViewHolder(parent, clickListener);
        } else if(type == ProfileViewHolder.Companion.getLAYOUT()) {
            viewHolder = new ProfileViewHolder(parent, clickListener);
        } else if(type == TopProfileViewHolder.Companion.getLAYOUT()) {
            viewHolder = new TopProfileViewHolder(parent, clickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }

}