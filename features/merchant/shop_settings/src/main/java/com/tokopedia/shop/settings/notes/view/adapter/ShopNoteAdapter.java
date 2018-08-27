package com.tokopedia.shop.settings.notes.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteFactory;

import java.util.List;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopNoteAdapter extends BaseListAdapter<ShopNoteViewModel, ShopNoteFactory> {
    public ShopNoteAdapter(ShopNoteFactory baseListAdapterTypeFactory, OnAdapterInteractionListener<ShopNoteViewModel> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }
}
