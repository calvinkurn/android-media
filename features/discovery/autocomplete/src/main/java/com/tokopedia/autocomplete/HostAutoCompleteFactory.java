package com.tokopedia.autocomplete;

import androidx.fragment.app.FragmentManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.adapter.ItemClickListener;

public class HostAutoCompleteFactory extends BaseAdapterTypeFactory
        implements HostAutoCompleteTypeFactory {

    private final ItemClickListener clickListener;
    private final FragmentManager fragmentManager;

    public HostAutoCompleteFactory(ItemClickListener clickListener,
                                   FragmentManager fragmentManager) {
        this.clickListener = clickListener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int type(DefaultAutoCompleteViewModel viewModel) {
        return DefaultAutoCompleteViewHolder.LAYOUT;
    }

    @Override
    public int type(TabAutoCompleteViewModel viewModel) {
        return TabAutoCompleteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == DefaultAutoCompleteViewHolder.LAYOUT) {
            viewHolder = new DefaultAutoCompleteViewHolder(view, clickListener);
        } else if (viewType == TabAutoCompleteViewHolder.LAYOUT) {
            viewHolder = new TabAutoCompleteViewHolder(view, fragmentManager, clickListener);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }
}
