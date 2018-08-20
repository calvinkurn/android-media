package com.tokopedia.shop.settings.notes.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.touchhelper.OnStartDragListener;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteReorderViewHolder;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopNoteReorderFactory extends BaseShopNoteFactory {

    private OnStartDragListener onStartDragListener;
    public ShopNoteReorderFactory(OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
    }

    public int type(ShopNoteViewModel model) {
        return ShopNoteReorderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ShopNoteReorderViewHolder.LAYOUT) {
            return new ShopNoteReorderViewHolder(parent, onStartDragListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}