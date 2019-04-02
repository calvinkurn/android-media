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
        renderGMDrawer();
        renderFlashSaleDrawer();
    }

    public void renderGMDrawer() {
        DrawerGroup goldMerchantMenu = getGoldMerchantMenu();
        goldMerchantMenu.setExpanded(false);

        // find gold merchant index based on drawerposition
        int goldMerchantIndex = -1;
        ArrayList<DrawerItem> adapterData = getData();
        int size = adapterData.size();
        for (int i = 0; i < size; i++) {
            if (adapterData.get(i).getId() == TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE) {
                goldMerchantIndex = i;
                break;
            }
        }

        // normally index will be greater than 0, because gold merchant menu already been laid initially
        if (goldMerchantIndex >= 0) {
            DrawerGroup drawerGroup = (DrawerGroup) adapterData.get(goldMerchantIndex);
            boolean isExpanded = drawerGroup.isExpanded;
            adapterData.removeAll(drawerGroup.getList());
            adapterData.remove(goldMerchantIndex);

            adapterData.add(goldMerchantIndex, goldMerchantMenu);

            goldMerchantMenu.setExpanded(isExpanded);
        }
        notifyDataSetChanged();
    }

    public DrawerGroup getGoldMerchantMenu() {
	String gm = context.getString(GMConstant.getGMTitleResource(context));
        DrawerGroup gmMenu = new DrawerGroup(gm,
            GMConstant.getGMDrawerDrawableResource(context),
            TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE,
            drawerCache.getBoolean(DrawerAdapter.IS_GM_OPENED, false),
            0);

        String gmString = isGoldMerchant ?
            context.getString(R.string.extend_gold_merchant, gm) :
            context.getString(R.string.upgrade_gold_merchant, gm);

        gmMenu.add(new DrawerItem(gmString,
                TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND,
                drawerCache.getBoolean(DrawerAdapter.IS_GM_OPENED, false),
                0));
        gmMenu.add(new DrawerItem(context.getString(com.tokopedia.seller.R.string.featured_product_title),
                TkpdState.DrawerPosition.FEATURED_PRODUCT,
                true
        ));
        return gmMenu;
    }

    public void renderFlashSaleDrawer() {
        if (isFlashSaleVisible) {
            ArrayList<DrawerItem> items = getData();
            int size = items.size();
            for (int i = 0; i < size; i++) {
                if (items.get(i).id == TkpdState.DrawerPosition.SELLER_TOP_ADS &&
                        items.get(i + 1).id != TkpdState.DrawerPosition.SELLER_FLASH_SALE) {
                    items.add(i + 1, new DrawerItem(context.getString(R.string.drawer_title_flash_sale),
                            R.drawable.ic_flash_sale,
                            TkpdState.DrawerPosition.SELLER_FLASH_SALE,
                            true));
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }
}
