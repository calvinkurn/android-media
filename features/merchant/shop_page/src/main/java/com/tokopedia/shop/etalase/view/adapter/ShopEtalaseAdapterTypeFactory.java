package com.tokopedia.shop.etalase.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.etalase.view.adapter.viewholder.ShopEtalaseViewHolder;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseAdapterTypeFactory extends BaseAdapterTypeFactory {
    public int type(ShopEtalaseViewModel shopEtalaseViewModel) {
        return ShopEtalaseViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ShopEtalaseViewHolder.LAYOUT){
            return new ShopEtalaseViewHolder(parent);
        }else {
            return super.createViewHolder(parent, type);
        }
    }
}
