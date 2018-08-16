package com.tokopedia.shop.settings.notes.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopNoteFactory;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;

import java.util.List;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopNoteAdapter extends BaseListAdapter<ShopNoteViewModel, ShopNoteFactory> {
    public ShopNoteAdapter(ShopNoteFactory baseListAdapterTypeFactory, OnAdapterInteractionListener<ShopNoteViewModel> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }

    public void deleteNote(String noteId) {
        List<ShopNoteViewModel> data = getData();

        for (int i = 0, sizei = data.size(); i < sizei; i++) {
            ShopNoteViewModel model = data.get(i);
            if (model.getId().equalsIgnoreCase(noteId)) {
                this.clearElement(model);
                notifyItemChanged(i);
                break;
            }
        }
    }
}
