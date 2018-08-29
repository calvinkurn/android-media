package com.tokopedia.shop.settings.etalase.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.touchhelper.OnStartDragListener;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleViewModel;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseReorderViewHolder;
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseTitleViewHolder;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.adapter.factory.BaseShopNoteFactory;
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteReorderViewHolder;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopEtalaseReorderFactory extends BaseShopEtalaseFactory {

    private OnStartDragListener onStartDragListener;
    public ShopEtalaseReorderFactory(OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
    }

    public int type(ShopEtalaseViewModel model) {
        return ShopEtalaseReorderViewHolder.Companion.getLAYOUT();
    }

    public int type(ShopEtalaseTitleViewModel model) {
        return ShopEtalaseTitleViewHolder.Companion.getLAYOUT();
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ShopEtalaseReorderViewHolder.Companion.getLAYOUT()) {
            return new ShopEtalaseReorderViewHolder(parent, onStartDragListener);
        } else if (type == ShopEtalaseTitleViewHolder.Companion.getLAYOUT()) {
            return new ShopEtalaseTitleViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}