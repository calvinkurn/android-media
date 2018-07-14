package com.tokopedia.shop.note.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.shop.note.view.adapter.viewholder.OldShopNoteViewHolder;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

@Deprecated
public class OldShopNoteAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final EmptyViewHolder.Callback emptyNoteOnClickListener;

    public OldShopNoteAdapterTypeFactory(EmptyViewHolder.Callback emptyNoteOnClickListener) {
        this.emptyNoteOnClickListener = emptyNoteOnClickListener;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    public int type(ShopNoteViewModel shopNoteViewModel) {
        return OldShopNoteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        if (viewType == EmptyViewHolder.LAYOUT) {
            return new EmptyViewHolder(view, emptyNoteOnClickListener);
        }  else if (viewType == OldShopNoteViewHolder.LAYOUT) {
            return new OldShopNoteViewHolder(view);
        } else {
            return super.createViewHolder(view, viewType);
        }
    }
}
