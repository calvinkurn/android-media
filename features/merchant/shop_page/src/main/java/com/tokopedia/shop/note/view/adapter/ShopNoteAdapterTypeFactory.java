package com.tokopedia.shop.note.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteViewHolder;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopNoteAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final EmptyViewHolder.Callback emptyNoteOnClickListener;

    public ShopNoteAdapterTypeFactory(EmptyViewHolder.Callback emptyNoteOnClickListener) {
        this.emptyNoteOnClickListener = emptyNoteOnClickListener;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    public int type(ShopNoteViewModel shopNoteViewModel) {
        return ShopNoteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        if (viewType == EmptyViewHolder.LAYOUT) {
            return new EmptyViewHolder(view, emptyNoteOnClickListener);
        }  else if (viewType == ShopNoteViewHolder.LAYOUT) {
            return new ShopNoteViewHolder(view);
        } else {
            return super.createViewHolder(view, viewType);
        }
    }
}
