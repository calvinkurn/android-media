package com.tokopedia.sellerapp.drawer;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.view.DrawerAdapter;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.var.TkpdState;

import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.sellerapp.R;

import java.util.ArrayList;

/**
 * Created by hendry on 19/11/18.
 */
public class SellerDrawerAdapter extends DrawerAdapter {

    private boolean isGoldMerchant;
    private boolean isFlashSaleVisible;
    private Context context;

    public SellerDrawerAdapter(Context context,
                               DrawerItemDataBinder.DrawerItemListener itemListener,
                               LocalCacheHandler drawerCache) {
        super(context, itemListener, drawerCache);
        this.context = context;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public boolean isFlashSaleVisible() {
        return isFlashSaleVisible;
    }

    public void setFlashSaleVisible(boolean flashSaleVisible) {
        isFlashSaleVisible = flashSaleVisible;
    }

    @Override
    public void setData(ArrayList<DrawerItem> data) {
        super.setData(data);
        renderFlashSaleDrawer();
    }

    public void renderFlashSaleDrawer() {
        if (isFlashSaleVisible) {
            ArrayList<DrawerItem> items = getData();
            int size = items.size();
            for (int i = 0; i < size; i++) {
                if (items.get(i).id == TkpdState.DrawerPosition.SELLER_TOP_ADS &&
                        items.get(i + 1).id != TkpdState.DrawerPosition.SELLER_FLASH_SALE) {
                    items.add(i + 1, new DrawerItem(context.getString(R.string.drawer_title_flash_sale),
                            R.drawable.ic_flash_sale_grey,
                            TkpdState.DrawerPosition.SELLER_FLASH_SALE,
                            true));
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }
}
