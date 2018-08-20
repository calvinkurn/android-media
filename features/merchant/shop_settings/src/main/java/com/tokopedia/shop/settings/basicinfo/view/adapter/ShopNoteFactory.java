package com.tokopedia.shop.settings.basicinfo.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteViewHolder;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopNoteFactory extends BaseShopNoteFactory {

    private ShopNoteViewHolder.OnShopNoteViewHolderListener onShopNoteViewHolderListener;
    public ShopNoteFactory( ShopNoteViewHolder.OnShopNoteViewHolderListener onShopNoteViewHolderListener) {
        this.onShopNoteViewHolderListener = onShopNoteViewHolderListener;
    }

    public int type(ShopNoteViewModel model) {
        return ShopNoteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ShopNoteViewHolder.LAYOUT) {
            return new ShopNoteViewHolder(parent, onShopNoteViewHolderListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}