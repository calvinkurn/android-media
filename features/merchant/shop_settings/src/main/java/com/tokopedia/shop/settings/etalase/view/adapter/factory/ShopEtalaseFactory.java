package com.tokopedia.shop.settings.etalase.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseViewHolder;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.adapter.factory.BaseShopNoteFactory;
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteViewHolder;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopEtalaseFactory extends BaseShopEtalaseFactory {

    private ShopEtalaseViewHolder.OnShopEtalaseViewHolderListener onShopEtalaseViewHolderListener;
    public ShopEtalaseFactory(ShopEtalaseViewHolder.OnShopEtalaseViewHolderListener onShopEtalaseViewHolderListener) {
        this.onShopEtalaseViewHolderListener = onShopEtalaseViewHolderListener;
    }

    public int type(ShopEtalaseViewModel model) {
        return ShopEtalaseViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ShopEtalaseViewHolder.LAYOUT) {
            return new ShopEtalaseViewHolder(parent, onShopEtalaseViewHolderListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}