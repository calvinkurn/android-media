package com.tokopedia.shop.settings.notes.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.design.touchhelper.ItemTouchHelperAdapter;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteReorderFactory;

import java.util.List;

/**
 * Created by hendry on 20/08/18.
 */
public class ShopNoteReorderAdapter extends BaseListAdapter<ShopNoteViewModel, ShopNoteReorderFactory>
    implements ItemTouchHelperAdapter {

    public ShopNoteReorderAdapter(ShopNoteReorderFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        List<ShopNoteViewModel> viewModelList = getData();
        ShopNoteViewModel modelFrom = viewModelList.get(fromPosition);
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
