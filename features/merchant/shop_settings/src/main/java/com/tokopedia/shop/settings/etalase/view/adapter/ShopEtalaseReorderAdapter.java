package com.tokopedia.shop.settings.etalase.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.design.touchhelper.ItemTouchHelperAdapter;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseReorderFactory;

import java.util.List;

/**
 * Created by hendry on 20/08/18.
 */
public class ShopEtalaseReorderAdapter extends BaseListAdapter<ShopEtalaseViewModel, ShopEtalaseReorderFactory>
    implements ItemTouchHelperAdapter {

    public ShopEtalaseReorderAdapter(ShopEtalaseReorderFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        List<ShopEtalaseViewModel> viewModelList = getData();
        ShopEtalaseViewModel modelFrom = viewModelList.get(fromPosition);
        viewModelList.remove(fromPosition);
        viewModelList.add(toPosition, modelFrom);
        notifyItemMoved(fromPosition, toPosition);
        return true;

    }

    @Override
    public void onItemDismiss(int position) {
        // no-op
    }
}
